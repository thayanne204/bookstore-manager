-- Schema do BookStore Manager (MySQL 8) - compatível com spring.jpa.hibernate.ddl-auto=validate
CREATE DATABASE IF NOT EXISTS bookstore_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bookstore_manager;

CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    ativo BIT DEFAULT 1,
    data_criacao DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE TABLE IF NOT EXISTS editoras (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL UNIQUE,
    cnpj VARCHAR(18),
    email VARCHAR(150),
    telefone VARCHAR(20),
    endereco VARCHAR(255),
    data_criacao DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE TABLE IF NOT EXISTS autores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    nacionalidade VARCHAR(100),
    biografia TEXT,
    data_criacao DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(150) UNIQUE,
    telefone VARCHAR(20),
    data_nascimento DATE,
    endereco VARCHAR(255),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    ativo BIT DEFAULT 1,
    data_criacao DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE TABLE IF NOT EXISTS fornecedores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cnpj VARCHAR(18) UNIQUE,
    email VARCHAR(150),
    telefone VARCHAR(20),
    endereco VARCHAR(255),
    ativo BIT DEFAULT 1,
    data_criacao DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE TABLE IF NOT EXISTS livros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    descricao TEXT,
    data_publicacao DATE,
    preco DECIMAL(10,2) NOT NULL,
    quantidade_estoque INT NOT NULL DEFAULT 0,
    estoque_minimo INT NOT NULL DEFAULT 5,
    categoria_id BIGINT NOT NULL,
    editora_id BIGINT NOT NULL,
    ativo BIT DEFAULT 1,
    data_criacao DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    data_atualizacao DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_livros_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    CONSTRAINT fk_livros_editora FOREIGN KEY (editora_id) REFERENCES editoras(id)
);

CREATE TABLE IF NOT EXISTS livro_autores (
    livro_id BIGINT NOT NULL,
    autor_id BIGINT NOT NULL,
    PRIMARY KEY (livro_id, autor_id),
    CONSTRAINT fk_la_livro FOREIGN KEY (livro_id) REFERENCES livros(id),
    CONSTRAINT fk_la_autor FOREIGN KEY (autor_id) REFERENCES autores(id)
);

CREATE TABLE IF NOT EXISTS fornecedor_livros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fornecedor_id BIGINT NOT NULL,
    livro_id BIGINT NOT NULL,
    custo_unitario DECIMAL(10,2) NOT NULL,
    prazo_entrega INT DEFAULT 0,
    CONSTRAINT fk_fl_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id),
    CONSTRAINT fk_fl_livro FOREIGN KEY (livro_id) REFERENCES livros(id)
);

CREATE TABLE IF NOT EXISTS vendas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    data_venda DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    valor_total DECIMAL(10,2) NOT NULL DEFAULT 0,
    desconto DECIMAL(10,2) DEFAULT 0,
    forma_pagamento VARCHAR(30) NOT NULL,
    status VARCHAR(20) DEFAULT 'CONCLUIDA',
    CONSTRAINT fk_vendas_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE IF NOT EXISTS itens_venda (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venda_id BIGINT NOT NULL,
    livro_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_iv_venda FOREIGN KEY (venda_id) REFERENCES vendas(id),
    CONSTRAINT fk_iv_livro FOREIGN KEY (livro_id) REFERENCES livros(id)
);

CREATE TABLE IF NOT EXISTS movimentacoes_estoque (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    livro_id BIGINT NOT NULL,
    tipo ENUM('ENTRADA','SAIDA','AJUSTE') NOT NULL,
    quantidade INT NOT NULL,
    motivo VARCHAR(255),
    data_movimentacao DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    usuario_responsavel VARCHAR(100),
    CONSTRAINT fk_me_livro FOREIGN KEY (livro_id) REFERENCES livros(id)
);

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil ENUM('ADMIN','GERENTE','VENDEDOR') DEFAULT 'VENDEDOR',
    ativo BIT DEFAULT 1,
    data_criacao DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)
);
