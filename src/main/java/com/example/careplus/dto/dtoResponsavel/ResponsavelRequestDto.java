package com.example.careplus.dto.dtoResponsavel;
import com.example.careplus.dto.dtoEndereco.EnderecoRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class ResponsavelRequestDto {
    @Schema(description = "Ana Josefa")
    @NotBlank
    private String nome;
    @Schema(description = "vitor_ribeiro@performa.com.br")
//    @NotBlank @Email
    private String email;
    @Schema(description = "(11) 98559-3381")
//    @NotBlank @Size(min = 11, max = 11) @Pattern(regexp = "\\d+")
    private String telefone;
    @Schema(description = "2025-10-14")
    @NotNull
    private LocalDate dtNascimento;
    @Schema(description = "614.997.268-21")
//    @NotBlank @Size(min = 11, max = 11) @Pattern(regexp = "\\d+")
    private String cpf;
    @Schema(description = "Endereco do responsavel")
    private EnderecoRequestDto endereco;
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
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public EnderecoRequestDto getEndereco() {
        return endereco;
    }
    public void setEndereco(EnderecoRequestDto endereco) {
        this.endereco = endereco;
    }
}