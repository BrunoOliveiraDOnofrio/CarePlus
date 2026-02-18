package com.example.careplus.dto.dtoConsultaProntuario;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DetalhesConsultaProntuarioAnteriorResponseDto {

    private Long consultaId;
    private LocalDate data;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private String especialidade;
    private String nomeProfissional;
    private String tipo;
    private String tratamentoAtual;
    private List<String> materiaisUtilizados;
    private String observacoesComportamentais;

    public DetalhesConsultaProntuarioAnteriorResponseDto() {
    }

    public Long getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Long consultaId) {
        this.consultaId = consultaId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getNomeProfissional() {
        return nomeProfissional;
    }

    public void setNomeProfissional(String nomeProfissional) {
        this.nomeProfissional = nomeProfissional;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTratamentoAtual() {
        return tratamentoAtual;
    }

    public void setTratamentoAtual(String tratamentoAtual) {
        this.tratamentoAtual = tratamentoAtual;
    }

    public List<String> getMateriaisUtilizados() {
        return materiaisUtilizados;
    }

    public void setMateriaisUtilizados(List<String> materiaisUtilizados) {
        this.materiaisUtilizados = materiaisUtilizados;
    }

    public String getObservacoesComportamentais() {
        return observacoesComportamentais;
    }

    public void setObservacoesComportamentais(String observacoesComportamentais) {
        this.observacoesComportamentais = observacoesComportamentais;
    }
}

