package com.example.careplus.controller.dtoEspecialista;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

public class EspecialistaResponseDto {
    private Long id;

    @Schema(description = "Iago Benedito Barbosa")
    private String nome;

    @Schema(description = "iago_benedito_barbosa@navescorat.com.br")
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL) // Anotação usada para não incluir o atributo abaixo no JSON caso o
    // campo seja nulo
    private SupervisorDto supervisor;

    @Schema(description = "Estagiaria")
    private String cargo;

    @Schema(description = "Fonoaudiologa")
    private String especialidade;

    public EspecialistaResponseDto(Long id, String nome, String email, SupervisorDto supervisor, String cargo, String especialidade) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.supervisor = supervisor;
        this.cargo = cargo;
        this.especialidade = especialidade;
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

    public SupervisorDto getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SupervisorDto supervisor) {
        this.supervisor = supervisor;
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
