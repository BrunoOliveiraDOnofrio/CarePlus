package com.example.careplus.service;

import com.example.careplus.controller.dtoTratamento.TratamentoRequestDto;
import com.example.careplus.model.ClassificacaoDoencas;
import com.example.careplus.model.Prontuario;
import com.example.careplus.model.Tratamento;
import com.example.careplus.repository.ProntuarioRepository;
import com.example.careplus.repository.TratamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TratamentoService {


    private final TratamentoRepository tratamentoRepository;
    private final ProntuarioRepository prontuarioRepository;

    public TratamentoService(TratamentoRepository tratamentoRepository, ProntuarioRepository prontuarioRepository) {
        this.tratamentoRepository = tratamentoRepository;
        this.prontuarioRepository = prontuarioRepository;
    }

    public Tratamento cadastrar(TratamentoRequestDto tratamento){

        Optional<Prontuario> existe = prontuarioRepository.findById(tratamento.getIdProntuario());

        if(existe.isPresent()){
            Tratamento tratamento1 = new Tratamento();
            tratamento1.setDataModificacao(LocalDateTime.now());
            tratamento1.setFinalizado(tratamento.getFinalizado());
            tratamento1.setTipoDeTratamento(tratamento.getTipoDeTratamento());
            tratamento1.setProntuario(existe.get());

            return tratamentoRepository.save(tratamento1);

        } else {
            throw new RuntimeException("Prontuário não encontrado");
        }
    }

    public List<Tratamento> buscarByNome(String nomeTratamento){
        List<Tratamento> tratamentoEncontrado = tratamentoRepository.findByTipoDeTratamento(nomeTratamento);
        if (tratamentoEncontrado.isEmpty()){
            return null;
        }

        return tratamentoEncontrado;
    }

  /*
    public Long buscarPeloIdProntuario(Long idProntuario){
        Long tratamentoContagem = tratamentoRepository.buscarQuantidadeDeTratamentosPorId(idProntuario);
        if (tratamentoContagem == null || tratamentoContagem == 0){
            return 0L;
        }

        return tratamentoContagem;
    }
   */
}
