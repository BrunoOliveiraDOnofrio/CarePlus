package com.example.careplus.service;

import com.example.careplus.controller.dtoMedicacao.MedicacaoRequestDto;
import com.example.careplus.model.Medicacao;
import com.example.careplus.model.Prontuario;
import com.example.careplus.model.Tratamento;
import com.example.careplus.repository.MedicacaoRepository;
import com.example.careplus.repository.ProntuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MedicacaoService {

    private final MedicacaoRepository repository;
    private final ProntuarioRepository prontuarioRepository;

    public MedicacaoService(MedicacaoRepository repository, ProntuarioRepository prontuarioRepository) {
        this.repository = repository;
        this.prontuarioRepository = prontuarioRepository;
    }

    public Medicacao adicionar(MedicacaoRequestDto medicacao) {

        Optional<Prontuario> existe = prontuarioRepository.findById(medicacao.getIdProntuario());

        if(existe.isPresent()){
            Medicacao medicacaoAdicionada = new Medicacao();
            medicacaoAdicionada.setAtivo(medicacao.getAtivo());
            medicacaoAdicionada.setDataFim(medicacao.getDataFim());
            medicacaoAdicionada.setDataInicio(medicacao.getDataInicio());
            medicacaoAdicionada.setNomeMedicacao(medicacao.getNomeMedicacao());
            medicacaoAdicionada.setProntuario(existe.get());

            return repository.save(medicacaoAdicionada);

        } else {
            throw new RuntimeException("Prontuário não encontrado");
        }
    }

    public long contarAtivas() {
        return repository.countByAtivoTrue();
    }


    public List<Medicacao> listarOrdenadasPorNome() {
        return repository.findAllByOrderByNomeMedicacaoAsc();
    }


    public Medicacao atualizar(Long id, MedicacaoRequestDto dto) {
        Optional<Medicacao> medicacaoExistente = repository.findById(id);

        if (medicacaoExistente.isPresent()) {
            Medicacao medicacao = medicacaoExistente.get();

            medicacao.setNomeMedicacao(dto.getNomeMedicacao());
            medicacao.setDataInicio(dto.getDataInicio());
            medicacao.setDataFim(dto.getDataFim());
            medicacao.setAtivo(dto.getAtivo());

            if (dto.getIdProntuario() != null) {
                Optional<Prontuario> prontuario = prontuarioRepository.findById(dto.getIdProntuario());
                prontuario.ifPresent(medicacao::setProntuario);
            }
            return repository.save(medicacao);
        } else {
            throw new RuntimeException("Medicação não encontrada para atualização.");
        }
    }


    public void deletar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Medicação não encontrada para exclusão.");
        }
    }


    public List<Medicacao> ordenarPorTempoMedicando() {
        List<Medicacao> todas = repository.findAll();

        todas.sort((m1, m2) -> {
            long dias1 = m1.getTempoMedicando().toDays();
            long dias2 = m2.getTempoMedicando().toDays();
            return Long.compare(dias1, dias2);
        });

        return todas;
    }
}
