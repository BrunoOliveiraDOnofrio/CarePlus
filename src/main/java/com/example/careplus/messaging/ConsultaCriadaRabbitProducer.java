package com.example.careplus.messaging;

import com.example.careplus.dto.messaging.EventoConsultaCriadaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConsultaCriadaRabbitProducer {

    private static final Logger log = LoggerFactory.getLogger(ConsultaCriadaRabbitProducer.class);

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange consultasCriadasExchange;

    @Value("${app.rabbitmq.routing-key.consultas-criadas}")
    private String consultasCriadasRoutingKey;

    public ConsultaCriadaRabbitProducer(RabbitTemplate rabbitTemplate,
                                        DirectExchange consultasCriadasExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.consultasCriadasExchange = consultasCriadasExchange;
    }

    public void publicarEvento(EventoConsultaCriadaDto evento) {
        if (evento == null || evento.getConsultasCriadas() == null || evento.getConsultasCriadas().isEmpty()) {
            log.warn("[RabbitMQ] Evento de consultas criadas vazio - nada será enviado.");
            return;
        }

        int quantidade = evento.getConsultasCriadas().size();

        try {
            rabbitTemplate.convertAndSend(consultasCriadasExchange.getName(), consultasCriadasRoutingKey, evento);
            log.info("[RabbitMQ] Evento com {} consulta(s) publicado com sucesso na exchange '{}' com routingKey '{}'",
                    quantidade, consultasCriadasExchange.getName(), consultasCriadasRoutingKey);
        } catch (AmqpException ex) {
            log.warn("[RabbitMQ] Falha ao publicar evento com {} consulta(s) na exchange '{}' com routingKey '{}': {}",
                    quantidade, consultasCriadasExchange.getName(), consultasCriadasRoutingKey, ex.getMessage());
        } catch (Exception ex) {
            log.error("[RabbitMQ] Erro inesperado ao publicar evento com {} consulta(s): {}", quantidade, ex.getMessage());
        }
    }
}
