package com.example.careplus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "consulta_funcionario")
public class ConsultaFuncionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @JsonBackReference("consulta-funcionario")
    @ManyToOne
    @JoinColumn(name = "consulta_id", nullable = false)
    private ConsultaProntuario consulta;

    public ConsultaFuncionario() {
    }

    public ConsultaFuncionario(Funcionario funcionario, ConsultaProntuario consulta) {
        this.funcionario = funcionario;
        this.consulta = consulta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public ConsultaProntuario getConsulta() {
        return consulta;
    }

    public void setConsulta(ConsultaProntuario consulta) {
        this.consulta = consulta;
    }
}
