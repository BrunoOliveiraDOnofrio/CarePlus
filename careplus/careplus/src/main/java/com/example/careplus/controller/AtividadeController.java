package com.example.careplus.controller;

import com.example.careplus.controller.dtoAtividade.*;
import com.example.careplus.service.AtividadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/atividades")
public class AtividadeController {

    private final AtividadeService atividadeService;

    public AtividadeController(AtividadeService atividadeService) {
        this.atividadeService = atividadeService;
    }

    @PostMapping
    public ResponseEntity<AtividadeResponseDto> cadastrar(@RequestBody AtividadeRequestDto dto) {
        return ResponseEntity.status(201).body(atividadeService.cadastrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<AtividadeResponseDto>> listar() {
        return ResponseEntity.ok(atividadeService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtividadeResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                atividadeService.listar().stream()
                        .filter(a -> a.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Atividade não encontrada"))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtividadeResponseDto> atualizar(@PathVariable Long id, @RequestBody AtividadeRequestDto dto) {
        return ResponseEntity.ok(atividadeService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        atividadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/quantidade")
    public ResponseEntity<Long> contarAtividades() {
        return ResponseEntity.ok(atividadeService.contarAtividades());
    }

    @GetMapping("/tempo-maior-que/{tempo}")
    public ResponseEntity<List<AtividadeResponseDto>> listarPorTempoExposicaoMaiorQue(@PathVariable Integer tempo) {
        return ResponseEntity.ok(atividadeService.listarPorTempoExposicaoMaiorQue(tempo));
    }
}
