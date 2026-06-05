package com.example.careplus.controller;

import com.example.careplus.dto.dtoNotificacao.NotificacaoResponseDto;
import com.example.careplus.dto.dtoNotificacao.RenovarRecorrenciaDto;
import com.example.careplus.service.NotificacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @GetMapping
    public ResponseEntity<List<NotificacaoResponseDto>> listar() {
        return ResponseEntity.ok(notificacaoService.listarNotificacoes());
    }

    @DeleteMapping("/{recorrenciaId}")
    public ResponseEntity<Void> dispensar(@PathVariable String recorrenciaId) {
        notificacaoService.dispensar(recorrenciaId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{recorrenciaId}/renovar")
    public ResponseEntity<Void> renovar(
            @PathVariable String recorrenciaId,
            @RequestBody RenovarRecorrenciaDto dto) {
        notificacaoService.renovar(recorrenciaId, dto);
        return ResponseEntity.ok().build();
    }
}
