package com.example.careplus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class ClassificacaoDoencas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cid;
    private LocalDate dtModificacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public LocalDate getDtModificacao() {
        return dtModificacao;
    }

    public void setDtModificacao(LocalDate dtModificacao) {
        this.dtModificacao = dtModificacao;
    }
}
