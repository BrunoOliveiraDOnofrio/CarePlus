package com.example.careplus.dto.dtoConsultaRecorrente;

import com.example.careplus.dto.dtoConsultaProntuario.ConsultaProntuarioResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaRecorrenteResponseDto {
    private int totalConsultasCriadas;
    private int totalFalhas;
    private List<ConsultaProntuarioResponseDto> consultasCriadas = new ArrayList<>();
    private List<ConflitoDatasDto> datasComConflito = new ArrayList<>();
}