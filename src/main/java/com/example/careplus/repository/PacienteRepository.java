package com.example.careplus.repository;

import com.example.careplus.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente,Long> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    List<Paciente> findAllByAtivoTrue();

    Page<Paciente> findAllByAtivoTrue(Pageable pageable);

    Optional<Paciente> findByIdAndAtivoTrue(Long id);

    Page<Paciente> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome, Pageable pageable);

    List<Paciente> findByEmailContainsIgnoreCaseAndAtivoTrue(String email);

    Page<Paciente> findByEmailContainsIgnoreCaseAndAtivoTrue(String email, Pageable pageable);


    @Query("SELECT p FROM Paciente p WHERE p.cpf LIKE %:cpf% AND p.ativo = true")
    Page<Paciente> buscarPorCpfEAtivoTrue(String cpf, Pageable pageable);

    @Query(value = "SELECT COUNT(DISTINCT p.id) FROM paciente p LEFT JOIN consulta_prontuario c ON p.id = c.paciente_id AND c.data >= CURDATE() WHERE p.ativo = true AND c.paciente_id IS NULL", nativeQuery = true)
    Long findPacientesSemConsulta();

    @Query("SELECT p.convenio, COUNT(p) FROM Paciente p WHERE p.ativo = true GROUP BY p.convenio ORDER BY COUNT(p) DESC")
    List<Object[]> findPacientesPorConvenio();

    @Query("SELECT COUNT(DISTINCT p.id) FROM Paciente p JOIN ConsultaProntuario c ON p.id = c.paciente.id WHERE p.ativo = true AND c.data >= CURRENT_DATE")
    Long countPacientesAtivosComConsultaMarcada();
}
