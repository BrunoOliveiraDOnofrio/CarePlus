package com.example.careplus.service;

import com.example.careplus.dto.dtoMaterial.MaterialMapper;
import com.example.careplus.dto.dtoMaterial.MaterialRequestDto;
import com.example.careplus.dto.dtoMaterial.MaterialResponseDto;
import com.example.careplus.model.ConsultaProntuario;
import com.example.careplus.model.Material;

import com.example.careplus.repository.ConsultaProntuarioRepository;
import com.example.careplus.repository.MaterialRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final ConsultaProntuarioRepository consultaProntuarioRepository;

    public MaterialService(MaterialRepository materialRepository, ConsultaProntuarioRepository consultaProntuarioRepository) {
        this.materialRepository = materialRepository;
        this.consultaProntuarioRepository = consultaProntuarioRepository;
    }

    public MaterialResponseDto cadastrar(MaterialRequestDto dto) {
        Optional<ConsultaProntuario> existe = consultaProntuarioRepository.findById(dto.getIdConsulta());
        Material materiasParaAdicionar = MaterialMapper.toEntity(dto);
        if(existe.isPresent()){
            ConsultaProntuario consultaProntuario = existe.get();

            materiasParaAdicionar.setConsultaProntuario(consultaProntuario);
            materialRepository.save(materiasParaAdicionar);
            return MaterialMapper.toResponseDto(materiasParaAdicionar);

        } else {
            throw new RuntimeException("Consulta não encontrada");
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

}
