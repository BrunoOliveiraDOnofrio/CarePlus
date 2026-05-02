package com.example.careplus.service;

import com.example.careplus.dto.dtoConsultaProntuario.*;
import com.example.careplus.dto.dtoConsultaRecorrente.ConflitoDatasDto;
import com.example.careplus.dto.dtoConsultaRecorrente.ConsultaRecorrenteRequestDto;
import com.example.careplus.dto.dtoConsultaRecorrente.ConsultaRecorrenteResponseDto;
import com.example.careplus.dto.messaging.EventoConsultaCriadaDto;
import com.example.careplus.dto.messaging.ConsultaCriadaMensagemDto;
import com.example.careplus.dto.messaging.PacienteMensagemDto;
import com.example.careplus.dto.messaging.ProfissionalMensagemDto;
import com.example.careplus.dto.messaging.ResponsavelMensagemDto;
import com.example.careplus.messaging.ConsultaCriadaRabbitProducer;
import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.model.ConsultaProntuario;
import com.example.careplus.model.Cuidador;
import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Material;
import com.example.careplus.model.Paciente;
import com.example.careplus.model.FichaClinica;
import com.example.careplus.model.ConsultaFuncionario;
import com.example.careplus.repository.ConsultaFuncionarioRepository;
import com.example.careplus.repository.ConsultaProntuarioRepository;
import com.example.careplus.repository.CuidadorRepository;
import com.example.careplus.repository.FuncionarioRepository;
import com.example.careplus.repository.PacienteRepository;
import com.example.careplus.repository.FichaClinicaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.careplus.dto.dtoConsultaRecorrente.AgendarConsultasRequestDto;
import com.example.careplus.dto.dtoConsultaRecorrente.AgendarConsultasResponseDto;
import com.example.careplus.dto.dtoConsultaRecorrente.ConsultaItemRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConsultaProntuarioService {

    private final ConsultaProntuarioRepository consultaProntuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final FichaClinicaRepository fichaClinicaRepository;
    private final CuidadorRepository cuidadorRepository;
    private final S3Service s3Service;
    private final ObjectMapper objectMapper;
    private final ConsultaCriadaRabbitProducer consultaCriadaRabbitProducer;
    private final ConsultaFuncionarioRepository consultaFuncionarioRepository;

    public ConsultaProntuarioService(ConsultaProntuarioRepository consultaProntuarioRepository, PacienteRepository pacienteRepository, FuncionarioRepository funcionarioRepository, FichaClinicaRepository fichaClinicaRepository, CuidadorRepository cuidadorRepository, S3Service s3Service, ObjectMapper objectMapper, ConsultaCriadaRabbitProducer consultaCriadaRabbitProducer, ConsultaFuncionarioRepository consultaFuncionarioRepository) {
        this.consultaProntuarioRepository = consultaProntuarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.fichaClinicaRepository = fichaClinicaRepository;
        this.cuidadorRepository = cuidadorRepository;
        this.s3Service = s3Service;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
        this.consultaCriadaRabbitProducer = consultaCriadaRabbitProducer;
        this.consultaFuncionarioRepository = consultaFuncionarioRepository;
    }

    private ConsultaCriadaMensagemDto toMensagemDto(ConsultaProntuarioResponseDto consulta) {
        String dataHora = (consulta.getData() != null && consulta.getHorarioInicio() != null)
                ? consulta.getData() + " " + consulta.getHorarioInicio()
                : null;
        ResponsavelMensagemDto responsavel = cuidadorRepository
                .findResponsavelNomeTelefoneByPacienteId(consulta.getPaciente().getId())
                .stream()
                .findFirst()
                .map(row -> new ResponsavelMensagemDto((String) row[0], (String) row[1]))
                .orElse(null);
        PacienteMensagemDto paciente = new PacienteMensagemDto(
                consulta.getPaciente().getNome(),
                responsavel
        );
        List<ProfissionalMensagemDto> profissionais = consulta.getFuncionarios() != null
                ? consulta.getFuncionarios().stream()
                        .map(f -> new ProfissionalMensagemDto(f.getNome(), f.getEspecialidade()))
                        .toList()
                : List.of();
        return new ConsultaCriadaMensagemDto(
                paciente,
                profissionais,
                dataHora,
                consulta.getTipo()
        );
    }

    /** Cria um vínculo ConsultaFuncionario apenas se ele ainda não existir. */
    private ConsultaFuncionario vincularFuncionario(Funcionario funcionario, ConsultaProntuario consulta) {
        if (consultaFuncionarioRepository.existsByConsultaIdAndFuncionarioId(consulta.getId(), funcionario.getId())) {
            throw new RuntimeException("Funcionário " + funcionario.getNome() + " já está vinculado a esta consulta.");
        }
        ConsultaFuncionario cf = new ConsultaFuncionario(funcionario, consulta);
        return consultaFuncionarioRepository.save(cf);
    }

    public ConsultaProntuarioResponseDto marcarConsulta(ConsultaProntuarioRequestDto request){

        Optional<Paciente> usuarioOpt = pacienteRepository.findById(request.getPacienteId());
        Paciente paciente = usuarioOpt.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));


        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findById(request.getFuncionarioId());
        Funcionario funcionario = funcionarioOpt.orElseThrow(() -> new ResourceNotFoundException("Funcionario não encontrado!"));


        LocalDateTime dataHoraConsulta = LocalDateTime.of(request.getData(), request.getHorarioInicio());
        LocalDate dataConsulta = request.getData();


        List<ConsultaProntuario> consultasExistentes = consultaProntuarioRepository
                .buscarConsultasPorFuncionarioEData(funcionario.getId(), dataConsulta);


        boolean temConflito = false;
        for (ConsultaProntuario consulta : consultasExistentes) {
            LocalDateTime inicioConsulta = consulta.getDataHoraInicio();
            LocalDateTime fimConsulta = consulta.getHorarioFim() != null
                    ? LocalDateTime.of(consulta.getData(), consulta.getHorarioFim())
                    : inicioConsulta.plusHours(1);
            LocalDateTime fimNovaConsulta = LocalDateTime.of(request.getData(), request.getHorarioFim() != null ? request.getHorarioFim() : request.getHorarioInicio().plusHours(1));

            if (dataHoraConsulta.isBefore(fimConsulta) && fimNovaConsulta.isAfter(inicioConsulta)) {
                temConflito = true;
                break;
            }
        }


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
                    LocalDateTime inicioConsulta = cons.getDataHoraInicio();
                    LocalDateTime fimConsulta = cons.getHorarioFim() != null
                            ? LocalDateTime.of(cons.getData(), cons.getHorarioFim())
                            : inicioConsulta.plusHours(1);
                    LocalDateTime fimNovaConsulta = LocalDateTime.of(request.getData(), request.getHorarioFim() != null ? request.getHorarioFim() : request.getHorarioInicio().plusHours(1));

                    if (dataHoraConsulta.isBefore(fimConsulta) && fimNovaConsulta.isAfter(inicioConsulta)) {
                        funcionarioTemConflito = true;
                        break;
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
                                request.getHorarioInicio().toString())
                );
            }
        }

        // cria a nova consulta
        ConsultaProntuario novaConsulta = new ConsultaProntuario();
        novaConsulta.setPaciente(paciente);
        novaConsulta.setData(request.getData());
        novaConsulta.setHorarioInicio(request.getHorarioInicio());
        novaConsulta.setHorarioFim(request.getHorarioFim());
        novaConsulta.setConfirmada(null);
        novaConsulta.setTipo(request.getTipo());

        // salva no banco
        ConsultaProntuario salvo = consultaProntuarioRepository.save(novaConsulta);        ConsultaFuncionario consultaFuncionario = vincularFuncionario(funcionarioAtribuido, salvo);
        salvo.getConsultaFuncionarios().add(consultaFuncionario);

        // envia email de notificação pro funcionário que vai atender
