use careplus_novo;

-- endereco
INSERT INTO endereco (id, cep, logradouro, numero, complemento, bairro, cidade, estado) VALUES
(1,'01310-100','Avenida Paulista','1578','Apartamento 42','Bela Vista','São Paulo','SP'),
(2,'029891712','Rua das Flores','123','','Paulista','São Paulo','SP'),
(3,'03928090','logradouro_9b248d9dfa6f','numero_5bf79a305363','complemento_6a920f3c1e3c','bairro_ba3946537bb6','cidade_f01caf541f4c','estado_c09d4e91f03e');

-- paciente
INSERT INTO paciente (id, nome, email, cpf, telefone, dt_nascimento, convenio, data_inicio, foto, ativo) VALUES
(1,'Lucas Silva','lucas.silva@email.com','123.456.789-00','(11) 91234-5678','1990-05-15','Bradesco','2025-12-05', NULL, 1),
(2,'Mariana Costa','mariana.costa@email.com','987.654.321-00','(21) 99876-5432','1985-11-22','Bradesco Saúde','2025-12-05', NULL, 1),
(3,'Pedro Oliveira','pedro.oliveira@email.com','456.789.123-11','(31) 91234-8765','1978-02-10','Unimed','2025-12-05', NULL, 1),
(7,'Maria Santos','maria.santos@email.com','98765432100','11988887777','1980-05-15','Bradesco Saúde','2026-01-16', NULL, 1);

-- responsavel
INSERT INTO responsavel (id, nome, email, telefone, dt_nascimento, cpf, id_endereco) VALUES
(1,'Clara','email2@gmail.com','1140028922','2002-01-16','49927132810',3);

-- funcionario
-- funcionario
INSERT INTO funcionario (id, nome, email, senha, supervisor_id, cargo, especialidade, tipo_atendimento, telefone, documento, foto, ativo) VALUES
(1,'Dra. Helena Castro','helena.castro@clinica.com','$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC',NULL,'Supervisor(a)','Fonoaudiologia', 'TO', '11940028922', '40028922', 'fotoPerfil.png', 1),
(2,'Juliana Almeida','admin@clinica.com','$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC',1,'Funcionário','Admin', 'TO', '11940028922', '40028922', 'fotoPerfil.png', 1),
(3,'Marcos Ribeiro','marcos.ribeiro@clinica.com','$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC',2,'Estagiário','Fonoaudiologia', 'ABA', '11940028922', '40028922', 'fotoPerfil.png', 1),
(4,'Vitor Almeida','vitor.almeida@clinica.com','$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC',2,'Funcionário','Agendamento', 'ABA', '11940028922', '40028922', 'fotoPerfil.png', 1),
(5,'Ana Paula Ferreira','ana.ferreira@clinica.com','$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC',1,'Funcionário','Psicologia', 'ABA', '11941100001', '50001001', NULL, 1),
(6,'Beatriz Souza','beatriz.souza@clinica.com','$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC',1,'Funcionário','Terapia Ocupacional', 'TO', '11941100002', '50001002', NULL, 1),
(7,'Camila Rocha','camila.rocha@clinica.com','$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC',1,'Funcionário','Psicopedagogia', 'ABA', '11941100003', '50001003', NULL, 1),
(8,'Diego Martins','diego.martins@clinica.com','$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC',1,'Funcionário','Nutricionista', 'TO', '11941100004', '50001004', NULL, 1),
(9,'Fernanda Lima','fernanda.lima@clinica.com','$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC',1,'Funcionário','Fisioterapia', 'TO', '11941100005', '50001005', NULL, 1),
(10,'Gabriel Costa','gabriel.costa@clinica.com','$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC',1,'Funcionário','Psicomotricidade', 'ABA', '11941100006', '50001006', NULL, 1),
(11,'Isabela Nunes','isabela.nunes@clinica.com','$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC',1,'Funcionário','Musicoterapia', 'TO', '11941100007', '50001007', NULL, 1);

-- role
INSERT INTO role (id, nome) VALUES
(1,'ADMIN'),
(2,'USER'),
(3,'MANAGER'),
(4, 'SCHEDULER');

-- funcionario_roles
INSERT INTO funcionario_roles (funcionario_id, role_id) VALUES
(2,1),(3,2),(1,4),(4, 4),
(5,2),(6,2),(7,2),(8,2),(9,2),(10,2),(11,2);

