package com.example.careplus.repository;

import com.example.careplus.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
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

}
