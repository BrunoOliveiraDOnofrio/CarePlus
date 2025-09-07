package com.example.careplus.repository;

import com.example.careplus.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {

    boolean existsByEmail(String email);

    List<Usuario> findByEmailContainsIgnoreCase(String email);

}