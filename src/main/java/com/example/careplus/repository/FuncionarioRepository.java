package com.example.careplus.repository;


import com.example.careplus.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    List<Funcionario> findByEmailContainingIgnoreCase(String email);
    Optional<Funcionario> findByEmail(String email);
    List<Funcionario> findByEspecialidadeIgnoreCase(String especialidade);

    List<Funcionario> findAllByAtivoTrue();
    Page<Funcionario> findAllByAtivoTrue(Pageable pageable);
    Optional<Funcionario> findByIdAndAtivoTrue(Long id);
    List<Funcionario> findByEmailContainingIgnoreCaseAndAtivoTrue(String email);
    List<Funcionario> findByEspecialidadeIgnoreCaseAndAtivoTrue(String especialidade);
}
