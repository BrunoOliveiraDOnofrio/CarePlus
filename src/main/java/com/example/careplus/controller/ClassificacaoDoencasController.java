package com.example.careplus.controller;

import com.example.careplus.dto.dtoCid.ClassificacaoDoencasMapper;
import com.example.careplus.dto.dtoCid.ClassificacaoDoencasRequestDto;
import com.example.careplus.dto.dtoCid.ClassificacaoDoencasResponseDto;
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
    public ResponseEntity<ClassificacaoDoencasResponseDto> cadastrar(@RequestBody ClassificacaoDoencasRequestDto doencaNew) {
        return ResponseEntity.status(201).body(
                ClassificacaoDoencasMapper.toResponseDto(classificacaoDoencasService.cadastrar(doencaNew))
        );
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ClassificacaoDoencasResponseDto>> listar() {
        List<ClassificacaoDoencasResponseDto> doencas = ClassificacaoDoencasMapper.toResponseDto(classificacaoDoencasService.listar());
        if (!doencas.isEmpty()) {
            return ResponseEntity.status(200).body(doencas);
        } else {
            return ResponseEntity.status(204).build();
        }
    }

    @GetMapping("/buscar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ClassificacaoDoencasResponseDto> buscarPorId(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(ClassificacaoDoencasMapper.toResponseDto(classificacaoDoencasService.buscarPorId(id)));
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ClassificacaoDoencasResponseDto> atualizar(@RequestParam Long id,
                                                                     @RequestBody ClassificacaoDoencasRequestDto dadosAtualizados) {
        try {
            return ResponseEntity.ok(ClassificacaoDoencasMapper.toResponseDto(classificacaoDoencasService.atualizar(id, dadosAtualizados)));
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletar(@RequestParam Long id) {
        try {
            classificacaoDoencasService.deletar(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }
}
