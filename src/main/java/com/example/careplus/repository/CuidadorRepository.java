package com.example.careplus.repository;

import com.example.careplus.model.Cuidador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CuidadorRepository extends JpaRepository<Cuidador, Long> {
    List<Cuidador> findByPacienteId(Long pacienteId);

    List<Cuidador> findByResponsavelId(Long responsavelId);

    List<Cuidador> findByPacienteNomeIgnoreCaseStartingWith(String nome);

    @Query("SELECT c.responsavel.nome, c.responsavel.telefone FROM Cuidador c WHERE c.paciente.id = :pacienteId")
    List<Object[]> findResponsavelNomeTelefoneByPacienteId(@Param("pacienteId") Long pacienteId);
}
