package com.example.careplus.service;

import com.example.careplus.config.GerenciadorTokenJwt;
import com.example.careplus.controller.dtoEspecialista.*;
import com.example.careplus.controller.dtoPaciente.PacienteMapper;
import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.model.Especialista;
import com.example.careplus.model.Paciente;
import com.example.careplus.repository.EspecialistaRepository;
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
public class EspecialistaService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final EspecialistaRepository repository;

    public EspecialistaService(EspecialistaRepository repository) {
        this.repository = repository;
    }

    public List<EspecialistaResponseDto> listarSubordinados(Long id, List<Especialista> todos) {
        List<EspecialistaResponseDto> subordinados = new ArrayList<>();

        Optional<Especialista> especialistaOpt = repository.findById(id);

        if (especialistaOpt.isPresent()) {
            Especialista usuario = especialistaOpt.get();
            for (Especialista f : todos) {
                if (f.getSupervisor() != null && f.getSupervisor().equals(usuario)) {
                    subordinados.add(EspecialistaMapper.toResponseDto(f));
                    subordinados.addAll(listarSubordinados(f.getId(), todos)); // aqui ta a recursividade, você consegue ver se os subordinados tem algum subordinado
                }
            }
            return subordinados;
        } else {
            throw new RuntimeException("Usuário não encontrado!");
        }
    }

    public List<Especialista> buscarTodos(){
        return repository.findAll();
    }

    public EspecialistaResponseDto salvar(EspecialistaResquestDto dto){

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

        Especialista supervisor = null;

        if (dto.getSupervisor() != null && dto.getSupervisor().getId() != null) {
            supervisor = repository.findById(dto.getSupervisor().getId())
                    .orElseThrow(() -> new RuntimeException("Supervisor não encontrado"));
        }

        // criptografa senha
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
        Especialista novoEspecialista = EspecialistaMapper.toEntity(dto, supervisor);
        novoEspecialista.setSenha(senhaCriptografada);

        Especialista salvo = repository.save(novoEspecialista);

        return EspecialistaMapper.toResponseDto(salvo);
    }

    // autenticar usuário
    public EspecialistaTokenDto autenticar(Especialista usuario){
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuario.getEmail(), usuario.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Especialista especialistaAtenticado =
                repository.findByEmail(usuario.getEmail())
                        .orElseThrow(
                                () -> new ResponseStatusException(404, "Email do especialista não cadastrado", null)
                        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication);

        return EspecialistaMapper.of(especialistaAtenticado, token);
    }

    public List<EspecialistaResponseDto> buscarPorEmail(String email){

        List<Especialista> especialistasEncontrados = repository.findByEmailContainingIgnoreCase(email);

        if (!especialistasEncontrados.isEmpty()){
            return EspecialistaMapper.toResponseDto(especialistasEncontrados);
        }else{
            throw new ResourceNotFoundException("Usuário não encontrado!");
        }
    }

    public List<EspecialistaResponseDto> listarTodos(){
        List<Especialista> especialistas = repository.findAll();

        if (!especialistas.isEmpty()){
            return EspecialistaMapper.toResponseDto(especialistas);
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

    public EspecialistaResponseDto atualizar(Especialista especialista, Long id){
        Optional<Especialista> existe = repository.findById(id);

        if (existe.isPresent()){
            Especialista especExistente = existe.get();

            especExistente.setNome(especialista.getNome());
            especExistente.setEmail(especialista.getEmail());
            especExistente.setCargo(especialista.getCargo());
            especExistente.setEspecialidade(especialista.getEspecialidade());
            Especialista atualizado = repository.save(especExistente);

            return EspecialistaMapper.toResponseDto(atualizado);
        }else{
            throw new ResourceNotFoundException("Usuario nao encontrado!");
        }
    }



}
