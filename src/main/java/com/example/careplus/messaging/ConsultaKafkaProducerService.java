package com.example.careplus.messaging;

import com.example.careplus.dto.dtoConsultaProntuario.ConsultaProntuarioResponseDto;
import com.example.careplus.dto.kafka.ConsultaCriadaKafkaDto;
import com.example.careplus.dto.kafka.EventoConsultaCriadaDto;
import com.example.careplus.dto.kafka.PacienteKafkaDto;
import com.example.careplus.dto.kafka.ProfissionalKafkaDto;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Service
public class ConsultaKafkaProducerService {

    private static final Logger log = LoggerFactory.getLogger(ConsultaKafkaProducerService.class);

    // Formato exigido pelo consumer do mensageria: "yyyy-MM-dd HH:mm:ss"
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final KafkaTemplate<String, EventoConsultaCriadaDto> kafkaTemplate;

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    // Nome do tópico configurável via application.properties
    @Value("${app.kafka.topic.consultas-criadas:consultas-criadas}")
    private String topicConsultasCriadas;

    public ConsultaKafkaProducerService(KafkaTemplate<String, EventoConsultaCriadaDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Verifica se o broker Kafka está acessível antes de tentar enviar
    private boolean isBrokerDisponivel() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "2000");
        props.put(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, "2000");
        try (AdminClient adminClient = AdminClient.create(props)) {
            adminClient.listTopics().names().get(2, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Chamado ao marcar uma consulta simples
    public void publicarConsultaCriada(ConsultaProntuarioResponseDto consulta) {
        publicarEvento(List.of(consulta));
    }

    // Chamado ao criar consultas recorrentes
    public void publicarConsultasRecorrentesCriadas(List<ConsultaProntuarioResponseDto> consultas) {
        publicarEvento(consultas);
    }

    // Monta o EventoConsultaCriadaDto com os campos exatos que o consumer espera e envia ao tópico
    private void publicarEvento(List<ConsultaProntuarioResponseDto> consultas) {
        if (!isBrokerDisponivel()) {
            log.warn("[Kafka] Broker indisponível em '{}' — evento não enviado. A consulta foi criada normalmente.", bootstrapServers);
            return;
        }

        try {
            // Converte cada ConsultaProntuarioResponseDto para o DTO específico do Kafka
            // usando apenas os campos conhecidos pelo consumer (evita UnrecognizedPropertyException)
            List<ConsultaCriadaKafkaDto> consultaDtos = consultas.stream().map(consulta -> {
                PacienteKafkaDto paciente = new PacienteKafkaDto(
                        consulta.getPaciente().getId(),
                        consulta.getPaciente().getNome(),
                        consulta.getPaciente().getEmail(),
                        consulta.getPaciente().getCpf(),
                        consulta.getPaciente().getTelefone(),
                        consulta.getPaciente().getDtNascimento() != null ? consulta.getPaciente().getDtNascimento().toString() : null,
                        consulta.getPaciente().getConvenio(),
                        consulta.getPaciente().getDataInicio() != null ? consulta.getPaciente().getDataInicio().toString() : null
                );
                ProfissionalKafkaDto profissional = new ProfissionalKafkaDto(
                        consulta.getFuncionario().getId(),
                        consulta.getFuncionario().getNome(),
                        consulta.getFuncionario().getEspecialidade(),
                        consulta.getFuncionario().getTipoAtendimento()
                );
                return new ConsultaCriadaKafkaDto(
                        consulta.getId(),
                        paciente,
                        profissional,
                        // FORMATTER garante que segundos sempre apareçam (ex: "16:00:00" e não "16:00")
                        consulta.getDataHora() != null ? FORMATTER.format(consulta.getDataHora()) : null,
                        consulta.getTipo()
                );
            }).toList();

            // Envelopa a lista no evento raiz antes de enviar
            EventoConsultaCriadaDto evento = new EventoConsultaCriadaDto(consultaDtos);

            // Envio assíncrono — o whenComplete loga sucesso ou falha sem bloquear a thread HTTP
            kafkaTemplate.send(topicConsultasCriadas, evento)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.warn("[Kafka] Falha ao publicar evento com {} consulta(s) no tópico '{}': {}",
                                    consultaDtos.size(), topicConsultasCriadas, ex.getMessage());
                        } else {
                            log.info("[Kafka] Evento com {} consulta(s) publicado com sucesso no tópico '{}' [partition={}, offset={}]",
                                    consultaDtos.size(),
                                    topicConsultasCriadas,
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception ex) {
            log.warn("[Kafka] Erro inesperado ao publicar evento: {}", ex.getMessage());
        }
    }
}

