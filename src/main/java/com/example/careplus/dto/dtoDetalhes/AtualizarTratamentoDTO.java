package com.example.careplus.dto.dtoDetalhes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarTratamentoDTO {
    private String tipoDeTratamento;
    private Boolean finalizado;
    private Long idFichaClinica;
}

