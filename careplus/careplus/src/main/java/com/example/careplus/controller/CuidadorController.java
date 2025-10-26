package com.example.careplus.controller;

import com.example.careplus.controller.dtoCuidador.CuidadorRequestDto;
import com.example.careplus.controller.dtoCuidador.CuidadorResponseDto;
import com.example.careplus.service.CuidadorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuidadores")
public class CuidadorController {

    private final CuidadorService cuidadorService;

    public CuidadorController(CuidadorService cuidadorService){
        this.cuidadorService = cuidadorService;
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<CuidadorResponseDto> cadastrar(@RequestBody CuidadorRequestDto cuidador){
        try {
            return ResponseEntity.status(201).body(cuidadorService.cadastrar(cuidador));
        } catch (Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<CuidadorResponseDto>> listar(){
        try{
            return ResponseEntity.status(200).body(cuidadorService.listar());
        } catch (Exception e){
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<CuidadorResponseDto>> listarPacientesPorResponsavel_Id(@PathVariable Long id){
        try {
            return ResponseEntity.status(200).body(cuidadorService.listarPacientesPorResponsavel_Id(id));
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/por-nome-paciente")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<CuidadorResponseDto>> listarResponsaveisPorPaciente_Nome(@RequestParam String nome) {
        try {
            return ResponseEntity.ok(cuidadorService.listarResponsaveisPorPaciente_Nome(nome));
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<CuidadorResponseDto> atualizar(@PathVariable Long id , @RequestParam CuidadorRequestDto cuidadorAtt){
        try {
            return ResponseEntity.status(200).body(cuidadorService.atualizar(id, cuidadorAtt));
        } catch (Exception e){
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        try{
            cuidadorService.deletar(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e){
            return ResponseEntity.status(404).build();
        }
    }

}
