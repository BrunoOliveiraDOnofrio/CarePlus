package com.example.careplus.repository;

import com.example.careplus.model.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {

    Optional<Prontuario> findByPacienteNomeContainsIgnoreCase(String pacienteNome);

    Optional<Prontuario> findByPacienteCpf(String pacienteCpf);

    Optional<Prontuario> findByPacienteId(Long pacienteId);

}
