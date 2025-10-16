package com.example.careplus.service;

import com.example.careplus.model.Tratamento;
import com.example.careplus.repository.TratamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TratamentoService {


    private final TratamentoRepository tratamentoRepository;

    public TratamentoService(TratamentoRepository tratamentoRepository) {
        this.tratamentoRepository = tratamentoRepository;
    }

    public Tratamento cadastrar(Tratamento tratamento){
        return tratamentoRepository.save(tratamento);
    }

    public List<Tratamento> buscarByNome(String nomeTratamento){
        List<Tratamento> tratamentoEncontrado = tratamentoRepository.findByTipoDeTratamento(nomeTratamento);
        if (tratamentoEncontrado.isEmpty()){
            return null;
        }

        return tratamentoEncontrado;
    }

    /*
    public Long buscarPeloNome(Long idProntuario){
        Long contagem = tratamentoRepository.buscarQuantidadeDeTratamentosPorId(Long idProntuario);
        if (idProntuario.isEmpty()){
            return null;
        }

        return idProntuario;
    }

     */
}
