package com.example.careplus.scheduler;

import com.example.careplus.model.ConsultaProntuario;
import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Notificacao;
import com.example.careplus.model.Paciente;
import com.example.careplus.repository.ConsultaProntuarioRepository;
import com.example.careplus.repository.NotificacaoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RecorrenciaScheduler {

    private static final DateTimeFormatter DATA_FIM_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final ConsultaProntuarioRepository consultaProntuarioRepository;
    private final NotificacaoRepository notificacaoRepository;

    public RecorrenciaScheduler(ConsultaProntuarioRepository consultaProntuarioRepository,
                                NotificacaoRepository notificacaoRepository) {
        this.consultaProntuarioRepository = consultaProntuarioRepository;
        this.notificacaoRepository = notificacaoRepository;
    }

    @Transactional
    @Scheduled(cron = "0 */2 * * * *")
    public void verificarRecorrenciasEncerrando() {
        LocalDate proximaSegunda = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate proximoSabado  = proximaSegunda.plusDays(5);

        // Remove notificações de recorrências já vencidas
        notificacaoRepository.deleteByDataFimBefore(LocalDate.now());

        // Busca consultas da próxima semana que pertencem a recorrências
        List<ConsultaProntuario> consultasSemana = consultaProntuarioRepository
                .findByRecorrenciaIdNotNullAndDataBetween(proximaSegunda, proximoSabado);

        // Agrupa por recorrenciaId
        Map<String, List<ConsultaProntuario>> porRecorrencia = consultasSemana.stream()
                .collect(Collectors.groupingBy(ConsultaProntuario::getRecorrenciaId,
                        LinkedHashMap::new, Collectors.toList()));

        for (Map.Entry<String, List<ConsultaProntuario>> entry : porRecorrencia.entrySet()) {
            String recorrenciaId = entry.getKey();
            List<ConsultaProntuario> grupo = entry.getValue();

            // Extrai dataFim do recorrenciaId (formato: {UUID}-DD-MM-YYYY)
            LocalDate dataFim = extrairDataFim(recorrenciaId);
            if (dataFim == null) continue;

            // Só cria notificação se o vencimento cair na próxima semana
            if (dataFim.isBefore(proximaSegunda) || dataFim.isAfter(proximoSabado)) continue;

            // Evita duplicatas
            if (notificacaoRepository.existsByRecorrenciaId(recorrenciaId)) continue;

            ConsultaProntuario template = grupo.get(0);
            Paciente paciente = template.getPaciente();

            String profissionalNome = null;
            String especialidade = null;
            if (!template.getConsultaFuncionarios().isEmpty()) {
                Funcionario func = template.getConsultaFuncionarios().get(0).getFuncionario();
                profissionalNome = func.getNome();
                especialidade = func.getEspecialidade();
            }

            // Coleta os dias da semana das consultas do grupo (1=Seg, 5=Sex)
            Set<Integer> diasSet = grupo.stream()
                    .map(c -> c.getData().getDayOfWeek().getValue())
                    .collect(Collectors.toCollection(TreeSet::new));
            String diasSemana = diasSet.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            Notificacao notificacao = new Notificacao();
            notificacao.setPaciente(paciente);
            notificacao.setRecorrenciaId(recorrenciaId);
            notificacao.setProfissionalNome(profissionalNome);
            notificacao.setEspecialidade(especialidade);
            notificacao.setHorarioInicio(template.getHorarioInicio());
            notificacao.setHorarioFim(template.getHorarioFim());
            notificacao.setTipo(template.getTipo());
            notificacao.setDiasSemana(diasSemana);
            notificacao.setDataFim(dataFim);

            notificacaoRepository.save(notificacao);
        }
    }

    private LocalDate extrairDataFim(String recorrenciaId) {
        if (recorrenciaId == null || recorrenciaId.length() < 10) return null;
        try {
            String datePart = recorrenciaId.substring(recorrenciaId.length() - 10);
            return LocalDate.parse(datePart, DATA_FIM_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }
}
