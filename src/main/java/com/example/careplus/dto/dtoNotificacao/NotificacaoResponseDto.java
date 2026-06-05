package com.example.careplus.dto.dtoNotificacao;

import java.util.List;

public class NotificacaoResponseDto {

    private Long pacienteId;
    private String pacienteNome;
    private List<RecorrenciaNotificacaoDto> recorrencias;

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public String getPacienteNome() { return pacienteNome; }
    public void setPacienteNome(String pacienteNome) { this.pacienteNome = pacienteNome; }

    public List<RecorrenciaNotificacaoDto> getRecorrencias() { return recorrencias; }
    public void setRecorrencias(List<RecorrenciaNotificacaoDto> recorrencias) { this.recorrencias = recorrencias; }
}
