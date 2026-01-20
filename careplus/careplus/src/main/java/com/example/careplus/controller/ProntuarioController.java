package com.example.careplus.controller;

import com.example.careplus.dto.dtoProntuario.ProntuarioRequestDto;
import com.example.careplus.model.Prontuario;
import com.example.careplus.service.ProntuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prontuarios")
public class ProntuarioController {

    private final ProntuarioService prontuarioService;

    public ProntuarioController(ProntuarioService prontuarioService) {
        this.prontuarioService = prontuarioService;
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Prontuario> cadastrarProntuario (@RequestBody ProntuarioRequestDto prontuario){
        Prontuario prontuarioCriado = prontuarioService.criarProntuario(prontuario);
        return ResponseEntity.status(201).body(prontuarioCriado);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<Prontuario>> listarTudoDeTodosProntuarios(){
        try {
            List<Prontuario> prontuarios = prontuarioService.listarProntuarios();
            return ResponseEntity.status(200).body(prontuarios);
        } catch (Exception e){
            return ResponseEntity.status(404).build();
        }

    }

    @GetMapping("/id/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Prontuario> buscarProntuarioPorId (@PathVariable Long id){
        try {
            Prontuario prontuario = prontuarioService.buscarProntuarioPorId(id);
            return ResponseEntity.status(200).body(prontuario);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/nome/{nome}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Prontuario> buscarProntuarioPorNome (@PathVariable String nome){
        try {
            Prontuario prontuario = prontuarioService.buscarProntuarioPorNome(nome);
            return ResponseEntity.status(200).body(prontuario);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/cpf/{cpf}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Prontuario> buscarProntuarioPorCpf (@PathVariable String cpf){
        try {
            Prontuario prontuario = prontuarioService.buscarProntuarioPorCpf(cpf);
            return ResponseEntity.status(200).body(prontuario);
        } catch (Exception e){
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Prontuario> atualizarProntuario(@RequestBody ProntuarioRequestDto prontuario, @PathVariable Long id){
        try {
            Prontuario prontuarioAtualizado = prontuarioService.atualizarProntuario(prontuario, id);
            return ResponseEntity.status(201).body(prontuarioAtualizado);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id){
        try {
            prontuarioService.deletarPorId(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }


}
