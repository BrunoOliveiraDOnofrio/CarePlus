package com.example.careplus.service;

import com.example.careplus.dto.dtoConsulta.*;
import com.example.careplus.dto.dtoConsultaRecorrente.ConflitoDatasDto;
import com.example.careplus.dto.dtoConsultaRecorrente.ConsultaRecorrenteRequestDto;
import com.example.careplus.dto.dtoConsultaRecorrente.ConsultaRecorrenteResponseDto;
import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.model.Consulta;
import com.example.careplus.model.ClassificacaoDoencas;
import com.example.careplus.model.Cuidador;
import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Material;
import com.example.careplus.model.Medicacao;
import com.example.careplus.model.Paciente;
import com.example.careplus.model.Prontuario;
import com.example.careplus.model.Tratamento;
import com.example.careplus.repository.ConsultaRepository;
import com.example.careplus.repository.CuidadorRepository;
import com.example.careplus.repository.FuncionarioRepository;
import com.example.careplus.repository.PacienteRepository;
import com.example.careplus.repository.ProntuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final EmailService emailService;
    private final ProntuarioRepository prontuarioRepository;
    private final CuidadorRepository cuidadorRepository;

    public ConsultaService(ConsultaRepository consultaRepository, PacienteRepository pacienteRepository, FuncionarioRepository funcionarioRepository, EmailService emailService, ProntuarioRepository prontuarioRepository, CuidadorRepository cuidadorRepository) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.emailService = emailService;
        this.prontuarioRepository = prontuarioRepository;
        this.cuidadorRepository = cuidadorRepository;
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

    public ConsultaResponseDto confirmarConsulta(Long consultaId){
        Optional<Consulta> consultaParaAtualizar = consultaRepository.findById(consultaId);
        if (consultaParaAtualizar.isPresent()){
            Consulta consulta = consultaParaAtualizar.get();
            consulta.setConfirmada(true);

            Consulta consultaSalva = consultaRepository.save(consulta);
            return ConsultaMapper.toResponseDto(consultaSalva);
        } else {
            throw new RuntimeException("Consulta não encontrada");
        }
    }

    public ConsultaResponseDto recusarConsulta(Long consultaId, String justificativa){
        Optional<Consulta> consultaParaAtualizar = consultaRepository.findById(consultaId);
        if (consultaParaAtualizar.isPresent()){
            Consulta consulta = consultaParaAtualizar.get();
            consulta.setObservacoesComportamentais(justificativa);
            consulta.setConfirmada(false);

            Consulta consultaSalva = consultaRepository.save(consulta);
            return ConsultaMapper.toResponseDto(consultaSalva);
        } else {
            throw new RuntimeException("Consulta não encontrada");
        }
    }

    public ConsultaResponseDto salvarObservacoes(Long consultaId, RealizarConsultaDto info){
        Optional<Consulta> consultaParaAtualizar = consultaRepository.findById(consultaId);
        if (consultaParaAtualizar.isPresent()){
            Consulta consulta = consultaParaAtualizar.get();
            consulta.setObservacoesComportamentais(info.getObservacao());
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
        // acha a segunda-feira da semana da data de referência
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
        List<Consulta> consultas = consultaRepository.findByFuncionarioIdAndConfirmadaNull(idFuncionario);
        return ConsultaMapper.toResponseDto(consultas);
    }

    public ProximaConsultaResponseDto buscarProximaConsultaConfirmada(Long pacienteId) {
        List<Consulta> consultas = consultaRepository.buscarProximaConsultaConfirmadaPorPaciente(pacienteId);

        if (consultas.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma consulta confirmada encontrada para este paciente!");
        }

        Consulta proximaConsulta = consultas.get(0);

        // Buscar o tratamento ativo do paciente (se existir)
        String tratamento = null;
        Prontuario prontuario = prontuarioRepository.findByPacienteId(pacienteId).orElse(null);
        if (prontuario != null && prontuario.getTratamentos() != null) {
            tratamento = prontuario.getTratamentos().stream()
                    .filter(t -> t.getFinalizado() == null || !t.getFinalizado())
                    .map(Tratamento::getTipoDeTratamento)
                    .findFirst()
                    .orElse(null);
        }

        return new ProximaConsultaResponseDto(
                proximaConsulta.getId(),
                proximaConsulta.getDataHora().toLocalDate(),
                proximaConsulta.getDataHora().toLocalTime(),
                proximaConsulta.getDataHora().toLocalTime().plusHours(1),
                proximaConsulta.getTipo(),
                proximaConsulta.getFuncionario().getNome(),
                tratamento
        );
    }

    public ConsultaAtualResponseDto buscarDetalhesConsultaAtual(Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada!"));

        Paciente paciente = consulta.getPaciente();
        Funcionario funcionario = consulta.getFuncionario();

        // Buscar prontuário do paciente
        Prontuario prontuario = prontuarioRepository.findByPacienteId(paciente.getId()).orElse(null);

        ConsultaAtualResponseDto response = new ConsultaAtualResponseDto();

        // Dados da consulta atual
        response.setConsultaId(consulta.getId());
        response.setData(consulta.getDataHora().toLocalDate());
        response.setHorarioInicio(consulta.getDataHora().toLocalTime());
        response.setHorarioFim(consulta.getDataHora().toLocalTime().plusHours(1));
        response.setTipo(consulta.getTipo());
        response.setEspecialidade(funcionario.getEspecialidade());
        response.setNomeProfissional(funcionario.getNome());

        // Tratamento atual (do prontuário)
        if (prontuario != null && prontuario.getTratamentos() != null) {
            String tratamentoAtual = prontuario.getTratamentos().stream()
                    .filter(t -> t.getFinalizado() == null || !t.getFinalizado())
                    .map(Tratamento::getTipoDeTratamento)
                    .findFirst()
                    .orElse(null);
            response.setTratamentoAtual(tratamentoAtual);
        }

        // Dados do paciente
        ConsultaAtualResponseDto.DadosPaciente dadosPaciente = new ConsultaAtualResponseDto.DadosPaciente();
        dadosPaciente.setPacienteId(paciente.getId());
        dadosPaciente.setNome(paciente.getNome());

        // Buscar contato do 1º cuidador legal (responsável)
        List<Cuidador> cuidadores = cuidadorRepository.findByPacienteId(paciente.getId());
        if (!cuidadores.isEmpty() && cuidadores.get(0).getResponsavel() != null) {
            dadosPaciente.setContato(cuidadores.get(0).getResponsavel().getTelefone());
        }

        // Calcular idade
        if (paciente.getDtNascimento() != null) {
            int idade = java.time.Period.between(paciente.getDtNascimento(), java.time.LocalDate.now()).getYears();
            dadosPaciente.setIdade(idade);
        }

        // Dados do prontuário
        if (prontuario != null) {
            dadosPaciente.setDesfraldado(prontuario.getDesfraldado());
            dadosPaciente.setHiperfocoAtual(prontuario.getHiperfoco());
            dadosPaciente.setDiagnostico(prontuario.getDiagnostico());

            // CID (pegar o primeiro ou concatenar)
            if (prontuario.getCid() != null && !prontuario.getCid().isEmpty()) {
                String cids = prontuario.getCid().stream()
                        .map(ClassificacaoDoencas::getCid)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse(null);
                dadosPaciente.setCid(cids);
            }

            // Medicações ativas
            if (prontuario.getMedicacoes() != null && !prontuario.getMedicacoes().isEmpty()) {
                String medicacoes = prontuario.getMedicacoes().stream()
                        .filter(Medicacao::getAtivo)
                        .map(Medicacao::getNomeMedicacao)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse(null);
                dadosPaciente.setMedicacoes(medicacoes);
            }

            // Atendimento especial baseado no nível de agressividade
            if (prontuario.getNivelAgressividade() != null && prontuario.getNivelAgressividade() > 0) {
                dadosPaciente.setAtendimentoEspecial("Lesivo");
            }
        }

        response.setDadosPaciente(dadosPaciente);

        // Última consulta passada do paciente
        List<Consulta> ultimasConsultas = consultaRepository.buscarUltimaConsultaPassadaPorPaciente(paciente.getId());
        if (!ultimasConsultas.isEmpty()) {
            Consulta ultimaConsultaEntity = ultimasConsultas.get(0);
            ConsultaAtualResponseDto.UltimaConsulta ultimaConsulta = new ConsultaAtualResponseDto.UltimaConsulta();
            ultimaConsulta.setConsultaId(ultimaConsultaEntity.getId());
            ultimaConsulta.setData(ultimaConsultaEntity.getDataHora().toLocalDate());

            // Tratamento da última consulta (mesmo do prontuário por enquanto)
            if (prontuario != null && prontuario.getTratamentos() != null) {
                String tratamentoUltima = prontuario.getTratamentos().stream()
                        .filter(t -> t.getFinalizado() == null || !t.getFinalizado())
                        .map(Tratamento::getTipoDeTratamento)
                        .findFirst()
                        .orElse(null);
                ultimaConsulta.setTratamento(tratamentoUltima);
            }

            response.setUltimaConsulta(ultimaConsulta);
        }

        return response;
    }

    public DetalhesConsultaAnteriorResponseDto buscarDetalhesConsultaAnterior(Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada!"));

        Paciente paciente = consulta.getPaciente();
        Funcionario funcionario = consulta.getFuncionario();

        // Buscar prontuário do paciente para pegar o tratamento
        Prontuario prontuario = prontuarioRepository.findByPacienteId(paciente.getId()).orElse(null);

        DetalhesConsultaAnteriorResponseDto response = new DetalhesConsultaAnteriorResponseDto();

        // Dados da consulta
        response.setConsultaId(consulta.getId());
        response.setData(consulta.getDataHora().toLocalDate());
        response.setHorarioInicio(consulta.getDataHora().toLocalTime());
        response.setHorarioFim(consulta.getDataHora().toLocalTime().plusHours(1));
        response.setEspecialidade(funcionario.getEspecialidade());
        response.setNomeProfissional(funcionario.getNome());
        response.setTipo(consulta.getTipo());
        response.setObservacoesComportamentais(consulta.getObservacoesComportamentais());

        // Tratamento atual (do prontuário)
        if (prontuario != null && prontuario.getTratamentos() != null) {
            String tratamentoAtual = prontuario.getTratamentos().stream()
                    .filter(t -> t.getFinalizado() == null || !t.getFinalizado())
                    .map(Tratamento::getTipoDeTratamento)
                    .findFirst()
                    .orElse(null);
            response.setTratamentoAtual(tratamentoAtual);
        }

        // Materiais utilizados na consulta
        if (consulta.getMateriais() != null && !consulta.getMateriais().isEmpty()) {
            List<String> materiais = consulta.getMateriais().stream()
                    .map(Material::getItem)
                    .toList();
            response.setMateriaisUtilizados(materiais);
        }

        return response;
    }

}
