package com.example.careplus.service;

import com.example.careplus.controller.dtoProntuario.ProntuarioRequestDto;
import com.example.careplus.model.Paciente;
import com.example.careplus.model.Prontuario;
import com.example.careplus.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProntuarioService {

    private final ProntuarioRepository prontuatioRepository;
    private final PacienteRepository pacienteRepository;
    private final ClassificacaoDoencasRepository cidRepository;
    private final TratamentoRepository tratamentoRepository;

    private final MedicacaoRepository medicacaoRepository;

    public ProntuarioService(ProntuarioRepository repository, PacienteRepository pacienteRepository, ClassificacaoDoencasRepository cidRepository, TratamentoRepository tratamentoRepository, MedicacaoRepository medicacaoRepository) {
        this.prontuatioRepository = repository;
        this.pacienteRepository = pacienteRepository;
        this.cidRepository = cidRepository;
        this.tratamentoRepository = tratamentoRepository;

        this.medicacaoRepository = medicacaoRepository;
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
            return prontuarios;
        }

        for (Prontuario p : prontuarios){
            p.setCid(cidRepository.findByProntuario_Id(p.getId()));
            p.setTratamentos(tratamentoRepository.findByProntuario_Id(p.getId()));

            p.setMedicacoes(medicacaoRepository.findByProntuario_Id(p.getId()));
        }

        return prontuarios;
    }

    public Prontuario buscarProntuarioPorId(Long id){
        Optional<Prontuario> prontuarioOpt = prontuatioRepository.findById(id);

        if (prontuarioOpt.isPresent()) {
            return prontuarioOpt.get();
        } else {
            throw new RuntimeException("Usuário não encontrado!");
        }

    }

    public Prontuario buscarProntuarioPorNome(String nome){
        Optional<Prontuario> prontuario = prontuatioRepository.findByPacienteNomeContainsIgnoreCase(nome);

        if (prontuario.isPresent()){
            return prontuario.get();
        } else {
            throw new RuntimeException("Paciente não encontrado");
        }

    }

    public Prontuario buscarProntuarioPorCpf(String cpf){
        Optional<Prontuario> prontuario = prontuatioRepository.findByPacienteCpf(cpf);

        if (prontuario.isPresent()){
            return prontuario.get();
        } else {
            throw new RuntimeException("Paciente não encontrado");
        }

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

            return prontuatioRepository.save(prontuarioExistente);
        }else {
            throw new RuntimeException("Paciente não encontrado");
        }
    }

    public void deletarPorId(Long id){
        boolean existe = prontuatioRepository.existsById(id);
        if (!existe){
            throw new NoSuchElementException();
        }
        prontuatioRepository.deleteById(id);
    }

}
