package com.example.careplus.controller;


import com.example.careplus.controller.dtoTratamento.TratamentoRequestDto;
import com.example.careplus.model.Tratamento;
import com.example.careplus.repository.TratamentoRepository;
import com.example.careplus.service.TratamentoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

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

    @GetMapping("/{nomeTratamento}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<Tratamento>> buscarPeloNome(@PathVariable String nomeTratamento){
        try{
            return ResponseEntity.status(200).body(tratamentoService.buscarByNome(nomeTratamento));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*

    @GetMapping("/{idProntuario}")
    public ResponseEntity<Long> buscarPeloIdProntuario(@PathVariable Long idProntuario){
        try{
            return ResponseEntity.status(200).body(tratamentoService.buscarPeloIdProntuario(idProntuario));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/
}
