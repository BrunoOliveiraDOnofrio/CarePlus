package com.example.careplus.repository;

import com.example.careplus.model.Cuidador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuidadorRepository extends JpaRepository<Cuidador, Long> {
    List<Cuidador> findByPacienteId(Long pacienteId);

    List<Cuidador> findByResponsavelId(Long responsavelId);

    List<Cuidador> findByPacienteNomeIgnoreCaseStartingWith(String nome);
}
