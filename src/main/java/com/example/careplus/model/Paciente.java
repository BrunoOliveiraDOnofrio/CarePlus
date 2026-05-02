package com.example.careplus.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "paciente")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Diogo Francisco dos Santos")
    private String nome;

    @Schema(description = "iago_benedito_barbosa@navescorat.com.br")
    private String email;

    @Schema(description = "484.356.058-84")
    private String cpf;

    @Schema(description = "(11) 98215-0272")
    private String telefone;

    @Schema(description = "2025-10-14")
    private LocalDate dtNascimento;

    @Schema(description = "Sul America")
    private String convenio;

    @Schema(description = "2025-11-25")
    private LocalDate dataInicio;

    private String foto;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "paciente")
    @JsonManagedReference("paciente-consulta")
    private List<ConsultaProntuario> consultas = new ArrayList<>();

    public Paciente() {
    }

    public List<ConsultaProntuario> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<ConsultaProntuario> consultas) {
        this.consultas = consultas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(LocalDate dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
