package com.example.careplus.repository;

import com.example.careplus.model.FichaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FichaClinicaRepository extends JpaRepository<FichaClinica, Long> {

    Optional<FichaClinica> findByPacienteNomeContainsIgnoreCase(String pacienteNome);

    Optional<FichaClinica> findByPacienteCpf(String pacienteCpf);

    Optional<FichaClinica> findByPacienteId(Long pacienteId);

}

