package com.example.careplus.repository;


import com.example.careplus.model.Especialista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EspecialistaRepository extends JpaRepository<Especialista, Long> {
    List<Especialista> findByEmailContainingIgnoreCase(String email);
    Optional<Especialista> findByEmail(String email);
}
