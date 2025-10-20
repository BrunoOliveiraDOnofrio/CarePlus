package com.example.careplus.service;

import com.example.careplus.controller.dtoEspecialista.EspecialistaMapper;
import com.example.careplus.controller.dtoEspecialista.EspecialistaResponseDto;
import com.example.careplus.controller.dtoEspecialista.EspecialistaResquestDto;
import com.example.careplus.controller.dtoPaciente.PacienteMapper;
import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.model.Especialista;
import com.example.careplus.repository.EspecialistaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EspecialistaService {

    private final EspecialistaRepository repository;

    public EspecialistaService(EspecialistaRepository repository) {
        this.repository = repository;
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

        Especialista novoEspecialista = EspecialistaMapper.toEntity(dto, supervisor);

        Especialista salvo = repository.save(novoEspecialista);

        return EspecialistaMapper.toResponseDto(salvo);
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
