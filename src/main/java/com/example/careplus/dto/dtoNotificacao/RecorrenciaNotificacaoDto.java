package com.example.careplus.dto.dtoNotificacao;

import java.util.List;

public class RecorrenciaNotificacaoDto {

    private String recorrenciaId;
    private String profissionalNome;
    private String especialidade;
    private List<Integer> diasSemana;
    private String horarioInicio;
    private String horarioFim;
    private String tipo;
    private String dataFim;
    private int diasRestantes;

    public String getRecorrenciaId() { return recorrenciaId; }
    public void setRecorrenciaId(String recorrenciaId) { this.recorrenciaId = recorrenciaId; }

    public String getProfissionalNome() { return profissionalNome; }
    public void setProfissionalNome(String profissionalNome) { this.profissionalNome = profissionalNome; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public List<Integer> getDiasSemana() { return diasSemana; }
    public void setDiasSemana(List<Integer> diasSemana) { this.diasSemana = diasSemana; }

    public String getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(String horarioInicio) { this.horarioInicio = horarioInicio; }

    public String getHorarioFim() { return horarioFim; }
    public void setHorarioFim(String horarioFim) { this.horarioFim = horarioFim; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDataFim() { return dataFim; }
    public void setDataFim(String dataFim) { this.dataFim = dataFim; }

    public int getDiasRestantes() { return diasRestantes; }
    public void setDiasRestantes(int diasRestantes) { this.diasRestantes = diasRestantes; }
}
