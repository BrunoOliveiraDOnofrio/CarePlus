package com.example.careplus.controller;

import com.example.careplus.dto.dtoMedicacao.MedicacaoMapper;
import com.example.careplus.dto.dtoMedicacao.MedicacaoRequestDto;
import com.example.careplus.dto.dtoMedicacao.MedicacaoResponseDto;
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
    public ResponseEntity<MedicacaoResponseDto> adicionar(@RequestBody MedicacaoRequestDto dto) {
        try {
            return ResponseEntity.status(201).body(MedicacaoMapper.toResponseDto(service.adicionar(dto)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<MedicacaoResponseDto> atualizar(@RequestParam Long id, @RequestBody MedicacaoRequestDto dto) {
        try {
            return ResponseEntity.status(200).body(MedicacaoMapper.toResponseDto(service.atualizar(id, dto)));
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
    public ResponseEntity<List<MedicacaoResponseDto>> listarOrdenadasPorNome() {
        List<MedicacaoResponseDto> lista = MedicacaoMapper.toResponseDto(service.listarOrdenadasPorNome());
        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(lista);
    }

    @GetMapping("/ordenadas-tempo")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<MedicacaoResponseDto>> listarPorTempoMedicando() {
        List<MedicacaoResponseDto> lista = MedicacaoMapper.toResponseDto(service.ordenarPorTempoMedicando());
        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(lista);
    }

    @GetMapping("/ativas")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Long> contarAtivas() {
        long qtd = service.contarAtivas();
        if (qtd == 0) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(qtd);
    }
}
