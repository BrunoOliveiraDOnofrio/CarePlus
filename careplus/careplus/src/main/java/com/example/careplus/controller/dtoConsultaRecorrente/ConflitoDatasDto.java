package com.example.careplus.controller.dtoConsultaRecorrente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConflitoDatasDto {
    private LocalDate data;
    private String horario;
    private String mensagemErro;

}