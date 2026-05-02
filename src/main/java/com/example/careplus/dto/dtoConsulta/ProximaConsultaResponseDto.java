package com.example.careplus.dto.dtoConsulta;

import java.time.LocalDate;
import java.time.LocalTime;

public class ProximaConsultaResponseDto {
    private Long consultaId;
    private LocalDate data;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private String tipo;
    private String nomeProfissional;

    public ProximaConsultaResponseDto() {
    }

    public ProximaConsultaResponseDto(Long consultaId, LocalDate data, LocalTime horarioInicio, LocalTime horarioFim, String tipo, String nomeProfissional) {
        this.consultaId = consultaId;
        this.data = data;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.tipo = tipo;
        this.nomeProfissional = nomeProfissional;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNomeProfissional() {
        return nomeProfissional;
    }

    public void setNomeProfissional(String nomeProfissional) {
        this.nomeProfissional = nomeProfissional;
    }

}

