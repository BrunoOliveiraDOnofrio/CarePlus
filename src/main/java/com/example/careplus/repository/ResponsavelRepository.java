package com.example.careplus.repository;

import com.example.careplus.model.Responsavel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResponsavelRepository extends JpaRepository<Responsavel, Long> {

    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);

    boolean existsByCpfAndIdNot(String cpf, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<Responsavel> findByEmailStartingWith(String email);
    Optional<Responsavel> findByCpf(String cpf);

    Page<Responsavel> findAll(Pageable pageable);

    List<Responsavel> findByNomeContainingIgnoreCase(String nome);
    List<Responsavel> findByEmailContainingIgnoreCase(String email);
    List<Responsavel> findByCpfContaining(String cpf);
}
