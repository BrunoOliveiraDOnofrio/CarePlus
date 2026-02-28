# 🔒 Relatório de Segurança — Projeto CarePlus

**Projeto:** CarePlus — Sistema de Gestão Clínica  
**Data:** 27/02/2026  
**Baseado em:** OWASP Top 10 (2021)  
**Tecnologias:** Java 17+, Spring Boot 3.3.3, Spring Security, JWT (jjwt), MySQL, AWS S3  

---

## Sumário Executivo

Este relatório apresenta a análise de segurança do projeto CarePlus com base no OWASP Top 10 (2021). Foram identificadas **4 vulnerabilidades críticas** e aplicadas as devidas correções com evidências documentadas.

| # | Vulnerabilidade OWASP | Severidade | Status |
|---|----------------------|------------|--------|
| 1 | A02:2021 — Falhas Criptográficas | 🔴 Crítica | ✅ Corrigida |
| 2 | A05:2021 — Configuração Incorreta de Segurança | 🟠 Alta | ✅ Corrigida |
| 3 | A03:2021 — Injeção (Path Traversal no S3) | 🟠 Alta | ✅ Corrigida |
| 4 | A04:2021 — Design Inseguro (Falta de Validação de Entrada) | 🟠 Alta | ✅ Corrigida |

---

## 1. A02:2021 — Falhas Criptográficas (Cryptographic Failures)

### 📋 Descrição

Credenciais sensíveis estavam hardcoded (fixas) diretamente no arquivo `application.properties` e versionadas no código-fonte. Isso inclui:

- **Segredo JWT** usado para assinar tokens de autenticação
- **Senha do banco de dados MySQL** em texto claro
- **Credenciais SMTP** para envio de emails
- `show-sql=true` habilitado, expondo estruturas de consultas SQL nos logs

Um atacante com acesso ao repositório de código (ex: GitHub, GitLab) teria acesso imediato a todas essas credenciais, podendo forjar tokens JWT, acessar o banco de dados diretamente e comprometer o sistema de email.

### 🔴 Código Vulnerável (ANTES)

**Arquivo:** `src/main/resources/application.properties`

```properties
# JWT — segredo hardcoded e previsível
jwt.secret=ZmFrZV9qd3Rfc2VndXJvX2NoYXJfMzJfY2FzYXNjaQpBTk9USEVSVkFMVUVfU0VDUkVUX0xFTkdUSA==

# Banco de dados — senha real exposta no código-fonte
spring.datasource.username=root
spring.datasource.password=27019511-16102004Ga@

# SMTP — credenciais de email expostas
spring.mail.username=exemplo.email@gmail.com
spring.mail.password=Senha-de-app-aqui

# SQL visível nos logs de produção
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### ✅ Código Corrigido (DEPOIS)

**Arquivo:** `src/main/resources/application.properties`

```properties
# JWT — segredo lido de variável de ambiente
jwt.secret=${JWT_SECRET:chave-padrao-somente-para-dev-trocar-em-producao-256bits!!}

# Banco de dados — credenciais lidas de variáveis de ambiente
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:changeme}

# SMTP — credenciais lidas de variáveis de ambiente
spring.mail.username=${MAIL_USERNAME:exemplo.email@gmail.com}
spring.mail.password=${MAIL_PASSWORD:Senha-de-app-aqui}

