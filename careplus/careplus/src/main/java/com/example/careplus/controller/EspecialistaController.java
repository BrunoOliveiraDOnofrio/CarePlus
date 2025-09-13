package com.example.careplus.controller;

import com.example.careplus.model.Especialista;
import com.example.careplus.model.Usuario;
import com.example.careplus.service.EspecialistaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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



}
