package com.example.careplus.controller;

import com.example.careplus.controller.dtoProntuario.ProntuarioRequestDto;
import com.example.careplus.model.Prontuario;
import com.example.careplus.service.ProntuarioService;
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
    public ResponseEntity<Prontuario> cadastrarProntuario (@RequestBody ProntuarioRequestDto prontuario){
        Prontuario prontuarioCriado = prontuarioService.criarProntuario(prontuario);
        return ResponseEntity.status(201).body(prontuarioCriado);
    }

    @GetMapping ResponseEntity<List<Prontuario>> listarTudoDeTodosProntuarios(){
        List<Prontuario> prontuarios = prontuarioService.listarProntuarios();
        return ResponseEntity.status(200).body(prontuarios);
    }
}