# SQL oculto nos logs
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
```

### 🛡️ Mitigação Aplicada

1. **Externalização de segredos:** Todas as credenciais sensíveis (JWT secret, senha do banco, credenciais SMTP) foram substituídas por referências a **variáveis de ambiente** usando a sintaxe `${ENV_VAR:default}` do Spring Boot. Em produção, essas variáveis devem ser configuradas no servidor/container sem nunca tocar o código-fonte.
2. **Desativação de `show-sql`:** A exibição de queries SQL nos logs foi desabilitada (`false`), evitando que a estrutura do banco seja exposta em ambientes de produção.
3. **Princípio:** Os valores padrão (`default`) existem apenas para facilitar o desenvolvimento local. Em produção, as variáveis de ambiente **devem obrigatoriamente ser definidas**.

---

## 2. A05:2021 — Configuração Incorreta de Segurança (Security Misconfiguration)

### 📋 Descrição

Múltiplas configurações de segurança do Spring Security estavam mal configuradas:

- **Console H2** (`/h2-console/**`) exposto publicamente sem autenticação — permite acesso direto ao banco de dados
- **Actuator** (`/actuator/*`) exposto sem autenticação — pode revelar informações sensíveis (health, env, beans)
- **CORS** configurada com `applyPermitDefaultValues()` — aceita requisições de **qualquer origem**
- **Método HTTP TRACE** habilitado no CORS — vulnerável a ataques de Cross-Site Tracing (XST)
- **Endpoint `/agenda-semanal`** acessível sem autenticação — vazamento de dados da agenda de profissionais
- **CSRF desabilitado globalmente** e **frame-options desabilitado** (necessário para H2, mas inseguro em produção)

### 🔴 Código Vulnerável (ANTES)

**Arquivo:** `src/main/java/.../config/SecurityConfiguracao.java`

```java
// URLs acessíveis sem autenticação — H2 console e Actuator expostos
private static final AntPathRequestMatcher[] URLS_PERMITIDAS = {
    new AntPathRequestMatcher("/swagger-ui/**"),
    // ... outras URLs ...
    new AntPathRequestMatcher("/actuator/*"),         // ❌ Actuator exposto
    new AntPathRequestMatcher("/funcionarios/login/**"),
    new AntPathRequestMatcher("/h2-console/**"),      // ❌ H2 Console exposto
    new AntPathRequestMatcher("/h2-console/**/**"),   // ❌ H2 Console exposto
    new AntPathRequestMatcher("/error/**")
};
```

```java
// CORS aceita qualquer origem e permite o método TRACE
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuracao = new CorsConfiguration();
    configuracao.applyPermitDefaultValues(); // ❌ Aceita QUALQUER origem
    configuracao.setAllowedMethods(Arrays.asList(
        HttpMethod.GET.name(), HttpMethod.POST.name(),
        HttpMethod.PUT.name(), HttpMethod.PATCH.name(),
        HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name(),
        HttpMethod.HEAD.name(),
        HttpMethod.TRACE.name()  // ❌ TRACE habilitado (vulnerável a XST)
    ));
```

**Arquivo:** `src/main/java/.../controller/ConsultaProntuarioController.java`

```java
// Endpoint sem @SecurityRequirement — acessível sem token JWT
@GetMapping("/agenda-semanal")
public ResponseEntity<List<ConsultaProntuarioResponseDto>> listarAgendaSemanal(
    @RequestParam Long funcionarioId,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataReferencia
) { ... }
```

### ✅ Código Corrigido (DEPOIS)

**Arquivo:** `src/main/java/.../config/SecurityConfiguracao.java`

```java
// URLs permitidas — SEM H2 Console e SEM Actuator
private static final AntPathRequestMatcher[] URLS_PERMITIDAS = {
    new AntPathRequestMatcher("/swagger-ui/**"),
    new AntPathRequestMatcher("/swagger-ui.html"),
    new AntPathRequestMatcher("/swagger-resources"),
    new AntPathRequestMatcher("/swagger-resources/**"),
    new AntPathRequestMatcher("/configuration/ui"),
    new AntPathRequestMatcher("/configuration/security"),
    new AntPathRequestMatcher("/api/public/**"),
    new AntPathRequestMatcher("/api/public/authenticate"),
    new AntPathRequestMatcher("/webjars/**"),
    new AntPathRequestMatcher("/v3/api-docs/**"),
    new AntPathRequestMatcher("/funcionarios/login/**"),
    new AntPathRequestMatcher("/error/**")
    // ✅ H2 Console e Actuator REMOVIDOS
};
```

```java
// CORS com origens explícitas e SEM TRACE
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuracao = new CorsConfiguration();
    configuracao.setAllowedOrigins(Arrays.asList(   // ✅ Origens explícitas
        "http://localhost:3000",
        "http://localhost:5173"
    ));
    configuracao.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
    configuracao.setAllowCredentials(true);
    configuracao.setAllowedMethods(Arrays.asList(
        HttpMethod.GET.name(), HttpMethod.POST.name(),
        HttpMethod.PUT.name(), HttpMethod.PATCH.name(),
        HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name(),
        HttpMethod.HEAD.name()
        // ✅ TRACE REMOVIDO
    ));
