package com.example.careplus.controller;

import com.example.careplus.dto.dtoResponsavel.ResponsavelRequestDto;
import com.example.careplus.dto.dtoResponsavel.ResponsavelResponseNotificacaoDto;
import com.example.careplus.model.Responsavel;
import com.example.careplus.service.ResponsavelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<Responsavel>> listar(
            @RequestParam(defaultValue = "0") Integer pagina){
        Pageable pageable = PageRequest.of(pagina, 8);
        Page<Responsavel> responsaveis = responsavelService.listarPaginado(pageable);
        return ResponseEntity.status(200).body(responsaveis);
    }

    @GetMapping("/por-paciente")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponsavelResponseNotificacaoDto> listarPorPaciente(
            @RequestParam Long idPaciente
    ){
        try {
            ResponsavelResponseNotificacaoDto responsavelResponseNotificacaoDto = responsavelService.buscarPorPaciente(idPaciente);
            return ResponseEntity.status(200).body(responsavelResponseNotificacaoDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/inativos")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Page<Responsavel>> listarInativos(
            @RequestParam(defaultValue = "0") Integer pagina){
        Pageable pageable = PageRequest.of(pagina, 8);
        return ResponseEntity.ok(responsavelService.listarInativosPaginado(pageable));
    }

    @PatchMapping("/reativar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> reativar(@RequestParam Long id){
        try {
            responsavelService.reativar(id);
            return ResponseEntity.status(204).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/buscar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<Responsavel>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String cpf) {
        return ResponseEntity.ok(responsavelService.buscar(nome, email, cpf));
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

    @GetMapping("/por-cpf")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Responsavel> buscarPorCpf(@RequestParam String cpf){
        try {
            return ResponseEntity.status(200).body(responsavelService.buscarPorCpf(cpf));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Responsavel> buscarPorId(@PathVariable Long id){
        try {
            return ResponseEntity.status(200).body(responsavelService.buscarPorId(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Responsavel> atualizar(@RequestParam Long id, @Valid @RequestBody ResponsavelRequestDto responsavelAtt){
        try {
            return ResponseEntity.status(200).body(responsavelService.atualizar(id, responsavelAtt));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).build();
        }
    }

    @DeleteMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletar(@RequestParam Long id){
        try {
            responsavelService.deletar(id);
            return ResponseEntity.status(204).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        }
    }
}
