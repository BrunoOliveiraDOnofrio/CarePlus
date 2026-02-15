package com.example.careplus.dto.dtoDetalhes;

import com.example.careplus.model.Tratamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarTratamentoDTO {
    private Tratamento tratamentoAtual;
}
