package com.example.careplus.repository;

import com.example.careplus.model.ClassificacaoDoencas;
import com.example.careplus.model.Cuidador;
import com.example.careplus.model.Tratamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TratamentoRepository extends JpaRepository<Tratamento, Long> {

    /* Como prontuario nao existe vou deixar comentado para buscar a quantidades de tratamentos já feito; */
    @Query("SELECT COUNT(t) FROM Prontuario p LEFT JOIN p.tratamentos t WHERE p.id = :idProntuario")
    Long buscarQuantidadeDeTratamentosPorId(Long idProntuario);

    List<Tratamento> findByProntuario_Id(Long idProntuario);

    List<Tratamento> findByTipoDeTratamento(String nome);
}
