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
(7,'Maria Santos','maria.santos@email.com','98765432100','11988887777','1980-05-15','Bradesco Saúde','2026-01-16', NULL, 1),

(8,'João Pereira','joao.pereira@email.com','11122233344','11990000001','1992-03-12','Unimed','2026-02-01',NULL,1),
(9,'Carla Mendes','carla.mendes@email.com','11122233345','11990000002','1988-07-25','Bradesco Saúde','2026-02-01',NULL,1),
(10,'Rafael Gomes','rafael.gomes@email.com','11122233346','11990000003','1995-09-10','SulAmérica','2026-02-01',NULL,1),
(11,'Patrícia Lima','patricia.lima@email.com','11122233347','11990000004','1982-01-30','Unimed','2026-02-01',NULL,1),
(12,'Bruno Rocha','bruno.rocha@email.com','11122233348','11990000005','2000-12-05','Particular','2026-02-01',NULL,1),
(13,'Fernanda Alves','fernanda.alves@email.com','11122233349','11990000006','1993-06-18','Amil','2026-02-01',NULL,1),
(14,'Diego Barros','diego.barros@email.com','11122233350','11990000007','1987-04-22','Unimed','2026-02-01',NULL,1),
(15,'Juliana Duarte','juliana.duarte@email.com','11122233351','11990000008','1991-11-11','Bradesco Saúde','2026-02-01',NULL,1),
(16,'Lucas Martins','lucas.martins@email.com','11122233352','11990000009','1998-08-09','SulAmérica','2026-02-01',NULL,1),
(17,'Amanda Freitas','amanda.freitas@email.com','11122233353','11990000010','1996-02-14','Amil','2026-02-01',NULL,1),

(18,'Gustavo Ribeiro','gustavo.ribeiro@email.com','11122233354','11990000011','1989-05-20','Unimed','2026-02-01',NULL,1),
(19,'Beatriz Carvalho','beatriz.carvalho@email.com','11122233355','11990000012','2001-07-03','Particular','2026-02-01',NULL,1),
(20,'Thiago Nogueira','thiago.nogueira@email.com','11122233356','11990000013','1994-10-27','Amil','2026-02-01',NULL,1),
(21,'Larissa Teixeira','larissa.teixeira@email.com','11122233357','11990000014','1997-03-08','Bradesco Saúde','2026-02-01',NULL,1),
(22,'Felipe Batista','felipe.batista@email.com','11122233358','11990000015','1986-09-15','Unimed','2026-02-01',NULL,1),
(23,'Camila Azevedo','camila.azevedo@email.com','11122233359','11990000016','1999-12-01','SulAmérica','2026-02-01',NULL,1),
(24,'Eduardo Pires','eduardo.pires@email.com','11122233360','11990000017','1990-06-06','Amil','2026-02-01',NULL,1),
(25,'Renata Correia','renata.correia@email.com','11122233361','11990000018','1983-11-19','Bradesco Saúde','2026-02-01',NULL,1),
(26,'Vinícius Farias','vinicius.farias@email.com','11122233362','11990000019','2002-01-01','Unimed','2026-02-01',NULL,1),
(27,'Tatiane Moura','tatiane.moura@email.com','11122233363','11990000020','1995-05-05','Particular','2026-02-01',NULL,1),

(28,'Rodrigo Cardoso','rodrigo.cardoso@email.com','11122233364','11990000021','1984-08-18','Amil','2026-02-01',NULL,1),
(29,'Aline Barbosa','aline.barbosa@email.com','11122233365','11990000022','1993-02-22','SulAmérica','2026-02-01',NULL,1),
(30,'Daniel Tavares','daniel.tavares@email.com','11122233366','11990000023','1991-07-07','Unimed','2026-02-01',NULL,1),
(31,'Priscila Lopes','priscila.lopes@email.com','11122233367','11990000024','1987-12-30','Bradesco Saúde','2026-02-01',NULL,1),
(32,'André Castro','andre.castro@email.com','11122233368','11990000025','1996-09-09','Amil','2026-02-01',NULL,1),
(33,'Simone Rezende','simone.rezende@email.com','11122233369','11990000026','1985-04-14','Unimed','2026-02-01',NULL,1),
(34,'Leandro Peixoto','leandro.peixoto@email.com','11122233370','11990000027','1998-10-10','SulAmérica','2026-02-01',NULL,1),
(35,'Vanessa Guedes','vanessa.guedes@email.com','11122233371','11990000028','1992-01-17','Bradesco Saúde','2026-02-01',NULL,1),
(36,'Paulo Queiroz','paulo.queiroz@email.com','11122233372','11990000029','1980-06-25','Particular','2026-02-01',NULL,1),
(37,'Cláudia Santana','claudia.santana@email.com','11122233373','11990000030','1994-03-03','Amil','2026-02-01',NULL,1),

