package com.example.careplus.controller;

import com.example.careplus.controller.dtoResponsavel.ResponsavelMapper;
import com.example.careplus.controller.dtoResponsavel.ResponsavelRequestDto;
import com.example.careplus.controller.dtoResponsavel.ResponsavelResponseDto;
import com.example.careplus.model.Responsavel;
import com.example.careplus.service.ResponsavelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/responsaveis")
public class ResponsavelController {

    private final ResponsavelService responsavelService;

    public ResponsavelController(ResponsavelService responsavelService){
        this.responsavelService = responsavelService;
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Responsavel> cadastrar(@Valid @RequestBody ResponsavelRequestDto responsavel){
        try {
            return ResponseEntity.status(201).body(responsavelService.cadastrar(responsavel));
        } catch (Exception e){
            return ResponseEntity.status(409).build();
        }
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<Responsavel>> listar(){
        try {
            return ResponseEntity.status(200).body(responsavelService.listar());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        }

    }

    @GetMapping("/por-email")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Responsavel> buscarPorEmail(@RequestParam String email){
        try {
            return ResponseEntity.status(200).body(responsavelService.buscarPorEmail(email));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Responsavel> atualizar(@PathVariable Long id, @Valid @RequestBody ResponsavelRequestDto responsavelAtt){
        try {
            return ResponseEntity.status(200).body(responsavelService.atualizar(id, responsavelAtt));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).build();
        }
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        try {
            responsavelService.deletar(id);
            return ResponseEntity.status(204).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        }
    }
}
