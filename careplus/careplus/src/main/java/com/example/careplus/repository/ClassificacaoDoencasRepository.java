package com.example.careplus.repository;

import com.example.careplus.model.ClassificacaoDoencas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassificacaoDoencasRepository extends JpaRepository<ClassificacaoDoencas, Long> {
    List<ClassificacaoDoencas> findByProntuario_Id(Long idProntuario);
}
