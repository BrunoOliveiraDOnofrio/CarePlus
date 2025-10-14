package com.example.careplus.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Especialista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Schema(description = "Brenda Stefany")
    private String nome;

    @Schema(description = "geraldo_dossantos@vbrasildigital.net")
    private String email;

    @Schema(description = "bRuurpXL9n")
    private String senha;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private Especialista supervisor;

    @OneToMany(mappedBy = "supervisor")
    private List<Especialista> subordinados = new ArrayList<>();

    @Schema(description = "Supervisora")
    private String cargo;

    @Schema(description = "Fonoaudiolgia")
    private String especialidade;

    public Especialista() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    public String getSenha() {
        return senha;
    }


    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Especialista getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Especialista supervisor) {
        this.supervisor = supervisor;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }


}
