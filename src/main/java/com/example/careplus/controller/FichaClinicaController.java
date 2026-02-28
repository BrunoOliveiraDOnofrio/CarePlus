package com.example.careplus.controller;

import com.example.careplus.dto.dtoFichaClinica.FichaClinicaRequestDto;
import com.example.careplus.model.FichaClinica;
import com.example.careplus.service.FichaClinicaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fichas-clinicas")
public class FichaClinicaController {

    private final FichaClinicaService fichaClinicaService;

    public FichaClinicaController(FichaClinicaService fichaClinicaService) {
        this.fichaClinicaService = fichaClinicaService;
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<FichaClinica> cadastrarFichaClinica(@RequestBody FichaClinicaRequestDto fichaClinica){
        FichaClinica fichaClinicaCriada = fichaClinicaService.criarFichaClinica(fichaClinica);
        return ResponseEntity.status(201).body(fichaClinicaCriada);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<FichaClinica>> listarTodasFichasClinicas(){
        try {
            List<FichaClinica> fichasClinicas = fichaClinicaService.listarFichasClinicas();
            return ResponseEntity.status(200).body(fichasClinicas);
        } catch (Exception e){
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/id")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<FichaClinica> buscarFichaClinicaPorId(@RequestParam Long id){
        try {
            FichaClinica fichaClinica = fichaClinicaService.buscarFichaClinicaPorId(id);
            return ResponseEntity.status(200).body(fichaClinica);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/nome")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<FichaClinica> buscarFichaClinicaPorNome(@RequestParam String nome){
        try {
            FichaClinica fichaClinica = fichaClinicaService.buscarFichaClinicaPorNome(nome);
            return ResponseEntity.status(200).body(fichaClinica);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/cpf")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<FichaClinica> buscarFichaClinicaPorCpf(@RequestParam String cpf){
        try {
            FichaClinica fichaClinica = fichaClinicaService.buscarFichaClinicaPorCpf(cpf);
            return ResponseEntity.status(200).body(fichaClinica);
        } catch (Exception e){
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<FichaClinica> atualizarFichaClinica(@RequestBody FichaClinicaRequestDto fichaClinica, @RequestParam Long id){
        try {
            FichaClinica fichaClinicaAtualizada = fichaClinicaService.atualizarFichaClinica(fichaClinica, id);
            return ResponseEntity.status(201).body(fichaClinicaAtualizada);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletarPorId(@RequestParam Long id){
        try {
            fichaClinicaService.deletarPorId(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }
}

