package com.example.careplus.dto.kafka;

/**
 * Subconjunto de PacienteResponseDto esperado pelo consumer.
 * Datas são enviadas como String (formato "yyyy-MM-dd") pois o consumer as recebe como texto.
 */
public class PacienteKafkaDto {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private String dtNascimento;
    private String convenio;
    private String dataInicio;

    public PacienteKafkaDto() {}

    public PacienteKafkaDto(Long id, String nome, String email, String cpf, String telefone,
                             String dtNascimento, String convenio, String dataInicio) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.dtNascimento = dtNascimento;
        this.convenio = convenio;
        this.dataInicio = dataInicio;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getDtNascimento() { return dtNascimento; }
    public void setDtNascimento(String dtNascimento) { this.dtNascimento = dtNascimento; }

    public String getConvenio() { return convenio; }
    public void setConvenio(String convenio) { this.convenio = convenio; }

    public String getDataInicio() { return dataInicio; }
    public void setDataInicio(String dataInicio) { this.dataInicio = dataInicio; }
}

