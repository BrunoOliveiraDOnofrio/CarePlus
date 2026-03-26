package com.example.careplus.messaging;

import com.example.careplus.dto.messaging.ConsultaCriadaMensagemDto;
import com.example.careplus.dto.messaging.EventoConsultaCriadaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ConsultaCriadaRabbitProducerTest {

    private RabbitTemplate rabbitTemplate;
    private DirectExchange exchange;
    private ConsultaCriadaRabbitProducer producer;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        exchange = new DirectExchange("consultas.criadas.exchange");
        producer = new ConsultaCriadaRabbitProducer(rabbitTemplate, exchange);
    }

    @Test
    void devePublicarEventoQuandoHaConsultas() {
        EventoConsultaCriadaDto evento = new EventoConsultaCriadaDto(List.of(mock(ConsultaCriadaMensagemDto.class)));
        producer.publicarEvento(evento);
        verify(rabbitTemplate, times(1)).convertAndSend(eq("consultas.criadas.exchange"), any(), eq(evento));
    }

    @Test
    void naoDevePublicarQuandoEventoVazio() {
        EventoConsultaCriadaDto evento = new EventoConsultaCriadaDto(List.of());
        producer.publicarEvento(evento);
        verifyNoInteractions(rabbitTemplate);
    }
}
