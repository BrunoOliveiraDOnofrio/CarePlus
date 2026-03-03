package com.example.careplus.controller;

import com.example.careplus.dto.dtoDetalhes.AtualizarFichaClinicaDTO;
import com.example.careplus.dto.dtoDetalhes.AtualizarObservacoesComportamentaisDTO;
import com.example.careplus.dto.dtoDetalhes.AtualizarTratamentoDTO;
import com.example.careplus.dto.dtoPaciente.DetalhePacienteDTO;
import com.example.careplus.service.DetalhePacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/detalhes-pacientes")
@RequiredArgsConstructor
public class DetalhePacienteController {

    private final DetalhePacienteService detalhePacienteService;

    @GetMapping("/detalhes-completos")
    public ResponseEntity<DetalhePacienteDTO> buscarDetalhesCompletos(@RequestParam Long id) {
        DetalhePacienteDTO detalhes = detalhePacienteService.buscarDetalhesCompletoPaciente(id);
        return ResponseEntity.ok(detalhes);
    }

    @PutMapping("/ficha-clinica")
    public ResponseEntity<Void> atualizarFichaClinica(
            @RequestParam Long id,
            @RequestBody AtualizarFichaClinicaDTO dto) {
        detalhePacienteService.atualizarFichaClinica(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/observacoes-comportamentais")
    public ResponseEntity<Void> atualizarObservacoesComportamentais(
            @RequestParam Long id,
            @RequestBody AtualizarObservacoesComportamentaisDTO dto) {
        detalhePacienteService.atualizarObservacoesComportamentais(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/tratamento")
    public ResponseEntity<Void> atualizarTratamento(
            @RequestParam Long id,
            @RequestBody AtualizarTratamentoDTO dto) {
        detalhePacienteService.atualizarTratamento(id, dto);
        return ResponseEntity.noContent().build();
    }
}
