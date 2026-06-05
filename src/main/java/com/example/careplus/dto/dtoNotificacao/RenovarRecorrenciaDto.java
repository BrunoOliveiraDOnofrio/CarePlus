package com.example.careplus.dto.dtoNotificacao;

import java.util.List;

public class RenovarRecorrenciaDto {

    private List<Integer> diasSemana;
    private String dataFim;

    public List<Integer> getDiasSemana() { return diasSemana; }
    public void setDiasSemana(List<Integer> diasSemana) { this.diasSemana = diasSemana; }

    public String getDataFim() { return dataFim; }
    public void setDataFim(String dataFim) { this.dataFim = dataFim; }
}
