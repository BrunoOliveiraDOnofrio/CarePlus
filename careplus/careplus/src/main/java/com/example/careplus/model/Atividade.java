package com.example.careplus.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAtividade;

    private String item;
    private Integer tempoExposicao; // minutos
    private LocalDate dataImplementacao;

    public Long getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(Long idAtividade) {
        this.idAtividade = idAtividade;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getTempoExposicao() {
        return tempoExposicao;
    }

    public void setTempoExposicao(Integer tempoExposicao) {
        this.tempoExposicao = tempoExposicao;
    }

    public LocalDate getDataImplementacao() {
        return dataImplementacao;
    }

    public void setDataImplementacao(LocalDate dataImplementacao) {
        this.dataImplementacao = dataImplementacao;
    }
}
