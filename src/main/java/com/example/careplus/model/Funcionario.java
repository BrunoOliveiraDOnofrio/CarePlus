package com.example.careplus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "funcionario")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Brenda Stefany")
    private String nome;

    @Schema(description = "geraldo_dossantos@vbrasildigital.net")
    private String email;

    @Schema(description = "bRuurpXL9n")
    private String senha;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    @JsonIgnore
    private Funcionario supervisor;

    @OneToMany(mappedBy = "supervisor")
    @JsonIgnore
    private List<Funcionario> subordinados = new ArrayList<>();

    @OneToMany(mappedBy = "funcionario")
    @JsonIgnore
    private List<ConsultaFuncionario> consultaFuncionarios = new ArrayList<>();

    @Schema(description = "Supervisora")
    private String cargo;

    @Schema(description = "Fonoaudiolgia")
    private String especialidade;

    @Schema(description = "(11)94002-8922")
    private String telefone;

    @Schema(description = "134122241")
    private String documento;

    @Schema(description = "ABA, Fono, TO, etc")
    @Column(name = "tipo_atendimento")
    private String tipoAtendimento;

    @Column(name = "foto")
    private String foto;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "funcionario_roles",
            joinColumns = @JoinColumn(name = "funcionario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public Funcionario() {
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

    public String getSenha() {
        return senha;
    }


    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Funcionario getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Funcionario supervisor) {
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

    public String getTipoAtendimento() {
        return tipoAtendimento;
    }

    public void setTipoAtendimento(String tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
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
