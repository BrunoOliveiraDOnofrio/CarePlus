package com.example.careplus.dto.dtoConsultaProntuario;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class ProximaConsultaProntuarioResponseDto {
    private Long consultaId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horarioInicio;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horarioFim;
    private String tipo;
    private String nomeProfissional;
    private String tratamento;

    public ProximaConsultaProntuarioResponseDto() {
    }

    public ProximaConsultaProntuarioResponseDto(Long consultaId, LocalDate data, LocalTime horarioInicio, LocalTime horarioFim, String tipo, String nomeProfissional, String tratamento) {
        this.consultaId = consultaId;
        this.data = data;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.tipo = tipo;
        this.nomeProfissional = nomeProfissional;
        this.tratamento = tratamento;
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

    public String getTratamento() {
        return tratamento;
    }

    public void setTratamento(String tratamento) {
        this.tratamento = tratamento;
    }
}

