package com.example.careplus.dto.dtoConsultaRecorrente;

import com.example.careplus.dto.dtoConsulta.ConsultaResponseDto;
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
    private List<ConsultaResponseDto> consultasCriadas = new ArrayList<>();
    private List<ConflitoDatasDto> datasComConflito = new ArrayList<>();
}