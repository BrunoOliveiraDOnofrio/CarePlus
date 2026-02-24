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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public FuncionarioService(FuncionarioRepository repository, PasswordEncoder encoder, ConsultaProntuarioRepository consultaProntuarioRepository) {
        this.repository = repository;
        this.passwordEncoder = encoder;
        this.consultaProntuarioRepository = consultaProntuarioRepository;
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
        return repository.findAll();
    }

    public FuncionarioResponseDto salvar(FuncionarioResquestDto dto){

        Funcionario supervisor = null;

        if (dto.getSupervisor() != null && dto.getSupervisor().getId() != null) {
            supervisor = repository.findById(dto.getSupervisor().getId())
                    .orElseThrow(() -> new RuntimeException("Supervisor não encontrado"));
        }

        // criptografa senha
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
        Funcionario novoFuncionario = FuncionarioMapper.toEntity(dto, supervisor);
        novoFuncionario.setSenha(senhaCriptografada);

        Funcionario salvo = repository.save(novoFuncionario);

        return FuncionarioMapper.toResponseDto(salvo);
    }
    // autenticar usuário
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
                nomeSupervisor
        );

        return FuncionarioMapper.of(funcionarioAtenticado, token);
    }

    public List<FuncionarioResponseDto> buscarPorEmail(String email){

        List<Funcionario> funcionariosEncontrados = repository.findByEmailContainingIgnoreCase(email);

        if (!funcionariosEncontrados.isEmpty()){
            return FuncionarioMapper.toResponseDto(funcionariosEncontrados);
        }else{
            throw new ResourceNotFoundException("Usuário não encontrado!");
        }
    }

    public List<FuncionarioResponseDto> listarTodos(){
        List<Funcionario> funcionarios = repository.findAll();

        if (!funcionarios.isEmpty()){
            return FuncionarioMapper.toResponseDto(funcionarios);
        }else{
            throw new RuntimeException("Nenhum usuario cadastrado");
        }
    }

    public void deletar(Long id){
        boolean existe = repository.existsById(id);

        if(!existe){
            throw new RuntimeException("Usuário não encontrado");
        }

        repository.deleteById(id);
    }

    public FuncionarioResponseDto atualizar(FuncionarioResquestDto funcionario, Long id){
        Optional<Funcionario> existe = repository.findById(id);

        if (existe.isPresent()){
            Funcionario funcExistente = existe.get();

            funcExistente.setNome(funcionario.getNome());
            funcExistente.setEmail(funcionario.getEmail());
            funcExistente.setCargo(funcionario.getCargo());
            funcExistente.setEspecialidade(funcionario.getEspecialidade());
            funcExistente.setTelefone(funcionario.getTelefone());
            funcExistente.setDocumento(funcionario.getDocumento());
            funcExistente.setTipoAtendimento(funcionario.getTipoAtendimento());
            Funcionario atualizado = repository.save(funcExistente);

            return FuncionarioMapper.toResponseDto(atualizado);
        }else{
            throw new ResourceNotFoundException("Usuario nao encontrado!");
        }
    }

    public List<String> listarEspecialidades(){

        List<Funcionario> funcionarios = repository.findAll();

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
        List<Funcionario> funcionarios = repository.findAll();
        if (funcionarios.isEmpty()){
            throw new ResourceNotFoundException("Nenhum funcionario cadastrado!");
        }

        List<FuncionarioResponseDto> nomes = funcionarios.stream()
                .filter(f -> f.getEspecialidade() != null && f.getEspecialidade().equalsIgnoreCase(especialidade))
                .map(FuncionarioMapper::toResponseDto)
                .toList();

        if (nomes.isEmpty()){
            throw new ResourceNotFoundException("Nenhum funcionário encontrado para a especialidade: " + especialidade);
        }

        return nomes;
    }

    public List<String> buscarHorariosDisponiveis(Long idFuncionario, LocalDate data){
        // Verifica se o funcionário existe
        if (!repository.existsById(idFuncionario)){
            throw new ResourceNotFoundException("Funcionário não encontrado!");
        }

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

            // Verifica se o horário está ocupado
            boolean ocupado = false;

            for (ConsultaProntuario consulta : consultas){
                LocalDateTime inicioConsulta = consulta.getDataHora();
                LocalDateTime fimConsulta = inicioConsulta.plusHours(1);

                // Verifica se o horário atual está dentro do período da consulta
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

    public List<FuncionarioResponseDto> buscarFuncionariosDisponiveis(String especialidade, String dataHoraStr){
        // Parse do dataHora (formato: "2026-01-30 16:00:00" ou "2026-01-30T16:00:00")
        LocalDateTime dataHora;
        try {
            // Tenta com espaço primeiro
            if (dataHoraStr.contains(" ")) {
                dataHoraStr = dataHoraStr.replace(" ", "T");
            }
            dataHora = LocalDateTime.parse(dataHoraStr);
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de data/hora inválido. Use: yyyy-MM-ddTHH:mm:ss ou yyyy-MM-dd HH:mm:ss");
        }

        // Busca todos os funcionários da especialidade
        List<Funcionario> funcionariosDaEspecialidade = repository.findByEspecialidadeIgnoreCase(especialidade);

        if (funcionariosDaEspecialidade.isEmpty()){
            throw new ResourceNotFoundException("Nenhum funcionário encontrado para a especialidade: " + especialidade);
        }

        // Filtra os funcionários que estão disponíveis no horário solicitado
        List<FuncionarioResponseDto> funcionariosDisponiveis = new ArrayList<>();

        for (Funcionario funcionario : funcionariosDaEspecialidade){
            // Busca as consultas do funcionário na data especificada
            List<ConsultaProntuario> consultas = consultaProntuarioRepository.buscarConsultasPorFuncionarioEData(
                    funcionario.getId(), dataHora.toLocalDate());

            // Verifica se o horário está livre
            boolean disponivel = true;

            for (ConsultaProntuario consulta : consultas){
                LocalDateTime inicioConsulta = consulta.getDataHora();
                LocalDateTime fimConsulta = inicioConsulta.plusHours(1);

                // Verifica se o horário solicitado conflita com alguma consulta existente
                if ((dataHora.isEqual(inicioConsulta) || dataHora.isAfter(inicioConsulta))
                        && dataHora.isBefore(fimConsulta)){
                    disponivel = false;
                    break;
                }
            }

            // Se está disponível, adiciona à lista
            if (disponivel){
                funcionariosDisponiveis.add(FuncionarioMapper.toResponseDto(funcionario));
            }
        }

        if (funcionariosDisponiveis.isEmpty()){
            throw new ResourceNotFoundException("Nenhum funcionário disponível para " + especialidade + " no horário " + dataHora);
        }

        return funcionariosDisponiveis;
    }



}
