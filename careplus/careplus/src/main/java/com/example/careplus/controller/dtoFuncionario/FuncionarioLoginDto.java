package com.example.careplus.controller.dtoFuncionario;

import io.swagger.v3.oas.annotations.media.Schema;

public class FuncionarioLoginDto {
    @Schema(description = "Email do usuário", example = "admin@clinica.com")
    private String email;

    @Schema(description = "Senha do usuário", example = "123456")
    private String senha;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
