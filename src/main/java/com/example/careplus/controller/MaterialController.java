package com.example.careplus.controller;

import com.example.careplus.dto.dtoMaterial.MaterialRequestDto;
import com.example.careplus.dto.dtoMaterial.MaterialResponseDto;
import com.example.careplus.service.MaterialService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/materiais")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<MaterialResponseDto> cadastrar(@RequestBody MaterialRequestDto dto) {
        return ResponseEntity.status(201).body(materialService.cadastrar(dto));
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<MaterialResponseDto>> listar() {
        return ResponseEntity.ok(materialService.listar());
    }

    @GetMapping("/buscar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<MaterialResponseDto> buscarPorId(@RequestParam Long id) {
        return ResponseEntity.ok(
                materialService.listar().stream()
                        .filter(a -> a.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Atividade não encontrada"))
        );
    }

    @PutMapping("/atualizar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<MaterialResponseDto> atualizar(@RequestParam Long id, @RequestBody MaterialRequestDto dto) {
        return ResponseEntity.ok(materialService.atualizar(id, dto));
    }

    @DeleteMapping("/deletar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletar(@RequestParam Long id) {
        materialService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/quantidade")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Long> contarAtividades() {
        return ResponseEntity.ok(materialService.contarAtividades());
    }

}
