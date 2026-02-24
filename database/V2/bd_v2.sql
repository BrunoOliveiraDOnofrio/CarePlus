CREATE DATABASE IF NOT EXISTS careplus_novo;
USE careplus_novo;

-- =========================
-- TABELA ENDERECO
-- =========================
DROP TABLE IF EXISTS endereco;

CREATE TABLE endereco (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cep VARCHAR(255),
    logradouro VARCHAR(255),
    numero VARCHAR(255),
    complemento VARCHAR(255),
    bairro VARCHAR(255),
    cidade VARCHAR(255),
    estado VARCHAR(255)
);

-- =========================
-- TABELA PACIENTE
-- =========================
DROP TABLE IF EXISTS paciente;

CREATE TABLE paciente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255),
    email VARCHAR(255),
    cpf VARCHAR(255),
    telefone VARCHAR(255),
    dt_nascimento DATE,
    convenio VARCHAR(255),
    data_inicio DATE
);

-- =========================
-- TABELA RESPONSAVEL
-- =========================
DROP TABLE IF EXISTS responsavel;

CREATE TABLE responsavel (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255),
    email VARCHAR(255),
    telefone VARCHAR(255),
    dt_nascimento DATE,
    cpf VARCHAR(255),
    id_endereco INT,
    CONSTRAINT fk_responsavel_endereco
        FOREIGN KEY (id_endereco)
        REFERENCES endereco(id)
);

-- =========================
-- TABELA FUNCIONARIO
-- =========================
DROP TABLE IF EXISTS funcionario;

CREATE TABLE funcionario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255),
    email VARCHAR(255),
    senha VARCHAR(255),
    supervisor_id BIGINT,
    cargo VARCHAR(255),
    especialidade VARCHAR(255),
    tipo_atendimento VARCHAR(45),
    telefone VARCHAR(45),
    documento VARCHAR(45),
    CONSTRAINT fk_funcionario_supervisor
        FOREIGN KEY (supervisor_id)
        REFERENCES funcionario(id)
);

-- =========================
-- TABELA ROLE
-- =========================
DROP TABLE IF EXISTS role;

CREATE TABLE role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255)
);

-- =========================
-- TABELA FUNCIONARIO_ROLES (N:N)
-- =========================
DROP TABLE IF EXISTS funcionario_roles;

CREATE TABLE funcionario_roles (
    funcionario_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (funcionario_id, role_id),
    CONSTRAINT fk_fr_funcionario
        FOREIGN KEY (funcionario_id)
        REFERENCES funcionario(id),
    CONSTRAINT fk_fr_role
        FOREIGN KEY (role_id)
        REFERENCES role(id)
);

-- =========================
-- TABELA CONSULTA_PRONTUARIO
-- =========================
DROP TABLE IF EXISTS consulta_prontuario;

CREATE TABLE consulta_prontuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    funcionario_id BIGINT,
    paciente_id BIGINT,
    data_hora DATETIME,
    tipo VARCHAR(255),
    observacoes_comportamentais VARCHAR(255),
    presenca TINYINT,
    confirmada TINYINT,
    CONSTRAINT fk_consulta_funcionario
        FOREIGN KEY (funcionario_id)
        REFERENCES funcionario(id),
    CONSTRAINT fk_consulta_paciente
        FOREIGN KEY (paciente_id)
        REFERENCES paciente(id)
);

-- =========================
-- TABELA FICHA CLINICA
-- =========================
DROP TABLE IF EXISTS fichaclinica;

CREATE TABLE fichaclinica (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT,
    desfraldado TINYINT,
    hiperfoco VARCHAR(255),
    anamnese VARCHAR(255),
    diagnostico VARCHAR(255),
    resumo_clinico VARCHAR(255),
    nivel_agressividade INT,
    CONSTRAINT fk_prontuario_paciente
        FOREIGN KEY (paciente_id)
        REFERENCES paciente(id)
);

-- =========================
-- TABELA CLASSIFICACAO_DOENCAS
-- =========================
DROP TABLE IF EXISTS classificacao_doencas;

CREATE TABLE classificacao_doencas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cid VARCHAR(255),
    dt_modificacao DATE,
    prontuario_id BIGINT,
    CONSTRAINT fk_classificacao_prontuario
        FOREIGN KEY (prontuario_id)
        REFERENCES fichaClinica(id)
);

-- =========================
-- TABELA MEDICACAO
-- =========================
DROP TABLE IF EXISTS medicacao;

CREATE TABLE medicacao (
    id_medicacao BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_medicacao VARCHAR(255),
    data_inicio DATE,
    data_fim DATE,
    ativo TINYINT,
    data_modificacao DATETIME,
    prontuario_id BIGINT,
    CONSTRAINT fk_medicacao_prontuario
        FOREIGN KEY (prontuario_id)
        REFERENCES fichaClinica(id)
);

-- =========================
-- TABELA TRATAMENTO
-- =========================
DROP TABLE IF EXISTS tratamento;

CREATE TABLE tratamento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_de_tratamento VARCHAR(255),
    finalizado TINYINT,
    data_modificacao DATETIME,
    prontuario_id BIGINT,
    CONSTRAINT fk_tratamento_prontuario
        FOREIGN KEY (prontuario_id)
        REFERENCES fichaClinica(id)
);

-- =========================
-- TABELA CUIDADOR
-- =========================
DROP TABLE IF EXISTS cuidador;

CREATE TABLE cuidador (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT,
    responsavel_id BIGINT,
    parentesco VARCHAR(255),
    CONSTRAINT fk_cuidador_paciente
        FOREIGN KEY (paciente_id)
        REFERENCES paciente(id),
    CONSTRAINT fk_cuidador_responsavel
        FOREIGN KEY (responsavel_id)
        REFERENCES responsavel(id)
);

-- =========================
-- TABELA MATERIAL
-- =========================
DROP TABLE IF EXISTS material;

CREATE TABLE material (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item VARCHAR(255),
    data_implementacao DATE,
    fk_consulta BIGINT,
    CONSTRAINT fk_material_consulta
        FOREIGN KEY (fk_consulta)
        REFERENCES consulta_prontuario(id)
);
