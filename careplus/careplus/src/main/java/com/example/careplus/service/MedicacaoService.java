package com.example.careplus.service;

import com.example.careplus.model.Medicacao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedicacaoService {

    private final List<Medicacao> medicacoes = new ArrayList<>();

    public void adicionar(Medicacao medicacao) {
        medicacoes.add(medicacao);
    }


    public long contarAtivas() {
        return medicacoes.stream().filter(Medicacao::isAtivo).count();
    }


    public List<Medicacao> listarOrdenadasPorNome() {
        List<Medicacao> copia = new ArrayList<>(medicacoes);
        Collections.sort(copia);
        return copia;
    }


    public List<Medicacao> ordenarPorTempoMedicando() {
        List<Medicacao> lista = new ArrayList<>(medicacoes);
        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                long tempoAtual = lista.get(j).getTempoMedicando().toDays();
                long tempoProximo = lista.get(j + 1).getTempoMedicando().toDays();
                if (tempoAtual > tempoProximo) {
                    Collections.swap(lista, j, j + 1);
                }
            }
        }
        return lista;
    }

    public List<Medicacao> getMedicacoes() {
        return medicacoes;
    }
}
