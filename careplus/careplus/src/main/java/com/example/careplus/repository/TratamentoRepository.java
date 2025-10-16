package com.example.careplus.repository;

import com.example.careplus.model.Cuidador;
import com.example.careplus.model.Tratamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TratamentoRepository extends JpaRepository<Tratamento, Long> {

    /* Como prontuario nao existe vou deixar comentado para buscar a quantidades de tratamentos já feito;
    @Query("SELECT COUNT(t) FROM Prontuario p LEFT JOIN p.tratamentos t WHERE p.id = :idProntuario")
    Long buscarQuantidadeDeTratamentosPorId( Long idProntuario);
     */

    List<Tratamento> findByTipoDeTratamento(String nome);
}
