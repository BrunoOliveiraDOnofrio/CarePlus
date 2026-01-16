package com.example.careplus.controller.dtoDetalhes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarObservacoesConsultaDTO {
    private String cid;
    private String medicacao;
    private String atendimentoEspecial;
    private String destratada;
    private String hiperfoco;
}
