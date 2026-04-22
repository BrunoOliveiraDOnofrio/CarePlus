package com.example.careplus.repository;

import com.example.careplus.model.ConsultaProntuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ConsultaProntuarioRepository extends JpaRepository<ConsultaProntuario, Long> {

    List<ConsultaProntuario> findByRecorrenciaId(String recorrenciaId);

    @Query("SELECT c FROM ConsultaProntuario c ORDER BY c.data ASC, c.horarioInicio ASC")
    List<ConsultaProntuario> buscarPorData();

    @Query("SELECT c FROM ConsultaProntuario c WHERE c.paciente.id = :idUsuario ORDER BY c.data ASC, c.horarioInicio ASC")
    List<ConsultaProntuario> buscarPorPaciente(Long idUsuario);

    @Query("SELECT c FROM ConsultaProntuario c JOIN c.consultaFuncionarios cf WHERE c.data = :data AND cf.funcionario.id = :idFuncionario ORDER BY c.horarioInicio ASC")
    List<ConsultaProntuario> buscarConsultasPorFuncionarioEData(Long idFuncionario, LocalDate data);

    @Query("SELECT c FROM ConsultaProntuario c JOIN c.consultaFuncionarios cf WHERE c.data = :data AND cf.funcionario.id = :idFuncionario ORDER BY c.horarioInicio ASC")
    List<ConsultaProntuario> buscarConsultasDiariaPorFuncionario(Long idFuncionario, LocalDate data);

    @Query("SELECT c FROM ConsultaProntuario c WHERE c.data = :data AND c.paciente.id = :idPaciente ORDER BY c.horarioInicio ASC")
    List<ConsultaProntuario> buscarConsultasDiariaPorPaciente(
            @Param("idPaciente") Long idPaciente,
            @Param("data") LocalDate data
    );

    @Query("SELECT c FROM ConsultaProntuario c JOIN c.consultaFuncionarios cf WHERE cf.funcionario.id = :funcionarioId " +
            "AND c.data >= :inicio AND c.data <= :fim " +
            "ORDER BY c.data ASC, c.horarioInicio ASC")
    List<ConsultaProntuario> buscarConsultasPorFuncionarioEPeriodo(
            @Param("funcionarioId") Long funcionarioId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    @Query("SELECT c FROM ConsultaProntuario c WHERE c.paciente.id = :pacienteId " +
            "AND c.data >= :inicio AND c.data <= :fim " +
            "ORDER BY c.data ASC, c.horarioInicio ASC")
    List<ConsultaProntuario> buscarConsultasPorPacienteEPeriodo(
            @Param("pacienteId") Long pacienteId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    @Query("SELECT c FROM ConsultaProntuario c JOIN c.consultaFuncionarios cf WHERE cf.funcionario.id = :funcionarioId AND c.confirmada IS NULL")
    List<ConsultaProntuario> findByFuncionarioIdAndConfirmadaNull(Long funcionarioId);

    @Query("SELECT c FROM ConsultaProntuario c WHERE c.paciente.id = :pacienteId " +
            "AND (c.data < CURRENT_DATE OR (c.data = CURRENT_DATE AND c.horarioInicio < CURRENT_TIME)) " +
            "AND c.observacoesComportamentais IS NOT NULL " +
            "AND c.observacoesComportamentais != '' " +
            "AND c.presenca = true " +
            "AND c.confirmada = true " +
            "ORDER BY c.data DESC, c.horarioInicio DESC")
    List<ConsultaProntuario> buscarUltimaConsultaPorPaciente(@Param("pacienteId") Long pacienteId);

    @Query("SELECT c FROM ConsultaProntuario c JOIN c.consultaFuncionarios cf " +
            "WHERE c.paciente.id = :pacienteId " +
            "AND cf.funcionario.id = :funcionarioId " +
            "AND (c.data < CURRENT_DATE OR (c.data = CURRENT_DATE AND c.horarioInicio < CURRENT_TIME)) " +
            "AND c.observacoesComportamentais IS NOT NULL " +
            "AND c.observacoesComportamentais != '' " +
            "AND c.presenca = true " +
            "AND c.confirmada = true " +
            "ORDER BY c.data DESC, c.horarioInicio DESC")
    List<ConsultaProntuario> buscarUltimaConsultaPorPacienteEFuncionario(
            @Param("pacienteId") Long pacienteId,
            @Param("funcionarioId") Long funcionarioId);

    @Query("SELECT c FROM ConsultaProntuario c WHERE c.paciente.id = :pacienteId AND c.data < CURRENT_DATE ORDER BY c.data DESC, c.horarioInicio DESC")
    List<ConsultaProntuario> buscarUltimaConsultaPassadaPorPaciente(@Param("pacienteId") Long pacienteId);

    // Retorna a última consulta realizada (inclui consultas de dias anteriores e consultas ocorridas hoje
    // cuja hora de início já passou). Usa confirmação = true para considerar apenas consultas efetivamente
    // confirmadas como realizadas.
    @Query("SELECT c FROM ConsultaProntuario c WHERE c.paciente.id = :pacienteId " +
            "AND (c.data < CURRENT_DATE OR (c.data = CURRENT_DATE AND c.horarioInicio <= CURRENT_TIME)) " +
            "AND c.confirmada = true " +
            "ORDER BY c.data DESC, c.horarioInicio DESC")
    List<ConsultaProntuario> buscarUltimaConsultaRealizadaPorPaciente(@Param("pacienteId") Long pacienteId);

    @Query("SELECT c FROM ConsultaProntuario c WHERE c.paciente.id = :pacienteId AND (c.data > CURRENT_DATE OR (c.data = CURRENT_DATE AND c.horarioInicio > CURRENT_TIME)) ORDER BY c.data ASC, c.horarioInicio ASC")
    List<ConsultaProntuario> buscarProximaConsultaPorPaciente(@Param("pacienteId") Long pacienteId);

    @Query("SELECT c FROM ConsultaProntuario c WHERE c.paciente.id = :pacienteId AND (c.data > CURRENT_DATE OR (c.data = CURRENT_DATE AND c.horarioInicio > CURRENT_TIME)) AND c.confirmada = true ORDER BY c.data ASC, c.horarioInicio ASC")
    List<ConsultaProntuario> buscarProximaConsultaConfirmadaPorPaciente(@Param("pacienteId") Long pacienteId);
}
