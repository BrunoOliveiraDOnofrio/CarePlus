package com.example.careplus.service;

import com.example.careplus.dto.dtoConsultaRecorrente.AgendarConsultasRequestDto;
import com.example.careplus.dto.dtoConsultaRecorrente.ConsultaItemRequestDto;
import com.example.careplus.dto.dtoNotificacao.NotificacaoResponseDto;
import com.example.careplus.dto.dtoNotificacao.RecorrenciaNotificacaoDto;
import com.example.careplus.dto.dtoNotificacao.RenovarRecorrenciaDto;
import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.model.ConsultaProntuario;
import com.example.careplus.model.Notificacao;
import com.example.careplus.repository.ConsultaProntuarioRepository;
import com.example.careplus.repository.NotificacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificacaoService {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final NotificacaoRepository notificacaoRepository;
    private final ConsultaProntuarioRepository consultaProntuarioRepository;
    private final ConsultaProntuarioService consultaProntuarioService;

    public NotificacaoService(NotificacaoRepository notificacaoRepository,
                              ConsultaProntuarioRepository consultaProntuarioRepository,
                              ConsultaProntuarioService consultaProntuarioService) {
        this.notificacaoRepository = notificacaoRepository;
        this.consultaProntuarioRepository = consultaProntuarioRepository;
        this.consultaProntuarioService = consultaProntuarioService;
    }

    public List<NotificacaoResponseDto> listarNotificacoes() {
        List<Notificacao> notificacoes = notificacaoRepository.findAllByOrderByDataFimAsc();
        LocalDate hoje = LocalDate.now();

        Map<Long, NotificacaoResponseDto> porPaciente = new LinkedHashMap<>();

        for (Notificacao n : notificacoes) {
            Long pacienteId = n.getPaciente().getId();

            porPaciente.computeIfAbsent(pacienteId, id -> {
                NotificacaoResponseDto dto = new NotificacaoResponseDto();
                dto.setPacienteId(id);
                dto.setPacienteNome(n.getPaciente().getNome());
                dto.setRecorrencias(new ArrayList<>());
                return dto;
            });

            RecorrenciaNotificacaoDto recDto = new RecorrenciaNotificacaoDto();
            recDto.setRecorrenciaId(n.getRecorrenciaId());
            recDto.setProfissionalNome(n.getProfissionalNome());
            recDto.setEspecialidade(n.getEspecialidade());
            recDto.setDiasSemana(parseDiasSemana(n.getDiasSemana()));
            recDto.setHorarioInicio(n.getHorarioInicio() != null ? n.getHorarioInicio().format(HORA) : null);
            recDto.setHorarioFim(n.getHorarioFim() != null ? n.getHorarioFim().format(HORA) : null);
            recDto.setTipo(n.getTipo());
            recDto.setDataFim(n.getDataFim().toString());
            recDto.setDiasRestantes((int) ChronoUnit.DAYS.between(hoje, n.getDataFim()));

            porPaciente.get(pacienteId).getRecorrencias().add(recDto);
        }

        return new ArrayList<>(porPaciente.values());
    }

    @Transactional
    public void dispensar(String recorrenciaId) {
        if (!notificacaoRepository.existsByRecorrenciaId(recorrenciaId)) {
            throw new ResourceNotFoundException("Notificação não encontrada: " + recorrenciaId);
        }
        notificacaoRepository.deleteByRecorrenciaId(recorrenciaId);
    }

    @Transactional
    public void renovar(String recorrenciaId, RenovarRecorrenciaDto dto) {
        if (!notificacaoRepository.existsByRecorrenciaId(recorrenciaId)) {
            throw new ResourceNotFoundException("Notificação não encontrada: " + recorrenciaId);
        }
        if (dto.getDiasSemana() == null || dto.getDiasSemana().isEmpty()) {
            throw new IllegalArgumentException("Selecione pelo menos um dia da semana.");
        }
        if (dto.getDataFim() == null || dto.getDataFim().isBlank()) {
            throw new IllegalArgumentException("Informe a nova data de término.");
        }

        LocalDate novaDataFim = LocalDate.parse(dto.getDataFim());

        List<ConsultaProntuario> consultas = consultaProntuarioRepository.findByRecorrenciaId(recorrenciaId);
        if (consultas.isEmpty()) {
            throw new ResourceNotFoundException("Consultas não encontradas para recorrência: " + recorrenciaId);
        }

        ConsultaProntuario template = consultas.get(0);

        List<Long> funcionarioIds = consultas.stream()
                .flatMap(c -> c.getConsultaFuncionarios().stream())
                .map(cf -> cf.getFuncionario().getId())
                .distinct()
                .collect(Collectors.toList());

        if (funcionarioIds.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum funcionário vinculado à recorrência.");
        }

        AgendarConsultasRequestDto agendarDto = new AgendarConsultasRequestDto();
        agendarDto.setPacienteId(template.getPaciente().getId());

        List<ConsultaItemRequestDto> itens = new ArrayList<>();
        for (Integer dia : dto.getDiasSemana()) {
            DayOfWeek dow = fromFrontendDia(dia);
            LocalDate proxOcorrencia = LocalDate.now().plusDays(1).with(TemporalAdjusters.nextOrSame(dow));

            ConsultaItemRequestDto item = new ConsultaItemRequestDto();
            item.setFuncionarioIds(funcionarioIds);
            item.setHorarioInicio(template.getHorarioInicio());
            item.setHorarioFim(template.getHorarioFim());
            item.setTipo(template.getTipo());
            item.setDataInicio(proxOcorrencia);
            item.setDataFim(novaDataFim);
            itens.add(item);
        }

        agendarDto.setConsultas(itens);
        consultaProntuarioService.agendarConsultas(agendarDto);

        notificacaoRepository.deleteByRecorrenciaId(recorrenciaId);
    }

    private List<Integer> parseDiasSemana(String diasSemana) {
        if (diasSemana == null || diasSemana.isBlank()) return new ArrayList<>();
        return Arrays.stream(diasSemana.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private DayOfWeek fromFrontendDia(int dia) {
        return switch (dia) {
            case 1 -> DayOfWeek.MONDAY;
            case 2 -> DayOfWeek.TUESDAY;
            case 3 -> DayOfWeek.WEDNESDAY;
            case 4 -> DayOfWeek.THURSDAY;
            case 5 -> DayOfWeek.FRIDAY;
            default -> throw new IllegalArgumentException("Dia inválido: " + dia);
        };
    }
}
