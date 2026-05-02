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
import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Responsavel;
import com.example.careplus.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import com.example.careplus.exception.MissingFieldException;
import com.example.careplus.model.Paciente;
import com.example.careplus.model.FichaClinica;
import com.example.careplus.repository.PacienteRepository;
import com.example.careplus.repository.FichaClinicaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final FichaClinicaRepository fichaClinicaRepository;
    private final FuncionarioRepository repositoryFuncionario;

    public PacienteService(PacienteRepository repositoryPaciente, ResponsavelService responsavelService, CuidadorService cuidadorService, EnderecoService enderecoService, S3Service s3Service, FichaClinicaRepository fichaClinicaRepository, FuncionarioRepository repositoryFuncionario) {
        this.repositoryPaciente = repositoryPaciente;
        this.responsavelService = responsavelService;
        this.cuidadorService = cuidadorService;
        this.enderecoService = enderecoService;
        this.s3Service = s3Service;
        this.fichaClinicaRepository = fichaClinicaRepository;
        this.repositoryFuncionario = repositoryFuncionario;
    }

    public List<PacienteResponseDto> listarTodos(){
        List<Paciente> pacientes = repositoryPaciente.findAllByAtivoTrue();
        List<PacienteResponseDto> dtos = PacienteMapper.toResponseDto(pacientes);
        return dtos;
    }

    public Page<PacienteResponseDto> listarTodosPaginado(Pageable pageable) {
        return repositoryPaciente.findAllByAtivoTrue(pageable)
                .map(PacienteMapper::toResponseDto);
    }

    public Page<PacienteResponseDto> listarInativosPaginado(Pageable pageable) {
        return repositoryPaciente.findAllByAtivoFalse(pageable)
                .map(PacienteMapper::toResponseDto);
    }

    public void reativar(Long id) {
        Paciente paciente = repositoryPaciente.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
        paciente.setAtivo(true);
        repositoryPaciente.save(paciente);
    }

    public Page<PacienteResponseDto> listarTodosPaginadoPorFuncionario(Pageable pageable, Long idFuncionario) {

        Funcionario funcionario = repositoryFuncionario.findById(idFuncionario)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        String especialidade = funcionario.getEspecialidade();

        if ("Admin".equalsIgnoreCase(especialidade) ||
                "Agendamento".equalsIgnoreCase(especialidade)) {

            return repositoryPaciente.findAllByAtivoTrue(pageable)
                    .map(PacienteMapper::toResponseDto);
        }

        return repositoryPaciente.findPacientesByFuncionario(pageable, idFuncionario)
                .map(PacienteMapper::toResponseDto);
    }

    public PacienteResponseDto listarPorId(Long id){
        Optional<Paciente> existePaciente = repositoryPaciente.findByIdAndAtivoTrue(id);

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
        entity.setAtivo(true);

        if (paciente.getFoto() != null && !paciente.getFoto().isEmpty()) {
            try {
                String nomeArquivo = s3Service.uploadImagemPaciente(paciente.getFoto(), paciente.getCpf());
                entity.setFoto(nomeArquivo);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao fazer upload da imagem: " + e.getMessage(), e);
            }
        }

        Paciente pacienteSalvo = repositoryPaciente.save(entity);
        criarFichaClinicaSeNecessario(pacienteSalvo);
        return PacienteMapper.toResponseDto(pacienteSalvo);
    }

    private void criarFichaClinicaSeNecessario(Paciente paciente) {
        if (paciente == null || paciente.getId() == null) {
            return;
        }
        if (fichaClinicaRepository.findByPacienteId(paciente.getId()).isPresent()) {
            return;
        }
        FichaClinica fichaClinica = new FichaClinica();
        fichaClinica.setPaciente(paciente);
        fichaClinicaRepository.save(fichaClinica);
    }

    // OK
    public void deletar(Long id){
        Paciente paciente = repositoryPaciente.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        paciente.setAtivo(false);
        repositoryPaciente.save(paciente);
    }

    // OK
    public PacienteResponseDto atualizar(PacienteRequestDto paciente, Long id){
        Optional<Paciente> existe = repositoryPaciente.findByIdAndAtivoTrue(id);

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

    public void atualizarFoto(String cpf, org.springframework.web.multipart.MultipartFile foto) throws IOException {
        Paciente paciente = repositoryPaciente.findByCpfAndAtivoTrue(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
        String nomeArquivo = s3Service.uploadImagemPaciente(foto, cpf);
        paciente.setFoto(nomeArquivo);
        repositoryPaciente.save(paciente);
    }

    public List<PacienteResponseDto> listarPorEmail(String email){
        List<Paciente> existeEmail = repositoryPaciente.findByEmailContainsIgnoreCaseAndAtivoTrue(email);



        if(existeEmail.isEmpty()){
            throw new ResourceNotFoundException("Email não existe");
        }
        return PacienteMapper.toResponseDto(existeEmail);
    }

    public List<PacienteResponseDto> buscarPorNome(String nome){
        Page<Paciente> pacientes = repositoryPaciente.findByNomeContainingIgnoreCaseAndAtivoTrue(
                nome, PageRequest.of(0, 10)
        );

        if (!pacientes.isEmpty()){
            return PacienteMapper.toResponseDto(pacientes.getContent());
        }else{
            throw new ResourceNotFoundException("Usuário não encontrado!");
        }
    }

    public List<PacienteResponseDto> buscar(String nome, String email, String cpf) {
        if (cpf != null && !cpf.isBlank()) {
            Page<Paciente> pacientes = repositoryPaciente.buscarPorCpfEAtivoTrue(cpf, PageRequest.of(0, 10));
            if (pacientes.isEmpty()) throw new ResourceNotFoundException("Paciente não encontrado!");
            return PacienteMapper.toResponseDto(pacientes.getContent());
        }
        if (email != null && !email.isBlank()) {
            Page<Paciente> pacientes = repositoryPaciente.findByEmailContainsIgnoreCaseAndAtivoTrue(
                    email, PageRequest.of(0, 10));
            if (pacientes.isEmpty()) throw new ResourceNotFoundException("Paciente não encontrado!");
            return PacienteMapper.toResponseDto(pacientes.getContent());
        }
        if (nome != null && !nome.isBlank()) {
            Page<Paciente> pacientes = repositoryPaciente.findByNomeContainingIgnoreCaseAndAtivoTrue(
                    nome, PageRequest.of(0, 10));
            if (pacientes.isEmpty()) throw new ResourceNotFoundException("Paciente não encontrado!");
            return PacienteMapper.toResponseDto(pacientes.getContent());
        }
        throw new ResourceNotFoundException("Informe ao menos um parâmetro de busca: nome, email ou cpf.");
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
