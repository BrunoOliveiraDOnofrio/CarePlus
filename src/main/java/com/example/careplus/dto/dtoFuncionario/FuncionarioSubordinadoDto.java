package com.example.careplus.dto.dtoFuncionario;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

public class FuncionarioSubordinadoDto {
    private Long id;

    @Schema(description = "Iago Benedito Barbosa")
    private String nome;

    @Schema(description = "iago_benedito_barbosa@navescorat.com.br")
    private String email;

    @Schema(description = "Estagiaria")
    private String cargo;

    @Schema(description = "Fonoaudiologa")
    private String especialidade;

    public FuncionarioSubordinadoDto(Long id, String nome, String email, String cargo, String especialidade) {
        this.id = id;
        this.nome = nome;
        this.email = email;
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
