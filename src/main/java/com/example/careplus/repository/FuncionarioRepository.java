package com.example.careplus.repository;


import com.example.careplus.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    List<Funcionario> findByEmailContainingIgnoreCase(String email);
    Optional<Funcionario> findByEmail(String email);
}
