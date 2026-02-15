package com.example.careplus.repository;

import com.example.careplus.model.Tratamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TratamentoRepository extends JpaRepository<Tratamento, Long> {

    @Query("SELECT COUNT(t) FROM FichaClinica f LEFT JOIN f.tratamentos t WHERE f.id = :idFichaClinica")
    Long buscarQuantidadeDeTratamentosPorId(@Param("idFichaClinica") Long idFichaClinica);

    List<Tratamento> findByFichaClinica_Id(Long idFichaClinica);

    List<Tratamento> findByTipoDeTratamento(String nome);
}