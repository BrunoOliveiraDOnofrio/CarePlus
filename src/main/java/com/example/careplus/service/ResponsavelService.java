package com.example.careplus.service;

import com.example.careplus.dto.dtoEndereco.EnderecoMapper;
import com.example.careplus.dto.dtoResponsavel.ResponsavelMapper;
import com.example.careplus.dto.dtoResponsavel.ResponsavelRequestDto;
import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.model.Endereco;
import com.example.careplus.model.Responsavel;
import com.example.careplus.repository.EnderecoRepository;
import com.example.careplus.repository.ResponsavelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ResponsavelService {

    private final ResponsavelRepository responsavelRepository;
    private final EnderecoRepository enderecoRepository;

    public ResponsavelService(ResponsavelRepository responsavelRepository, EnderecoRepository enderecoRepository){
        this.responsavelRepository = responsavelRepository;
        this.enderecoRepository = enderecoRepository;
    }
    // CRUD SIMPLES
    public Responsavel cadastrar(ResponsavelRequestDto responsavelNew){
        Responsavel paraRegistrar = ResponsavelMapper.toEntity(responsavelNew);
        if(responsavelRepository.existsByEmail(paraRegistrar.getEmail()) || responsavelRepository.existsByCpf(paraRegistrar.getCpf())){
            throw new IllegalArgumentException();
        }

        // Salvar o endereço primeiro, se presente
        if (responsavelNew.getEndereco() != null) {
            Endereco endereco = EnderecoMapper.toEntity(responsavelNew.getEndereco());
            Endereco enderecoSalvo = enderecoRepository.save(endereco);
            paraRegistrar.setEndereco(enderecoSalvo);
        }

        Responsavel responsavelRegistrado = responsavelRepository.save(paraRegistrar);
        return responsavelRegistrado;
    }

    public List<Responsavel> listar(){
        List<Responsavel> responsaveis = responsavelRepository.findAll();
        return responsaveis;
    }

    public Responsavel buscarPorEmail(String email){
        Responsavel responsavelEncontrado = responsavelRepository.findByEmailStartingWith(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));

        return responsavelEncontrado;
    }

    public Responsavel atualizar(Long id, ResponsavelRequestDto responsavelAtt){
        Optional<Responsavel> selecionar = responsavelRepository.findById(id);
        if(selecionar.isEmpty()){
            throw new EntityNotFoundException();
        }
        if(responsavelRepository.existsByEmailAndIdNot(responsavelAtt.getEmail(), id) || responsavelRepository.existsByCpfAndIdNot(responsavelAtt.getCpf(), id)){
            throw new IllegalArgumentException();
        }

        Responsavel selecionado = selecionar.get();

        // Salvar ou atualizar o endereço primeiro, se presente
        if (responsavelAtt.getEndereco() != null) {
            Endereco endereco = EnderecoMapper.toEntity(responsavelAtt.getEndereco());
            if (selecionado.getEndereco() != null) {
                // Se já existe endereço, atualiza mantendo o ID
                endereco.setId(selecionado.getEndereco().getId());
            }
            Endereco enderecoSalvo = enderecoRepository.save(endereco);
            selecionado.setEndereco(enderecoSalvo);
        }

        responsavelRepository.save(ResponsavelMapper.updateEntityFromDto(responsavelAtt , selecionado));

        return selecionado;
    }

    public void deletar(Long id){
        boolean existe = responsavelRepository.existsById(id);
        if (!existe) {
            throw new NoSuchElementException();
        }
        responsavelRepository.deleteById(id);
    }
    // CRUD SIMPLES
    // "ORDENAÇÃO" DE RESPONSÁVEL


}

