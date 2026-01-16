package com.example.careplus.repository;

import com.example.careplus.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {


    @Query("SELECT c FROM Consulta c ORDER BY c.dataHora")
    List<Consulta> buscarPorData();

    @Query("SELECT c FROM Consulta c WHERE c.paciente.id= :idUsuario ORDER BY c.dataHora ")
    List<Consulta> buscarPorPaciente(Long idUsuario);

    @Query("SELECT c FROM Consulta c WHERE FUNCTION('DATE', c.dataHora) = CURRENT_DATE AND c.funcionario.Id = :idFuncionario")
    List<Consulta> consultasDoDia(Long idFuncionario);

    @Query("SELECT c FROM Consulta c WHERE FUNCTION('DATE', c.dataHora) = :data AND c.funcionario.Id = :idFuncionario ORDER BY c.dataHora")
    List<Consulta> buscarConsultasPorFuncionarioEData(Long idFuncionario, LocalDate data);

    @Query("SELECT c FROM Consulta c WHERE c.funcionario.Id = :funcionarioId " +
            "AND c.dataHora >= :inicio AND c.dataHora <= :fim " +
            "ORDER BY c.dataHora ASC")
    List<Consulta> buscarConsultasPorFuncionarioEPeriodo(
            @Param("funcionarioId") Long funcionarioId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    List<Consulta> findByFuncionarioIdAndConfirmadaNull(Long funcionarioId);

    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = :pacienteId ORDER BY c.dataHora DESC")
    List<Consulta> buscarUltimaConsultaPorPaciente(@Param("pacienteId") Long pacienteId);

    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = :pacienteId AND c.dataHora > CURRENT_TIMESTAMP ORDER BY c.dataHora ASC")
    List<Consulta> buscarProximaConsultaPorPaciente(@Param("pacienteId") Long pacienteId);
}
