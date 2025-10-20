INSERT INTO especialista (id, nome, email, senha, supervisor_id, cargo, especialidade)
VALUES
    (1, 'Dra. Helena Castro', 'helena.castro@clinica.com', 'SenhaForte@2024', NULL,
     'Supervisora de Fonoaudiologia', 'Motricidade Orofacial'),

    (2, 'Juliana Almeida', 'juliana.almeida@clinica.com', 'SenhaForte@2024', 1,
     'Fonoaudióloga', 'Linguagem Infantil'),

    (3, 'Marcos Ribeiro', 'marcos.ribeiro@clinica.com', 'SenhaForte@2024', 1,
     'Fonoaudiólogo', 'Voz e Reabilitação Vocal'),

    (4, 'Carla Menezes', 'carla.menezes@clinica.com', 'SenhaForte@2024', 1,
     'Fonoaudióloga', 'Audiologia e Processamento Auditivo');

INSERT INTO paciente (nome, email, cpf, cargo, telefone, senha, dt_nascimento)
VALUES
   ('Lucas Silva', 'lucas.silva@email.com', '123.456.789-00', 'Analista', '(11) 91234-5678', 'senha123', '1990-05-15'),
   ('Mariana Costa', 'mariana.costa@email.com', '987.654.321-00', 'Enfermeira', '(21) 99876-5432', 'mariana2025', '1985-11-22'),
   ('Pedro Oliveira', 'pedro.oliveira@email.com', '456.789.123-11', 'Médico', '(31) 91234-8765', 'pedroMed123', '1978-02-10'),
   ('Ana Paula Lima', 'ana.lima@email.com', '321.654.987-22', 'Fisioterapeuta', '(41) 98765-4321', 'anaFisio2025', '1992-08-30'),
   ('Rafael Souza', 'rafael.souza@email.com', '654.321.987-33', 'Psicólogo', '(51) 91234-0987', 'rafaPsi2025', '1988-03-12');
