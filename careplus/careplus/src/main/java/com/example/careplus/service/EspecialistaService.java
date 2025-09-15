package com.example.careplus.service;

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

    public Especialista salvar(Especialista especialista){

        if(especialista.getEmail().isEmpty() && especialista.getNome().isEmpty()){
            throw new ResourceNotFoundException("Preencha o nome e email");
        }

        return repository.save(especialista);
    }

    public List<Especialista> buscarPorEmail(String email){

        List<Especialista> especialistasEncontrados = repository.findByEmailStartingWith(email);

        if (!especialistasEncontrados.isEmpty()){
            return especialistasEncontrados;
        }else{
            throw new ResourceNotFoundException("Usuário não encontrado!");
        }
    }

    public List<Especialista> listarTodos(){
        List<Especialista> especialistas = repository.findAll();

        if (!especialistas.isEmpty()){
            return especialistas;
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

    public Especialista atualizar(Especialista especialista, Long id){
        Optional<Especialista> existe = repository.findById(id);

        if (existe.isPresent()){
            Especialista especExistente = existe.get();

            especExistente.setNome(especialista.getNome());
            especExistente.setEmail(especialista.getEmail());
            especExistente.setCargo(especialista.getCargo());
            especExistente.setEspecialidade(especialista.getEspecialidade());
            return repository.save(especExistente);
        }else{
            throw new ResourceNotFoundException("Usuario nao encontrado!");
        }
    }



}
