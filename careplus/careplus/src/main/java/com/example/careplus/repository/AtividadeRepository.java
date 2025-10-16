package com.example.careplus.repository;

import com.example.careplus.model.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
    List<Atividade> findByTempoExposicaoGreaterThan(Integer tempo);
}
