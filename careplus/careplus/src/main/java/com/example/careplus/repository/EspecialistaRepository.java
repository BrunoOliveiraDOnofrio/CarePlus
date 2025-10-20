package com.example.careplus.repository;


import com.example.careplus.model.Especialista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EspecialistaRepository extends JpaRepository<Especialista, Long> {
    List<Especialista> findByEmailContainingIgnoreCase(String email);
}
