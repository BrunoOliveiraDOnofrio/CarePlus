package com.example.careplus.service;

import com.example.careplus.dto.dtoTratamento.TratamentoRequestDto;
import com.example.careplus.model.FichaClinica;
import com.example.careplus.model.Tratamento;
import com.example.careplus.repository.FichaClinicaRepository;
import com.example.careplus.repository.TratamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TratamentoService {


    private final TratamentoRepository tratamentoRepository;
    private final FichaClinicaRepository fichaClinicaRepository;

    public TratamentoService(TratamentoRepository tratamentoRepository, FichaClinicaRepository fichaClinicaRepository) {
        this.tratamentoRepository = tratamentoRepository;
        this.fichaClinicaRepository = fichaClinicaRepository;
    }

    public Tratamento cadastrar(TratamentoRequestDto tratamento){

        Optional<FichaClinica> existe = fichaClinicaRepository.findById(tratamento.getIdProntuario());

        if(existe.isPresent()){
            Tratamento tratamento1 = new Tratamento();
            tratamento1.setDataModificacao(LocalDateTime.now());
            tratamento1.setFinalizado(tratamento.getFinalizado());
            tratamento1.setTipoDeTratamento(tratamento.getTipoDeTratamento());
            tratamento1.setFichaClinica(existe.get());

            return tratamentoRepository.save(tratamento1);

        } else {
            throw new RuntimeException("Ficha Clínica não encontrada");
        }
    }

    public List<Tratamento> buscarByNome(String nomeTratamento){
        List<Tratamento> tratamentoEncontrado = tratamentoRepository.findByTipoDeTratamento(nomeTratamento);
        if (tratamentoEncontrado.isEmpty()){
            return null;
        }

        return tratamentoEncontrado;
    }

    public Long buscarPeloIdFichaClinica(Long idFichaClinica){
        Long tratamentoContagem = tratamentoRepository.buscarQuantidadeDeTratamentosPorId(idFichaClinica);
        if (tratamentoContagem == null || tratamentoContagem == 0){
            return 0L;
        }

        return tratamentoContagem;
    }
}
