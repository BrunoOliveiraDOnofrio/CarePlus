package com.example.careplus.service;

import com.example.careplus.dto.dtoEndereco.EnderecoMapper;
import com.example.careplus.dto.dtoEndereco.EnderecoRequestDto;
import com.example.careplus.dto.dtoEndereco.EnderecoResponseDto;
import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.model.Endereco;
import com.example.careplus.repository.EnderecoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    private final EnderecoRepository repository;

    public EnderecoService(EnderecoRepository repository) {
        this.repository = repository;
    }

    public List<EnderecoResponseDto> listarTodos() {
        List<Endereco> enderecos = repository.findAll();
        return EnderecoMapper.toResponseDto(enderecos);
    }

    public EnderecoResponseDto listarPorId(Long id) {
        Optional<Endereco> endereco = repository.findById(id);

        if (endereco.isPresent()) {
            return EnderecoMapper.toResponseDto(endereco.get());
        }

        throw new ResourceNotFoundException("Endereço não encontrado!");
    }

    public EnderecoResponseDto salvar(EnderecoRequestDto enderecoDto) {
        Endereco entity = EnderecoMapper.toEntity(enderecoDto);
        Endereco enderecoSalvo = repository.save(entity);
        return EnderecoMapper.toResponseDto(enderecoSalvo);
    }

    public void deletar(Long id) {
        boolean existe = repository.existsById(id);

        if (!existe) {
            throw new ResourceNotFoundException("Endereço não encontrado");
        }

        repository.deleteById(id);
    }

    public EnderecoResponseDto atualizar(EnderecoRequestDto enderecoDto, Long id) {
        Optional<Endereco> enderecoExistente = repository.findById(id);

        if (enderecoExistente.isEmpty()) {
            throw new ResourceNotFoundException("Endereço não encontrado");
        }

        Endereco endereco = enderecoExistente.get();
        endereco.setCep(enderecoDto.getCep());
        endereco.setLogradouro(enderecoDto.getLogradouro());
        endereco.setNumero(enderecoDto.getNumero());
        endereco.setComplemento(enderecoDto.getComplemento());
        endereco.setBairro(enderecoDto.getBairro());
        endereco.setCidade(enderecoDto.getCidade());
        endereco.setEstado(enderecoDto.getEstado());

        Endereco enderecoAtualizado = repository.save(endereco);
        return EnderecoMapper.toResponseDto(enderecoAtualizado);
    }

    public List<EnderecoResponseDto> listarPorCidade(String cidade) {
        List<Endereco> enderecos = repository.findAll()
                .stream()
                .filter(e -> e.getCidade() != null && e.getCidade().equalsIgnoreCase(cidade))
                .toList();

        if (enderecos.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum endereço encontrado para a cidade: " + cidade);
        }

        return EnderecoMapper.toResponseDto(enderecos);
    }

    public List<EnderecoResponseDto> listarPorEstado(String estado) {
        List<Endereco> enderecos = repository.findAll()
                .stream()
                .filter(e -> e.getEstado() != null && e.getEstado().equalsIgnoreCase(estado))
                .toList();

        if (enderecos.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum endereço encontrado para o estado: " + estado);
        }

        return EnderecoMapper.toResponseDto(enderecos);
    }

    public EnderecoResponseDto buscarPorCep(String cep) {
        List<Endereco> enderecos = repository.findAll()
                .stream()
                .filter(e -> e.getCep() != null && e.getCep().equals(cep))
                .toList();

        if (enderecos.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum endereço encontrado para o CEP: " + cep);
        }

        return EnderecoMapper.toResponseDto(enderecos.get(0));
    }
}

