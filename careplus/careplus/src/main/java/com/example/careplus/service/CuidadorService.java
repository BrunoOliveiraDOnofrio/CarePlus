package com.example.careplus.service;

import com.example.careplus.controller.dtoCuidador.CuidadorMapper;
import com.example.careplus.controller.dtoCuidador.CuidadorRequestDto;
import com.example.careplus.controller.dtoCuidador.CuidadorResponseDto;
import com.example.careplus.model.Cuidador;
import com.example.careplus.model.Paciente;
import com.example.careplus.model.Responsavel;
import com.example.careplus.repository.CuidadorRepository;
import com.example.careplus.repository.PacienteRepository;
import com.example.careplus.repository.ResponsavelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CuidadorService {

    private final CuidadorRepository cuidadorRepository;
    private final PacienteRepository pacienteRepository;
    private final ResponsavelRepository responsavelRepository;

    public CuidadorService(CuidadorRepository cuidadorRepository, PacienteRepository pacienteRepository, ResponsavelRepository responsavelRepository) {
        this.cuidadorRepository = cuidadorRepository;
        this.pacienteRepository = pacienteRepository;
        this.responsavelRepository = responsavelRepository;
    }

    public CuidadorResponseDto cadastrar (CuidadorRequestDto cuidadorNew){
        Optional<Paciente> paciente = pacienteRepository.findById(cuidadorNew.getPacienteId());
        if(paciente.isEmpty()){
            throw new EntityNotFoundException();
        }
        Optional<Responsavel> responsavel = responsavelRepository.findById(cuidadorNew.getResponsavelId());
        if (responsavel.isEmpty()){
            throw new EntityNotFoundException();
        }
        Responsavel responsavelEscolhido = responsavel.get();
        Paciente pacienteEscolhido = paciente.get();
        Cuidador paraRegistro = CuidadorMapper.toEntity(cuidadorNew, pacienteEscolhido, responsavelEscolhido);

        paraRegistro = cuidadorRepository.save(paraRegistro);

        return CuidadorMapper.toResponseDto(paraRegistro);
    }

    public List<CuidadorResponseDto> listar(){

        List<Cuidador> registros = cuidadorRepository.findAll();
        if (registros.isEmpty()){
            throw new EntityNotFoundException();
        }

        return CuidadorMapper.toResponseDto(registros);

    }

    public CuidadorResponseDto atualizar(Long id, CuidadorRequestDto cuidadorAtt){
        Optional<Cuidador> escolhido = cuidadorRepository.findById(id);
        if (escolhido.isEmpty()){
            throw new EntityNotFoundException();
        }
        Optional<Paciente> paciente = pacienteRepository.findById(cuidadorAtt.getPacienteId());
        if(paciente.isEmpty()){
            throw new EntityNotFoundException();
        }
        Optional<Responsavel> responsavel = responsavelRepository.findById(cuidadorAtt.getResponsavelId());
        if (responsavel.isEmpty()){
            throw new EntityNotFoundException();
        }
        Cuidador cuidadorEscolhido = escolhido.get();
        Responsavel responsavelEscolhido = responsavel.get();
        Paciente pacienteEscolhido = paciente.get();
        cuidadorEscolhido = CuidadorMapper.updateEntityFromDto(cuidadorAtt, cuidadorEscolhido, pacienteEscolhido, responsavelEscolhido);

        cuidadorEscolhido = cuidadorRepository.save(cuidadorEscolhido);

        return CuidadorMapper.toResponseDto(cuidadorEscolhido);
    }

    public void deletar(Long id){
        boolean existe = cuidadorRepository.existsById(id);
        if (!existe){
            throw new EntityNotFoundException();
        }

        cuidadorRepository.deleteById(id);
    }

    public List<CuidadorResponseDto> listarPacientesPorResponsavel_Id(Long responsavelId) {
        List<Cuidador> cuidadores = cuidadorRepository.findByResponsavelId(responsavelId);

        if (cuidadores.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return CuidadorMapper.toResponseDto(cuidadores);
    }

    public List<CuidadorResponseDto> listarResponsaveisPorPaciente_Nome(String nomePaciente) {
        List<Cuidador> cuidadores = cuidadorRepository.findByPacienteNomeIgnoreCaseStartingWith(nomePaciente);

        if (cuidadores.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return CuidadorMapper.toResponseDto(cuidadores);
    }


}
