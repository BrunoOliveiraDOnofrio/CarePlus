package com.example.careplus.service;

import com.example.careplus.controller.dtoResponsavel.ResponsavelMapper;
import com.example.careplus.controller.dtoResponsavel.ResponsavelRequestDto;
import com.example.careplus.controller.dtoResponsavel.ResponsavelResponseDto;
import com.example.careplus.model.Responsavel;
import com.example.careplus.model.Usuario;
import com.example.careplus.repository.ResponsavelRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ResponsavelService {

    private final ResponsavelRepository responsavelRepository;

    public ResponsavelService(ResponsavelRepository responsavelRepository){
        this.responsavelRepository = responsavelRepository;
    }

    public ResponsavelResponseDto cadastrar(ResponsavelRequestDto responsavelNew){
        Responsavel paraRegistrar = ResponsavelMapper.toEntity(responsavelNew);
        if(responsavelRepository.existsByEmail(paraRegistrar.getEmail()) || responsavelRepository.existsByCpf(paraRegistrar.getCpf())){
            throw new IllegalArgumentException();
        }
        Responsavel responsavelRegistrado = responsavelRepository.save(paraRegistrar);
        ResponsavelResponseDto resposta = ResponsavelMapper.toResponseDto(responsavelRegistrado);
        return resposta;
    }

    public List<ResponsavelResponseDto> listar(){
        List<Responsavel> responsaveis = responsavelRepository.findAll();
        if(responsaveis.isEmpty()){
            throw new NoSuchElementException();
        }

        return ResponsavelMapper.toResponseDto(responsaveis);
    }

    public ResponsavelResponseDto atualizar(Long id, ResponsavelRequestDto responsavelAtt){
        Optional<Responsavel> selecionar = responsavelRepository.findById(id);
        if(selecionar.isEmpty()){
            throw new NoSuchElementException();
        }

        Responsavel selecionado = selecionar.get();

        responsavelRepository.save(ResponsavelMapper.updateEntityFromDto(responsavelAtt , selecionado));

        return ResponsavelMapper.toResponseDto(selecionado);
    }

    public void deletar(Long id){
        boolean existe = responsavelRepository.existsById(id);
        if (!existe) {
            throw new NoSuchElementException();
        }
        responsavelRepository.deleteById(id);
    }

}

