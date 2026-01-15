package com.example.careplus.controller;

import com.example.careplus.controller.dtoPaciente.DetalhePacienteDTO;
import com.example.careplus.service.DetalhePacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class DetalhePacienteController {

    private final DetalhePacienteService detalhePacienteService;

    @GetMapping("/{id}/detalhes-completos")
    public ResponseEntity<DetalhePacienteDTO> buscarDetalhesCompletos(@PathVariable Long id) {
        DetalhePacienteDTO detalhes = detalhePacienteService.buscarDetalhesCompletoPaciente(id);
        return ResponseEntity.ok(detalhes);
    }
}
