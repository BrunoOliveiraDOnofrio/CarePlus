package com.example.careplus.service;

import com.example.careplus.dto.dtoCuidador.CuidadorContatoResponseDto;
import com.example.careplus.dto.dtoCuidador.CuidadorMapper;
import com.example.careplus.dto.dtoCuidador.CuidadorRequestDto;
import com.example.careplus.dto.dtoCuidador.CuidadorResponseDto;
import com.example.careplus.model.Cuidador;
import com.example.careplus.model.Paciente;
import com.example.careplus.model.Responsavel;
import com.example.careplus.repository.CuidadorRepository;
import com.example.careplus.repository.PacienteRepository;
import com.example.careplus.repository.ResponsavelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Cuidador cadastrar (CuidadorRequestDto cuidadorNew){
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

        return paraRegistro;
    }

    public List<Cuidador> listar(){
        List<Cuidador> registros = cuidadorRepository.findAll();

        return registros;
    }

    public Cuidador atualizar(Long id, CuidadorRequestDto dto) {

        Cuidador cuidador = cuidadorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuidador não encontrado"));

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        Responsavel responsavel = responsavelRepository.findById(dto.getResponsavelId())
                .orElseThrow(() -> new EntityNotFoundException("Responsável não encontrado"));

        CuidadorMapper.updateEntityFromDto(dto, cuidador, paciente, responsavel);

        return cuidadorRepository.save(cuidador);
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

    public List<CuidadorContatoResponseDto> buscarContato(Long idPaciente){
        List<Cuidador> cuidadores = cuidadorRepository.findByPacienteId(idPaciente);
        if (cuidadores.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return CuidadorMapper.toContatoResponseDto(cuidadores);
    }

}
