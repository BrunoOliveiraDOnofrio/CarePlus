package com.example.careplus.dto.dtoFuncionario;


import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

public class FuncionarioResquestDto {

    @Schema(description = "Iago Benedito Barbosa")
    private String nome;

    @Schema(description = "iago_benedito_barbosa@navescorat.com.br")
    private String email;

    @Schema(description = "olgyT0E7nH")
    private String senha;

    private SupervisorDto supervisor;

    @Schema(description = "Estagiaria")
    private String cargo;

    @Schema(description = "Fonoaudiologa")
    private String especialidade;

    @Schema(description = "(11)94002-8922")
    private String telefone;

    @Schema(description = "134122241")
    private String documento;

    @Schema(description = "ABA, Fono, TO, etc")
    private String tipoAtendimento;

    private MultipartFile foto;

    public MultipartFile getFoto() {
        return foto;
    }

    public void setFoto(MultipartFile foto) {
        this.foto = foto;
    }

    public FuncionarioResquestDto(String nome, String email, String senha, SupervisorDto supervisor, String cargo, String especialidade, String telefone, String documento, String tipoAtendimento) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.supervisor = supervisor;
        this.cargo = cargo;
        this.especialidade = especialidade;
        this.telefone = telefone;
        this.documento = documento;
        this.tipoAtendimento = tipoAtendimento;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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

    public String getTipoAtendimento() {
        return tipoAtendimento;
    }

    public void setTipoAtendimento(String tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
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
}
