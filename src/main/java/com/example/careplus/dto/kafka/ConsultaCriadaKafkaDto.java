package com.example.careplus.dto.kafka;

/**
 * Representa uma consulta dentro do evento enviado via mensageria.
 */
public class ConsultaCriadaKafkaDto {

    private Long id;
    private PacienteKafkaDto paciente;
    private ProfissionalKafkaDto funcionario;
    private String dataHora;
    private String tipo;

    public ConsultaCriadaKafkaDto() {}

    public ConsultaCriadaKafkaDto(Long id, PacienteKafkaDto paciente, ProfissionalKafkaDto funcionario,
                                  String dataHora, String tipo) {
        this.id = id;
        this.paciente = paciente;
        this.funcionario = funcionario;
        this.dataHora = dataHora;
        this.tipo = tipo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PacienteKafkaDto getPaciente() { return paciente; }
    public void setPaciente(PacienteKafkaDto paciente) { this.paciente = paciente; }

    public ProfissionalKafkaDto getFuncionario() { return funcionario; }
    public void setFuncionario(ProfissionalKafkaDto funcionario) { this.funcionario = funcionario; }

    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
