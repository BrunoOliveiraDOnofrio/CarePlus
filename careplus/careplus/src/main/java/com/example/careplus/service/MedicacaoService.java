package com.example.careplus.service;

import com.example.careplus.dto.dtoMedicacao.MedicacaoRequestDto;
import com.example.careplus.model.Medicacao;
import com.example.careplus.model.FichaClinica;
import com.example.careplus.repository.MedicacaoRepository;
import com.example.careplus.repository.FichaClinicaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicacaoService {

    private final MedicacaoRepository repository;
    private final FichaClinicaRepository fichaClinicaRepository;

    public MedicacaoService(MedicacaoRepository repository, FichaClinicaRepository fichaClinicaRepository) {
        this.repository = repository;
        this.fichaClinicaRepository = fichaClinicaRepository;
    }

    public Medicacao adicionar(MedicacaoRequestDto medicacao) {

        Optional<FichaClinica> existe = fichaClinicaRepository.findById(medicacao.getIdProntuario());

        if(existe.isPresent()){
            Medicacao medicacaoAdicionada = new Medicacao();
            medicacaoAdicionada.setAtivo(medicacao.getAtivo());
            medicacaoAdicionada.setDataFim(medicacao.getDataFim());
            medicacaoAdicionada.setDataInicio(medicacao.getDataInicio());
            medicacaoAdicionada.setNomeMedicacao(medicacao.getNomeMedicacao());
            medicacaoAdicionada.setFichaClinica(existe.get());

            return repository.save(medicacaoAdicionada);

        } else {
            throw new RuntimeException("Ficha Clínica não encontrada");
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
                Optional<FichaClinica> fichaClinica = fichaClinicaRepository.findById(dto.getIdProntuario());
                fichaClinica.ifPresent(medicacao::setFichaClinica);
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
