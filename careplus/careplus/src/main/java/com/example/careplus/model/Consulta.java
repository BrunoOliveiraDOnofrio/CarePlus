package com.example.careplus.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // essa anotação associa com outro objeto
    @ManyToOne //muitas consultas podem ser de um usuário
    @JoinColumn(name = "especialista_id") // define o nome no banco
    private Especialista especialista;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Paciente paciente;

    private LocalDateTime dataHora;
    private String status;

    public Paciente getUsuario() {
        return paciente;
    }

    public void setUsuario(Paciente paciente) {
        this.paciente = paciente;
    }

    public Especialista getEspecialista() {
        return especialista;
    }

    public void setEspecialista(Especialista especialista) {
        this.especialista = especialista;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
