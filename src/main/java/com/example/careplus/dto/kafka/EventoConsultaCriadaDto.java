package com.example.careplus.dto.kafka;

import java.util.List;

/**
 * Evento raiz enviado para a mensageria (via RabbitMQ) contendo uma ou mais consultas criadas.
 */
public class EventoConsultaCriadaDto {

    private List<ConsultaCriadaKafkaDto> consultasCriadas;
    private int totalConsultasCriadas;
    private int totalFalhas;
    private List<String> datasComConflito;

    public EventoConsultaCriadaDto() {}

    public EventoConsultaCriadaDto(List<ConsultaCriadaKafkaDto> consultasCriadas) {
        this.consultasCriadas = consultasCriadas;
        this.totalConsultasCriadas = consultasCriadas != null ? consultasCriadas.size() : 0;
        this.totalFalhas = 0;
        this.datasComConflito = List.of();
    }

    public List<ConsultaCriadaKafkaDto> getConsultasCriadas() {
        return consultasCriadas;
    }

    public void setConsultasCriadas(List<ConsultaCriadaKafkaDto> consultasCriadas) {
        this.consultasCriadas = consultasCriadas;
        this.totalConsultasCriadas = consultasCriadas != null ? consultasCriadas.size() : 0;
    }

    public int getTotalConsultasCriadas() {
        return totalConsultasCriadas;
    }

    public void setTotalConsultasCriadas(int totalConsultasCriadas) {
        this.totalConsultasCriadas = totalConsultasCriadas;
    }

    public int getTotalFalhas() {
        return totalFalhas;
    }

    public void setTotalFalhas(int totalFalhas) {
        this.totalFalhas = totalFalhas;
    }

    public List<String> getDatasComConflito() {
        return datasComConflito;
    }

    public void setDatasComConflito(List<String> datasComConflito) {
        this.datasComConflito = datasComConflito;
    }
}
