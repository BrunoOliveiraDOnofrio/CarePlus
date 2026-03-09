package com.example.careplus.service;

import com.example.careplus.dto.dtoPaciente.TudoPacienteDto;
import com.example.careplus.dto.dtoCuidador.CuidadorRequestDto;
import com.example.careplus.dto.dtoEndereco.EnderecoRequestDto;
import com.example.careplus.dto.dtoPaciente.PacienteMapper;
import com.example.careplus.dto.dtoPaciente.PacienteRequestDto;
import com.example.careplus.dto.dtoPaciente.PacienteResponseDto;
import com.example.careplus.dto.dtoResponsavel.ResponsavelRequestDto;
import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.exception.UserAlreadyExistsException;
import com.example.careplus.model.Responsavel;
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
    private final PacienteRepository repositoryPaciente;
    private final ResponsavelService responsavelService;
    private final CuidadorService cuidadorService;
    private final EnderecoService enderecoService;
    private final S3Service s3Service;

    public PacienteService(PacienteRepository repositoryPaciente, ResponsavelService responsavelService, CuidadorService cuidadorService, EnderecoService enderecoService, S3Service s3Service) {
        this.repositoryPaciente = repositoryPaciente;
        this.responsavelService = responsavelService;
        this.cuidadorService = cuidadorService;
        this.enderecoService = enderecoService;
        this.s3Service = s3Service;
    }

    public List<PacienteResponseDto> listarTodos(){
        List<Paciente> pacientes = repositoryPaciente.findAll();
        List<PacienteResponseDto> dtos = PacienteMapper.toResponseDto(pacientes);
        return dtos;
    }

    public Page<PacienteResponseDto> listarTodosPaginado(Pageable pageable) {
        return repositoryPaciente.findAll(pageable)
                .map(PacienteMapper::toResponseDto);
    }

    public PacienteResponseDto listarPorId(Long id){
        Optional<Paciente> existePaciente = repositoryPaciente.findById(id);

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
        } else if (repositoryPaciente.existsByEmail(paciente.getEmail())){
            throw new UserAlreadyExistsException("Paciente com este email ja existe");
        } else if (repositoryPaciente.existsByCpf(paciente.getCpf())){
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

        return PacienteMapper.toResponseDto(repositoryPaciente.save(entity));
    }

    // OK
    public void deletar(Long id){
        boolean existe = repositoryPaciente.existsById(id);

        if(!existe){
            throw new ResourceNotFoundException("Paciente não encontrado");
        }
        repositoryPaciente.deleteById(id);
    }

    // OK
    public PacienteResponseDto atualizar(PacienteRequestDto paciente, Long id){
        Optional<Paciente> existe = repositoryPaciente.findById(id);

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

            Paciente atualizado = repositoryPaciente.save(pacienteExistente);
            return PacienteMapper.toResponseDto(atualizado);
        }else {
            throw new RuntimeException("Paciente não encontrado");
        }
    }

    public byte[] buscarFoto(String cpf) throws IOException {
        return s3Service.buscarUltimaFotoPaciente(cpf);
    }

    public List<PacienteResponseDto> listarPorEmail(String email){
        List<Paciente> existeEmail = repositoryPaciente.findByEmailContainsIgnoreCase(email);



        if(existeEmail.isEmpty()){
            throw new ResourceNotFoundException("Email não existe");
        }
        return PacienteMapper.toResponseDto(existeEmail);
    }

    public List<PacienteResponseDto> buscarPorNome(String nome){

        List<Paciente> pacientes = repositoryPaciente.findByNomeContainingIgnoreCase(nome);

        if (!pacientes.isEmpty()){
            return PacienteMapper.toResponseDto(pacientes);
        }else{
            throw new ResourceNotFoundException("Usuário não encontrado!");
        }
    }


    public PacienteResponseDto cadastrarPacienteFormulario(TudoPacienteDto dto) {

        EnderecoRequestDto enderecoRequestDto = new EnderecoRequestDto();
        enderecoRequestDto.setCep(dto.getCep());
        enderecoRequestDto.setLogradouro(dto.getLogradouro());
        enderecoRequestDto.setNumero(dto.getNumero());
        enderecoRequestDto.setComplemento(dto.getComplemento());
        enderecoRequestDto.setBairro(dto.getBairro());
        enderecoRequestDto.setCidade(dto.getCidade());
        enderecoRequestDto.setEstado(dto.getEstado());

        ResponsavelRequestDto responsavelRequestDto = new ResponsavelRequestDto();
        responsavelRequestDto.setNome(dto.getNomeResponsavel());
        responsavelRequestDto.setEmail(dto.getEmailResponsavel());
        responsavelRequestDto.setCpf(dto.getCpfResponsavel());
        responsavelRequestDto.setTelefone(dto.getTelefoneResponsavel());
        responsavelRequestDto.setEndereco(enderecoRequestDto);
        responsavelRequestDto.setDtNascimento(dto.getDtNascimentoResponsavel());

        Responsavel responsavelCadastrado = responsavelService.buscarOuCadastrar(responsavelRequestDto);

        PacienteRequestDto pacienteRequestDto = new PacienteRequestDto();
        pacienteRequestDto.setNome(dto.getNomePaciente());
        pacienteRequestDto.setEmail(dto.getEmailPaciente());
        pacienteRequestDto.setCpf(dto.getCpfPaciente());
        pacienteRequestDto.setTelefone(dto.getTelefonePaciente());
        pacienteRequestDto.setDtNascimento(dto.getDtNascimentoPaciente());
        pacienteRequestDto.setConvenio(dto.getConvenioPaciente());
        pacienteRequestDto.setFoto(dto.getFotoPaciente());

        PacienteResponseDto pacienteSalvo = salvar(pacienteRequestDto);

        CuidadorRequestDto cuidadorRequestDto = new CuidadorRequestDto();
        cuidadorRequestDto.setPacienteId(pacienteSalvo.getId());
        cuidadorRequestDto.setResponsavelId(responsavelCadastrado.getId());
        cuidadorRequestDto.setParentesco(dto.getParentesco());

        cuidadorService.cadastrar(cuidadorRequestDto);

        return pacienteSalvo;
    }
}

