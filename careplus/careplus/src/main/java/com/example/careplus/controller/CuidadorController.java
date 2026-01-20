package com.example.careplus.controller;

import com.example.careplus.dto.dtoCuidador.CuidadorContatoResponseDto;
import com.example.careplus.dto.dtoCuidador.CuidadorRequestDto;
import com.example.careplus.dto.dtoCuidador.CuidadorResponseDto;
import com.example.careplus.model.Cuidador;
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
    public ResponseEntity<Cuidador> cadastrar(@RequestBody CuidadorRequestDto cuidador){
        try {
            return ResponseEntity.status(201).body(cuidadorService.cadastrar(cuidador));
        } catch (Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<Cuidador>> listar(){
        if (cuidadorService.listar().isEmpty()){
            return ResponseEntity.status(204).build();
        }
            return ResponseEntity.status(200).body(cuidadorService.listar());
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
    public ResponseEntity<Cuidador> atualizar(@PathVariable Long id , @RequestBody CuidadorRequestDto cuidadorAtt){
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

    @GetMapping("contato/{idPaciente}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<CuidadorContatoResponseDto>> contatosPorPaciente(@PathVariable Long idPaciente){
        try {
            List<CuidadorContatoResponseDto> cuidadorContatoResponseDto = cuidadorService.buscarContato(idPaciente);
            return ResponseEntity.status(200).body(cuidadorContatoResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

}
