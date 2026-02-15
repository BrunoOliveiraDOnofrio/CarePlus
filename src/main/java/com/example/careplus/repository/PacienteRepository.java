package com.example.careplus.repository;

import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente,Long> {

    boolean existsByEmail(String email);

    List<Paciente> findByNomeContainingIgnoreCase(String nome);

    boolean existsByCpf(String cpf);

    List<Paciente> findByEmailContainsIgnoreCase(String email);

}