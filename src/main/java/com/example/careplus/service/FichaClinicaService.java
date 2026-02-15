package com.example.careplus.service;

import com.example.careplus.dto.dtoFichaClinica.FichaClinicaRequestDto;
import com.example.careplus.model.Paciente;
import com.example.careplus.model.FichaClinica;
import com.example.careplus.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FichaClinicaService {

    private final FichaClinicaRepository fichaClinicaRepository;
    private final PacienteRepository pacienteRepository;
    private final ClassificacaoDoencasRepository cidRepository;
    private final TratamentoRepository tratamentoRepository;
    private final MedicacaoRepository medicacaoRepository;

    public FichaClinicaService(FichaClinicaRepository repository, PacienteRepository pacienteRepository, ClassificacaoDoencasRepository cidRepository, TratamentoRepository tratamentoRepository, MedicacaoRepository medicacaoRepository) {
        this.fichaClinicaRepository = repository;
        this.pacienteRepository = pacienteRepository;
        this.cidRepository = cidRepository;
        this.tratamentoRepository = tratamentoRepository;
        this.medicacaoRepository = medicacaoRepository;
    }

    public FichaClinica criarFichaClinica(FichaClinicaRequestDto fichaClinica){
        Optional<Paciente> usuarioOpt = pacienteRepository.findById(fichaClinica.getIdPaciente());
        Paciente paciente;
        if (usuarioOpt.isPresent()) {
            paciente = usuarioOpt.get();
        } else {
            throw new RuntimeException("Usuário não encontrado!");
        }

        FichaClinica fichaClinicaCriada = new FichaClinica();
        fichaClinicaCriada.setPaciente(paciente);
        fichaClinicaCriada.setDesfraldado(fichaClinica.getDesfraldado());
        fichaClinicaCriada.setHiperfoco(fichaClinica.getHiperfoco());
        fichaClinicaCriada.setAnamnese(fichaClinica.getAnamnese());
        fichaClinicaCriada.setDiagnostico(fichaClinica.getDiagnostico());
        fichaClinicaCriada.setResumoClinico(fichaClinica.getResumoClinico());
        fichaClinicaCriada.setNivelAgressividade(fichaClinica.getNivelAgressividade());

        return fichaClinicaRepository.save(fichaClinicaCriada);
    }

    public List<FichaClinica> listarFichasClinicas(){
        List<FichaClinica> fichasClinicas = fichaClinicaRepository.findAll();
        if (fichasClinicas.isEmpty()){
            return fichasClinicas;
        }

        for (FichaClinica f : fichasClinicas){
            f.setCid(cidRepository.findByFichaClinica_Id(f.getId()));
            f.setTratamentos(tratamentoRepository.findByFichaClinica_Id(f.getId()));
            f.setMedicacoes(medicacaoRepository.findByFichaClinica_Id(f.getId()));
        }

        return fichasClinicas;
    }

    public FichaClinica buscarFichaClinicaPorId(Long id){
        Optional<FichaClinica> fichaClinicaOpt = fichaClinicaRepository.findById(id);

        if (fichaClinicaOpt.isPresent()) {
            return fichaClinicaOpt.get();
        } else {
            throw new RuntimeException("Usuário não encontrado!");
        }

    }

    public FichaClinica buscarFichaClinicaPorNome(String nome){
        Optional<FichaClinica> fichaClinica = fichaClinicaRepository.findByPacienteNomeContainsIgnoreCase(nome);

        if (fichaClinica.isPresent()){
            return fichaClinica.get();
        } else {
            throw new RuntimeException("Paciente não encontrado");
        }

    }

    public FichaClinica buscarFichaClinicaPorCpf(String cpf){
        Optional<FichaClinica> fichaClinica = fichaClinicaRepository.findByPacienteCpf(cpf);

        if (fichaClinica.isPresent()){
            return fichaClinica.get();
        } else {
            throw new RuntimeException("Paciente não encontrado");
        }

    }

    public FichaClinica atualizarFichaClinica(FichaClinicaRequestDto fichaClinica, Long id){
        Optional<FichaClinica> existe = fichaClinicaRepository.findById(id);

        if(existe.isPresent()){

            FichaClinica fichaClinicaExistente = existe.get();

            fichaClinicaExistente.setDesfraldado(fichaClinica.getDesfraldado());
            fichaClinicaExistente.setHiperfoco(fichaClinica.getHiperfoco());
            fichaClinicaExistente.setAnamnese(fichaClinica.getAnamnese());
            fichaClinicaExistente.setDiagnostico(fichaClinica.getDiagnostico());
            fichaClinicaExistente.setResumoClinico(fichaClinica.getResumoClinico());
            fichaClinicaExistente.setNivelAgressividade(fichaClinica.getNivelAgressividade());

            return fichaClinicaRepository.save(fichaClinicaExistente);
        }else {
            throw new RuntimeException("Paciente não encontrado");
        }
    }

    public void deletarPorId(Long id){
        boolean existe = fichaClinicaRepository.existsById(id);
        if (!existe){
            throw new NoSuchElementException();
        }
        fichaClinicaRepository.deleteById(id);
    }

}

