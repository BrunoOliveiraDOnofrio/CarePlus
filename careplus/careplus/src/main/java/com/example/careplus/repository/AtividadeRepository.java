package com.example.careplus.repository;

import com.example.careplus.model.Atividade;
import com.example.careplus.model.Tratamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
    List<Atividade> findByTempoExposicaoGreaterThan(Integer tempo);

    List<Atividade> findByProntuario_Id(Long idProntuario);
}