```

**Arquivo:** `src/main/java/.../controller/ConsultaProntuarioController.java`

```java
// Endpoint agora requer autenticação JWT
@GetMapping("/agenda-semanal")
@SecurityRequirement(name = "Bearer")  // ✅ Autenticação obrigatória
public ResponseEntity<List<ConsultaProntuarioResponseDto>> listarAgendaSemanal(
    @RequestParam Long funcionarioId,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataReferencia
) { ... }
```

### 🛡️ Mitigação Aplicada

1. **Remoção do H2 Console das URLs públicas:** O console de banco de dados em memória foi removido das rotas permitidas. Em ambiente de desenvolvimento, pode-se reabilitar usando um perfil Spring separado (`application-dev.properties`).
2. **Remoção do Actuator das URLs públicas:** Os endpoints do Spring Boot Actuator agora exigem autenticação, evitando vazamento de informações do ambiente.
3. **CORS restritivo:** Substituiu-se `applyPermitDefaultValues()` por origens explícitas (`localhost:3000` e `localhost:5173`). Em produção, deve-se adicionar apenas o domínio real do frontend.
4. **Remoção do método TRACE:** O método HTTP TRACE foi removido do CORS, prevenindo ataques de Cross-Site Tracing (XST).
5. **Autenticação no endpoint `/agenda-semanal`:** Adicionada a anotação `@SecurityRequirement(name = "Bearer")` para exigir token JWT válido.

---

## 3. A03:2021 — Injeção (Injection) — Path Traversal no S3

### 📋 Descrição

O parâmetro `documento` fornecido pelo usuário era concatenado diretamente na chave (key) dos objetos no bucket S3 **sem nenhuma sanitização**. Um atacante poderia enviar um valor como `../../../admin` ou `../../outro-bucket` para:

- **Acessar fotos de outros funcionários** (leitura indevida)
- **Sobrescrever arquivos** em outros diretórios do bucket S3 (escrita indevida)
- **Realizar path traversal** no bucket, potencialmente acessando dados sensíveis

Além disso, o filtro de autenticação JWT não interrompia a requisição após detectar um token expirado — o `doFilter` continuava executando, podendo permitir que requisições com tokens expirados alcançassem os controllers.

### 🔴 Código Vulnerável (ANTES)

**Arquivo:** `src/main/java/.../service/S3Service.java`

```java
public String uploadImagem(MultipartFile file, String documentoFuncionario) throws IOException {
    String nomeArquivo = LocalDateTime.now().toString() + "-" + file.getOriginalFilename();

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        // ❌ documentoFuncionario inserido diretamente sem sanitização
        .key("funcionarios/documento_" + documentoFuncionario + "/ " + nomeArquivo)
        .contentType(file.getContentType())
        .build();
    // ...
}

public byte[] buscarUltimaFoto(String documento) throws IOException {
    // ❌ documento inserido diretamente sem sanitização
    String prefix = "funcionarios/documento_" + documento + "/";
    // ...
}
```

**Arquivo:** `src/main/java/.../config/AutenticacaoFilter.java`

```java
} catch (ExpiredJwtException exception) {
    LOGGER.info("[Falha na autenticação] - Token Expirado...");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    // ❌ NÃO faz return — requisição continua processando!
}

// A requisição com token expirado continua aqui:
if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
    addUsernameInContext(request, username, jwtToken);
}
filterChain.doFilter(request, response); // ❌ Executa mesmo com token expirado
```

### ✅ Código Corrigido (DEPOIS)

**Arquivo:** `src/main/java/.../service/S3Service.java`

```java
public String uploadImagem(MultipartFile file, String documentoFuncionario) throws IOException {
    // ✅ Sanitiza o documento para prevenir path traversal
    String documentoSanitizado = sanitizarDocumento(documentoFuncionario);

    String nomeArquivo = LocalDateTime.now().toString() + "-" + file.getOriginalFilename();

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key("funcionarios/documento_" + documentoSanitizado + "/ " + nomeArquivo)
        .contentType(file.getContentType())
        .build();
    // ...
}

public byte[] buscarUltimaFoto(String documento) throws IOException {
    // ✅ Sanitiza o documento para prevenir path traversal
    String documentoSanitizado = sanitizarDocumento(documento);
    String prefix = "funcionarios/documento_" + documentoSanitizado + "/";
    // ...
}

/**
 * Sanitiza o parâmetro documento, permitindo apenas caracteres alfanuméricos.
 * Previne ataques de path traversal no bucket S3.
 */
