package com.example.careplus.repository;

import com.example.careplus.model.ConsultaProntuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaProntuarioRepository extends JpaRepository<ConsultaProntuario, Long> {

    @Query("SELECT c FROM ConsultaProntuario c ORDER BY c.dataHora")
    List<ConsultaProntuario> buscarPorData();

    @Query("SELECT c FROM ConsultaProntuario c WHERE c.paciente.id= :idUsuario ORDER BY c.dataHora ")
    List<ConsultaProntuario> buscarPorPaciente(Long idUsuario);

    @Query("SELECT c FROM ConsultaProntuario c WHERE FUNCTION('DATE', c.dataHora) = CURRENT_DATE AND c.funcionario.Id = :idFuncionario")
    List<ConsultaProntuario> consultasDoDia(Long idFuncionario);

    @Query("SELECT c FROM ConsultaProntuario c WHERE FUNCTION('DATE', c.dataHora) = :data AND c.funcionario.Id = :idFuncionario ORDER BY c.dataHora")
    List<ConsultaProntuario> buscarConsultasPorFuncionarioEData(Long idFuncionario, LocalDate data);

    @Query("SELECT c FROM ConsultaProntuario c WHERE c.funcionario.Id = :funcionarioId " +
            "AND c.dataHora >= :inicio AND c.dataHora <= :fim " +
            "ORDER BY c.dataHora ASC")
    List<ConsultaProntuario> buscarConsultasPorFuncionarioEPeriodo(
            @Param("funcionarioId") Long funcionarioId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    List<ConsultaProntuario> findByFuncionarioIdAndConfirmadaNull(Long funcionarioId);

    @Query("SELECT c FROM ConsultaProntuario c WHERE c.paciente.id = :pacienteId " +
            "AND c.observacoesComportamentais IS NOT NULL " +
            "AND c.observacoesComportamentais != '' " +
            "AND c.presenca = true " +
            "AND c.confirmada = true " +
            "ORDER BY c.dataHora DESC")
    List<ConsultaProntuario> buscarUltimaConsultaPorPaciente(@Param("pacienteId") Long pacienteId);

    @Query("SELECT c FROM ConsultaProntuario c WHERE c.paciente.id = :pacienteId AND c.dataHora < CURRENT_TIMESTAMP ORDER BY c.dataHora DESC")
    List<ConsultaProntuario> buscarUltimaConsultaPassadaPorPaciente(@Param("pacienteId") Long pacienteId);

    @Query("SELECT c FROM ConsultaProntuario c WHERE c.paciente.id = :pacienteId AND c.dataHora > CURRENT_TIMESTAMP ORDER BY c.dataHora ASC")
    List<ConsultaProntuario> buscarProximaConsultaPorPaciente(@Param("pacienteId") Long pacienteId);

    @Query("SELECT c FROM ConsultaProntuario c WHERE c.paciente.id = :pacienteId AND c.dataHora > CURRENT_TIMESTAMP AND c.confirmada = true ORDER BY c.dataHora ASC")
    List<ConsultaProntuario> buscarProximaConsultaConfirmadaPorPaciente(@Param("pacienteId") Long pacienteId);
}

