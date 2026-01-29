package com.example.careplus.service;

import com.example.careplus.dto.dtoCid.ClassificacaoDoencasRequestDto;
import com.example.careplus.model.ClassificacaoDoencas;
import com.example.careplus.model.FichaClinica;
import com.example.careplus.repository.ClassificacaoDoencasRepository;
import com.example.careplus.repository.FichaClinicaRepository;
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
    private final FichaClinicaRepository fichaClinicaRepository;


    public ClassificacaoDoencasService(ClassificacaoDoencasRepository classificacaoDoencasRepository, FichaClinicaRepository fichaClinicaRepository){
        this.classificacaoDoencasRepository = classificacaoDoencasRepository;
        this.fichaClinicaRepository = fichaClinicaRepository;
    }



    public ClassificacaoDoencas cadastrar(ClassificacaoDoencasRequestDto doencaNew){

        Optional<FichaClinica> existe = fichaClinicaRepository.findById(doencaNew.getIdProntuario());

        if(existe.isPresent()){
            ClassificacaoDoencas cidNovo = new ClassificacaoDoencas();
            cidNovo.setCid(doencaNew.getCid());
            cidNovo.setDtModificacao(LocalDate.now());
            cidNovo.setFichaClinica(existe.get());

            return classificacaoDoencasRepository.save(cidNovo);

        } else {
            throw new RuntimeException("Ficha Clínica não encontrada");
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
            FichaClinica fichaClinica = fichaClinicaRepository.findById(dadosAtualizados.getIdProntuario())
                    .orElseThrow(() -> new EntityNotFoundException("Ficha Clínica não encontrada"));
            existente.setFichaClinica(fichaClinica);
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
