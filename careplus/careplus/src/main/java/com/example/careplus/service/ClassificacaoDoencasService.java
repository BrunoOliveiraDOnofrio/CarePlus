package com.example.careplus.service;

import com.example.careplus.model.ClassificacaoDoencas;
import com.example.careplus.repository.ClassificacaoDoencasRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.time.LocalTime.now;

@Service
public class ClassificacaoDoencasService {

    private final ClassificacaoDoencasRepository classificacaoDoencasRepository;

    public ClassificacaoDoencasService(ClassificacaoDoencasRepository classificacaoDoencasRepository){
        this.classificacaoDoencasRepository = classificacaoDoencasRepository;
    }

    public ClassificacaoDoencas cadastrar(ClassificacaoDoencas doencaNew){
        doencaNew.setDtModificacao(LocalDate.now());
        return classificacaoDoencasRepository.save(doencaNew);
    }

    public List<ClassificacaoDoencas> listar() {
        List<ClassificacaoDoencas> doencas = classificacaoDoencasRepository.findAll();
        if (doencas.isEmpty()){
            throw new NoSuchElementException();
        }
        return doencas;
    }

    public Optional<ClassificacaoDoencas> buscarPorId(Long id) {
        boolean existe = classificacaoDoencasRepository.existsById(id);
        if (!existe){
            throw new NoSuchElementException();
        }
        return classificacaoDoencasRepository.findById(id);
    }

    public Optional<ClassificacaoDoencas> atualizar(Long id, ClassificacaoDoencas dadosAtualizados) {
        return classificacaoDoencasRepository.findById(id).map(doencaExistente -> {
            doencaExistente.setCid(dadosAtualizados.getCid());
            doencaExistente.setDtModificacao(LocalDate.now());
            return classificacaoDoencasRepository.save(doencaExistente);
        });
    }

    public void deletar(Long id){
        boolean existe = classificacaoDoencasRepository.existsById(id);
        if (!existe){
            throw new NoSuchElementException();
        }
        classificacaoDoencasRepository.deleteById(id);
    }


}
