package com.example.careplus.dto.dtoDetalhes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarFichaClinicaDTO {
    private String anamnese;
    private String diagnostico;
    private String planoTerapeutico;
}
