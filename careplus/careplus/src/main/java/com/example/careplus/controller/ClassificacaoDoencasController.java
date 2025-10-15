package com.example.careplus.controller;

import com.example.careplus.model.ClassificacaoDoencas;
import com.example.careplus.service.ClassificacaoDoencasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/classificacao-doencas")
public class ClassificacaoDoencasController {

    private final ClassificacaoDoencasService classificacaoDoencasService;

    public ClassificacaoDoencasController(ClassificacaoDoencasService classificacaoDoencasService){
        this.classificacaoDoencasService = classificacaoDoencasService;
    }

    @PostMapping
    public ResponseEntity<ClassificacaoDoencas> cadastrar(@RequestBody ClassificacaoDoencas doencaNew) {
        ClassificacaoDoencas criada = classificacaoDoencasService.cadastrar(doencaNew);
        return ResponseEntity.status(201).body(criada);
    }

    @GetMapping
    public ResponseEntity<List<ClassificacaoDoencas>> listar() {
        try {
            return ResponseEntity.status(200).body(classificacaoDoencasService.listar());
        } catch (Exception e){
            return ResponseEntity.status(204).build();
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ClassificacaoDoencas>> buscarPorId(@PathVariable Long id) {
        try {
            Optional<ClassificacaoDoencas> achado = classificacaoDoencasService.buscarPorId(id);
            return ResponseEntity.status(200).body(achado);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<ClassificacaoDoencas>> atualizar(@PathVariable Long id,
                                                          @RequestBody ClassificacaoDoencas dadosAtualizados) {
        try {
            Optional<ClassificacaoDoencas> atualizada = classificacaoDoencasService.atualizar(id, dadosAtualizados);
            return ResponseEntity.status(200).body(atualizada);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            classificacaoDoencasService.deletar(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

}