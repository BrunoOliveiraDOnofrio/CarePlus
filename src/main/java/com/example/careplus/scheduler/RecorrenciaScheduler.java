package com.example.careplus.scheduler;

import com.example.careplus.repository.ConsultaProntuarioRepository;
import com.example.careplus.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class RecorrenciaScheduler {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

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

    @Scheduled(cron = "0 0 23 * * SUN")
    public void verificarRecorrenciasEncerrando() {
        LocalDate proximaSegunda = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate proximoSabado  = proximaSegunda.plusDays(5);
        String    inicioStr      = proximaSegunda.toString();
        String    fimStr         = proximoSabado.toString();

        List<Object[]> rows = consultaProntuarioRepository.buscarRecorrenciasEncerrando(
                inicioStr, fimStr, proximaSegunda, proximoSabado
        );

        // agrupa: pacienteId → { nomePaciente, dia → lista de consultas }
        Map<Long, PacienteAgenda> agendaPorPaciente = new LinkedHashMap<>();

        for (Object[] row : rows) {
            Long      pacienteId    = ((Number) row[0]).longValue();
            String    nomePaciente  = (String) row[1];
            LocalDate data          = (LocalDate) row[2];
            LocalTime horarioInicio = (LocalTime) row[3];
            LocalTime horarioFim    = (LocalTime) row[4];
            String    especialidade = (String) row[5];

            agendaPorPaciente
                    .computeIfAbsent(pacienteId, id -> new PacienteAgenda(id, nomePaciente))
                    .adicionarConsulta(data, horarioInicio, horarioFim, especialidade);
        }

        for (PacienteAgenda agenda : agendaPorPaciente.values()) {
            try {
                String json = objectMapper.writeValueAsString(agenda.toPayload());
                String key  = "recorrencias/paciente_" + agenda.pacienteId + ".json";
                s3Service.uploadJson(bucket, key, json);
            } catch (Exception e) {
                throw new RuntimeException(
                        "Erro ao enviar JSON de renovação para paciente " + agenda.pacienteId, e);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Estrutura auxiliar de agrupamento (uso interno do scheduler)
    // -------------------------------------------------------------------------

    private class PacienteAgenda {

        final Long   pacienteId;
        final String nomePaciente;
        final Map<LocalDate, List<Map<String, String>>> consultasPorDia = new LinkedHashMap<>();

        PacienteAgenda(Long pacienteId, String nomePaciente) {
            this.pacienteId   = pacienteId;
            this.nomePaciente = nomePaciente;
        }

        void adicionarConsulta(LocalDate data, LocalTime inicio, LocalTime fim, String especialidade) {
            Map<String, String> consulta = new LinkedHashMap<>();
            consulta.put("horarioInicio",   inicio != null ? inicio.format(HORA) : null);
            consulta.put("horarioTermino",  fim    != null ? fim.format(HORA)    : null);
            consulta.put("especialidade",   especialidade);
            consultasPorDia.computeIfAbsent(data, d -> new ArrayList<>()).add(consulta);
        }

        Map<String, Object> toPayload() {
            List<Map<String, Object>> agendamentos = new ArrayList<>();
            for (Map.Entry<LocalDate, List<Map<String, String>>> entry : consultasPorDia.entrySet()) {
                Map<String, Object> dia = new LinkedHashMap<>();
                dia.put("diaSemana", entry.getKey().toString());
                dia.put("consultas", entry.getValue());
                agendamentos.add(dia);
            }

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("pacienteId",   pacienteId);
            payload.put("nomePaciente", nomePaciente);
            payload.put("agendamentos", agendamentos);
            return payload;
        }
    }
}