package com.example.careplus.repository;

import com.example.careplus.model.Responsavel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponsavelRepository extends JpaRepository<Responsavel, Long> {

    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);

    boolean existsByCpfAndIdNot(String cpf, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);

}
