package com.example.careplus.controller;


import com.example.careplus.dto.dtoTratamento.TratamentoRequestDto;
import com.example.careplus.model.Tratamento;
import com.example.careplus.service.TratamentoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tratamentos")
public class TratamentoController {

    private final TratamentoService tratamentoService;

    public TratamentoController(TratamentoService tratamentoService) {
        this.tratamentoService = tratamentoService;
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Tratamento> cadastrarTratamento(@Valid @RequestBody TratamentoRequestDto tratamento){
        try{
            return ResponseEntity.status(201).body(tratamentoService.cadastrar(tratamento));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/buscar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<Tratamento>> buscarPeloNome(@RequestParam String nomeTratamento){
        try{
            return ResponseEntity.status(200).body(tratamentoService.buscarByNome(nomeTratamento));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletarTratamento(@RequestParam Long id){
        try{
            tratamentoService.deletar(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
