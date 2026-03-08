package com.example.careplus.dto.dtoPaciente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class TudoPacienteDto {

    private String parentesco;

    @Schema(description = "01310-100", example = "01310-100")
    private String cep;

    @Schema(description = "Avenida Paulista", example = "Avenida Paulista")
    private String logradouro;

    @Schema(description = "1578", example = "1578")
    private String numero;

    @Schema(description = "Apartamento 42", example = "Apartamento 42")
    private String complemento;

    @Schema(description = "Bela Vista", example = "Bela Vista")
    private String bairro;

    @Schema(description = "São Paulo", example = "São Paulo")
    private String cidade;

    @Schema(description = "SP", example = "SP")
    private String estado;

    @Schema(description = "Ana Josefa")
    @NotBlank
    private String nomeResponsavel;

    @Schema(description = "vitor_ribeiro@performa.com.br")
//    @NotBlank @Email
    private String emailResponsavel;

    @Schema(description = "(11) 98559-3381")
//    @NotBlank @Size(min = 11, max = 11) @Pattern(regexp = "\\d+")
    private String telefoneResponsavel;
    @Schema(description = "2025-10-14")
    @NotNull
    private LocalDate dtNascimentoResponsavel;
    @Schema(description = "614.997.268-21")
//    @NotBlank @Size(min = 11, max = 11) @Pattern(regexp = "\\d+")
    private String cpfResponsavel;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Schema(description = "Vitor Miguel Raimundo Ribeiro")
    private String nomePaciente;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Schema(description = "vitor_ribeiro@performa.com.br")
    private String emailPaciente;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$", message = "CPF deve ser válido (formato: 000.000.000-00)")
    @Schema(description = "191.644.388-56")
    private String cpfPaciente;

    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$", message = "Telefone deve ser válido")
    @Schema(description = "(11) 99182-8249")
    private String telefonePaciente;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Schema(description = "2025-10-14")
    private LocalDate dtNascimentoPaciente;

    @Schema(description = "Sul America")
    private String convenioPaciente;

    private MultipartFile fotoPaciente;

    public TudoPacienteDto() {
    }

    public TudoPacienteDto(String parentesco, String cep, String logradouro, String numero, String complemento, String bairro, String cidade, String estado, String nomeResponsavel, String emailResponsavel, String telefoneResponsavel, LocalDate dtNascimentoResponsavel, String cpfResponsavel, String nomePaciente, String emailPaciente, String cpfPaciente, String telefonePaciente, LocalDate dtNascimentoPaciente, String convenioPaciente, MultipartFile fotoPaciente) {
        this.parentesco = parentesco;
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.nomeResponsavel = nomeResponsavel;
        this.emailResponsavel = emailResponsavel;
        this.telefoneResponsavel = telefoneResponsavel;
        this.dtNascimentoResponsavel = dtNascimentoResponsavel;
        this.cpfResponsavel = cpfResponsavel;
        this.nomePaciente = nomePaciente;
        this.emailPaciente = emailPaciente;
        this.cpfPaciente = cpfPaciente;
        this.telefonePaciente = telefonePaciente;
        this.dtNascimentoPaciente = dtNascimentoPaciente;
        this.convenioPaciente = convenioPaciente;
        this.fotoPaciente = fotoPaciente;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getEmailResponsavel() {
        return emailResponsavel;
    }

    public void setEmailResponsavel(String emailResponsavel) {
        this.emailResponsavel = emailResponsavel;
    }

    public String getTelefoneResponsavel() {
        return telefoneResponsavel;
    }

    public void setTelefoneResponsavel(String telefoneResponsavel) {
        this.telefoneResponsavel = telefoneResponsavel;
    }

    public LocalDate getDtNascimentoResponsavel() {
        return dtNascimentoResponsavel;
    }

    public void setDtNascimentoResponsavel(LocalDate dtNascimentoResponsavel) {
        this.dtNascimentoResponsavel = dtNascimentoResponsavel;
    }

    public String getCpfResponsavel() {
        return cpfResponsavel;
    }

    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getEmailPaciente() {
        return emailPaciente;
    }

    public void setEmailPaciente(String emailPaciente) {
        this.emailPaciente = emailPaciente;
    }

    public String getCpfPaciente() {
        return cpfPaciente;
    }

    public void setCpfPaciente(String cpfPaciente) {
        this.cpfPaciente = cpfPaciente;
    }

    public String getTelefonePaciente() {
        return telefonePaciente;
    }

    public void setTelefonePaciente(String telefonePaciente) {
        this.telefonePaciente = telefonePaciente;
    }

    public LocalDate getDtNascimentoPaciente() {
        return dtNascimentoPaciente;
    }

    public void setDtNascimentoPaciente(LocalDate dtNascimentoPaciente) {
        this.dtNascimentoPaciente = dtNascimentoPaciente;
    }

    public String getConvenioPaciente() {
        return convenioPaciente;
    }

    public void setConvenioPaciente(String convenioPaciente) {
        this.convenioPaciente = convenioPaciente;
    }

    public MultipartFile getFotoPaciente() {
        return fotoPaciente;
    }

    public void setFotoPaciente(MultipartFile fotoPaciente) {
        this.fotoPaciente = fotoPaciente;
    }
}
