-- Tabela de Usuários
CREATE TABLE IF NOT EXISTS usuario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    senha TEXT NOT NULL,
    tipo TEXT CHECK(tipo IN ('ADMIN', 'PARTICIPANTE')) NOT NULL,
    perfil TEXT CHECK(perfil IN ('ALUNO', 'PROFESSOR', 'PROFISSIONAL'))
    );

-- Tabela de Eventos
CREATE TABLE IF NOT EXISTS evento (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo TEXT NOT NULL,
    descricao TEXT,
    data_inicio DATETIME NOT NULL,
    data_fim DATETIME NOT NULL,
    localizacao TEXT NOT NULL,
    administrador_id INTEGER NOT NULL,
    FOREIGN KEY (administrador_id) REFERENCES usuario(id)
    );

-- Tabela de Atividades
CREATE TABLE IF NOT EXISTS atividade (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    descricao TEXT,
    data_realizacao DATETIME NOT NULL,
    limite_inscritos INTEGER NOT NULL,
    tipo TEXT NOT NULL,
    evento_id INTEGER NOT NULL,
    FOREIGN KEY (evento_id) REFERENCES evento(id)
    );

-- Tabela de Valores de Inscrição
CREATE TABLE IF NOT EXISTS valor_inscricao (
    evento_id INTEGER NOT NULL,
    perfil TEXT NOT NULL,
    valor REAL NOT NULL,
    PRIMARY KEY (evento_id, perfil),
    FOREIGN KEY (evento_id) REFERENCES evento(id)
    );

-- Tabela de Inscrições
CREATE TABLE IF NOT EXISTS inscricao (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    participante_id INTEGER NOT NULL,
    evento_id INTEGER NOT NULL,
    atividade_id INTEGER,
    data_inscricao DATETIME DEFAULT CURRENT_TIMESTAMP,
    status_pagamento TEXT CHECK(status_pagamento
                                    IN ('PENDENTE', 'CONFIRMADO', 'CANCELADO'))
                                    DEFAULT 'PENDENTE',
    valor_pago REAL,
    FOREIGN KEY (participante_id) REFERENCES usuario(id),
    FOREIGN KEY (evento_id) REFERENCES evento(id),
    FOREIGN KEY (atividade_id) REFERENCES atividade(id)
    );