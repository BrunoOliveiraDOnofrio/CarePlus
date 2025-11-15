package com.example.careplus.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // essa anotação associa com outro objeto
    @ManyToOne //muitas consultas podem ser de um usuário
    @JoinColumn(name = "funcionario_id") // define o nome no banco
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Paciente paciente;

    @Schema(description = "2025-10-14T01:09:31.734Z")
    private LocalDateTime dataHora;

    @Schema(description = "Retorno")
    private String tipo;

    @Schema(description = "Observacoes Comportamentais sobre o paciente")
    private String observacoesComportamentais;

    @Schema(description = "Sim")
    private Boolean presenca;

    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Material> materiais;

    public Consulta() {
    }

    public Consulta(Long id, Funcionario funcionario, Paciente paciente, LocalDateTime dataHora, String tipo, String observacoesComportamentais, Boolean presenca) {
        this.id = id;
        this.funcionario = funcionario;
        this.paciente = paciente;
        this.dataHora = dataHora;
        this.tipo = tipo;
        this.observacoesComportamentais = observacoesComportamentais;
        this.presenca = presenca;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getObservacoesComportamentais() {
        return observacoesComportamentais;
    }

    public void setObservacoesComportamentais(String observacoesComportamentais) {
        this.observacoesComportamentais = observacoesComportamentais;
    }

    public Boolean getPresenca() {
        return presenca;
    }

    public void setPresenca(Boolean presenca) {
        this.presenca = presenca;
    }

    public List<Material> getMateriais() {
        return materiais;
    }

    public void setMateriais(List<Material> materiais) {
        this.materiais = materiais;
    }

}
