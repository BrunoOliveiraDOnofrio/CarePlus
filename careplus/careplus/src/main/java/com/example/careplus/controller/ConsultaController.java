package com.example.careplus.controller;

import com.example.careplus.dto.dtoConsulta.*;
import com.example.careplus.dto.dtoConsultaRecorrente.ConsultaRecorrenteRequestDto;
import com.example.careplus.dto.dtoConsultaRecorrente.ConsultaRecorrenteResponseDto;
import com.example.careplus.model.Consulta;
import com.example.careplus.service.ConsultaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("consultas")
public class ConsultaController {
    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaResponseDto> marcarConsulta(@RequestBody ConsultaRequestDto request) {
        ConsultaResponseDto consulta = service.marcarConsulta(request);
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
    public ResponseEntity<List<Consulta>> listarConsultas(){
        List<Consulta> consultas = service.listarConsultas();
        if (consultas.isEmpty()){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(consultas);
        }
    }

    @GetMapping("/por-data")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaResponseDto>> listarPorData(){
        List<ConsultaResponseDto> consultas = service.listarPorData();

        if (consultas.isEmpty()){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(consultas);
        }

    }

    @GetMapping("/por-paciente")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaResponseDto>> listarPorPaciente(@RequestParam Long idPaciente){
        List<ConsultaResponseDto> consultas = service.listarPorPaciente(idPaciente);

        if (consultas.isEmpty()){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(consultas);
        }

    }

    @PutMapping("/confirmar/{idConsulta}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaResponseDto> confirmarConsulta(@PathVariable Long idConsulta){
        ConsultaResponseDto consultaResponseDto = service.confirmarConsulta(idConsulta);
        return ResponseEntity.status(200).body(consultaResponseDto);
    }

    @PutMapping("/recusar/{idConsulta}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaResponseDto> recusarConsulta(@PathVariable Long idConsulta, @RequestBody String justificativa){
        ConsultaResponseDto consultaResponseDto = service.recusarConsulta(idConsulta, justificativa);
        return ResponseEntity.status(200).body(consultaResponseDto);
    }

    @PutMapping("/realizarObservacoes/{idConsulta}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaResponseDto> editarObervacoes(@PathVariable Long idConsulta, @RequestBody RealizarConsultaDto obs){
        ConsultaResponseDto consultaResponseDto = service.salvarObservacoes(idConsulta, obs);
        return ResponseEntity.status(200).body(consultaResponseDto);
    }

    @GetMapping("/consultasDoDia/{idFuncionario}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaResponseDto>> buscarConsultasDoDia(@PathVariable Long idFuncionario){
        List<ConsultaResponseDto> consultaResponseDtos = service.consultasDoDia(idFuncionario);
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
    public ResponseEntity<List<ConsultaResponseDto>> listarAgendaSemanal(
            @RequestParam Long funcionarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataReferencia
    ) {
        List<ConsultaResponseDto> agenda = service.listarAgendaSemanal(funcionarioId, dataReferencia);
        return ResponseEntity.ok(agenda);
    }

    @GetMapping("/pendentes/{idFuncionario}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaResponseDto>> listarConsultasPendentes(@PathVariable Long idFuncionario) {
        List<ConsultaResponseDto> consultasPendentes = service.listarConsultasPendentes(idFuncionario);

        if (consultasPendentes.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(200).body(consultasPendentes);
    }

    @GetMapping("/proxima/{idPaciente}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ProximaConsultaResponseDto> buscarProximaConsultaConfirmada(@PathVariable Long idPaciente) {
        ProximaConsultaResponseDto proximaConsulta = service.buscarProximaConsultaConfirmada(idPaciente);
        return ResponseEntity.ok(proximaConsulta);
    }

    @GetMapping("/detalhes/{idConsulta}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaAtualResponseDto> buscarDetalhesConsultaAtual(@PathVariable Long idConsulta) {
        ConsultaAtualResponseDto detalhes = service.buscarDetalhesConsultaAtual(idConsulta);
        return ResponseEntity.ok(detalhes);
    }

    @GetMapping("/detalhes-anterior/{idConsulta}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<DetalhesConsultaAnteriorResponseDto> buscarDetalhesConsultaAnterior(@PathVariable Long idConsulta) {
        DetalhesConsultaAnteriorResponseDto detalhes = service.buscarDetalhesConsultaAnterior(idConsulta);
        return ResponseEntity.ok(detalhes);
    }

}
