package com.example.careplus.service;

import com.example.careplus.exception.EmailNotExistsException;
import com.example.careplus.model.Especialista;
import com.example.careplus.repository.EspecialistaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

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
            throw new EmailNotExistsException("Preencha o nome e email");
        }
        
        return repository.save(especialista);
    }


}
