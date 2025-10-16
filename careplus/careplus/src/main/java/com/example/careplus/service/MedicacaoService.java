package com.example.careplus.service;

import com.example.careplus.model.Medicacao;
import com.example.careplus.repository.MedicacaoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MedicacaoService {

    private final MedicacaoRepository repository;

    public MedicacaoService(MedicacaoRepository repository) {
        this.repository = repository;
    }

    public void adicionar(Medicacao medicacao) {
        repository.save(medicacao);
    }

    public long contarAtivas() {
        return repository.countByAtivoTrue();
    }

    public List<Medicacao> listarOrdenadasPorNome() {
        return repository.findAllByOrderByNomeMedicacaoAsc();
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
