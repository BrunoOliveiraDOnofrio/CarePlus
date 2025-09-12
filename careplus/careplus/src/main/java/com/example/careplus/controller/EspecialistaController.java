package com.example.careplus.controller;

import com.example.careplus.model.Especialista;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/especialistas")
public class EspecialistaController {

    @GetMapping
    public ResponseEntity<ArrayList<Especialista>> listarUsuarios(){

    }



}
