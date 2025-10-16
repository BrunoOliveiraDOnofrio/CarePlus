package com.example.careplus.service;

import com.example.careplus.controller.dtoAtividade.*;
import com.example.careplus.model.Atividade;
import com.example.careplus.repository.AtividadeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;

    public AtividadeService(AtividadeRepository atividadeRepository) {
        this.atividadeRepository = atividadeRepository;
    }

    public AtividadeResponseDto cadastrar(AtividadeRequestDto dto) {
        Atividade atividade = AtividadeMapper.toEntity(dto);
        atividade = atividadeRepository.save(atividade);
        return AtividadeMapper.toResponseDto(atividade);
    }

    public List<AtividadeResponseDto> listar() {
        List<Atividade> atividades = atividadeRepository.findAll();
        if (atividades.isEmpty()) throw new EntityNotFoundException("Nenhuma atividade encontrada");
        return AtividadeMapper.toResponseDto(atividades);
    }

    public AtividadeResponseDto atualizar(Long id, AtividadeRequestDto dto) {
        Atividade existente = atividadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada"));

        existente.setItem(dto.getItem());
        existente.setTempoExposicao(dto.getTempoExposicao());
        existente.setDataImplementacao(dto.getDataImplementacao());

        atividadeRepository.save(existente);
        return AtividadeMapper.toResponseDto(existente);
    }

    public void deletar(Long id) {
        if (!atividadeRepository.existsById(id))
            throw new EntityNotFoundException("Atividade não encontrada");
        atividadeRepository.deleteById(id);
    }

    public long contarAtividades() {
        return atividadeRepository.count();
    }

    public List<AtividadeResponseDto> listarPorTempoExposicaoMaiorQue(Integer tempo) {
        List<Atividade> atividades = atividadeRepository.findByTempoExposicaoGreaterThan(tempo);
        if (atividades.isEmpty()) throw new EntityNotFoundException("Nenhuma atividade com tempo maior que " + tempo);
        return AtividadeMapper.toResponseDto(atividades);
    }
}
