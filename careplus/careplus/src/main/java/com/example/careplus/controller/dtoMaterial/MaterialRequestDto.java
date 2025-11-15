package com.example.careplus.controller.dtoMaterial;

import java.time.LocalDate;

public class MaterialRequestDto {
    private String item;
    private Integer tempoExposicao;
    private LocalDate dataImplementacao;
    private Long idConsulta;

    public Long getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Long idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public Integer getTempoExposicao() { return tempoExposicao; }
    public void setTempoExposicao(Integer tempoExposicao) { this.tempoExposicao = tempoExposicao; }

    public LocalDate getDataImplementacao() { return dataImplementacao; }
    public void setDataImplementacao(LocalDate dataImplementacao) { this.dataImplementacao = dataImplementacao; }

}
