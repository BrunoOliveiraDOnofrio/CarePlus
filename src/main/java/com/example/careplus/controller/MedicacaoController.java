package com.example.careplus.controller;

import com.example.careplus.dto.dtoMedicacao.MedicacaoRequestDto;
import com.example.careplus.model.Medicacao;
import com.example.careplus.service.MedicacaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Medicacao> adicionar(@RequestBody MedicacaoRequestDto dto) {
        try {
            Medicacao novaMedicacao = service.adicionar(dto);
            return ResponseEntity.status(201).body(novaMedicacao);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }


    @PutMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Medicacao> atualizar(@RequestParam Long id, @RequestBody MedicacaoRequestDto dto) {

        try {
            Medicacao medicacaoAtualizada = service.atualizar(id, dto);
            return ResponseEntity.status(200).body(medicacaoAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletar(@RequestParam Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.status(204).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<Medicacao>> listarOrdenadasPorNome() {
        List<Medicacao> lista = service.listarOrdenadasPorNome();

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(200).body(lista);
        }
    }

    @GetMapping("/ordenadas-tempo")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<Medicacao>> listarPorTempoMedicando() {
        List<Medicacao> lista = service.ordenarPorTempoMedicando();

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(200).body(lista);
        }
    }

    @GetMapping("/ativas")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Long> contarAtivas() {
        long qtd = service.contarAtivas();

        if (qtd == 0) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(200).body(qtd);
        }
    }
}
