package com.example.careplus.dto.dtoConsultaRecorrente;

import com.example.careplus.dto.dtoConsultaProntuario.ConsultaProntuarioResponseDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Resposta do endpoint POST /recorrentes com o novo formato.
 * Agrupa o resultado total de todas as consultas agendadas no batch.
 */
public class AgendarConsultasResponseDto {

    private int totalConsultasCriadas;
    private int totalFalhas;
    private List<ConsultaProntuarioResponseDto> consultasCriadas = new ArrayList<>();
    private List<ConflitoDatasDto> datasComConflito = new ArrayList<>();

    public AgendarConsultasResponseDto() {}

    public int getTotalConsultasCriadas() { return totalConsultasCriadas; }
    public void setTotalConsultasCriadas(int totalConsultasCriadas) { this.totalConsultasCriadas = totalConsultasCriadas; }

    public int getTotalFalhas() { return totalFalhas; }
    public void setTotalFalhas(int totalFalhas) { this.totalFalhas = totalFalhas; }

    public List<ConsultaProntuarioResponseDto> getConsultasCriadas() { return consultasCriadas; }
    public void setConsultasCriadas(List<ConsultaProntuarioResponseDto> consultasCriadas) { this.consultasCriadas = consultasCriadas; }

    public List<ConflitoDatasDto> getDatasComConflito() { return datasComConflito; }
    public void setDatasComConflito(List<ConflitoDatasDto> datasComConflito) { this.datasComConflito = datasComConflito; }
}