(38,'Ricardo Neves','ricardo.neves@email.com','11122233374','11990000031','1988-11-11','Unimed','2026-02-01',NULL,1),
(39,'Débora Monteiro','debora.monteiro@email.com','11122233375','11990000032','1997-08-08','SulAmérica','2026-02-01',NULL,1),
(40,'Marcelo Dantas','marcelo.dantas@email.com','11122233376','11990000033','1991-02-02','Bradesco Saúde','2026-02-01',NULL,1),
(41,'Luciana Prado','luciana.prado@email.com','11122233377','11990000034','1983-07-21','Unimed','2026-02-01',NULL,1),
(42,'Sérgio Pacheco','sergio.pacheco@email.com','11122233378','11990000035','1990-05-29','Amil','2026-02-01',NULL,1),
(43,'Elaine Borges','elaine.borges@email.com','11122233379','11990000036','1996-12-12','Particular','2026-02-01',NULL,1),
(44,'Fábio Macedo','fabio.macedo@email.com','11122233380','11990000037','1985-09-09','SulAmérica','2026-02-01',NULL,1),
(45,'Kelly Ramos','kelly.ramos@email.com','11122233381','11990000038','1993-06-06','Bradesco Saúde','2026-02-01',NULL,1),
(46,'Igor Medeiros','igor.medeiros@email.com','11122233382','11990000039','2000-01-15','Unimed','2026-02-01',NULL,1),
(47,'Natália Figueiredo','natalia.figueiredo@email.com','11122233383','11990000040','1998-04-04','Amil','2026-02-01',NULL,1),
(48,'Maria Oliveira','maria.oliveira@email.com','22233344401','11990000041','1992-02-02','Unimed','2026-02-10',NULL,1),
(49,'Maria Souza','maria.souza@email.com','22233344402','11990000042','1989-05-10','Bradesco Saúde','2026-02-10',NULL,1),
(50,'Maria Pereira','maria.pereira@email.com','22233344403','11990000043','1995-08-18','Amil','2026-02-10',NULL,1),

(51,'João Silva','joao.silva@email.com','22233344404','11990000044','1990-01-01','Unimed','2026-02-10',NULL,1),
(52,'João Santos','joao.santos@email.com','22233344405','11990000045','1987-03-22','SulAmérica','2026-02-10',NULL,1),
(53,'João Ferreira','joao.ferreira@email.com','22233344406','11990000046','1993-07-30','Particular','2026-02-10',NULL,1),

(54,'Ana Lima','ana.lima@email.com','22233344407','11990000047','1998-11-11','Amil','2026-02-10',NULL,1),
(55,'Ana Rocha','ana.rocha@email.com','22233344408','11990000048','2000-04-04','Unimed','2026-02-10',NULL,1),

(56,'Carlos Mendes','carlos.mendes@email.com','22233344409','11990000049','1985-06-15','Bradesco Saúde','2026-02-10',NULL,1),
(57,'Carlos Alves','carlos.alves@email.com','22233344410','11990000050','1991-09-09','SulAmérica','2026-02-10',NULL,1);


-- responsavel
INSERT INTO responsavel (id, nome, email, telefone, dt_nascimento, cpf, id_endereco, ativo) VALUES
(1,'Clara','email2@gmail.com','1140028922','2002-01-16','49927132810',3, true);

-- funcionario
-- funcionario
INSERT INTO funcionario (id, nome, email, senha, supervisor_id, cargo, especialidade, tipo_atendimento, telefone, documento, foto, ativo) VALUES
(1,'Dra. Helena Castro','helena.castro@clinica.com','$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC',NULL,'Supervisor(a)','Fonoaudiologia', 'TO', '11940028922', '40028922', 'fotoPerfil.png', 1),
(2,'Juliana Almeida','admin@clinica.com','$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC',1,'Funcionário',NULL, 'TO', '11940028922', '40028922', 'fotoPerfil.png', 1),
(3,'Marcos Ribeiro','marcos.ribeiro@clinica.com','$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC',2,'Estagiário','Fonoaudiologia', 'ABA', '11940028922', '40028922', 'fotoPerfil.png', 1),
(4,'Vitor Almeida','vitor.almeida@clinica.com','$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC',2,'Funcionário',NULL, 'ABA', '11940028922', '40028922', 'fotoPerfil.png', 1),
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
(2,1),(3,2),(1,2),(4, 4),
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
