package com.example.careplus.scheduler;

import com.example.careplus.repository.ConsultaProntuarioRepository;
import com.example.careplus.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class RecorrenciaScheduler {

    private final ConsultaProntuarioRepository consultaProntuarioRepository;
    private final S3Service s3Service;
    private final ObjectMapper objectMapper;

    @Value("${aws.s3.bucket-renovar-agenda-name}")
    private String bucket;

    public RecorrenciaScheduler(ConsultaProntuarioRepository consultaProntuarioRepository,
                                S3Service s3Service,
                                ObjectMapper objectMapper) {
        this.consultaProntuarioRepository = consultaProntuarioRepository;
        this.s3Service = s3Service;
        this.objectMapper = objectMapper;
    }

    // TESTE: a cada 1 minuto. Produção: "0 0 23 * * SUN"
    @Scheduled(cron = "0 * * * * *")
    public void verificarRecorrenciasEncerrando() {
        LocalDate proximaSegunda = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate proximoSabado  = proximaSegunda.plusDays(5);

        List<Object[]> resultados = consultaProntuarioRepository.buscarRecorrenciasEncerrando(
                proximaSegunda.toString(),
                proximoSabado.toString()
        );

        for (Object[] row : resultados) {
            Long   pacienteId    = ((Number) row[0]).longValue();
            String nomePaciente  = (String) row[1];
            String dataFinal     = (String) row[2];
            String especialidade = (String) row[3];

            try {
                Map<String, Object> payload = new LinkedHashMap<>();
                payload.put("pacienteId",    pacienteId);
                payload.put("nomePaciente",  nomePaciente);
                payload.put("especialidade", especialidade);
                payload.put("dataFinal",     dataFinal);

                String json = objectMapper.writeValueAsString(payload);
                String key  = "recorrencias/paciente_" + pacienteId + "_" + dataFinal + ".json";

                s3Service.uploadJson(bucket, key, json);
            } catch (Exception e) {
                throw new RuntimeException(
                        "Erro ao enviar JSON de renovação para paciente " + pacienteId, e);
            }
        }
    }
}