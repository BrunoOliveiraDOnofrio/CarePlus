package com.example.careplus.dto.dtoConsultaProntuario;

import com.example.careplus.dto.dtoFuncionario.FuncionarioResponseDto;
import com.example.careplus.dto.dtoPaciente.PacienteResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ConsultaProntuarioResponseDto {
    private Long id;
    private PacienteResponseDto paciente;
    private List<FuncionarioResponseDto> funcionarios;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horarioInicio;

    @JsonFormat(pattern = "HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalTime horarioFim;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tipo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String observacoesComportamentais;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean presenca;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean confirmada;

    public ConsultaProntuarioResponseDto() {}

    public ConsultaProntuarioResponseDto(Long id, PacienteResponseDto paciente, List<FuncionarioResponseDto> funcionarios,
                                         LocalDate data, LocalTime horarioInicio, LocalTime horarioFim,
                                         String tipo, String observacoesComportamentais,
                                         Boolean presenca, Boolean confirmada) {
        this.id = id;
        this.paciente = paciente;
        this.funcionarios = funcionarios;
        this.data = data;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.tipo = tipo;
        this.observacoesComportamentais = observacoesComportamentais;
        this.presenca = presenca;
        this.confirmada = confirmada;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PacienteResponseDto getPaciente() { return paciente; }
    public void setPaciente(PacienteResponseDto paciente) { this.paciente = paciente; }

    public List<FuncionarioResponseDto> getFuncionarios() { return funcionarios; }
    public void setFuncionarios(List<FuncionarioResponseDto> funcionarios) { this.funcionarios = funcionarios; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(LocalTime horarioInicio) { this.horarioInicio = horarioInicio; }

    public LocalTime getHorarioFim() { return horarioFim; }
    public void setHorarioFim(LocalTime horarioFim) { this.horarioFim = horarioFim; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getObservacoesComportamentais() { return observacoesComportamentais; }
    public void setObservacoesComportamentais(String obs) { this.observacoesComportamentais = obs; }

    public Boolean getPresenca() { return presenca; }
    public void setPresenca(Boolean presenca) { this.presenca = presenca; }

    public Boolean getConfirmada() { return confirmada; }
    public void setConfirmada(Boolean confirmada) { this.confirmada = confirmada; }
}
