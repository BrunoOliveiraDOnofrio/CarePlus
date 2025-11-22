package com.example.careplus.service;

import com.example.careplus.controller.dtoCid.ClassificacaoDoencasRequestDto;
import com.example.careplus.model.ClassificacaoDoencas;
import com.example.careplus.model.Prontuario;
import com.example.careplus.repository.ClassificacaoDoencasRepository;
import com.example.careplus.repository.PacienteRepository;
import com.example.careplus.repository.ProntuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.time.LocalTime.now;

@Service
public class ClassificacaoDoencasService {

    private final ClassificacaoDoencasRepository classificacaoDoencasRepository;
    private final ProntuarioRepository prontuarioRepository;


    public ClassificacaoDoencasService(ClassificacaoDoencasRepository classificacaoDoencasRepository, ProntuarioRepository prontuarioRepository){
        this.classificacaoDoencasRepository = classificacaoDoencasRepository;
        this.prontuarioRepository = prontuarioRepository;
    }



    public ClassificacaoDoencas cadastrar(ClassificacaoDoencasRequestDto doencaNew){

        Optional<Prontuario> existe = prontuarioRepository.findById(doencaNew.getIdProntuario());

        if(existe.isPresent()){
            ClassificacaoDoencas cidNovo = new ClassificacaoDoencas();
            cidNovo.setCid(doencaNew.getCid());
            cidNovo.setDtModificacao(LocalDate.now());
            cidNovo.setProntuario(existe.get());

            return classificacaoDoencasRepository.save(cidNovo);

        } else {
            throw new RuntimeException("Prontuário não encontrado");
        }

    }

    public List<ClassificacaoDoencas> listar() {
        List<ClassificacaoDoencas> doencas = classificacaoDoencasRepository.findAll();
        return doencas;
    }

    public ClassificacaoDoencas buscarPorId(Long id) {
        Optional<ClassificacaoDoencas> classificacaoDoencasOpt = classificacaoDoencasRepository.findById(id);

        if (classificacaoDoencasOpt.isPresent()){
            return classificacaoDoencasOpt.get();
        }else{
            throw new NoSuchElementException();
        }
    }

    public ClassificacaoDoencas atualizar(Long id, ClassificacaoDoencasRequestDto dadosAtualizados) {
        ClassificacaoDoencas existente = classificacaoDoencasRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Classificação não encontrada"));
        existente.setCid(dadosAtualizados.getCid());
        existente.setDtModificacao(LocalDate.now());
        if (dadosAtualizados.getIdProntuario() != null) {
            Prontuario prontuario = prontuarioRepository.findById(dadosAtualizados.getIdProntuario())
                    .orElseThrow(() -> new EntityNotFoundException("Prontuário não encontrado"));
            existente.setProntuario(prontuario);
        }
        return classificacaoDoencasRepository.save(existente);
    }


    public void deletar(Long id){
        boolean existe = classificacaoDoencasRepository.existsById(id);
        if (!existe){
            throw new NoSuchElementException();
        }
        classificacaoDoencasRepository.deleteById(id);
    }


}
