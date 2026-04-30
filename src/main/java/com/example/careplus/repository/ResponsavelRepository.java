package com.example.careplus.repository;

import com.example.careplus.dto.dtoResponsavel.ResponsavelResponseNotificacaoDto;
import com.example.careplus.model.Responsavel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResponsavelRepository extends JpaRepository<Responsavel, Long> {

    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);

    boolean existsByCpfAndIdNot(String cpf, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<Responsavel> findByEmailStartingWith(String email);
    Optional<Responsavel> findByCpf(String cpf);

    Page<Responsavel> findAll(Pageable pageable);
    Page<Responsavel> findAllByAtivoTrue(Pageable pageable);
    Page<Responsavel> findAllByAtivoFalse(Pageable pageable);

    List<Responsavel> findByNomeContainingIgnoreCase(String nome);
    List<Responsavel> findByEmailContainingIgnoreCase(String email);
    List<Responsavel> findByCpfContaining(String cpf);

    @Query("SELECT new com.example.careplus.dto.dtoResponsavel.ResponsavelResponseNotificacaoDto(c.responsavel.nome, c.responsavel.email, c.responsavel.telefone) FROM Cuidador c WHERE c.paciente.id = :idPaciente")
    Optional<ResponsavelResponseNotificacaoDto> findNotificacaoDtoByPacienteId(@Param("idPaciente") Long idPaciente);
}
