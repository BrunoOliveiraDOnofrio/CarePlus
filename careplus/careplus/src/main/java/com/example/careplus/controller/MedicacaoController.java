package com.example.careplus.controller;

import com.example.careplus.controller.dtoMedicacao.MedicacaoRequestDto;
import com.example.careplus.model.Medicacao;
import com.example.careplus.service.MedicacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicacoes")
public class MedicacaoController {

    private final MedicacaoService service;

    public MedicacaoController(MedicacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Medicacao> adicionar(@RequestBody MedicacaoRequestDto medicacao) {
        try{
            return ResponseEntity.status(201).body(service.adicionar(medicacao));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/ativas")
    public long contarAtivas() {
        return service.contarAtivas();
    }

    @GetMapping("/ordenadas")
    public List<Medicacao> listarOrdenadasPorNome() {
        return service.listarOrdenadasPorNome();
    }

    @GetMapping("/tempo")
    public List<Medicacao> ordenarPorTempo() {
        return service.ordenarPorTempoMedicando();
    }
}