-- fichaClinica
INSERT INTO fichaClinica (id, paciente_id, desfraldado, hiperfoco, anamnese, diagnostico, resumo_clinico, nivel_agressividade) VALUES
(1, 1, 1, 'Brinquedos', 'Histórico de atraso de fala', 'TEA leve', 'Paciente comunicativo com apoio', 2),
(2, 2, 1, 'Desenhos', 'Dificuldade de interação social', 'TEA moderado', 'Boa evolução clínica', 3),
(3, 3, 0, 'Movimentos repetitivos', 'Atraso cognitivo', 'TDAH', 'Necessita acompanhamento contínuo', 4),
(4, 7, 1, 'Música', 'Ansiedade frequente', 'Transtorno de Ansiedade', 'Responde bem à terapia', 1);

-- consulta_prontuario
INSERT INTO consulta_prontuario
(id, paciente_id, data, horario_inicio, horario_fim, tipo, observacoes_comportamentais, presenca, confirmada) VALUES
(1, 1, '2026-01-10', '09:00:00', '10:00:00', 'Avaliação Inicial', 'Paciente colaborativo', 1, 1),
(2, 2, '2026-01-12', '10:30:00', '11:30:00', 'Sessão Terapêutica', 'Paciente não compareceu', 0, 1),
(3, 3, '2026-02-05', '14:00:00', '15:00:00', 'Fonoaudiologia', NULL, NULL, 1),
(4, 7, '2026-02-10', '15:30:00', '16:30:00', 'Avaliação de Rotina', NULL, NULL, 0),
(5, 1, CURDATE(), '16:00:00', '17:00:00', 'Sessão Regular', NULL, NULL, 1);

-- consulta_funcionario (vínculo entre consulta e funcionário)
INSERT INTO consulta_funcionario (id, consulta_id, funcionario_id) VALUES
(1, 1, 2),  -- consulta 1 → funcionário 2 (Juliana)
(2, 2, 2),  -- consulta 2 → funcionário 2 (Juliana)
(3, 3, 3),  -- consulta 3 → funcionário 3 (Marcos)
(4, 4, 3),  -- consulta 4 → funcionário 3 (Marcos)
(5, 5, 2);  -- consulta 5 → funcionário 2 (Juliana)

-- classificacao_doencas
INSERT INTO classificacao_doencas (id, cid, dt_modificacao, prontuario_id) VALUES
(1, 'F84.0', '2026-01-10', 1),
(2, 'F84.1', '2026-01-12', 2),
(3, 'F90.0', '2026-01-15', 3),
(4, 'F41.1', '2026-01-16', 4);

-- medicacao
INSERT INTO medicacao (id_medicacao, nome_medicacao, data_inicio, data_fim, ativo, data_modificacao, prontuario_id) VALUES
(1, 'Risperidona', '2025-12-01', NULL, 1, NOW(), 1),
(2, 'Melatonina', '2025-11-10', '2026-01-10', 0, NOW(), 2),
(3, 'Metilfenidato', '2026-01-05', NULL, 1, NOW(), 3);

-- tratamento
INSERT INTO tratamento (id, tipo_de_tratamento, finalizado, data_modificacao, prontuario_id) VALUES
(1, 'Terapia Fonoaudiológica', 0, NOW(), 1),
(2, 'Terapia Cognitiva', 1, NOW(), 2),
(3, 'Estimulação Sensorial', 0, NOW(), 3),
(4, 'Acompanhamento Psicológico', 0, NOW(), 4);

-- cuidador
INSERT INTO cuidador (id, paciente_id, responsavel_id, parentesco) VALUES
(1, 1, 1, 'Mãe'),
(2, 2, 1, 'Tia'),
(3, 7, 1, 'Responsável Legal');


-- material
INSERT INTO material (id, item, data_implementacao, fk_consulta) VALUES
(1, 'Cartões de Comunicação', '2026-01-10', 1),
(2, 'Jogos Sensoriais', '2026-01-12', 2),
(3, 'Espelho Terapêutico', '2026-02-05', 3);

-- SELECT de todos os profissionais atralados as consultas
SELECT 
    c.id AS consulta_id,
    c.data,
    c.horario_inicio,
    c.horario_fim,
    GROUP_CONCAT(f.nome SEPARATOR ', ') AS funcionarios
FROM consulta_prontuario c
JOIN consulta_funcionario cf 
    ON c.id = cf.consulta_id
JOIN funcionario f 
    ON cf.funcionario_id = f.id
GROUP BY 
    c.id, c.data, c.horario_inicio, c.horario_fim;
