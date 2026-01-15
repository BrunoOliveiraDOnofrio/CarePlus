package com.example.careplus.service;

import com.example.careplus.controller.dtoConsulta.ConsultaMapper;
import com.example.careplus.controller.dtoConsulta.ConsultaRequestDto;
import com.example.careplus.controller.dtoConsulta.ConsultaResponseDto;
import com.example.careplus.controller.dtoConsultaRecorrente.ConflitoDatasDto;
import com.example.careplus.controller.dtoConsultaRecorrente.ConsultaRecorrenteRequestDto;
import com.example.careplus.controller.dtoConsultaRecorrente.ConsultaRecorrenteResponseDto;
import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.model.Consulta;
import com.example.careplus.model.ConsultaRequest;
import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Paciente;
import com.example.careplus.repository.ConsultaRepository;
import com.example.careplus.repository.FuncionarioRepository;
import com.example.careplus.repository.PacienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final EmailService emailService;

    public ConsultaService(ConsultaRepository consultaRepository, PacienteRepository pacienteRepository, FuncionarioRepository funcionarioRepository, EmailService emailService) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.emailService = emailService;
    }

    //montando a Consulta a partir do ConsultaRequest
    public ConsultaResponseDto marcarConsulta(ConsultaRequestDto request){
        Optional<Paciente> usuarioOpt = pacienteRepository.findById(request.getPacienteId());
        Paciente paciente = usuarioOpt.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));

        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findById(request.getFuncionarioId());
        Funcionario funcionario = funcionarioOpt.orElseThrow(() -> new ResourceNotFoundException("Funcionario não encontrado!"));

        Consulta novaConsulta = new Consulta();
        novaConsulta.setPaciente(paciente);
        novaConsulta.setFuncionario(funcionario);
        novaConsulta.setDataHora(request.getDataHora());
        novaConsulta.setConfirmada(null);
        novaConsulta.setTipo(request.getTipo() != null ? request.getTipo() : "Pendente");

        Consulta salvo = consultaRepository.save(novaConsulta);
        emailService.EnviarNotificacao(funcionario, novaConsulta, paciente);
        return ConsultaMapper.toResponseDto(salvo);
    }

    public void removerConsulta(Long consultaId){
        boolean consulta = consultaRepository.existsById(consultaId);
        if (!consulta){
            throw new RuntimeException("Consulta não encontrada");
        }
        consultaRepository.deleteById(consultaId);
    }

    public List<Consulta> listarConsultas(){
        return consultaRepository.findAll();
    }

    public List<ConsultaResponseDto> listarPorData(){
        List<Consulta> consultas = consultaRepository.buscarPorData();

        if (consultas.isEmpty()){
            throw new ResourceNotFoundException("Nenhuma consulta cadastrada!");
        }

        return ConsultaMapper.toResponseDto(consultas);

    }

    public List<ConsultaResponseDto> listarPorPaciente(Long idPaciente){
        List<Consulta> consultas = consultaRepository.buscarPorPaciente(idPaciente);
        if (consultas.isEmpty()){
            throw new ResourceNotFoundException("Nenhuma consulta cadastrada para esse paciente!");
        }
        return ConsultaMapper.toResponseDto(consultas);
    }

    public List<ConsultaResponseDto> consultasDoDia(Long idFuncionario){
        List<Consulta> consultas = consultaRepository.consultasDoDia(idFuncionario);
        if (consultas.isEmpty()){
            throw new ResourceNotFoundException("Nenhuma consulta cadastrada para esse paciente!");
        }
        return ConsultaMapper.toResponseDto(consultas);
    }

    public ConsultaResponseDto editarConsulta(Long consultaId, ConsultaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));
        Funcionario funcionario = funcionarioRepository.findById(request.getFuncionarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario não encontrado"));
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));
        consulta.setPaciente(paciente);
        consulta.setFuncionario(funcionario);
        consulta.setDataHora(request.getDataHora());
        consulta.setTipo("Retorno");
        // manter confirmada caso já esteja setada
        consulta.setConfirmada(consulta.getConfirmada() != null ? consulta.getConfirmada() : Boolean.FALSE);

        Consulta salva = consultaRepository.save(consulta);
        return ConsultaMapper.toResponseDto(salva);
    }

    public ConsultaResponseDto confirmarConsulta(Long consultaId, Boolean status){
        Optional<Consulta> consultaParaAtualizar = consultaRepository.findById(consultaId);
        if (consultaParaAtualizar.isPresent()){
            Consulta consulta = consultaParaAtualizar.get();
            consulta.setConfirmada(status);

            Consulta consultaSalva = consultaRepository.save(consulta);
            return ConsultaMapper.toResponseDto(consultaSalva);
        } else {
            throw new RuntimeException("Consulta não encontrada");
        }
    }

    public ConsultaResponseDto salvarObservacoes(Long consultaId, String observacoes){
        Optional<Consulta> consultaParaAtualizar = consultaRepository.findById(consultaId);
        if (consultaParaAtualizar.isPresent()){
            Consulta consulta = consultaParaAtualizar.get();
            consulta.setObservacoesComportamentais(observacoes);
            consulta.setPresenca(true);

            Consulta consultaSalva = consultaRepository.save(consulta);
            return ConsultaMapper.toResponseDto(consultaSalva);
        } else {
            throw new RuntimeException("Consulta não encontrada");
        }
    }

    @Transactional
    public ConsultaRecorrenteResponseDto criarConsultasRecorrentes(ConsultaRecorrenteRequestDto dto) {
        ConsultaRecorrenteResponseDto response = new ConsultaRecorrenteResponseDto();

        // Verifica se paciente e funcionário existem
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado!"));

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado!"));

        // PRIMEIRA FASE: Validar TODAS as datas
        for (LocalDate data : dto.getDatas()) {
            LocalDateTime dataHora = LocalDateTime.of(data, dto.getHorario());

            List<Consulta> consultasExistentes = consultaRepository
                    .buscarConsultasPorFuncionarioEData(dto.getFuncionarioId(), data);

            for (Consulta consulta : consultasExistentes) {
                LocalDateTime inicioConsulta = consulta.getDataHora();
                LocalDateTime fimConsulta = inicioConsulta.plusHours(1);
                LocalDateTime fimNovaConsulta = dataHora.plusHours(1);

                // Verifica sobreposição de horários
                boolean temConflito = (dataHora.isBefore(fimConsulta) && fimNovaConsulta.isAfter(inicioConsulta));

                if (temConflito) {
                    ConflitoDatasDto conflito = new ConflitoDatasDto(
                            data,
                            dto.getHorario().toString(),
                            String.format("Horário indisponível - Funcionário já possui consulta marcada às %s",
                                    inicioConsulta.toLocalTime().toString())
                    );
                    response.getDatasComConflito().add(conflito);
                }
            }
        }

        // Se houver QUALQUER conflito, não cria NENHUMA consulta
        if (!response.getDatasComConflito().isEmpty()) {
            response.setTotalConsultasCriadas(0);
            response.setTotalFalhas(response.getDatasComConflito().size());
            return response;
        }

        // SEGUNDA FASE: Criar TODAS as consultas (só chega aqui se NÃO houver conflitos)
        for (LocalDate data : dto.getDatas()) {
            LocalDateTime dataHora = LocalDateTime.of(data, dto.getHorario());

            Consulta consulta = new Consulta();
            consulta.setPaciente(paciente);
            consulta.setFuncionario(funcionario);
            consulta.setDataHora(dataHora);
            consulta.setTipo(dto.getTipo());
            consulta.setConfirmada(false);

            Consulta consultaSalva = consultaRepository.save(consulta);
            response.getConsultasCriadas().add(ConsultaMapper.toResponseDto(consultaSalva));
        }

        response.setTotalConsultasCriadas(response.getConsultasCriadas().size());
        response.setTotalFalhas(0);

        return response;
    }

    public List<ConsultaResponseDto> listarAgendaSemanal(Long funcionarioId, LocalDate dataReferencia) {
        // Encontra a segunda-feira da semana da data de referência
        LocalDate inicioDaSemana = dataReferencia.minusDays(dataReferencia.getDayOfWeek().getValue() - 1);
        LocalDate fimDaSemana = inicioDaSemana.plusDays(6);

        LocalDateTime inicioDateTime = inicioDaSemana.atStartOfDay();
        LocalDateTime fimDateTime = fimDaSemana.atTime(23, 59, 59);

        List<Consulta> consultas = consultaRepository.buscarConsultasPorFuncionarioEPeriodo(
                funcionarioId,
                inicioDateTime,
                fimDateTime
        );

        if (consultas.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma consulta encontrada para este período!");
        }

        return ConsultaMapper.toResponseDto(consultas);
    }

    public List<ConsultaResponseDto> listarConsultasPendentes(Long idFuncionario) {
        List<Consulta> consultas = consultaRepository.findByFuncionarioIdAndConfirmadaFalse(idFuncionario);
        return ConsultaMapper.toResponseDto(consultas);
    }

}
