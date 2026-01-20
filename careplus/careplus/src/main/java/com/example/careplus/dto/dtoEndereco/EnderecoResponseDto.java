package com.example.careplus.dto.dtoEndereco;

import io.swagger.v3.oas.annotations.media.Schema;

public class EnderecoResponseDto {

    private Integer id;

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

    public EnderecoResponseDto() {
    }

    public EnderecoResponseDto(Integer id, String cep, String logradouro, String numero, String complemento, String bairro, String cidade, String estado) {
        this.id = id;
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}

