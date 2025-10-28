package com.example.careplus.service;

import com.example.careplus.controller.dtoConsulta.ConsultaMapper;
import com.example.careplus.controller.dtoConsulta.ConsultaRequestDto;
import com.example.careplus.controller.dtoConsulta.ConsultaResponseDto;
import com.example.careplus.controller.dtoFuncionario.FuncionarioMapper;
import com.example.careplus.controller.dtoPaciente.PacienteMapper;
import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.model.Consulta;
import com.example.careplus.model.ConsultaRequest;
import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Paciente;
import com.example.careplus.repository.ConsultaRepository;
import com.example.careplus.repository.FuncionarioRepository;
import com.example.careplus.repository.PacienteRepository;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final EmailService emailService;
    private final ResourcePatternResolver resourcePatternResolver;

    public ConsultaService(ConsultaRepository consultaRepository, PacienteRepository pacienteRepository, FuncionarioRepository funcionarioRepository, EmailService emailService, ResourcePatternResolver resourcePatternResolver) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.emailService = emailService;
        this.resourcePatternResolver = resourcePatternResolver;
    }

    //montando a Consulta a partir do ConsultaRequest
    public ConsultaResponseDto marcarConsulta(ConsultaRequestDto request){
        Optional<Paciente> usuarioOpt = pacienteRepository.findById(request.getPacienteId());
        Paciente paciente;
        if (usuarioOpt.isPresent()) {
            paciente = usuarioOpt.get();
        } else {
            throw new ResourceNotFoundException("Usuário não encontrado!");
        }

        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findById(request.getFuncionarioId());
        Funcionario funcionario;
        if (funcionarioOpt.isPresent()) {
            funcionario = funcionarioOpt.get();
        } else {
            throw new ResourceNotFoundException("Funcionario não encontrado!");
        }

//        Consulta consulta = new Consulta();
//        consulta.setFuncionario(funcionario);
//        consulta.setPaciente(paciente);
//        consulta.setDataHora(request.getDataHora());
//        consulta.setTipo("Pendente");

        Consulta novaConsulta = new Consulta();
        novaConsulta.setPaciente(paciente);
        novaConsulta.setFuncionario(funcionario);
        novaConsulta.setDataHora(request.getDataHora());

        Consulta salvo = consultaRepository.save(novaConsulta);

        emailService.EnviarNotificacao(funcionario, novaConsulta, paciente);

        return ConsultaMapper.toResponseDto(salvo);
    }

    public void removerConsulta(Long consultaId){
        Boolean consulta = consultaRepository.existsById(consultaId);
        if (!consulta){
            throw new RuntimeException("Consulta não encontrada");
        } else {
            consultaRepository.deleteById(consultaId);
        }
    }

    public List<Consulta> listarConsultas(){
        return consultaRepository.findAll();
    }

    public List<ConsultaResponseDto> listarPorData(){
        List<Consulta> consultas = consultaRepository.buscarPorData();

        if (consultas.isEmpty()){
            throw new ResourceNotFoundException("Nenhuma consulta cadastrada!");
        }

        return ConsultaMapper.toResponseDto(consultas);

    }

    public List<ConsultaResponseDto> listarPorPaciente(Long idPaciente){
        List<Consulta> consultas = consultaRepository.buscarPorPaciente(idPaciente);

        if (consultas.isEmpty()){
            throw new ResourceNotFoundException("Nenhuma consulta cadastrada para esse paciente!");
        }

        return ConsultaMapper.toResponseDto(consultas);

    }



    public ConsultaResponseDto editarConsulta(Long consultaId, ConsultaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));

        Funcionario funcionario = funcionarioRepository.findById(request.getFuncionarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario não encontrado"));

        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));

        consulta.setPaciente(paciente);
        consulta.setFuncionario(funcionario);
        consulta.setDataHora(request.getDataHora());
        consulta.setTipo("Retorno");

        Consulta salva = consultaRepository.save(consulta);

        return ConsultaMapper.toResponseDto(salva);
    }

}
