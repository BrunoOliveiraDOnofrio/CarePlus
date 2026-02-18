package com.example.careplus.service;

import com.example.careplus.dto.dtoConsultaProntuario.*;
import com.example.careplus.dto.dtoConsultaRecorrente.ConflitoDatasDto;
import com.example.careplus.dto.dtoConsultaRecorrente.ConsultaRecorrenteRequestDto;
import com.example.careplus.dto.dtoConsultaRecorrente.ConsultaRecorrenteResponseDto;
import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.model.ConsultaProntuario;
import com.example.careplus.model.ClassificacaoDoencas;
import com.example.careplus.model.Cuidador;
import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Material;
import com.example.careplus.model.Medicacao;
import com.example.careplus.model.Paciente;
import com.example.careplus.model.FichaClinica;
import com.example.careplus.model.Tratamento;
import com.example.careplus.repository.ConsultaProntuarioRepository;
import com.example.careplus.repository.CuidadorRepository;
import com.example.careplus.repository.FuncionarioRepository;
import com.example.careplus.repository.PacienteRepository;
import com.example.careplus.repository.FichaClinicaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaProntuarioService {

    private final ConsultaProntuarioRepository consultaProntuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final EmailService emailService;
    private final FichaClinicaRepository fichaClinicaRepository;
    private final CuidadorRepository cuidadorRepository;
    private final S3Service s3Service;
    private final ObjectMapper objectMapper;

    public ConsultaProntuarioService(ConsultaProntuarioRepository consultaProntuarioRepository, PacienteRepository pacienteRepository, FuncionarioRepository funcionarioRepository, EmailService emailService, FichaClinicaRepository fichaClinicaRepository, CuidadorRepository cuidadorRepository, S3Service s3Service, ObjectMapper objectMapper) {
        this.consultaProntuarioRepository = consultaProntuarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.emailService = emailService;
        this.fichaClinicaRepository = fichaClinicaRepository;
        this.cuidadorRepository = cuidadorRepository;
        this.s3Service = s3Service;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    public ConsultaProntuarioResponseDto marcarConsulta(ConsultaProntuarioRequestDto request){
        // busca o paciente no banco
        Optional<Paciente> usuarioOpt = pacienteRepository.findById(request.getPacienteId());
        Paciente paciente = usuarioOpt.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));

        // busca o funcionário que foi escolhido
        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findById(request.getFuncionarioId());
        Funcionario funcionario = funcionarioOpt.orElseThrow(() -> new ResourceNotFoundException("Funcionario não encontrado!"));

        // pega a data e hora da consulta que querem marcar
        LocalDateTime dataHoraConsulta = request.getDataHora();
        LocalDate dataConsulta = dataHoraConsulta.toLocalDate();

        // busca todas as consultas que esse funcionário já tem nessa data
        List<ConsultaProntuario> consultasExistentes = consultaProntuarioRepository
                .buscarConsultasPorFuncionarioEData(funcionario.getId(), dataConsulta);

        // vamos ver se o funcionário escolhido tá livre no horário
        boolean temConflito = false;
        for (ConsultaProntuario consulta : consultasExistentes) {
            LocalDateTime inicioConsulta = consulta.getDataHora();
            LocalDateTime fimConsulta = inicioConsulta.plusHours(1); // consultas duram 1 hora
            LocalDateTime fimNovaConsulta = dataHoraConsulta.plusHours(1);

            // verifica se os horários se sobrepõem
            if (dataHoraConsulta.isBefore(fimConsulta) && fimNovaConsulta.isAfter(inicioConsulta)) {
                temConflito = true;
                break; // já achou conflito, pode parar
            }
        }

        // variável pra guardar qual funcionário vai atender (pode ser o original ou um substituto)
        Funcionario funcionarioAtribuido = funcionario;

        // se o funcionário escolhido tá ocupado, vamos procurar outro da mesma especialidade
        if (temConflito) {
            // busca todos os funcionários que têm a mesma especialidade
            List<Funcionario> funcionariosComMesmaEspecialidade = funcionarioRepository
                    .findByEspecialidadeIgnoreCase(funcionario.getEspecialidade());

            // variável pra marcar se achou alguém disponível
            Funcionario funcionarioDisponivel = null;

            // percorre cada funcionário pra ver se algum tá livre
            for (Funcionario func : funcionariosComMesmaEspecialidade) {
                // pula o funcionário original porque já sabemos que ele tá ocupado
                if (func.getId().equals(funcionario.getId())) {
                    continue;
                }

                // busca as consultas que esse outro funcionário tem na data
                List<ConsultaProntuario> consultasFuncionario = consultaProntuarioRepository
                        .buscarConsultasPorFuncionarioEData(func.getId(), dataConsulta);

                // verifica se esse funcionário tem conflito no horário
                boolean funcionarioTemConflito = false;
                for (ConsultaProntuario cons : consultasFuncionario) {
                    LocalDateTime inicioConsulta = cons.getDataHora();
                    LocalDateTime fimConsulta = inicioConsulta.plusHours(1);
                    LocalDateTime fimNovaConsulta = dataHoraConsulta.plusHours(1);

                    // checa se os horários batem
                    if (dataHoraConsulta.isBefore(fimConsulta) && fimNovaConsulta.isAfter(inicioConsulta)) {
                        funcionarioTemConflito = true;
                        break; // esse também tá ocupado
                    }
                }

                // se esse funcionário tá livre, escolhe ele
                if (!funcionarioTemConflito) {
                    funcionarioDisponivel = func;
                    break; // achou alguém disponível, pode parar
                }
            }

            // se achou alguém disponível, usa ele
            if (funcionarioDisponivel != null) {
                funcionarioAtribuido = funcionarioDisponivel;
            } else {
                // nenhum profissional da especialidade tá disponível no horário
                throw new ResourceNotFoundException(
                        String.format("Horário indisponível - Nenhum funcionário da especialidade %s disponível às %s",
                                funcionario.getEspecialidade(),
                                dataHoraConsulta.toLocalTime().toString())
                );
            }
        }

        // cria a nova consulta
        ConsultaProntuario novaConsulta = new ConsultaProntuario();
        novaConsulta.setPaciente(paciente);
        novaConsulta.setFuncionario(funcionarioAtribuido); // pode ser o original ou um substituto
        novaConsulta.setDataHora(request.getDataHora());
        novaConsulta.setConfirmada(null);
        novaConsulta.setTipo(request.getTipo());

        // salva no banco
        ConsultaProntuario salvo = consultaProntuarioRepository.save(novaConsulta);

        // envia email de notificação pro funcionário que vai atender
        emailService.EnviarNotificacaoConsultaProntuario(funcionarioAtribuido, novaConsulta, paciente);

        // retorna a consulta criada com todos os detalhes
        return ConsultaProntuarioMapper.toResponseDto(salvo);
    }


    public ConsultaProntuarioResponseDto revisarConsulta(ConsultaProntuarioRequestDto request){
        Optional<Paciente> usuarioOpt = pacienteRepository.findById(request.getPacienteId());
        Paciente paciente = usuarioOpt.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));

        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findById(request.getFuncionarioId());
        Funcionario funcionario = funcionarioOpt.orElseThrow(() -> new ResourceNotFoundException("Funcionario não encontrado!"));

        ConsultaProntuario novaConsulta = new ConsultaProntuario();
        novaConsulta.setPaciente(paciente);
        novaConsulta.setFuncionario(funcionario);
        novaConsulta.setDataHora(request.getDataHora());
        novaConsulta.setConfirmada(null);
        novaConsulta.setTipo(request.getTipo() != null ? request.getTipo() : "Pendente");

        return ConsultaProntuarioMapper.toResponseDto(novaConsulta);
    }

    public void removerConsulta(Long consultaId){
        boolean consulta = consultaProntuarioRepository.existsById(consultaId);
        if (!consulta){
            throw new RuntimeException("Consulta não encontrada");
        }
        consultaProntuarioRepository.deleteById(consultaId);
    }

    public List<ConsultaProntuario> listarConsultas(){
        return consultaProntuarioRepository.findAll();
    }

    public List<ConsultaProntuarioResponseDto> listarPorData(){
        List<ConsultaProntuario> consultas = consultaProntuarioRepository.buscarPorData();

        if (consultas.isEmpty()){
            throw new ResourceNotFoundException("Nenhuma consulta cadastrada!");
        }

        return ConsultaProntuarioMapper.toResponseDto(consultas);
    }

    public List<ConsultaProntuarioResponseDto> listarPorPaciente(Long idPaciente){
        List<ConsultaProntuario> consultas = consultaProntuarioRepository.buscarPorPaciente(idPaciente);
        if (consultas.isEmpty()){
            throw new ResourceNotFoundException("Nenhuma consulta cadastrada para esse paciente!");
        }
        return ConsultaProntuarioMapper.toResponseDto(consultas);
    }

    public List<ConsultaProntuarioResponseDto> consultasDoDia(Long idFuncionario){
        List<ConsultaProntuario> consultas = consultaProntuarioRepository.consultasDoDia(idFuncionario);
        if (consultas.isEmpty()){
            throw new ResourceNotFoundException("Nenhuma consulta para esse funcionario hoje!");
        }
        return ConsultaProntuarioMapper.toResponseDto(consultas);
    }

    public ConsultaProntuarioResponseDto editarConsulta(Long consultaId, ConsultaProntuarioRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));
        Funcionario funcionario = funcionarioRepository.findById(request.getFuncionarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario não encontrado"));
        ConsultaProntuario consulta = consultaProntuarioRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));
        consulta.setPaciente(paciente);
        consulta.setFuncionario(funcionario);
        consulta.setDataHora(request.getDataHora());
        consulta.setTipo("Retorno");
        consulta.setConfirmada(consulta.getConfirmada() != null ? consulta.getConfirmada() : Boolean.FALSE);

        ConsultaProntuario salva = consultaProntuarioRepository.save(consulta);
        return ConsultaProntuarioMapper.toResponseDto(salva);
    }

    public ConsultaProntuarioResponseDto confirmarConsulta(Long consultaId){
        Optional<ConsultaProntuario> consultaParaAtualizar = consultaProntuarioRepository.findById(consultaId);
        if (consultaParaAtualizar.isPresent()){
            ConsultaProntuario consulta = consultaParaAtualizar.get();
            consulta.setConfirmada(true);

            ConsultaProntuario consultaSalva = consultaProntuarioRepository.save(consulta);
            return ConsultaProntuarioMapper.toResponseDto(consultaSalva);
        } else {
            throw new RuntimeException("Consulta não encontrada");
        }
    }

    public ConsultaProntuarioResponseDto recusarConsulta(Long consultaId, String justificativa){
        Optional<ConsultaProntuario> consultaParaAtualizar = consultaProntuarioRepository.findById(consultaId);
        if (consultaParaAtualizar.isPresent()){
            ConsultaProntuario consulta = consultaParaAtualizar.get();
            consulta.setObservacoesComportamentais(justificativa);
            consulta.setConfirmada(false);

            ConsultaProntuario consultaSalva = consultaProntuarioRepository.save(consulta);
            return ConsultaProntuarioMapper.toResponseDto(consultaSalva);
        } else {
            throw new RuntimeException("Consulta não encontrada");
        }
    }

    public ConsultaProntuarioResponseDto salvarObservacoes(Long consultaId, RealizarConsultaProntuarioDto info) throws JsonProcessingException {
        Optional<ConsultaProntuario> consultaParaAtualizar = consultaProntuarioRepository.findById(consultaId);
        if (consultaParaAtualizar.isPresent()){
            ConsultaProntuario consulta = consultaParaAtualizar.get();
            consulta.setObservacoesComportamentais(info.getObservacao());
            consulta.setPresenca(true);
            consulta.setConfirmada(true);

            ConsultaProntuario consultaSalva = consultaProntuarioRepository.save(consulta);

            String json = objectMapper.writeValueAsString(consultaSalva);

            s3Service.uploadJson(
                    "bucket-prontuarios-1",
                    "pacienteId" + consultaSalva.getPaciente().getId() + "/" + consultaSalva.getDataHora() + ".json",
                    json
            );

            return ConsultaProntuarioMapper.toResponseDto(consultaSalva);
        } else {
            throw new RuntimeException("Consulta não encontrada");
        }
    }

    @Transactional
    public ConsultaRecorrenteResponseDto criarConsultasRecorrentes(ConsultaRecorrenteRequestDto dto) {
        // prepara o objeto que vai guardar a resposta
        ConsultaRecorrenteResponseDto response = new ConsultaRecorrenteResponseDto();

        // busca o paciente no banco, se n achar, retorna erro
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado!"));

        // busca o funcionário que foi escolhido como preferencia
        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado!"));

        // primeira passagem: aqui a gente só verifica se vai dar pra marcar todas as consultas
        // se alguma data não tiver profissional disponível, a gnt já avisa antes de criar qualquer consulta
        for (LocalDate data : dto.getDatas()) {
            // junta a data com o horário para ter o momento exato da consulta
            LocalDateTime dataHora = LocalDateTime.of(data, dto.getHorario());

            // busca todas as consultas que esse funcionário já tem nessa data
            List<ConsultaProntuario> consultasExistentes = consultaProntuarioRepository
                    .buscarConsultasPorFuncionarioEData(dto.getFuncionarioId(), data);

            // vamos ver se tem alguma consulta que bate com o horário que a gente quer
            boolean temConflito = false;
            for (ConsultaProntuario consulta : consultasExistentes) {
                // pega o início e fim da consulta que já existe
                LocalDateTime inicioConsulta = consulta.getDataHora();
                LocalDateTime fimConsulta = inicioConsulta.plusHours(1); // consultas duram 1 hora
                LocalDateTime fimNovaConsulta = dataHora.plusHours(1);

                // verifica se os horários se sobrepõem
                temConflito = (dataHora.isBefore(fimConsulta) && fimNovaConsulta.isAfter(inicioConsulta));

                if (temConflito) {
                    break; // já achou conflito, não precisa verificar o resto
                }
            }

            // se o funcionário escolhido tá ocupado, vamos procurar outro da mesma especialidade
            if (temConflito) {
                // busca todos os funcionários que têm a mesma especialidade
                List<Funcionario> funcionariosComMesmaEspecialidade = funcionarioRepository
                        .findByEspecialidadeIgnoreCase(funcionario.getEspecialidade());

                // variável pra guardar o funcionário que estiver livre
                Funcionario funcionarioDisponivel = null;

                // percorre cada funcionário pra ver se algum tá livre
                for (Funcionario func : funcionariosComMesmaEspecialidade) {
                    // pula o funcionário original porque já sabemos que ele tá ocupado
                    if (func.getId().equals(funcionario.getId())) {
                        continue;
                    }

                    // busca as consultas que esse outro funcionário tem na data
                    List<ConsultaProntuario> consultasFuncionario = consultaProntuarioRepository
                            .buscarConsultasPorFuncionarioEData(func.getId(), data);

                    // verifica se esse funcionário tem conflito no horário
                    boolean funcionarioTemConflito = false;
                    for (ConsultaProntuario consulta : consultasFuncionario) {
                        LocalDateTime inicioConsulta = consulta.getDataHora();
                        LocalDateTime fimConsulta = inicioConsulta.plusHours(1);
                        LocalDateTime fimNovaConsulta = dataHora.plusHours(1);

                        // checa se os horários batem
                        if (dataHora.isBefore(fimConsulta) && fimNovaConsulta.isAfter(inicioConsulta)) {
                            funcionarioTemConflito = true;
                            break; // esse também tá ocupado
                        }
                    }

                    // se esse funcionário tá livre, a gente escolhe ele
                    if (!funcionarioTemConflito) {
                        funcionarioDisponivel = func;
                        break; // achou alguém disponível, pode parar de procurar
                    }
                }

                // se nenhum funcionário da especialidade tá disponível, anota como conflito
                if (funcionarioDisponivel == null) {
                    ConflitoDatasDto conflito = new ConflitoDatasDto(
                            data,
                            dto.getHorario().toString(),
                            String.format("Horário indisponível - Nenhum funcionário da especialidade %s disponível às %s",
                                    funcionario.getEspecialidade(),
                                    dataHora.toLocalTime().toString())
                    );
                    response.getDatasComConflito().add(conflito);
                }
            }
        }

        // se achou alguma data que não tem profissional disponível, retorna os conflitos e não cria nada
        if (!response.getDatasComConflito().isEmpty()) {
            response.setTotalConsultasCriadas(0);
            response.setTotalFalhas(response.getDatasComConflito().size());
            return response;
        }

        // segunda passagem: agora sim, vamos criar todas as consultas
        // já sabemos que tem profissional disponível pra todas as datas
        for (LocalDate data : dto.getDatas()) {
            LocalDateTime dataHora = LocalDateTime.of(data, dto.getHorario());

            // busca as consultas do funcionário original nessa data
            List<ConsultaProntuario> consultasExistentes = consultaProntuarioRepository
                    .buscarConsultasPorFuncionarioEData(dto.getFuncionarioId(), data);

            // começa assumindo que vai marcar com o funcionário escolhido
            Funcionario funcionarioAtribuido = funcionario;
            boolean temConflito = false;

            // verifica se o funcionário original tem conflito
            for (ConsultaProntuario consulta : consultasExistentes) {
                LocalDateTime inicioConsulta = consulta.getDataHora();
                LocalDateTime fimConsulta = inicioConsulta.plusHours(1);
                LocalDateTime fimNovaConsulta = dataHora.plusHours(1);

                if (dataHora.isBefore(fimConsulta) && fimNovaConsulta.isAfter(inicioConsulta)) {
                    temConflito = true;
                    break;
                }
            }

            // se tem conflito, procura outro funcionário disponível
            if (temConflito) {
                // busca os funcionários da mesma especialidade de novo
                List<Funcionario> funcionariosComMesmaEspecialidade = funcionarioRepository
                        .findByEspecialidadeIgnoreCase(funcionario.getEspecialidade());

                // testa cada um até achar um livre
                for (Funcionario func : funcionariosComMesmaEspecialidade) {
                    // pula o original
                    if (func.getId().equals(funcionario.getId())) {
                        continue;
                    }

                    // pega as consultas desse funcionário
                    List<ConsultaProntuario> consultasFuncionario = consultaProntuarioRepository
                            .buscarConsultasPorFuncionarioEData(func.getId(), data);

                    boolean funcionarioTemConflito = false;
                    for (ConsultaProntuario cons : consultasFuncionario) {
                        LocalDateTime inicioConsulta = cons.getDataHora();
                        LocalDateTime fimConsulta = inicioConsulta.plusHours(1);
                        LocalDateTime fimNovaConsulta = dataHora.plusHours(1);

                        if (dataHora.isBefore(fimConsulta) && fimNovaConsulta.isAfter(inicioConsulta)) {
                            funcionarioTemConflito = true;
                            break;
                        }
                    }

                    // se esse tá livre, usa ele
                    if (!funcionarioTemConflito) {
                        funcionarioAtribuido = func;
                        break; // achou, pode parar
                    }
                }
            }

            // cria a consulta nova
            ConsultaProntuario consulta = new ConsultaProntuario();
            consulta.setPaciente(paciente);
            consulta.setFuncionario(funcionarioAtribuido); // pode ser o original ou um substituto
            consulta.setDataHora(dataHora);
            consulta.setTipo(dto.getTipo());
            consulta.setPresenca(false);
            consulta.setConfirmada(false); // começa como não confirmada

            // salva no banco
            ConsultaProntuario consultaSalva = consultaProntuarioRepository.save(consulta);

            // converte pra dto e adiciona na lista de consultas criadas
            // assim a resposta vai ter todas as informações, incluindo o nome do médico
            ConsultaProntuarioResponseDto consultaDto = ConsultaProntuarioMapper.toResponseDto(consultaSalva);
            response.getConsultasCriadas().add(consultaDto);
        }

        // atualiza os totais e retorna tudo certinho
        response.setTotalConsultasCriadas(response.getConsultasCriadas().size());
        response.setTotalFalhas(0);
        return response;
    }

    public List<ConsultaProntuarioResponseDto> listarAgendaSemanal(Long funcionarioId, LocalDate dataReferencia) {
        LocalDate inicioDaSemana = dataReferencia.minusDays(dataReferencia.getDayOfWeek().getValue() - 1);
        LocalDate fimDaSemana = inicioDaSemana.plusDays(6);

        LocalDateTime inicioDateTime = inicioDaSemana.atStartOfDay();
        LocalDateTime fimDateTime = fimDaSemana.atTime(23, 59, 59);

        List<ConsultaProntuario> consultas = consultaProntuarioRepository.buscarConsultasPorFuncionarioEPeriodo(
                funcionarioId,
                inicioDateTime,
                fimDateTime
        );

        if (consultas.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma consulta encontrada para este período!");
        }

        return ConsultaProntuarioMapper.toResponseDto(consultas);
    }

    public List<ConsultaProntuarioResponseDto> listarConsultasPendentes(Long idFuncionario) {
        List<ConsultaProntuario> consultas = consultaProntuarioRepository.findByFuncionarioIdAndConfirmadaNull(idFuncionario);
        return ConsultaProntuarioMapper.toResponseDto(consultas);
    }

    public ProximaConsultaProntuarioResponseDto buscarProximaConsultaConfirmada(Long pacienteId) {
        List<ConsultaProntuario> consultas = consultaProntuarioRepository.buscarProximaConsultaPorPaciente(pacienteId);

        if (consultas.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma consulta confirmada encontrada para este paciente!");
        }

        ConsultaProntuario proximaConsulta = consultas.get(0);

        String tratamento = null;
        FichaClinica fichaClinica = fichaClinicaRepository.findByPacienteId(pacienteId).orElse(null);
        if (fichaClinica != null && fichaClinica.getTratamentos() != null) {
            tratamento = fichaClinica.getTratamentos().stream()
                    .filter(t -> t.getFinalizado() == null || !t.getFinalizado())
                    .map(Tratamento::getTipoDeTratamento)
                    .findFirst()
                    .orElse(null);
        }

        return new ProximaConsultaProntuarioResponseDto(
                proximaConsulta.getId(),
                proximaConsulta.getDataHora().toLocalDate(),
                proximaConsulta.getDataHora().toLocalTime(),
                proximaConsulta.getDataHora().toLocalTime().plusHours(1),
                proximaConsulta.getTipo(),
                proximaConsulta.getFuncionario().getNome(),
                tratamento
        );
    }

    public ConsultaProntuarioAtualResponseDto buscarDetalhesConsultaAtual(Long consultaId) {
        ConsultaProntuario consulta = consultaProntuarioRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada!"));

        Paciente paciente = consulta.getPaciente();
        Funcionario funcionario = consulta.getFuncionario();

        FichaClinica fichaClinica = fichaClinicaRepository.findByPacienteId(paciente.getId()).orElse(null);

        ConsultaProntuarioAtualResponseDto response = new ConsultaProntuarioAtualResponseDto();

        response.setConsultaId(consulta.getId());
        response.setData(consulta.getDataHora().toLocalDate());
        response.setHorarioInicio(consulta.getDataHora().toLocalTime());
        response.setHorarioFim(consulta.getDataHora().toLocalTime().plusHours(1));
        response.setTipo(consulta.getTipo());
        response.setEspecialidade(funcionario.getEspecialidade());
        response.setNomeProfissional(funcionario.getNome());

        if (fichaClinica != null && fichaClinica.getTratamentos() != null) {
            String tratamentoAtual = fichaClinica.getTratamentos().stream()
                    .filter(t -> t.getFinalizado() == null || !t.getFinalizado())
                    .map(Tratamento::getTipoDeTratamento)
                    .findFirst()
                    .orElse(null);
            response.setTratamentoAtual(tratamentoAtual);
        }

        ConsultaProntuarioAtualResponseDto.DadosPaciente dadosPaciente = new ConsultaProntuarioAtualResponseDto.DadosPaciente();
        dadosPaciente.setPacienteId(paciente.getId());
        dadosPaciente.setNome(paciente.getNome());

        List<Cuidador> cuidadores = cuidadorRepository.findByPacienteId(paciente.getId());
        if (!cuidadores.isEmpty() && cuidadores.get(0).getResponsavel() != null) {
            dadosPaciente.setContato(cuidadores.get(0).getResponsavel().getTelefone());
        }

        if (paciente.getDtNascimento() != null) {
            int idade = java.time.Period.between(paciente.getDtNascimento(), java.time.LocalDate.now()).getYears();
            dadosPaciente.setIdade(idade);
        }

        if (fichaClinica != null) {
            dadosPaciente.setDesfraldado(fichaClinica.getDesfraldado());
            dadosPaciente.setHiperfocoAtual(fichaClinica.getHiperfoco());
            dadosPaciente.setDiagnostico(fichaClinica.getDiagnostico());

            if (fichaClinica.getCid() != null && !fichaClinica.getCid().isEmpty()) {
                String cids = fichaClinica.getCid().stream()
                        .map(ClassificacaoDoencas::getCid)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse(null);
                dadosPaciente.setCid(cids);
            }

            if (fichaClinica.getMedicacoes() != null && !fichaClinica.getMedicacoes().isEmpty()) {
                String medicacoes = fichaClinica.getMedicacoes().stream()
                        .filter(Medicacao::getAtivo)
                        .map(Medicacao::getNomeMedicacao)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse(null);
                dadosPaciente.setMedicacoes(medicacoes);
            }

            if (fichaClinica.getNivelAgressividade() != null && fichaClinica.getNivelAgressividade() > 0) {
                dadosPaciente.setAtendimentoEspecial("Lesivo");
            }
        }

        response.setDadosPaciente(dadosPaciente);

        List<ConsultaProntuario> ultimasConsultas = consultaProntuarioRepository.buscarUltimaConsultaPassadaPorPaciente(paciente.getId());
        if (!ultimasConsultas.isEmpty()) {
            ConsultaProntuario ultimaConsultaEntity = ultimasConsultas.get(0);
            ConsultaProntuarioAtualResponseDto.UltimaConsulta ultimaConsulta = new ConsultaProntuarioAtualResponseDto.UltimaConsulta();
            ultimaConsulta.setConsultaId(ultimaConsultaEntity.getId());
            ultimaConsulta.setData(ultimaConsultaEntity.getDataHora().toLocalDate());

            if (fichaClinica != null && fichaClinica.getTratamentos() != null) {
                String tratamentoUltima = fichaClinica.getTratamentos().stream()
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

    public DetalhesConsultaProntuarioAnteriorResponseDto buscarDetalhesConsultaAnterior(Long consultaId) {
        ConsultaProntuario consulta = consultaProntuarioRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada!"));

        Paciente paciente = consulta.getPaciente();
        Funcionario funcionario = consulta.getFuncionario();

        FichaClinica fichaClinica = fichaClinicaRepository.findByPacienteId(paciente.getId()).orElse(null);

        DetalhesConsultaProntuarioAnteriorResponseDto response = new DetalhesConsultaProntuarioAnteriorResponseDto();

        response.setConsultaId(consulta.getId());
        response.setData(consulta.getDataHora().toLocalDate());
        response.setHorarioInicio(consulta.getDataHora().toLocalTime());
        response.setHorarioFim(consulta.getDataHora().toLocalTime().plusHours(1));
        response.setEspecialidade(funcionario.getEspecialidade());
        response.setNomeProfissional(funcionario.getNome());
        response.setTipo(consulta.getTipo());
        response.setObservacoesComportamentais(consulta.getObservacoesComportamentais());

        if (fichaClinica != null && fichaClinica.getTratamentos() != null) {
            String tratamentoAtual = fichaClinica.getTratamentos().stream()
                    .filter(t -> t.getFinalizado() == null || !t.getFinalizado())
                    .map(Tratamento::getTipoDeTratamento)
                    .findFirst()
                    .orElse(null);
            response.setTratamentoAtual(tratamentoAtual);
        }

        if (consulta.getMateriais() != null && !consulta.getMateriais().isEmpty()) {
            List<String> materiais = consulta.getMateriais().stream()
                    .map(Material::getItem)
                    .toList();
            response.setMateriaisUtilizados(materiais);
        }

        return response;
    }

}

