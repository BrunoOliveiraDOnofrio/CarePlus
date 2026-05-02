package com.example.careplus.dto.dtoFuncionario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class PerfilFuncionarioRequestDto {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$", message = "Telefone deve ser válido")
    private String telefone;

    private String tipoAtendimento;

    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    private String senha;

    private MultipartFile foto;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getTipoAtendimento() { return tipoAtendimento; }
    public void setTipoAtendimento(String tipoAtendimento) { this.tipoAtendimento = tipoAtendimento; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public MultipartFile getFoto() { return foto; }
    public void setFoto(MultipartFile foto) { this.foto = foto; }
}
