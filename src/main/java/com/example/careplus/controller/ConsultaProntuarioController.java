package com.example.careplus.controller;

import com.example.careplus.dto.dtoConsultaProntuario.*;
import com.example.careplus.dto.dtoConsultaRecorrente.ConsultaRecorrenteRequestDto;
import com.example.careplus.dto.dtoConsultaRecorrente.ConsultaRecorrenteResponseDto;
import com.example.careplus.model.ConsultaProntuario;
import com.example.careplus.service.ConsultaProntuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("consultas-prontuario")
public class ConsultaProntuarioController {
    private final ConsultaProntuarioService service;

    public ConsultaProntuarioController(ConsultaProntuarioService service) {
        this.service = service;
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaProntuarioResponseDto> marcarConsulta(@RequestBody ConsultaProntuarioRequestDto request) {
        ConsultaProntuarioResponseDto consulta = service.marcarConsulta(request);
        return ResponseEntity.ok(consulta);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> deletarConsulta(@PathVariable Long id){
        try{
            service.removerConsulta(id);
            return ResponseEntity.status(204).build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaProntuario>> listarConsultas(){
        List<ConsultaProntuario> consultas = service.listarConsultas();
        if (consultas.isEmpty()){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(consultas);
        }
    }

    @GetMapping("/por-data")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaProntuarioResponseDto>> listarPorData(){
        List<ConsultaProntuarioResponseDto> consultas = service.listarPorData();

        if (consultas.isEmpty()){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(consultas);
        }

    }

    @GetMapping("/por-paciente")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaProntuarioResponseDto>> listarPorPaciente(@RequestParam Long idPaciente){
        List<ConsultaProntuarioResponseDto> consultas = service.listarPorPaciente(idPaciente);

        if (consultas.isEmpty()){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(consultas);
        }

    }

    @PutMapping("/confirmar/{idConsulta}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaProntuarioResponseDto> confirmarConsulta(@PathVariable Long idConsulta){
        ConsultaProntuarioResponseDto consultaResponseDto = service.confirmarConsulta(idConsulta);
        return ResponseEntity.status(200).body(consultaResponseDto);
    }

    @PutMapping("/recusar/{idConsulta}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaProntuarioResponseDto> recusarConsulta(@PathVariable Long idConsulta, @RequestBody String justificativa){
        ConsultaProntuarioResponseDto consultaResponseDto = service.recusarConsulta(idConsulta, justificativa);
        return ResponseEntity.status(200).body(consultaResponseDto);
    }

    @PutMapping("/realizarObservacoes/{idConsulta}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaProntuarioResponseDto> editarObervacoes(@PathVariable Long idConsulta, @RequestBody RealizarConsultaProntuarioDto obs) throws JsonProcessingException {
        ConsultaProntuarioResponseDto consultaResponseDto = service.salvarObservacoes(idConsulta, obs);
        return ResponseEntity.status(200).body(consultaResponseDto);
    }

    @GetMapping("/consultasDoDia/{idFuncionario}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaProntuarioResponseDto>> buscarConsultasDoDia(@PathVariable Long idFuncionario){
        List<ConsultaProntuarioResponseDto> consultaResponseDtos = service.consultasDoDia(idFuncionario);
        return ResponseEntity.status(200).body(consultaResponseDtos);
    }

    @PostMapping("/recorrentes")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaRecorrenteResponseDto> criarConsultasRecorrentes(
            @RequestBody ConsultaRecorrenteRequestDto request) {
        ConsultaRecorrenteResponseDto response = service.criarConsultasRecorrentes(request);
        return ResponseEntity.status(207).body(response);
    }

    @GetMapping("/agenda-semanal")
    public ResponseEntity<List<ConsultaProntuarioResponseDto>> listarAgendaSemanal(
            @RequestParam Long funcionarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataReferencia
    ) {
        List<ConsultaProntuarioResponseDto> agenda = service.listarAgendaSemanal(funcionarioId, dataReferencia);
        return ResponseEntity.ok(agenda);
    }

    @GetMapping("/pendentes/{idFuncionario}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaProntuarioResponseDto>> listarConsultasPendentes(@PathVariable Long idFuncionario) {
        List<ConsultaProntuarioResponseDto> consultasPendentes = service.listarConsultasPendentes(idFuncionario);

        if (consultasPendentes.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(200).body(consultasPendentes);
    }

    @GetMapping("/proxima/{idPaciente}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ProximaConsultaProntuarioResponseDto> buscarProximaConsultaConfirmada(@PathVariable Long idPaciente) {
        ProximaConsultaProntuarioResponseDto proximaConsulta = service.buscarProximaConsultaConfirmada(idPaciente);
        return ResponseEntity.ok(proximaConsulta);
    }

    @GetMapping("/detalhes/{idConsulta}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaProntuarioAtualResponseDto> buscarDetalhesConsultaAtual(@PathVariable Long idConsulta) {
        ConsultaProntuarioAtualResponseDto detalhes = service.buscarDetalhesConsultaAtual(idConsulta);
        return ResponseEntity.ok(detalhes);
    }

    @GetMapping("/detalhes-anterior/{idConsulta}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<DetalhesConsultaProntuarioAnteriorResponseDto> buscarDetalhesConsultaAnterior(@PathVariable Long idConsulta) {
        DetalhesConsultaProntuarioAnteriorResponseDto detalhes = service.buscarDetalhesConsultaAnterior(idConsulta);
        return ResponseEntity.ok(detalhes);
    }

}

