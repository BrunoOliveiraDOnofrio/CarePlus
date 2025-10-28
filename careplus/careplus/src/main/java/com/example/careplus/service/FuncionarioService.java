package com.example.careplus.service;

import com.example.careplus.config.GerenciadorTokenJwt;
import com.example.careplus.controller.dtoFuncionario.*;
import com.example.careplus.controller.dtoPaciente.PacienteMapper;
import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Paciente;
import com.example.careplus.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
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

// Modelo de JSON
//        {
//            "id": 2,
//                "nome": "Dra. Ana Souza",
//                "email": "ana.souza@clinica.com",
//                "senha": "12322",
//                "cargo": "Residente",
//                "especialidade": "Cardiologia",
//                "supervisor": {
//                 "id": 1
//                 }
//        }
// Caso não tiver o especialista não tiver um supervisor, basta não enviar o campo

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

        final String token = gerenciadorTokenJwt.generateToken(authentication);

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
            Funcionario atualizado = repository.save(funcExistente);

            return FuncionarioMapper.toResponseDto(atualizado);
        }else{
            throw new ResourceNotFoundException("Usuario nao encontrado!");
        }
    }



}
