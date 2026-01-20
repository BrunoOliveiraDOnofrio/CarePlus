package com.example.careplus.service;

import com.example.careplus.dto.dtoMaterial.MaterialMapper;
import com.example.careplus.dto.dtoMaterial.MaterialRequestDto;
import com.example.careplus.dto.dtoMaterial.MaterialResponseDto;
import com.example.careplus.model.Consulta;
import com.example.careplus.model.Material;

import com.example.careplus.repository.ConsultaRepository;
import com.example.careplus.repository.MaterialRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final ConsultaRepository consultaRepository;

    public MaterialService(MaterialRepository materialRepository, ConsultaRepository consultaRepository) {
        this.materialRepository = materialRepository;
        this.consultaRepository = consultaRepository;
    }

    public List<MaterialResponseDto> cadastrar(List<MaterialRequestDto> dto) {
        Optional<Consulta> existe = consultaRepository.findById(dto.getFirst().getIdConsulta());
        List<Material> materiasParaAdicionar = MaterialMapper.toEntityList(dto);
        if(existe.isPresent()){

            Consulta consulta = existe.get();

            // Setar a referência da consulta em cada material
            for (Material material : materiasParaAdicionar) {
                material.setConsulta(consulta);
            }

            consulta.setMateriais(materiasParaAdicionar);

            consultaRepository.save(consulta);
            return MaterialMapper.toResponseDto(materiasParaAdicionar);

        } else {
            throw new RuntimeException("Consulta não encontrado");
        }



    }

    public List<MaterialResponseDto> listar() {
        List<Material> materials = materialRepository.findAll();
        if (materials.isEmpty()) throw new EntityNotFoundException("Nenhuma atividade encontrada");
        return MaterialMapper.toResponseDto(materials);
    }

    public MaterialResponseDto atualizar(Long id, MaterialRequestDto dto) {
        Material existente = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada"));

        existente.setItem(dto.getItem());
        existente.setTempoExposicao(dto.getTempoExposicao());

        materialRepository.save(existente);
        return MaterialMapper.toResponseDto(existente);
    }

    public void deletar(Long id) {
        if (!materialRepository.existsById(id))
            throw new EntityNotFoundException("Atividade não encontrada");
        materialRepository.deleteById(id);
    }

    public long contarAtividades() {
        return materialRepository.count();
    }

    public List<MaterialResponseDto> listarPorTempoExposicaoMaiorQue(Integer tempo) {
        List<Material> materials = materialRepository.findByTempoExposicaoGreaterThan(tempo);
        if (materials.isEmpty()) throw new EntityNotFoundException("Nenhuma atividade com tempo maior que " + tempo);
        return MaterialMapper.toResponseDto(materials);
    }
}