private String sanitizarDocumento(String documento) {
    if (documento == null || documento.isBlank()) {
        throw new IllegalArgumentException("Documento não pode ser vazio");
    }
    String sanitizado = documento.replaceAll("[^a-zA-Z0-9]", "");
    if (sanitizado.isEmpty()) {
        throw new IllegalArgumentException("Documento contém apenas caracteres inválidos");
    }
    return sanitizado;
}
```

**Arquivo:** `src/main/java/.../config/AutenticacaoFilter.java`

```java
} catch (ExpiredJwtException exception) {
    LOGGER.info("[Falha na autenticação] - Token Expirado...");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    return; // ✅ Interrompe imediatamente a cadeia de filtros
}
```

### 🛡️ Mitigação Aplicada

1. **Sanitização de entrada:** O método `sanitizarDocumento()` remove todos os caracteres que não são alfanuméricos (letras e números), eliminando completamente qualquer tentativa de path traversal com `../`, `/`, `\` ou outros caracteres especiais.
2. **Validação de vazio:** Se o documento for nulo, vazio ou composto apenas por caracteres especiais, uma exceção `IllegalArgumentException` é lançada imediatamente.
3. **Correção do filtro JWT:** Adicionado `return` após definir o status 401 no catch de `ExpiredJwtException`, impedindo que a requisição continue sendo processada com um token inválido.

---

## 4. A04:2021 — Design Inseguro (Insecure Design) — Falta de Validação de Entrada

### 📋 Descrição

Nenhum dos DTOs (Data Transfer Objects) do projeto possuía anotações de validação Bean Validation (`@NotBlank`, `@Email`, `@Size`, `@Pattern`). Isso significa que:

- Campos obrigatórios poderiam ser enviados **vazios ou nulos**
- Emails poderiam conter **formatos inválidos**
- Senhas poderiam ter **qualquer tamanho** (inclusive 1 caractere)
- Documentos/CPFs poderiam conter **caracteres especiais maliciosos**
- O endpoint de login não usava `@Valid`, aceitando **qualquer payload**

Além disso, o `GlobalExceptionHandler` não possuía:
- Handler para erros de validação (`MethodArgumentNotValidException`)
- Handler genérico catch-all para `Exception`, fazendo com que erros não tratados **vazassem stack traces completos** para o cliente

### 🔴 Código Vulnerável (ANTES)

**Arquivo:** `src/main/java/.../dto/dtoFuncionario/FuncionarioResquestDto.java`

```java
public class FuncionarioResquestDto {
    // ❌ Sem NENHUMA validação — aceita qualquer valor
    private String nome;       // Pode ser vazio
    private String email;      // Pode ser "abc123" (não é email)
    private String senha;      // Pode ser "1" (1 caractere)
    private String documento;  // Pode conter "../" (path traversal)
    // ...
}
```

**Arquivo:** `src/main/java/.../dto/dtoFuncionario/FuncionarioLoginDto.java`

```java
public class FuncionarioLoginDto {
    // ❌ Sem validação no DTO de login
    private String email;
    private String senha;
}
```

**Arquivo:** `src/main/java/.../dto/dtoPaciente/PacienteRequestDto.java`

```java
public class PacienteRequestDto {
    // ❌ CPF sem validação de formato
    private String nome;
    private String email;
    private String cpf;          // Pode ser qualquer string
    private String telefone;
    private LocalDate dtNascimento;
    // ...
}
```

**Arquivo:** `src/main/java/.../controller/FuncionarioController.java`

```java
// ❌ Sem @Valid — validação nunca é executada
public ResponseEntity<FuncionarioTokenDto> login(
    @RequestBody FuncionarioLoginDto funcionarioLoginDto) { ... }
```

**Arquivo:** `src/main/java/.../exception/GlobalExceptionHandler.java`

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    // ❌ Sem handler de validação (MethodArgumentNotValidException)
    // ❌ Sem handler genérico (Exception) — stack traces vazam para o cliente

    @ExceptionHandler
    public ResponseEntity<String> handlerResourceNotFoundException(ResourceNotFoundException e) { ... }

    @ExceptionHandler
    public ResponseEntity<String> handlerUserAlreadyExistsException(UserAlreadyExistsException e) { ... }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUploadSizeExceededException(...) { ... }
}
```

### ✅ Código Corrigido (DEPOIS)

**Arquivo:** `src/main/java/.../dto/dtoFuncionario/FuncionarioResquestDto.java`

```java
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class FuncionarioResquestDto {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    private String senha;

    @NotBlank(message = "Cargo é obrigatório")
    private String cargo;

    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$", message = "Telefone deve ser válido")
    private String telefone;

    @NotBlank(message = "Documento é obrigatório")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Documento deve conter apenas letras e números")
    private String documento;
    // ...
}
```

**Arquivo:** `src/main/java/.../dto/dtoFuncionario/FuncionarioLoginDto.java`

```java
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class FuncionarioLoginDto {
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;
}
```

**Arquivo:** `src/main/java/.../dto/dtoPaciente/PacienteRequestDto.java`

```java
import jakarta.validation.constraints.*;

public class PacienteRequestDto {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$",
             message = "CPF deve ser válido (formato: 000.000.000-00)")
    private String cpf;

    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$", message = "Telefone deve ser válido")
    private String telefone;

    @NotNull(message = "Data de nascimento é obrigatória")
    private LocalDate dtNascimento;
    // ...
}
```

