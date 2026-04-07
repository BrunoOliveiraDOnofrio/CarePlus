package com.example.careplus.repository;

import com.example.careplus.model.ConsultaFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaFuncionarioRepository extends JpaRepository<ConsultaFuncionario, Long> {

    List<ConsultaFuncionario> findByConsultaId(Long consultaId);

    List<ConsultaFuncionario> findByFuncionarioId(Long funcionarioId);

    boolean existsByConsultaIdAndFuncionarioId(Long consultaId, Long funcionarioId);
}

