package com.example.careplus.repository;

import com.example.careplus.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findAllByOrderByDataFimAsc();

    boolean existsByRecorrenciaId(String recorrenciaId);

    @Transactional
    void deleteByRecorrenciaId(String recorrenciaId);

    @Transactional
    void deleteByDataFimBefore(LocalDate data);
}