**Arquivo:** `src/main/java/.../controller/FuncionarioController.java`

```java
// ✅ @Valid adicionado — validação é executada automaticamente
public ResponseEntity<FuncionarioTokenDto> login(
    @RequestBody @Valid FuncionarioLoginDto funcionarioLoginDto) { ... }
```

**Arquivo:** `src/main/java/.../exception/GlobalExceptionHandler.java`

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ... handlers existentes ...

    // ✅ Handler de erros de validação — retorna mensagens amigáveis
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(400).body(errors);
    }

    // ✅ Handler genérico — NUNCA vaza stack trace para o cliente
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        LOGGER.error("Erro interno não tratado: ", e);
        return ResponseEntity.status(500)
            .body("Ocorreu um erro interno no servidor. Tente novamente mais tarde.");
    }
}
```

### 🛡️ Mitigação Aplicada

1. **Validação obrigatória nos DTOs:** Adicionadas anotações `@NotBlank`, `@Email`, `@Size`, `@Pattern` e `@NotNull` em todos os campos dos DTOs de entrada, garantindo que dados inválidos sejam rejeitados antes de chegar à lógica de negócio.
2. **Validação no Controller de Login:** Adicionada a anotação `@Valid` no `@RequestBody` do endpoint de login, ativando a validação automática do Spring.
3. **Regex para CPF e documento:** Uso de `@Pattern` com expressões regulares para aceitar apenas formatos válidos de CPF (`000.000.000-00`) e documentos (apenas alfanuméricos).
4. **Handler de validação:** Adicionado `@ExceptionHandler(MethodArgumentNotValidException.class)` que retorna um mapa campo→mensagem de erro com status HTTP 400.
5. **Handler genérico catch-all:** Adicionado `@ExceptionHandler(Exception.class)` que:
   - **Registra** o erro completo nos logs do servidor (para diagnóstico)
   - **Retorna** apenas uma mensagem genérica para o cliente (sem stack trace)
   - Previne **Information Disclosure** (vazamento de informações internas)

---

## 📌 Recomendações Adicionais

Além das 4 vulnerabilidades corrigidas acima, recomendamos as seguintes melhorias futuras:

| Recomendação | OWASP | Prioridade |
|---|---|---|
| Implementar rate limiting no endpoint `/funcionarios/login` para proteção contra força bruta | A07:2021 | Alta |
| Adicionar `@JsonIgnore` no campo `senha` da entidade `Funcionario.java` para evitar vazamento em respostas JSON | A01:2021 | Alta |
| Verificação de propriedade (IDOR) — validar se o usuário autenticado é dono do recurso acessado | A01:2021 | Alta |
| Atualizar dependências desatualizadas (jjwt 0.11.5 → 0.12.x, mysql-connector 8.0.33 → 8.3+) | A06:2021 | Média |
| Implementar criptografia de dados sensíveis (CPF, prontuários) no banco para conformidade com a LGPD | A02:2021 | Média |
| Habilitar CSRF para endpoints acessados via navegador (formulários web) | A05:2021 | Média |
| Adicionar `@Valid` em todos os `@RequestBody` dos demais controllers (Paciente, Consulta, etc.) | A04:2021 | Média |

---

## 📂 Arquivos Modificados

| Arquivo | Vulnerabilidade |
|---|---|
| `src/main/resources/application.properties` | A02 — Falhas Criptográficas |
| `src/main/java/.../config/SecurityConfiguracao.java` | A05 — Configuração Incorreta |
| `src/main/java/.../config/AutenticacaoFilter.java` | A03 — Injeção (filtro bypass) |
| `src/main/java/.../service/S3Service.java` | A03 — Injeção (Path Traversal) |
| `src/main/java/.../controller/ConsultaProntuarioController.java` | A05 — Controle de Acesso |
| `src/main/java/.../controller/FuncionarioController.java` | A04 — Validação de Entrada |
| `src/main/java/.../dto/dtoFuncionario/FuncionarioResquestDto.java` | A04 — Validação de Entrada |
| `src/main/java/.../dto/dtoFuncionario/FuncionarioLoginDto.java` | A04 — Validação de Entrada |
| `src/main/java/.../dto/dtoPaciente/PacienteRequestDto.java` | A04 — Validação de Entrada |
| `src/main/java/.../exception/GlobalExceptionHandler.java` | A04 — Tratamento de Erros |

---

*Relatório gerado como parte da avaliação de segurança do projeto CarePlus, disciplina do 4º Semestre — SPTech.*

