package com.example.careplus.controller.dtoAtividade;

import java.time.LocalDate;

public class AtividadeRequestDto {
    private String item;
    private Integer tempoExposicao;
    private LocalDate dataImplementacao;

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public Integer getTempoExposicao() { return tempoExposicao; }
    public void setTempoExposicao(Integer tempoExposicao) { this.tempoExposicao = tempoExposicao; }

    public LocalDate getDataImplementacao() { return dataImplementacao; }
    public void setDataImplementacao(LocalDate dataImplementacao) { this.dataImplementacao = dataImplementacao; }
}
