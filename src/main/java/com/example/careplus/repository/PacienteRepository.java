package com.example.careplus.repository;

import com.example.careplus.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente,Long> {

    boolean existsByEmail(String email);

    List<Paciente> findByNomeContainingIgnoreCase(String nome);

    boolean existsByCpf(String cpf);

    List<Paciente> findByEmailContainsIgnoreCase(String email);

    List<Paciente> findAllByAtivoTrue();

    Page<Paciente> findAllByAtivoTrue(Pageable pageable);

    Optional<Paciente> findByIdAndAtivoTrue(Long id);

    List<Paciente> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    List<Paciente> findByEmailContainsIgnoreCaseAndAtivoTrue(String email);

}