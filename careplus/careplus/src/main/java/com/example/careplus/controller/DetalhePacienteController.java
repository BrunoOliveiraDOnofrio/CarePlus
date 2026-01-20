package com.example.careplus.controller;

import com.example.careplus.dto.dtoDetalhes.AtualizarFichaClinicaDTO;
import com.example.careplus.dto.dtoDetalhes.AtualizarObservacoesComportamentaisDTO;
import com.example.careplus.dto.dtoPaciente.DetalhePacienteDTO;
import com.example.careplus.model.Tratamento;
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

    @PutMapping("/{id}/ficha-clinica")
    public ResponseEntity<Void> atualizarFichaClinica(
            @PathVariable Long id,
            @RequestBody AtualizarFichaClinicaDTO dto) {
        detalhePacienteService.atualizarFichaClinica(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/observacoes-comportamentais")
    public ResponseEntity<Void> atualizarObservacoesComportamentais(
            @PathVariable Long id,
            @RequestBody AtualizarObservacoesComportamentaisDTO dto) {
        detalhePacienteService.atualizarObservacoesComportamentais(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/tratamento")
    public ResponseEntity<Void> atualizarTratamento(
            @PathVariable Long id,
            @RequestBody Tratamento dto) {
        detalhePacienteService.atualizarTratamento(id, dto);
        return ResponseEntity.noContent().build();
    }
}
