package com.example.careplus.service;

import com.example.careplus.controller.dtoProntuario.ProntuarioRequestDto;
import com.example.careplus.model.ClassificacaoDoencas;
import com.example.careplus.model.Paciente;
import com.example.careplus.model.Prontuario;
import com.example.careplus.repository.ClassificacaoDoencasRepository;
import com.example.careplus.repository.PacienteRepository;
import com.example.careplus.repository.ProntuarioRepository;
import com.example.careplus.repository.TratamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProntuarioService {

    private final ProntuarioRepository prontuatioRepository;
    private final PacienteRepository pacienteRepository;
    private final ClassificacaoDoencasRepository cidRepository;
    private final TratamentoRepository tratamentoRepository;

    public ProntuarioService(ProntuarioRepository repository, PacienteRepository pacienteRepository, ClassificacaoDoencasRepository cidRepository, TratamentoRepository tratamentoRepository) {
        this.prontuatioRepository = repository;
        this.pacienteRepository = pacienteRepository;
        this.cidRepository = cidRepository;
        this.tratamentoRepository = tratamentoRepository;
    }

    public Prontuario criarProntuario (ProntuarioRequestDto prontuario){
        Optional<Paciente> usuarioOpt = pacienteRepository.findById(prontuario.getIdPaciente());
        Paciente paciente;
        if (usuarioOpt.isPresent()) {
            paciente = usuarioOpt.get();
        } else {
            throw new RuntimeException("Usuário não encontrado!");
        }

        Prontuario prontuarioCriado = new Prontuario();
        prontuarioCriado.setPaciente(paciente);
        prontuarioCriado.setDesfraldado(prontuario.getDesfraldado());
        prontuarioCriado.setHiperfoco(prontuario.getHiperfoco());
        prontuarioCriado.setAnamnese(prontuario.getAnamnese());
        prontuarioCriado.setDiagnostico(prontuario.getDiagnostico());
        prontuarioCriado.setResumoClinico(prontuario.getResumoClinico());
        prontuarioCriado.setNivelAgressividade(prontuario.getNivelAgressividade());

        return prontuatioRepository.save(prontuarioCriado);
    }

    public List<Prontuario> listarProntuarios(){
        List<Prontuario> prontuarios = prontuatioRepository.findAll();
        if (prontuarios.isEmpty()){
            return null;
        }

        for (Prontuario p : prontuarios){
            p.setCid(cidRepository.findByProntuario_Id(p.getId()));
            p.setTratamentos(tratamentoRepository.findByProntuario_Id(p.getId()));
        }

        return prontuarios;
    }

    public Prontuario atualizarProntuario(ProntuarioRequestDto prontuario, Long id){
        Optional<Prontuario> existe = prontuatioRepository.findById(id);

        if(existe.isPresent()){

            Prontuario prontuarioExistente = existe.get();

            prontuarioExistente.setDesfraldado(prontuario.getDesfraldado());
            prontuarioExistente.setHiperfoco(prontuario.getHiperfoco());
            prontuarioExistente.setAnamnese(prontuario.getAnamnese());
            prontuarioExistente.setDiagnostico(prontuario.getDiagnostico());
            prontuarioExistente.setResumoClinico(prontuario.getResumoClinico());
            prontuarioExistente.setNivelAgressividade(prontuario.getNivelAgressividade());

            return prontuarioExistente;
        }else {
            throw new RuntimeException("Paciente não encontrado");
        }
    }

}