//        emailService.EnviarNotificacaoConsultaProntuario(funcionarioAtribuido, novaConsulta, paciente);

        // retorna a consulta criada com todos os detalhes
        ConsultaProntuarioResponseDto responseDto = ConsultaProntuarioMapper.toResponseDto(salvo);

        return responseDto;
    }


    public ConsultaProntuarioResponseDto revisarConsulta(ConsultaProntuarioRequestDto request){
        Optional<Paciente> usuarioOpt = pacienteRepository.findById(request.getPacienteId());
        Paciente paciente = usuarioOpt.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));

        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findById(request.getFuncionarioId());
        Funcionario funcionario = funcionarioOpt.orElseThrow(() -> new ResourceNotFoundException("Funcionario não encontrado!"));

        ConsultaProntuario novaConsulta = new ConsultaProntuario();
        novaConsulta.setPaciente(paciente);
        novaConsulta.setData(request.getData());
        novaConsulta.setHorarioInicio(request.getHorarioInicio());
        novaConsulta.setHorarioFim(request.getHorarioFim());
        novaConsulta.setConfirmada(null);
        novaConsulta.setTipo(request.getTipo() != null ? request.getTipo() : "Pendente");

        // adiciona o funcionário como ConsultaFuncionario para que o mapper consiga montar o DTO
        ConsultaFuncionario cf = new ConsultaFuncionario(funcionario, novaConsulta);
        novaConsulta.getConsultaFuncionarios().add(cf);

        return ConsultaProntuarioMapper.toResponseDto(novaConsulta);
    }

    public void removerConsulta(Long consultaId){
        boolean consulta = consultaProntuarioRepository.existsById(consultaId);
        if (!consulta){
            throw new RuntimeException("Consulta não encontrada");
        }
        consultaProntuarioRepository.deleteById(consultaId);
    }

    @Transactional
    public void editarRecorrencia(String recorrenciaId, ConsultaProntuarioRequest request) {
        List<ConsultaProntuario> consultas = consultaProntuarioRepository.findByRecorrenciaId(recorrenciaId);
        if (consultas.isEmpty()) {
            throw new ResourceNotFoundException("Recorrência não encontrada");
        }
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));

        for (ConsultaProntuario consulta : consultas) {
            consulta.setPaciente(paciente);
            consulta.setHorarioInicio(request.getHorarioInicio());
            consulta.setHorarioFim(request.getHorarioFim());
            consulta.setTipo(request.getTipo() != null ? request.getTipo() : consulta.getTipo());
            consultaProntuarioRepository.save(consulta);

            List<ConsultaFuncionario> existentes = consultaFuncionarioRepository.findByConsultaId(consulta.getId());
            consultaFuncionarioRepository.deleteAllInBatch(existentes);

            List<Long> ids = request.getFuncionarioIds();
            if (ids != null && !ids.isEmpty()) {
                for (Long funcId : ids) {
                    Funcionario funcionario = funcionarioRepository.findById(funcId)
                            .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado: " + funcId));
                    consultaFuncionarioRepository.save(new ConsultaFuncionario(funcionario, consulta));
                }
            }
        }
    }

    @Transactional
    public void removerRecorrencia(String recorrenciaId) {
        List<ConsultaProntuario> consultas = consultaProntuarioRepository.findByRecorrenciaId(recorrenciaId);
        if (consultas.isEmpty()) {
            throw new ResourceNotFoundException("Recorrência não encontrada");
        }
        consultaProntuarioRepository.deleteAll(consultas);
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

    @Transactional
    public ConsultaProntuarioResponseDto editarConsulta(Long consultaId, ConsultaProntuarioRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));
        ConsultaProntuario consulta = consultaProntuarioRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));

        consulta.setPaciente(paciente);
        consulta.setData(request.getData());
        consulta.setHorarioInicio(request.getHorarioInicio());
        consulta.setHorarioFim(request.getHorarioFim());
        consulta.setTipo(request.getTipo() != null ? request.getTipo() : consulta.getTipo());
        consulta.setConfirmada(consulta.getConfirmada() != null ? consulta.getConfirmada() : Boolean.FALSE);

        consultaProntuarioRepository.save(consulta);

        // Substitui todos os vínculos com profissionais diretamente pelo repositório
        List<ConsultaFuncionario> existentes = consultaFuncionarioRepository.findByConsultaId(consultaId);
        consultaFuncionarioRepository.deleteAllInBatch(existentes);

        List<Long> ids = request.getFuncionarioIds();
        if (ids != null && !ids.isEmpty()) {
            for (Long funcId : ids) {
                Funcionario funcionario = funcionarioRepository.findById(funcId)
                        .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado: " + funcId));
                consultaFuncionarioRepository.save(new ConsultaFuncionario(funcionario, consulta));
            }
        }

        // Re-busca para retornar com a coleção atualizada
        ConsultaProntuario atualizada = consultaProntuarioRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));
        return ConsultaProntuarioMapper.toResponseDto(atualizada);
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
                     "prontuarios/pacienteId" + consultaSalva.getPaciente().getId() + "/" + consultaSalva.getData() + "_" + consultaSalva.getHorarioInicio() + ".json",
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

            boolean temConflito = false;
            for (ConsultaProntuario consulta : consultasExistentes) {
                LocalDateTime inicioConsulta = consulta.getDataHoraInicio();
                LocalDateTime fimConsulta = consulta.getHorarioFim() != null
                        ? LocalDateTime.of(consulta.getData(), consulta.getHorarioFim())
                        : inicioConsulta.plusHours(1);
                LocalDateTime fimNovaConsulta = dto.getHorarioFim() != null
                        ? LocalDateTime.of(data, dto.getHorarioFim())
                        : dataHora.plusHours(1);

                temConflito = (dataHora.isBefore(fimConsulta) && fimNovaConsulta.isAfter(inicioConsulta));

                if (temConflito) {
                    break;
                }
            }

            if (temConflito) {
                List<Funcionario> funcionariosComMesmaEspecialidade = funcionarioRepository
                        .findByEspecialidadeIgnoreCase(funcionario.getEspecialidade());

                Funcionario funcionarioDisponivel = null;

                for (Funcionario func : funcionariosComMesmaEspecialidade) {
                    if (func.getId().equals(funcionario.getId())) {
                        continue;
                    }

                    List<ConsultaProntuario> consultasFuncionario = consultaProntuarioRepository
                            .buscarConsultasPorFuncionarioEData(func.getId(), data);

                    boolean funcionarioTemConflito = false;
                    for (ConsultaProntuario consulta : consultasFuncionario) {
                        LocalDateTime inicioConsulta = consulta.getDataHoraInicio();
                        LocalDateTime fimConsulta = consulta.getHorarioFim() != null
                                ? LocalDateTime.of(consulta.getData(), consulta.getHorarioFim())
                                : inicioConsulta.plusHours(1);
                        LocalDateTime fimNovaConsulta = dto.getHorarioFim() != null
                                ? LocalDateTime.of(data, dto.getHorarioFim())
                                : dataHora.plusHours(1);

                        if (dataHora.isBefore(fimConsulta) && fimNovaConsulta.isAfter(inicioConsulta)) {
                            funcionarioTemConflito = true;
                            break;
                        }
                    }

                    if (!funcionarioTemConflito) {
                        funcionarioDisponivel = func;
                        break;
                    }
                }

                if (funcionarioDisponivel == null) {
                    ConflitoDatasDto conflito = new ConflitoDatasDto(
                            data,
                            dto.getHorario().toString(),
                            String.format("Horário indisponível - Nenhum funcionário da especialidade %s disponível às %s",
                                    funcionario.getEspecialidade(),
                                    dto.getHorario().toString())
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
                LocalDateTime inicioConsulta = consulta.getDataHoraInicio();
                LocalDateTime fimConsulta = consulta.getHorarioFim() != null
                        ? LocalDateTime.of(consulta.getData(), consulta.getHorarioFim())
                        : inicioConsulta.plusHours(1);
                LocalDateTime fimNovaConsulta = dto.getHorarioFim() != null
                        ? LocalDateTime.of(data, dto.getHorarioFim())
                        : dataHora.plusHours(1);

                if (dataHora.isBefore(fimConsulta) && fimNovaConsulta.isAfter(inicioConsulta)) {
                    temConflito = true;
                    break;
                }
            }

            if (temConflito) {
                List<Funcionario> funcionariosComMesmaEspecialidade = funcionarioRepository
                        .findByEspecialidadeIgnoreCase(funcionario.getEspecialidade());

                for (Funcionario func : funcionariosComMesmaEspecialidade) {
                    if (func.getId().equals(funcionario.getId())) {
                        continue;
                    }

                    List<ConsultaProntuario> consultasFuncionario = consultaProntuarioRepository
                            .buscarConsultasPorFuncionarioEData(func.getId(), data);

                    boolean funcionarioTemConflito = false;
                    for (ConsultaProntuario cons : consultasFuncionario) {
                        LocalDateTime inicioConsulta = cons.getDataHoraInicio();
                        LocalDateTime fimConsulta = cons.getHorarioFim() != null
                                ? LocalDateTime.of(cons.getData(), cons.getHorarioFim())
                                : inicioConsulta.plusHours(1);
                        LocalDateTime fimNovaConsulta = dto.getHorarioFim() != null
                                ? LocalDateTime.of(data, dto.getHorarioFim())
                                : dataHora.plusHours(1);

                        if (dataHora.isBefore(fimConsulta) && fimNovaConsulta.isAfter(inicioConsulta)) {
                            funcionarioTemConflito = true;
                            break;
                        }
                    }

                    if (!funcionarioTemConflito) {
                        funcionarioAtribuido = func;
                        break;
                    }
                }
            }

            // cria a consulta nova
            ConsultaProntuario consulta = new ConsultaProntuario();
            consulta.setPaciente(paciente);
            consulta.setData(data);
            consulta.setHorarioInicio(dto.getHorario());
            consulta.setHorarioFim(dto.getHorarioFim());
            consulta.setTipo(dto.getTipo());
            consulta.setPresenca(false);
            consulta.setConfirmada(false);

            // salva no banco
            ConsultaProntuario consultaSalva = consultaProntuarioRepository.save(consulta);

            // cria o vínculo entre a consulta e o funcionário atribuído
            ConsultaFuncionario cf = vincularFuncionario(funcionarioAtribuido, consultaSalva);
            consultaSalva.getConsultaFuncionarios().add(cf);

            // converte pra dto e adiciona na lista de consultas criadas
            // assim a resposta vai ter todas as informações, incluindo o nome do médico
            ConsultaProntuarioResponseDto consultaDto = ConsultaProntuarioMapper.toResponseDto(consultaSalva);
            response.getConsultasCriadas().add(consultaDto);
        }

        // atualiza os totais e retorna tudo certinho
        response.setTotalConsultasCriadas(response.getConsultasCriadas().size());
        response.setTotalFalhas(0);

        // publica todas as consultas recorrentes criadas no RabbitMQ no formato esperado pelo consumer (envelope)
        consultaCriadaRabbitProducer.publicarEvento(new EventoConsultaCriadaDto(response.getConsultasCriadas().stream().map(this::toMensagemDto).toList()));

        return response;
    }

    public List<ConsultaProntuarioResponseDto> listarAgendaDiaria(Long id, String tipo, LocalDate dataReferencia){

        List<ConsultaProntuario> consultas = new ArrayList<>();

        if ("funcionario".equalsIgnoreCase(tipo)) {
            consultas = consultaProntuarioRepository.buscarConsultasDiariaPorFuncionario(id, dataReferencia);
            if (consultas.isEmpty()){
                throw new ResourceNotFoundException("Nenhuma consulta para esse funcionario hoje!");
            }
        } else if ("paciente".equalsIgnoreCase(tipo)) {
            consultas = consultaProntuarioRepository.buscarConsultasDiariaPorPaciente(id, dataReferencia);
            if (consultas.isEmpty()){
                throw new ResourceNotFoundException("Nenhuma consulta para esse paciente hoje!");
            }
        }

        return ConsultaProntuarioMapper.toResponseDto(consultas);
    }

    public List<ConsultaProntuarioResponseDto> listarAgendaSemanal(Long id, String tipo, LocalDate dataReferencia) {
        // ISO: segunda=1 … sábado=6, domingo=7. Domingo deve avançar para a próxima segunda.
        int dow = dataReferencia.getDayOfWeek().getValue();
        LocalDate inicioDaSemana = (dow == 7)
                ? dataReferencia.plusDays(1)
                : dataReferencia.minusDays(dow - 1);
        LocalDate fimDaSemana = inicioDaSemana.plusDays(4);

        List<ConsultaProntuario> consultas = new ArrayList<>();

        if ("funcionario".equalsIgnoreCase(tipo)){
            consultas = consultaProntuarioRepository.buscarConsultasPorFuncionarioEPeriodo(
                    id,
                    inicioDaSemana,
                    fimDaSemana
            );
        } else if ("paciente".equalsIgnoreCase(tipo)){
            consultas = consultaProntuarioRepository.buscarConsultasPorPacienteEPeriodo(
                    id,
                    inicioDaSemana,
                    fimDaSemana
            );
        }

        return ConsultaProntuarioMapper.toResponseDto(consultas);
    }

    public List<ConsultaProntuarioResponseDto> listarAgendaMensal(Long id, String tipo, LocalDate dataReferencia) {
        LocalDate inicioDoMes = dataReferencia.withDayOfMonth(1);
        LocalDate fimDoMes = dataReferencia.withDayOfMonth(dataReferencia.lengthOfMonth());

        List<ConsultaProntuario> consultas = new ArrayList<>();

        if ("funcionario".equalsIgnoreCase(tipo)){
            consultas = consultaProntuarioRepository.buscarConsultasPorFuncionarioEPeriodo(
                    id,
                    inicioDoMes,
                    fimDoMes
            );

            if (consultas.isEmpty()) {
                throw new ResourceNotFoundException("Nenhuma consulta encontrada para este período!");
            }
        } else if ("paciente".equalsIgnoreCase(tipo)){
            consultas = consultaProntuarioRepository.buscarConsultasPorPacienteEPeriodo(
                    id,
                    inicioDoMes,
                    fimDoMes
            );

            if (consultas.isEmpty()) {
                throw new ResourceNotFoundException("Nenhuma consulta encontrada para este período!");
            }
        }

        return ConsultaProntuarioMapper.toResponseDto(consultas);
    }

    public EventoConsultaCriadaDto notificarResponsavel(Long id, LocalDate dataReferencia) {
        List<ConsultaProntuarioResponseDto> agenda = listarAgendaSemanal(id, "paciente", dataReferencia);
        EventoConsultaCriadaDto evento = new EventoConsultaCriadaDto(agenda.stream().map(this::toMensagemDto).toList());

        if (!agenda.isEmpty()) {
            consultaCriadaRabbitProducer.publicarEvento(evento);
        }

        return evento;
    }

    public List<ConsultaProntuarioResponseDto> listarConsultasPendentes(Long idFuncionario) {
        List<ConsultaProntuario> consultas = consultaProntuarioRepository.findByFuncionarioIdAndConfirmadaNull(idFuncionario);
        return ConsultaProntuarioMapper.toResponseDto(consultas);
    }

    public ProximaConsultaProntuarioResponseDto buscarProximaConsultaConfirmada(Long pacienteId, Long funcionarioId) {
        List<ConsultaProntuario> consultas = (funcionarioId != null)
                ? consultaProntuarioRepository.buscarProximaConsultaPorPacienteEFuncionario(pacienteId, funcionarioId)
                : consultaProntuarioRepository.buscarProximaConsultaNaoConcluidaPorPaciente(pacienteId);

        if (consultas.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma consulta confirmada encontrada para este paciente!");
        }

        ConsultaProntuario proximaConsulta = consultas.get(0);

        return new ProximaConsultaProntuarioResponseDto(
                proximaConsulta.getId(),
                proximaConsulta.getData(),
                proximaConsulta.getHorarioInicio(),
                proximaConsulta.getHorarioFim(),
                proximaConsulta.getTipo(),
                proximaConsulta.getFuncionario().getNome()
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
        response.setData(consulta.getData());
        response.setHorarioInicio(consulta.getHorarioInicio());
        response.setHorarioFim(consulta.getHorarioFim());
        response.setTipo(consulta.getTipo());
        response.setEspecialidade(funcionario.getEspecialidade());
        response.setNomeProfissional(funcionario.getNome());
        response.setObservacoesComportamentais(consulta.getObservacoesComportamentais());


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
            ultimaConsulta.setData(ultimaConsultaEntity.getData());
            ultimaConsulta.setNomeFuncionarioUltimaConsulta(ultimaConsultaEntity.getFuncionario().getNome());
            response.setUltimaConsulta(ultimaConsulta);
        }

        List<ConsultaProntuarioAtualResponseDto.MaterialInfo> materiaisDto = new ArrayList<>();
        if (consulta.getMateriais() != null) {
            for (Material m : consulta.getMateriais()) {
                ConsultaProntuarioAtualResponseDto.MaterialInfo info = new ConsultaProntuarioAtualResponseDto.MaterialInfo();
                info.setId(m.getId());
                info.setItem(m.getItem());
                materiaisDto.add(info);
            }
        }
        response.setMateriais(materiaisDto);

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
        response.setData(consulta.getData());
        response.setHorarioInicio(consulta.getHorarioInicio());
        response.setHorarioFim(consulta.getHorarioFim());
        response.setEspecialidade(funcionario.getEspecialidade());
        response.setNomeProfissional(funcionario.getNome());
        response.setTipo(consulta.getTipo());
        response.setObservacoesComportamentais(consulta.getObservacoesComportamentais());

        if (consulta.getMateriais() != null && !consulta.getMateriais().isEmpty()) {
            List<String> materiais = consulta.getMateriais().stream()
                    .map(Material::getItem)
                    .toList();
            response.setMateriaisUtilizados(materiais);
        }

        return response;
    }

    /**
     * Agenda um batch de consultas (recorrentes ou únicas) de uma única vez.
     *
     * Para cada item da lista:
     *  - funcionarioId  → um único funcionário
     *  - funcionarioIds → múltiplos funcionários (todos vinculados a cada data)
     *  - dataInicio / dataFim → gera as datas semanalmente (mesmo dia da semana).
     *    Para consulta única, informe dataInicio == dataFim.
     */
    @Transactional
    public AgendarConsultasResponseDto agendarConsultas(AgendarConsultasRequestDto dto) {
        AgendarConsultasResponseDto response = new AgendarConsultasResponseDto();

        // valida o paciente uma única vez
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado!"));

        List<ConsultaProntuarioResponseDto> todasCriadas = new ArrayList<>();
        List<ConflitoDatasDto> todosConflitos = new ArrayList<>();

        for (ConsultaItemRequestDto item : dto.getConsultas()) {
            List<Long> idsFunc = item.getFuncionarioIdsList();
            if (idsFunc.isEmpty()) {
                throw new IllegalArgumentException("Cada consulta deve ter ao menos um funcionarioId ou funcionarioIds.");
            }

            // gera as datas semanalmente entre dataInicio e dataFim (inclusive)
            List<LocalDate> datas = gerarDatasSemanais(item.getDataInicio(), item.getDataFim());

            // atribui um ID de recorrência único para este bloco quando há mais de uma data
            String recorrenciaId = datas.size() > 1 ? UUID.randomUUID().toString() : null;

            // carrega os funcionários do item
            List<Funcionario> funcionariosDoItem = new ArrayList<>();
            for (Long fId : idsFunc) {
                Funcionario f = funcionarioRepository.findById(fId)
                        .orElseThrow(() -> new ResourceNotFoundException("Funcionário com id " + fId + " não encontrado!"));
                funcionariosDoItem.add(f);
            }

            // para cada data, cria a consulta e vincula todos os funcionários do item
            for (LocalDate data : datas) {
                try {
                    // valida se a data não é anterior ao dia atual
                    if (data.isBefore(LocalDate.now())) {
                        throw new RuntimeException(
                                "Não é possível agendar consultas em datas passadas. Data inválida: " + data + ".");
                    }

                    // valida se o horário já passou quando a consulta é para hoje
                    if (data.isEqual(LocalDate.now())) {
                        LocalDateTime agora = LocalDateTime.now();
                        LocalDateTime inicioSolicitado = LocalDateTime.of(data, item.getHorarioInicio());
                        if (inicioSolicitado.isBefore(agora)) {
                            throw new RuntimeException(
                                    "Não é possível agendar consultas em horários que já passaram. Horário inválido: " + item.getHorarioInicio() + ".");
                        }
                    }

                    LocalDateTime novoInicio = LocalDateTime.of(data, item.getHorarioInicio());
                    LocalDateTime novoFim = item.getHorarioFim() != null
                            ? LocalDateTime.of(data, item.getHorarioFim())
                            : novoInicio.plusHours(1);

                    // valida conflito de horário do paciente
                    List<ConsultaProntuario> consultasPaciente =
                            consultaProntuarioRepository.buscarConsultasDiariaPorPaciente(paciente.getId(), data);
                    for (ConsultaProntuario cp : consultasPaciente) {
                        LocalDateTime inicioExist = LocalDateTime.of(cp.getData(), cp.getHorarioInicio());
                        LocalDateTime fimExist = cp.getHorarioFim() != null
                                ? LocalDateTime.of(cp.getData(), cp.getHorarioFim())
                                : inicioExist.plusHours(1);
                        if (novoInicio.isBefore(fimExist) && novoFim.isAfter(inicioExist)) {
                            throw new RuntimeException(
                                    "Paciente já possui uma consulta neste horário (" + item.getHorarioInicio() + ").");
                        }
                    }

                    // valida conflito de horário de cada funcionário
                    for (Funcionario func : funcionariosDoItem) {
                        List<ConsultaProntuario> consultasFunc =
                                consultaProntuarioRepository.buscarConsultasPorFuncionarioEData(func.getId(), data);
                        for (ConsultaProntuario cf : consultasFunc) {
                            LocalDateTime inicioExist = LocalDateTime.of(cf.getData(), cf.getHorarioInicio());
                            LocalDateTime fimExist = cf.getHorarioFim() != null
                                    ? LocalDateTime.of(cf.getData(), cf.getHorarioFim())
                                    : inicioExist.plusHours(1);
                            if (novoInicio.isBefore(fimExist) && novoFim.isAfter(inicioExist)) {
                                throw new RuntimeException(
                                        "Funcionário " + func.getNome() + " já possui uma consulta neste horário (" + item.getHorarioInicio() + ").");
                            }
                        }
                    }

                    ConsultaProntuario consulta = new ConsultaProntuario();
                    consulta.setPaciente(paciente);
                    consulta.setData(data);
                    consulta.setHorarioInicio(item.getHorarioInicio());
                    consulta.setHorarioFim(item.getHorarioFim());
                    consulta.setTipo(item.getTipo());
                    consulta.setPresenca(false);
                    consulta.setConfirmada(false);
                    consulta.setRecorrenciaId(recorrenciaId);

                    ConsultaProntuario consultaSalva = consultaProntuarioRepository.save(consulta);

                    for (Funcionario func : funcionariosDoItem) {
                        ConsultaFuncionario cf = vincularFuncionario(func, consultaSalva);
                        consultaSalva.getConsultaFuncionarios().add(cf);
                    }

                    ConsultaProntuarioResponseDto consultaDto = ConsultaProntuarioMapper.toResponseDto(consultaSalva);
                    todasCriadas.add(consultaDto);
                } catch (Exception e) {
                    ConflitoDatasDto conflito = new ConflitoDatasDto(
                            data,
                            item.getHorarioInicio() != null ? item.getHorarioInicio().toString() : "N/A",
                            e.getMessage()
                    );
                    todosConflitos.add(conflito);
                }
            }
        }

        response.setConsultasCriadas(todasCriadas);
        response.setDatasComConflito(todosConflitos);
        response.setTotalConsultasCriadas(todasCriadas.size());
        response.setTotalFalhas(todosConflitos.size());

        return response;
    }

    /**
     * Gera uma lista de datas com frequência semanal (mesmo dia da semana de dataInicio)
     * entre dataInicio e dataFim (inclusive).
     * Se dataInicio == dataFim, retorna apenas uma data (consulta única).
     */
    private List<LocalDate> gerarDatasSemanais(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("dataInicio e dataFim são obrigatórios.");
        }
        List<LocalDate> datas = new ArrayList<>();
        LocalDate cursor = dataInicio;
        while (!cursor.isAfter(dataFim)) {
            datas.add(cursor);
            if (cursor.equals(dataFim)) break; // consulta única
            cursor = cursor.plusWeeks(1);
        }
        return datas;
    }

    public Page<ConsultaProntuarioResponseDto> listarUltimasConsultas(
            Long idPaciente,
            Long idFuncionario,
            Pageable pageable
    ) {
        return consultaProntuarioRepository
                .findUltimasConsultas(idPaciente, idFuncionario, pageable)
                .map(ConsultaProntuarioMapper::toResponseDto);
    }

}

