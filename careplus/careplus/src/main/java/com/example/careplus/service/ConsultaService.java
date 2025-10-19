package com.example.careplus.service;

import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.model.Consulta;
import com.example.careplus.model.ConsultaRequest;
import com.example.careplus.model.Especialista;
import com.example.careplus.model.Paciente;
import com.example.careplus.repository.ConsultaRepository;
import com.example.careplus.repository.EspecialistaRepository;
import com.example.careplus.repository.PacienteRepository;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final EspecialistaRepository especialistaRepository;
    private final EmailService emailService;
    private final ResourcePatternResolver resourcePatternResolver;

    public ConsultaService(ConsultaRepository consultaRepository, PacienteRepository pacienteRepository, EspecialistaRepository especialistaRepository, EmailService emailService, ResourcePatternResolver resourcePatternResolver) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.especialistaRepository = especialistaRepository;
        this.emailService = emailService;
        this.resourcePatternResolver = resourcePatternResolver;
    }

    //montando a Consulta a partir do ConsultaRequest
    public Consulta marcarConsulta(ConsultaRequest request){
        Optional<Paciente> usuarioOpt = pacienteRepository.findById(request.getPacienteId());
        Paciente paciente;
        if (usuarioOpt.isPresent()) {
            paciente = usuarioOpt.get();
        } else {
            throw new RuntimeException("Usuário não encontrado!");
        }

        Optional<Especialista> especialistaOpt = especialistaRepository.findById(request.getEspecialistaId());
        Especialista especialista;
        if (especialistaOpt.isPresent()) {
            especialista = especialistaOpt.get();
        } else {
            throw new RuntimeException("Especialista não encontrado!");
        }

        Consulta consulta = new Consulta();
        consulta.setEspecialista(especialista);
        consulta.setUsuario(paciente);
        consulta.setDataHora(request.getDataHora());
        consulta.setTipo("Pendente");

        emailService.EnviarNotificacao(especialista, consulta, paciente);
        return consultaRepository.save(consulta);
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

    public List<Consulta> listarPorData(){
        List<Consulta> consultas = consultaRepository.buscarPorData();

        if (consultas.isEmpty()){
            throw new ResourceNotFoundException("Nenhuma consulta cadastrada!");
        }

        return consultas;

    }

    public List<Consulta> listarPorPaciente(Long idPaciente){
        List<Consulta> consultas = consultaRepository.buscarPorPaciente(idPaciente);

        if (consultas.isEmpty()){
            throw new ResourceNotFoundException("Nenhuma consulta cadastrada para esse paciente!");
        }

        return consultas;

    }



    public Consulta editarConsulta(Long consultaId, ConsultaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));

        Especialista especialista = especialistaRepository.findById(request.getEspecialistaId())
                .orElseThrow(() -> new ResourceNotFoundException("Especialista não encontrado"));

        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));

        consulta.setUsuario(paciente);
        consulta.setEspecialista(especialista);
        consulta.setDataHora(request.getDataHora());
        consulta.setTipo("Retorno");

        return consultaRepository.save(consulta);
    }

}
