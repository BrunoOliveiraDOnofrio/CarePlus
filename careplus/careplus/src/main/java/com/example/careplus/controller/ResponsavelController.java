package com.example.careplus.controller;

import com.example.careplus.controller.dtoResponsavel.ResponsavelRequestDto;
import com.example.careplus.controller.dtoResponsavel.ResponsavelResponseDto;
import com.example.careplus.service.ResponsavelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/responsaveis")
public class ResponsavelController {

    private final ResponsavelService responsavelService;

    public ResponsavelController(ResponsavelService responsavelService){
        this.responsavelService = responsavelService;
    }

    @PostMapping
    public ResponseEntity<ResponsavelResponseDto> cadastrar(@RequestBody ResponsavelRequestDto responsavel){
        return ResponseEntity.status(201).body(responsavelService.cadastrar(responsavel));
    }

    @GetMapping
    public ResponseEntity<List<ResponsavelResponseDto>> listar(){
        return ResponseEntity.status(200).body(responsavelService.listar());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsavelResponseDto> atualizar(@PathVariable Long id, @RequestBody ResponsavelRequestDto responsavelAtt){
        return ResponseEntity.status(200).body(responsavelService.atualizar(id, responsavelAtt));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        responsavelService.deletar(id);
        return ResponseEntity.status(200).build();
    }
}
