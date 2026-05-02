package com.example.careplus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "consulta_prontuario")
public class ConsultaProntuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL)
    @JsonManagedReference("consulta-funcionario")
    private List<ConsultaFuncionario> consultaFuncionarios = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    @JsonBackReference("paciente-consulta")
    private Paciente paciente;

    @Schema(description = "2026-01-15", example = "2026-01-15")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;

    @Schema(description = "10:00:00", example = "10:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horarioInicio;

    @Schema(description = "11:00:00", example = "11:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horarioFim;

    @Schema(description = "Retorno")
    private String tipo;

    @Schema(description = "Observacoes Comportamentais sobre o paciente")
    private String observacoesComportamentais;

    @Schema(description = "Sim")
    private Boolean presenca;

    @Schema(description = "Sim")
    private Boolean confirmada;

    private String recorrenciaId;

    @OneToMany(mappedBy = "consultaProntuario", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Material> materiais = new ArrayList<>();

    public ConsultaProntuario() {
    }

    /** Helper: monta um LocalDateTime a partir de data + horarioInicio (usado internamente no service) */
    public LocalDateTime getDataHoraInicio() {
        if (data == null || horarioInicio == null) return null;
        return LocalDateTime.of(data, horarioInicio);
    }

    /** Helper: retorna o primeiro Funcionario associado */
    public Funcionario getFuncionario() {
        if (consultaFuncionarios == null || consultaFuncionarios.isEmpty()) return null;
        return consultaFuncionarios.get(0).getFuncionario();
    }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public List<ConsultaFuncionario> getConsultaFuncionarios() { return consultaFuncionarios; }
    public void setConsultaFuncionarios(List<ConsultaFuncionario> consultaFuncionarios) { this.consultaFuncionarios = consultaFuncionarios; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(LocalTime horarioInicio) { this.horarioInicio = horarioInicio; }

    public LocalTime getHorarioFim() { return horarioFim; }
    public void setHorarioFim(LocalTime horarioFim) { this.horarioFim = horarioFim; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getObservacoesComportamentais() { return observacoesComportamentais; }
    public void setObservacoesComportamentais(String observacoesComportamentais) { this.observacoesComportamentais = observacoesComportamentais; }

    public Boolean getPresenca() { return presenca; }
    public void setPresenca(Boolean presenca) { this.presenca = presenca; }

    public Boolean getConfirmada() { return confirmada; }
    public void setConfirmada(Boolean confirmada) { this.confirmada = confirmada; }

    public String getRecorrenciaId() { return recorrenciaId; }
    public void setRecorrenciaId(String recorrenciaId) { this.recorrenciaId = recorrenciaId; }

    public List<Material> getMateriais() { return materiais; }
    public void setMateriais(List<Material> materiais) { this.materiais = materiais; }
}
