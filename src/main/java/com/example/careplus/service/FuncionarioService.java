package com.example.careplus.service;

import com.example.careplus.config.GerenciadorTokenJwt;
import com.example.careplus.dto.dtoFuncionario.FuncionarioMapper;
import com.example.careplus.dto.dtoFuncionario.FuncionarioResponseDto;
import com.example.careplus.dto.dtoFuncionario.FuncionarioResquestDto;
import com.example.careplus.dto.dtoFuncionario.FuncionarioTokenDto;
import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.model.ConsultaProntuario;
import com.example.careplus.model.Funcionario;
import com.example.careplus.repository.ConsultaProntuarioRepository;
import com.example.careplus.repository.FuncionarioRepository;
import com.example.careplus.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final FuncionarioRepository repository;
    private final ConsultaProntuarioRepository consultaProntuarioRepository;
    private final S3Service s3Service;
    private final RoleRepository roleRepository;

    public FuncionarioService(FuncionarioRepository repository, PasswordEncoder encoder, ConsultaProntuarioRepository consultaProntuarioRepository, S3Service s3Service, RoleRepository roleRepository) {
        this.repository = repository;
        this.passwordEncoder = encoder;
        this.consultaProntuarioRepository = consultaProntuarioRepository;
        this.s3Service = s3Service;
        this.roleRepository = roleRepository;
    }

    public FuncionarioResponseDto salvar(FuncionarioResquestDto dto) {

        Funcionario supervisor = null;

        if (dto.getSupervisor() != null && dto.getSupervisor().getId() != null) {
            supervisor = repository.findById(dto.getSupervisor().getId())
                    .orElseThrow(() -> new RuntimeException("Supervisor não encontrado"));
        }

        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());

        Funcionario novoFuncionario = FuncionarioMapper.toEntity(dto, supervisor);
        novoFuncionario.setSenha(senhaCriptografada);

        // UPLOAD DA IMAGEM
        if (dto.getFoto() != null && !dto.getFoto().isEmpty()) {
            try {
                String nomeArquivo = s3Service.uploadImagem(dto.getFoto(), dto.getDocumento());
                novoFuncionario.setFoto(nomeArquivo);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao fazer upload da imagem: " + e.getMessage(), e);
            }
        }

        String nomeRole = resolverRole(dto.getCargo());
        roleRepository.findByNome(nomeRole).ifPresent(role -> novoFuncionario.getRoles().add(role));

        Funcionario salvo = repository.save(novoFuncionario);

        return FuncionarioMapper.toResponseDto(salvo);
    }

    public void deletar(Long id){
        Funcionario funcionario = repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        funcionario.setAtivo(false);
        repository.save(funcionario);
    }

    public FuncionarioResponseDto atualizarFuncionario(FuncionarioResquestDto dto, Long idFuncionario) {

        Funcionario supervisor = null;

        if (dto.getSupervisor() != null && dto.getSupervisor().getId() != null) {
            supervisor = repository.findById(dto.getSupervisor().getId())
                    .orElseThrow(() -> new RuntimeException("Supervisor não encontrado"));
        }

        Optional<Funcionario> existe = repository.findById(idFuncionario);

        if (existe.isPresent()) {
            Funcionario funcExistente = existe.get();

            funcExistente.setNome(dto.getNome());
            funcExistente.setEmail(dto.getEmail());
            funcExistente.setCargo(dto.getCargo());
            funcExistente.setEspecialidade(dto.getEspecialidade());
            funcExistente.setTelefone(dto.getTelefone());
            funcExistente.setDocumento(dto.getDocumento());
            funcExistente.setTipoAtendimento(dto.getTipoAtendimento());
            funcExistente.setSupervisor(supervisor);

            // Atualiza a senha criptografada
            String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
            funcExistente.setSenha(senhaCriptografada);

            // UPLOAD DA IMAGEM
            if (dto.getFoto() != null && !dto.getFoto().isEmpty()) {
                try {
                    String nomeArquivo = s3Service.uploadImagem(dto.getFoto(), dto.getDocumento());
                    funcExistente.setFoto(nomeArquivo);
                } catch (IOException e) {
                    throw new RuntimeException("Erro ao fazer upload da imagem: " + e.getMessage(), e);
                }
            }

            Funcionario salvo = repository.save(funcExistente);
            return FuncionarioMapper.toResponseDto(salvo);
        } else {
            throw new ResourceNotFoundException("Usuario nao encontrado!");
        }
    }

    public byte[] buscarFoto(String documento) throws IOException {
        return s3Service.buscarUltimaFoto(documento);
    }

    public FuncionarioTokenDto autenticar(Funcionario usuario){
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuario.getEmail(), usuario.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Funcionario funcionarioAtenticado =
                repository.findByEmail(usuario.getEmail())
                        .orElseThrow(
                                () -> new ResponseStatusException(404, "Email do funcionario não cadastrado", null)
                        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String nomeSupervisor = funcionarioAtenticado.getSupervisor() != null ? funcionarioAtenticado.getSupervisor().getNome() : "N/A";

        final String token = gerenciadorTokenJwt.generateToken(
                authentication,
                funcionarioAtenticado.getId(),
                funcionarioAtenticado.getNome(),
                funcionarioAtenticado.getCargo(),
                funcionarioAtenticado.getEspecialidade(),
                nomeSupervisor,
                funcionarioAtenticado.getDocumento()
        );

        return FuncionarioMapper.of(funcionarioAtenticado, token);
    }

    public List<FuncionarioResponseDto> listarSubordinados(Long id, List<Funcionario> todos) {
        List<FuncionarioResponseDto> subordinados = new ArrayList<>();

        Optional<Funcionario> funcionarioOpt = repository.findById(id);

        if (funcionarioOpt.isPresent()) {
            Funcionario usuario = funcionarioOpt.get();
            for (Funcionario f : todos) {
                if (f.getSupervisor() != null && f.getSupervisor().equals(usuario)) {
                    subordinados.add(FuncionarioMapper.toResponseDto(f));
                    subordinados.addAll(listarSubordinados(f.getId(), todos)); // aqui ta a recursividade, você consegue ver se os subordinados tem algum subordinado
                }
            }
            return subordinados;
        } else {
            throw new RuntimeException("Usuário não encontrado!");
        }
    }

    public List<Funcionario> buscarTodos(){
        return repository.findAllByAtivoTrue();
    }

    public List<FuncionarioResponseDto> buscarPorEmail(String email){

        List<Funcionario> funcionariosEncontrados = repository.findByEmailContainingIgnoreCaseAndAtivoTrue(email);

        if (!funcionariosEncontrados.isEmpty()){
            return FuncionarioMapper.toResponseDto(funcionariosEncontrados);
        }else{
            throw new ResourceNotFoundException("Usuário não encontrado!");
        }
    }

    public List<FuncionarioResponseDto> listarTodos(){
        List<Funcionario> funcionarios = repository.findAllByAtivoTrue();

        if (!funcionarios.isEmpty()){
            return FuncionarioMapper.toResponseDto(funcionarios);
        }else{
            throw new RuntimeException("Nenhum usuario cadastrado");
        }
    }

    public Page<FuncionarioResponseDto> listarTodosPaginado(Pageable pageable) {
        return repository.findAllByAtivoTrue(pageable)
                .map(FuncionarioMapper::toResponseDto);
    }

    public Page<FuncionarioResponseDto> listarInativosPaginado(Pageable pageable) {
        return repository.findAllByAtivoFalse(pageable)
                .map(FuncionarioMapper::toResponseDto);
    }

    public void reativar(Long id) {
        Funcionario funcionario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
        funcionario.setAtivo(true);
        repository.save(funcionario);
    }

    public List<String> listarEspecialidades(){

        List<Funcionario> funcionarios = repository.findAllByAtivoTrue();

        if (funcionarios.isEmpty()){
            throw new ResourceNotFoundException("Nenhum funcionario cadastrado!");
        }

        List<String> especialidades = funcionarios.stream()
                .map(Funcionario::getEspecialidade)
                .filter(especialidade -> especialidade != null && !especialidade.isEmpty())
                .distinct()
                .toList();

        if (especialidades.isEmpty()){
            throw new ResourceNotFoundException("Nenhuma especialidade encontrada!");
        }

        return especialidades;
    }

    public List<FuncionarioResponseDto> nomesFuncionariosPorEspecialidade(String especialidade){
        List<Funcionario> funcionarios = repository.findByEspecialidadeIgnoreCaseAndAtivoTrue(especialidade);
        if (funcionarios.isEmpty()){
            throw new ResourceNotFoundException("Nenhum funcionario cadastrado!");
        }

        List<FuncionarioResponseDto> nomes = funcionarios.stream()
                .map(FuncionarioMapper::toResponseDto)
                .toList();

        if (nomes.isEmpty()){
            throw new ResourceNotFoundException("Nenhum funcionário encontrado para a especialidade: " + especialidade);
        }

        return nomes;
    }

    public List<String> buscarHorariosDisponiveis(Long idFuncionario, LocalDate data){
        // Verifica se o funcionário existe e está ativo
        repository.findByIdAndAtivoTrue(idFuncionario)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado!"));

        // Busca todas as consultas do funcionário nessa data
        List<ConsultaProntuario> consultas = consultaProntuarioRepository.buscarConsultasPorFuncionarioEData(idFuncionario, data);

        // Define horário de funcionamento: 8h às 18h
        LocalTime horarioInicio = LocalTime.of(8, 0);
        LocalTime horarioFim = LocalTime.of(18, 0);

        // Lista de horários disponíveis
        List<String> horariosDisponiveis = new ArrayList<>();

        // Percorre todos os horários possíveis de 30 em 30 minutos
        LocalTime horarioAtual = horarioInicio;

        while (horarioAtual.isBefore(horarioFim)){
            LocalDateTime horarioConsulta = LocalDateTime.of(data, horarioAtual);

            boolean ocupado = false;

            for (ConsultaProntuario consulta : consultas){
                LocalDateTime inicioConsulta = consulta.getDataHoraInicio();
                LocalDateTime fimConsulta = consulta.getHorarioFim() != null
                        ? LocalDateTime.of(data, consulta.getHorarioFim())
                        : inicioConsulta.plusHours(1);

                if ((horarioConsulta.isEqual(inicioConsulta) || horarioConsulta.isAfter(inicioConsulta))
                        && horarioConsulta.isBefore(fimConsulta)){
                    ocupado = true;
                    break;
                }
            }

            // Se não está ocupado, adiciona à lista
            if (!ocupado){
                horariosDisponiveis.add(horarioAtual.toString());
            }

            // Avança 30 minutos
            horarioAtual = horarioAtual.plusMinutes(30);
        }

        return horariosDisponiveis;
    }

    public List<FuncionarioResponseDto> buscarFuncionariosDisponiveis(String especialidade, String dataStr, String horarioInicioStr){
        LocalDate data;
        LocalTime horarioInicio;
        try {
            data = LocalDate.parse(dataStr);
            horarioInicio = LocalTime.parse(horarioInicioStr);
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato inválido. Use data: yyyy-MM-dd e horario: HH:mm:ss");
        }

        LocalDateTime dataHora = LocalDateTime.of(data, horarioInicio);

        List<Funcionario> funcionariosDaEspecialidade = repository.findByEspecialidadeIgnoreCaseAndAtivoTrue(especialidade);

        if (funcionariosDaEspecialidade.isEmpty()){
            throw new ResourceNotFoundException("Nenhum funcionário encontrado para a especialidade: " + especialidade);
        }

        List<FuncionarioResponseDto> funcionariosDisponiveis = new ArrayList<>();

        for (Funcionario funcionario : funcionariosDaEspecialidade){
            List<ConsultaProntuario> consultas = consultaProntuarioRepository.buscarConsultasPorFuncionarioEData(
                    funcionario.getId(), data);

            boolean disponivel = true;

            for (ConsultaProntuario consulta : consultas){
                LocalDateTime inicioConsulta = consulta.getDataHoraInicio();
                LocalDateTime fimConsulta = consulta.getHorarioFim() != null
                        ? LocalDateTime.of(consulta.getData(), consulta.getHorarioFim())
                        : inicioConsulta.plusHours(1);

                if ((dataHora.isEqual(inicioConsulta) || dataHora.isAfter(inicioConsulta))
                        && dataHora.isBefore(fimConsulta)){
                    disponivel = false;
                    break;
                }
            }

            if (disponivel){
                funcionariosDisponiveis.add(FuncionarioMapper.toResponseDto(funcionario));
            }
        }

        if (funcionariosDisponiveis.isEmpty()){
            throw new ResourceNotFoundException("Nenhum funcionário disponível para " + especialidade + " no horário " + dataHora);
        }

        return funcionariosDisponiveis;
    }

    public List<FuncionarioResponseDto> listarSupervisores() {
        List<Funcionario> supervisores = repository.findByCargoIgnoreCase("Supervisor(a)");
        return supervisores.stream()
                .map(FuncionarioMapper::toResponseDto)
                .toList();
    }

    public List<FuncionarioResponseDto> buscar(String nome, String email, String documento) {
        if (documento != null && !documento.isBlank()) {
            Page<Funcionario> funcionarios = repository.buscarPorCpf(documento, PageRequest.of(0, 10));
            if (funcionarios.isEmpty()) throw new ResourceNotFoundException("Funcionário não encontrado!");
            return FuncionarioMapper.toResponseDto(funcionarios.getContent());
        }
        if (email != null && !email.isBlank()) {
            Page<Funcionario> funcionarios = repository.findByEmailContainingIgnoreCaseAndAtivoTrue(
                    email, PageRequest.of(0, 10));
            if (funcionarios.isEmpty()) throw new ResourceNotFoundException("Funcionário não encontrado!");
            return FuncionarioMapper.toResponseDto(funcionarios.getContent());
        }
        if (nome != null && !nome.isBlank()) {
            Page<Funcionario> funcionarios = repository.findByNomeContainingIgnoreCaseAndAtivoTrue(
                    nome, PageRequest.of(0, 10));
            if (funcionarios.isEmpty()) throw new ResourceNotFoundException("Funcionário não encontrado!");
            return FuncionarioMapper.toResponseDto(funcionarios.getContent());
        }
        throw new ResourceNotFoundException("Informe ao menos um parâmetro de busca: nome, email ou documento.");
    }

    public FuncionarioResponseDto buscarPerfil(String email) {
        Funcionario funcionario = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
        return FuncionarioMapper.toResponseDto(funcionario);
    }

    public FuncionarioResponseDto atualizarPerfil(String email, com.example.careplus.dto.dtoFuncionario.PerfilFuncionarioRequestDto dto) {
        Funcionario funcionario = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));

        funcionario.setNome(dto.getNome());
        funcionario.setEmail(dto.getEmail());
        if (dto.getTelefone() != null) funcionario.setTelefone(dto.getTelefone());
        if (dto.getTipoAtendimento() != null) funcionario.setTipoAtendimento(dto.getTipoAtendimento());
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            funcionario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        if (dto.getFoto() != null && !dto.getFoto().isEmpty()) {
            try {
                String nomeArquivo = s3Service.uploadImagem(dto.getFoto(), funcionario.getDocumento());
                funcionario.setFoto(nomeArquivo);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao fazer upload da imagem: " + e.getMessage(), e);
            }
        }

        return FuncionarioMapper.toResponseDto(repository.save(funcionario));
    }

    private String resolverRole(String cargo) {
        if (cargo == null) return "USER";
        return switch (cargo.toLowerCase()) {
            case "supervisor(a)" -> "MANAGER";
            case "agendamento"   -> "SCHEDULER";
            default              -> "USER";
        };
    }

}
