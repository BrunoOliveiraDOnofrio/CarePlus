package com.example.careplus.controller;

import com.example.careplus.controller.dtoEspecialista.*;
import com.example.careplus.model.Especialista;
import com.example.careplus.service.EspecialistaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especialistas")
public class EspecialistaController {

    public final EspecialistaService especialistaService;

    public EspecialistaController(EspecialistaService especialistaService) {
        this.especialistaService = especialistaService;
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<EspecialistaResponseDto> salvarEspecialista(@RequestBody @Valid EspecialistaResquestDto especialista){
        EspecialistaResponseDto especialistaSalvo = especialistaService.salvar(especialista);
        return ResponseEntity.status(201).body(especialistaSalvo);
    }

    @PostMapping("/login")
    public ResponseEntity<EspecialistaTokenDto> login(@RequestBody EspecialistaLoginDto especialistaLoginDto){

        final Especialista especialista = EspecialistaMapper.of(especialistaLoginDto);
        EspecialistaTokenDto especialistaTokenDto = this.especialistaService.autenticar(especialista);

        return ResponseEntity.status(200).body(especialistaTokenDto);

    }

    @GetMapping("/por-email")
    public ResponseEntity<List<EspecialistaResponseDto>> buscarPorEmail(@RequestParam String email){
        List<EspecialistaResponseDto> especialistas = especialistaService.buscarPorEmail(email);
        return ResponseEntity.status(200).body(especialistas);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<EspecialistaResponseDto>> listarTodos(){
        List<EspecialistaResponseDto> especialistas = especialistaService.listarTodos();
        return ResponseEntity.status(200).body(especialistas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarEspecialista(@PathVariable Long id){
        try{
            especialistaService.deletar(id);
            return ResponseEntity.status(200).build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarEspecialista(@RequestBody Especialista especialista, @PathVariable Long id){
            especialistaService.atualizar(especialista, id);
            return ResponseEntity.status(200).build();
    }

}
