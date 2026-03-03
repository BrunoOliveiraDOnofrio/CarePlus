package com.example.careplus.service;

import com.example.careplus.dto.dtoPaciente.PacienteMapper;
import com.example.careplus.dto.dtoPaciente.PacienteRequestDto;
import com.example.careplus.dto.dtoPaciente.PacienteResponseDto;
import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.exception.UserAlreadyExistsException;
import org.springframework.stereotype.Service;

import com.example.careplus.exception.MissingFieldException;
import com.example.careplus.model.Paciente;
import com.example.careplus.repository.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {
    private final PacienteRepository repository;
    private final S3Service s3Service;

    public PacienteService(PacienteRepository repository, S3Service s3Service) {
        this.repository = repository;
        this.s3Service = s3Service;
    }

    public List<PacienteResponseDto> listarTodos(){
        List<Paciente> pacientes = repository.findAll();
        List<PacienteResponseDto> dtos = PacienteMapper.toResponseDto(pacientes);
        return dtos;
    }

    public Page<PacienteResponseDto> listarTodosPaginado(Pageable pageable) {
        return repository.findAll(pageable)
                .map(PacienteMapper::toResponseDto);
    }

    public PacienteResponseDto listarPorId(Long id){
        Optional<Paciente> existePaciente = repository.findById(id);

        if(existePaciente.isPresent()){

            PacienteResponseDto paciente = PacienteMapper.toResponseDto(existePaciente.get());

            return paciente;
        }

        throw new ResourceNotFoundException("Paciente não encontrado!");

    }

    // OK
    public PacienteResponseDto salvar(PacienteRequestDto paciente){

        if(paciente.getEmail() == null){
            throw new MissingFieldException("Campos faltando: Email ou senha!");
        } else if (repository.existsByEmail(paciente.getEmail())){
            throw new UserAlreadyExistsException("Paciente com este email ja existe");
        } else if (repository.existsByCpf(paciente.getCpf())){
            throw new UserAlreadyExistsException("Paciente com esse cpf já existe");
        }

        Paciente entity = PacienteMapper.toEntity(paciente);

        if (paciente.getFoto() != null && !paciente.getFoto().isEmpty()) {
            try {
                String nomeArquivo = s3Service.uploadImagemPaciente(paciente.getFoto(), paciente.getCpf());
                entity.setFoto(nomeArquivo);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao fazer upload da imagem: " + e.getMessage(), e);
            }
        }

        return PacienteMapper.toResponseDto(repository.save(entity));
    }

    // OK
    public void deletar(Long id){
        boolean existe = repository.existsById(id);

        if(!existe){
            throw new ResourceNotFoundException("Paciente não encontrado");
        }
        repository.deleteById(id);
    }

    // OK
    public PacienteResponseDto atualizar(PacienteRequestDto paciente, Long id){
        Optional<Paciente> existe = repository.findById(id);

        if(existe.isPresent()){
            Paciente pacienteExistente = existe.get();

            pacienteExistente.setNome(paciente.getNome());
            pacienteExistente.setEmail(paciente.getEmail());
            pacienteExistente.setDtNascimento(paciente.getDtNascimento());
            pacienteExistente.setTelefone(paciente.getTelefone());
            pacienteExistente.setConvenio(paciente.getConvenio());

            if (paciente.getFoto() != null && !paciente.getFoto().isEmpty()) {
                try {
                    String nomeArquivo = s3Service.uploadImagemPaciente(paciente.getFoto(), pacienteExistente.getCpf());
                    pacienteExistente.setFoto(nomeArquivo);
                } catch (IOException e) {
                    throw new RuntimeException("Erro ao fazer upload da imagem: " + e.getMessage(), e);
                }
            }

            Paciente atualizado = repository.save(pacienteExistente);
            return PacienteMapper.toResponseDto(atualizado);
        }else {
            throw new RuntimeException("Paciente não encontrado");
        }
    }

    public byte[] buscarFoto(String cpf) throws IOException {
        return s3Service.buscarUltimaFotoPaciente(cpf);
    }

    public List<PacienteResponseDto> listarPorEmail(String email){
        List<Paciente> existeEmail = repository.findByEmailContainsIgnoreCase(email);



        if(existeEmail.isEmpty()){
            throw new ResourceNotFoundException("Email não existe");
        }
        return PacienteMapper.toResponseDto(existeEmail);
    }

    public List<PacienteResponseDto> buscarPorNome(String nome){

        List<Paciente> pacientes = repository.findByNomeContainingIgnoreCase(nome);

        if (!pacientes.isEmpty()){
            return PacienteMapper.toResponseDto(pacientes);
        }else{
            throw new ResourceNotFoundException("Usuário não encontrado!");
        }
    }



}

