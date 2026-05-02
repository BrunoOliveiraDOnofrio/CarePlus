package com.example.careplus.controller;

import com.example.careplus.dto.dtoDetalhes.AtualizarFichaClinicaDTO;
import com.example.careplus.dto.dtoDetalhes.AtualizarObservacoesComportamentaisDTO;
import com.example.careplus.dto.dtoPaciente.DetalhePacienteDTO;
import com.example.careplus.model.Funcionario;
import com.example.careplus.repository.FuncionarioRepository;
import com.example.careplus.service.DetalhePacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/detalhes-pacientes")
@RequiredArgsConstructor
public class DetalhePacienteController {

    private final DetalhePacienteService detalhePacienteService;
    private final FuncionarioRepository funcionarioRepository;

    @GetMapping("/detalhes-completo")
    public ResponseEntity<DetalhePacienteDTO> buscarDetalhesCompletos(@RequestParam Long idPaciente) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Funcionário não autenticado");
        }

        Funcionario funcionario = funcionarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Funcionário não encontrado"));

        DetalhePacienteDTO detalhes = detalhePacienteService.buscarDetalhesCompletoPaciente(idPaciente, funcionario.getId());
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

}
