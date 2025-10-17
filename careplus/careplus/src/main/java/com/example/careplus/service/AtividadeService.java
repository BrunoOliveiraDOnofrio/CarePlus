package com.example.careplus.service;

import com.example.careplus.controller.dtoAtividade.*;
import com.example.careplus.model.Atividade;
import com.example.careplus.model.Prontuario;
import com.example.careplus.model.Tratamento;
import com.example.careplus.repository.AtividadeRepository;
import com.example.careplus.repository.ProntuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final ProntuarioRepository prontuarioRepository;

    public AtividadeService(AtividadeRepository atividadeRepository, ProntuarioRepository prontuarioRepository) {
        this.atividadeRepository = atividadeRepository;
        this.prontuarioRepository = prontuarioRepository;
    }

    public AtividadeResponseDto cadastrar(AtividadeRequestDto dto) {
        Atividade atividade = AtividadeMapper.toEntity(dto);

        Optional<Prontuario> existe = prontuarioRepository.findById(dto.getIdProntuario());

        if(existe.isPresent()){
            atividade.setProntuario(existe.get());

            atividade = atividadeRepository.save(atividade);
            return AtividadeMapper.toResponseDto(atividade);

        } else {
            throw new RuntimeException("Prontuário não encontrado");
        }



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
