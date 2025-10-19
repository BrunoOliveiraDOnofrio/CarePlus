package com.example.careplus.controller;

import com.example.careplus.model.Consulta;
import com.example.careplus.model.ConsultaRequest;
import com.example.careplus.service.ConsultaService;
import org.apache.commons.lang3.concurrent.ConcurrentUtils;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("consultas")
public class ConsultaController {
    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Consulta> marcarConsulta(@RequestBody ConsultaRequest request) {
        Consulta consulta = service.marcarConsulta(request);
        return ResponseEntity.ok(consulta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarConsulta(@PathVariable Long id){
        try{
            service.removerConsulta(id);
            return ResponseEntity.status(204).build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<List<Consulta>> listarConsultas(){
        List<Consulta> consultas = service.listarConsultas();
        if (consultas.isEmpty()){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(consultas);
        }
    }

    @GetMapping("/por-data")
    public ResponseEntity<List<Consulta>> listarPorData(){
        List<Consulta> consultas = service.listarPorData();

        if (consultas.isEmpty()){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(consultas);
        }

    }

    @GetMapping("/por-paciente")
    public ResponseEntity<List<Consulta>> listarPorPaciente(@RequestParam Long idPaciente){
        List<Consulta> consultas = service.listarPorPaciente(idPaciente);

        if (consultas.isEmpty()){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(consultas);
        }

    }



    @PutMapping("/{id}")
    public ResponseEntity<Consulta> editarConsulta(
            @PathVariable Long id,
            @RequestBody ConsultaRequest request) {

        Consulta consultaEditada = service.editarConsulta(id, request);
        return ResponseEntity.status(200).body(consultaEditada);
    }


}
