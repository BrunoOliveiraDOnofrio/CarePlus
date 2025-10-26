package com.example.careplus.controller;

import com.example.careplus.controller.dtoAtividade.*;
import com.example.careplus.service.AtividadeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<AtividadeResponseDto> cadastrar(@RequestBody AtividadeRequestDto dto) {
        return ResponseEntity.status(201).body(atividadeService.cadastrar(dto));
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<AtividadeResponseDto>> listar() {
        return ResponseEntity.ok(atividadeService.listar());
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<AtividadeResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                atividadeService.listar().stream()
                        .filter(a -> a.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Atividade não encontrada"))
        );
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<AtividadeResponseDto> atualizar(@PathVariable Long id, @RequestBody AtividadeRequestDto dto) {
        return ResponseEntity.ok(atividadeService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        atividadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/quantidade")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Long> contarAtividades() {
        return ResponseEntity.ok(atividadeService.contarAtividades());
    }

    @GetMapping("/tempo-maior-que/{tempo}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<AtividadeResponseDto>> listarPorTempoExposicaoMaiorQue(@PathVariable Integer tempo) {
        return ResponseEntity.ok(atividadeService.listarPorTempoExposicaoMaiorQue(tempo));
    }
}
