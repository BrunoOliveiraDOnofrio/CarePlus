# CarePlus — Backend

API REST do sistema CarePlus, desenvolvida em Spring Boot para gerenciamento de clínicas especializadas no atendimento de pacientes com TEA (Transtorno do Espectro Autista).

---

## Sobre o Projeto

O CarePlus centraliza o fluxo clínico de uma clínica multidisciplinar: agendamento de consultas, prontuários eletrônicos, controle de tratamentos e medicações, notificações automáticas via e-mail e armazenamento de registros na nuvem. A API é consumida exclusivamente pelo frontend React e exposta via Swagger para fins de desenvolvimento e testes.

---

## Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.3.3 | Framework base |
| Spring Security + JJWT | — | Autenticação JWT |
| Spring Data JPA | — | Persistência |
| MySQL | — | Banco de dados principal |
| H2 | — | Banco em memória para testes |
| Spring AMQP (RabbitMQ) | — | Mensageria assíncrona |
| AWS SDK S3 | 2.32.21 | Armazenamento de arquivos |
| Spring Mail + Thymeleaf | — | Envio de e-mails HTML |
| SpringDoc OpenAPI | 2.6.0 | Documentação Swagger |
| Lombok | — | Redução de boilerplate |
| Maven | — | Gerenciamento de dependências |

---

## Arquitetura

```
careplus/
├── controller/        # Endpoints REST
├── service/           # Regras de negócio
├── repository/        # Queries JPA (JPQL)
├── model/             # Entidades JPA
├── dto/               # Request e Response DTOs
├── config/            # Segurança, RabbitMQ, AWS, CORS
├── infra/             # Filtros JWT, tratamento de erros
└── messaging/         # Produtores/consumidores RabbitMQ
```

A aplicação segue o padrão MVC com separação clara entre camadas. Os repositórios utilizam JPQL nativo com `@Query` para consultas mais complexas (filtros por data/hora, profissional e confirmação).

---

## Domínios

| Domínio | Descrição |
|---|---|
| **Paciente** | Cadastro e dados pessoais dos pacientes |
| **Funcionário** | Profissionais de saúde com especialidade e cargo |
| **ConsultaProntuario** | Consultas agendadas com data, horário, tipo e status de confirmação |
| **FichaClinica** | Prontuário: diagnóstico, anamnese, hiperfoco, nível de agressividade |
| **Tratamento** | Tratamentos vinculados à ficha clínica do paciente |
| **Medicação** | Medicamentos com período de uso e status ativo |
| **Cuidador / Responsável** | Contato do cuidador e responsável legal |
| **Material** | Materiais utilizados em cada consulta |
| **Classificação de Doenças** | CIDs vinculados ao prontuário |
| **Relatório** | Geração e armazenamento de relatórios em S3 |
| **Endereço** | Dados de endereço de pacientes |

---

## Autenticação e Perfis

A autenticação é baseada em JWT. Cada token carrega os roles do usuário:

| Role | Perfil | Acesso |
|---|---|---|
| `USER` | Profissional | Agenda própria, fichas clínicas, iniciar e concluir consultas |
| `ADMIN` | Administrador | Acesso total, gestão de funcionários e relatórios |
| `SCHEDULER` | Agendamento | Criação e edição de consultas, visualização de agenda |

---

## Principais Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/consultas-prontuario/proxima` | Próxima consulta não concluída do paciente |
| `GET` | `/consultas-prontuario/ultimas-consultas` | Consultas concluídas (paginadas) por profissional e paciente |
| `GET` | `/consultas-prontuario/detalhes` | Detalhes da consulta atual |
| `GET` | `/consultas-prontuario/detalhes-anterior` | Detalhes de consulta passada |
| `GET` | `/consultas-prontuario/agenda-diaria` | Agenda do dia por profissional ou paciente |
| `GET` | `/consultas-prontuario/agenda-semanal` | Agenda semanal |
| `GET` | `/consultas-prontuario/agenda-mensal` | Agenda mensal |
| `PUT` | `/consultas-prontuario/confirmar` | Confirma uma consulta (`confirmada = true`) |
| `PUT` | `/consultas-prontuario/recusar` | Recusa uma consulta (`confirmada = false`) |
| `PUT` | `/consultas-prontuario/realizarObservacoes` | Conclui a consulta com observações |
| `GET` | `/detalhes-pacientes/detalhes-completo` | Ficha clínica completa do paciente |
| `GET` | `/consultas-prontuario/pendentes` | Consultas pendentes de confirmação por profissional |

A documentação completa está disponível em `/swagger-ui/index.html` com a aplicação em execução.

---

## Campo `confirmada`

O campo `confirmada` (Boolean) em `ConsultaProntuario` controla o ciclo de vida da consulta:

| Valor | Significado |
|---|---|
| `null` | Consulta criada, aguardando resposta do profissional |
| `true` | Consulta confirmada ou concluída |
| `false` | Consulta recusada pelo profissional |

A tela de **Últimas Consultas** exibe somente registros com `confirmada = true`. A **Próxima Consulta** exibe o próximo agendamento com `confirmada IS NULL OR confirmada = false`.

---

## Como Executar

### Pré-requisitos

- Java 21+
- Maven
- MySQL em execução
- RabbitMQ em execução (opcional para funcionalidades de notificação)
- Conta AWS com bucket S3 configurado (opcional para relatórios)

### Configuração

Edite `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/careplus
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672

aws.s3.bucket=nome-do-bucket
aws.region=sa-east-1
```

### Executando

```bash
./mvnw spring-boot:run
```

Windows:
```bash
mvnw.cmd spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

---

## Equipe

Projeto acadêmico desenvolvido por alunos do 4º semestre da SPTech.
