package com.example.careplus.controller;

import com.example.careplus.model.Especialista;
import com.example.careplus.service.EspecialistaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especialistas")
public class EspecialistaController {

    public final EspecialistaService especialistaService;

    public EspecialistaController(EspecialistaService especialistaService) {
        this.especialistaService = especialistaService;
    }

    @PostMapping
    public ResponseEntity<Especialista> salvarEspecialista(@RequestBody Especialista especialista){
        Especialista especialistaSalvo = especialistaService.salvar(especialista);
        return ResponseEntity.status(201).body(especialistaSalvo);
    }

    @GetMapping("/por-email")
    public ResponseEntity<List<Especialista>> buscarPorEmail(@RequestParam String email){
        List<Especialista> especialistas = especialistaService.buscarPorEmail(email);
        return ResponseEntity.status(404).body(especialistas);
    }

    @GetMapping
    public ResponseEntity<List<Especialista>> listarTodos(){
        List<Especialista> especialistas = especialistaService.listarTodos();
        return ResponseEntity.status(200).body(especialistas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarEspecialista(@PathVariable Long id){
        try{
            especialistaService.deletar(id);
            return ResponseEntity.status(200).build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarEspecialista(@RequestBody Especialista especialista, @PathVariable Long id){
            especialistaService.atualizar(especialista, id);
            return ResponseEntity.status(200).build();
    }

}
