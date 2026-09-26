-- Script de criação do banco de dados MySQL
-- Banco: locadora_filmes
-- Usuário: root
-- Senha: root

DROP DATABASE IF EXISTS locadora_filmes;
CREATE DATABASE locadora_filmes CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE locadora_filmes;

-- Tabela de Usuários
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    nome VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

-- Tabela de Clientes
CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(100),
    endereco VARCHAR(200)
) ENGINE=InnoDB;

-- Tabela de Filmes
CREATE TABLE filme (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    genero VARCHAR(50),
    ano INT NOT NULL,
    diretor VARCHAR(100),
    idioma VARCHAR(30),
    disponivel BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB;

-- Tabela de Locações
CREATE TABLE locacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    filme_id INT NOT NULL,
    data_locacao DATE NOT NULL,
    data_devolucao_prevista DATE NOT NULL,
    data_devolucao_real DATE,
    devolvido BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE RESTRICT,
    FOREIGN KEY (filme_id) REFERENCES filme(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Inserir dados de teste

-- Usuários
INSERT INTO usuario (login, senha, nome) VALUES 
('admin', 'admin123', 'Administrador do Sistema'),
('funcionario', 'func123', 'João Silva');

-- Clientes
INSERT INTO cliente (nome, cpf, telefone, email, endereco) VALUES
('Maria Santos', '12345678901', '(62) 98765-4321', 'maria@email.com', 'Rua das Flores, 123 - Goiânia'),
('José Oliveira', '98765432109', '(62) 99876-5432', 'jose@email.com', 'Av. Principal, 456 - Goiânia'),
('Ana Costa', '45678912345', '(62) 91234-5678', 'ana@email.com', 'Rua do Comércio, 789 - Goiânia'),
('Carlos Pereira', '32165498712', '(62) 98888-7777', 'carlos@email.com', 'Rua Central, 321 - Goiânia'),
('Fernanda Lima', '65498732165', '(62) 97777-6666', 'fernanda@email.com', 'Av. Goiás, 654 - Goiânia');

-- Filmes
INSERT INTO filme (titulo, genero, ano, diretor, idioma, disponivel) VALUES
('Matrix', 'Ficção Científica', 1999, 'Lana Wachowski', 'Inglês', TRUE),
('O Senhor dos Anéis', 'Fantasia', 2001, 'Peter Jackson', 'Inglês', TRUE),
('Cidade de Deus', 'Drama', 2002, 'Fernando Meirelles', 'Português', TRUE),
('Interestelar', 'Ficção Científica', 2014, 'Christopher Nolan', 'Inglês', TRUE),
('Pantera Negra', 'Ação', 2018, 'Ryan Coogler', 'Inglês', TRUE),
('Parasita', 'Suspense', 2019, 'Bong Joon-ho', 'Coreano', TRUE),
('Coringa', 'Drama', 2019, 'Todd Phillips', 'Inglês', TRUE),
('Vingadores: Ultimato', 'Ação', 2019, 'Anthony Russo', 'Inglês', TRUE),
('Toy Story 4', 'Animação', 2019, 'Josh Cooley', 'Inglês', TRUE),
('1917', 'Guerra', 2019, 'Sam Mendes', 'Inglês', TRUE);

-- Locações (alguns exemplos)
INSERT INTO locacao (cliente_id, filme_id, data_locacao, data_devolucao_prevista, devolvido) VALUES
(1, 1, '2025-12-01', '2025-12-08', FALSE),
(2, 3, '2025-11-28', '2025-12-05', TRUE),
(3, 5, '2025-12-03', '2025-12-10', FALSE);

-- Atualizar disponibilidade dos filmes locados e não devolvidos
UPDATE filme SET disponivel = FALSE WHERE id IN (
    SELECT filme_id FROM locacao WHERE devolvido = FALSE
);

SELECT 'Banco de dados criado com sucesso!' AS status;
