package com.example.careplus.controller;

import com.example.careplus.dto.dtoCid.ClassificacaoDoencasRequestDto;
import com.example.careplus.model.ClassificacaoDoencas;
import com.example.careplus.service.ClassificacaoDoencasService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classificacao-doencas")
public class ClassificacaoDoencasController {

    private final ClassificacaoDoencasService classificacaoDoencasService;

    public ClassificacaoDoencasController(ClassificacaoDoencasService classificacaoDoencasService){
        this.classificacaoDoencasService = classificacaoDoencasService;
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ClassificacaoDoencas> cadastrar(@RequestBody ClassificacaoDoencasRequestDto doencaNew) {
        ClassificacaoDoencas criada = classificacaoDoencasService.cadastrar(doencaNew);
        return ResponseEntity.status(201).body(criada);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ClassificacaoDoencas>> listar() {
        List<ClassificacaoDoencas> doencas = classificacaoDoencasService.listar();
        if (!doencas.isEmpty()){

            return ResponseEntity.status(200).body(classificacaoDoencasService.listar());
        } else {
            return ResponseEntity.status(204).build();
        }

    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ClassificacaoDoencas> buscarPorId(@PathVariable Long id) {
        ClassificacaoDoencas achado = classificacaoDoencasService.buscarPorId(id);
        if (achado != null) {
            return ResponseEntity.ok(achado);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ClassificacaoDoencas> atualizar(@PathVariable Long id,
                                                          @RequestBody ClassificacaoDoencasRequestDto dadosAtualizados) {
        ClassificacaoDoencas atualizada = classificacaoDoencasService.atualizar(id, dadosAtualizados);
        if (atualizada != null) {
            return ResponseEntity.ok(atualizada);
        } else {
            return ResponseEntity.status(404).build();
        }
    }


    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            classificacaoDoencasService.deletar(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

}