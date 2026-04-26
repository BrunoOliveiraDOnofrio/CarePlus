package com.example.careplus.repository;


import com.example.careplus.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    List<Funcionario> findByEmailContainingIgnoreCase(String email);
    Optional<Funcionario> findByEmail(String email);
    List<Funcionario> findByEspecialidadeIgnoreCase(String especialidade);

    List<Funcionario> findAllByAtivoTrue();
    Page<Funcionario> findAllByAtivoTrue(Pageable pageable);
    Optional<Funcionario> findByIdAndAtivoTrue(Long id);
    List<Funcionario> findByEmailContainingIgnoreCaseAndAtivoTrue(String email);
    Page<Funcionario> findByEmailContainingIgnoreCaseAndAtivoTrue(String email, Pageable pageable);
    List<Funcionario> findByEspecialidadeIgnoreCaseAndAtivoTrue(String especialidade);
    List<Funcionario> findByCargoIgnoreCase(String supervisor);

    Page<Funcionario> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome, Pageable pageable);

    // Busca funcionários ativos cuja especialidade não seja 'admin' nem 'agendamento' (case-insensitive)
    // Inclui registros com especialidade NULL (tratados como diferentes desses valores)
    @Query(value = "SELECT f.especialidade AS Setor, COUNT(DISTINCT f.id) AS Total_Funcionarios, COUNT(DISTINCT cp.paciente_id) AS Total_Pacientes FROM funcionario f LEFT JOIN consulta_funcionario cf ON cf.funcionario_id = f.id LEFT JOIN consulta_prontuario cp ON cp.id = cf.consulta_id WHERE f.ativo = 1 GROUP BY f.especialidade ORDER BY Total_Pacientes DESC", nativeQuery = true)
    List<Object[]> findAllAtivosExcluindoAdminEAgendamento();

    @Query("SELECT f FROM Funcionario f WHERE f.documento LIKE %:cpf% AND f.ativo = true")
    Page<Funcionario> buscarPorCpf(String cpf,  Pageable pageable);

    Page<Funcionario> findAllByAtivoFalse(Pageable pageable);
}
