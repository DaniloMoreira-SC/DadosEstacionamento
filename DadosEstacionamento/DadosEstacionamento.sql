-- ====================================================================
-- BANCO DE DADOS ESTACIONAMENTO
-- VERSÃO 2.0
-- ====================================================================

-- DESABILITANDO O MODE SEGURO
SET SQL_SAFE_UPDATES = 0;

-- CRIANDO O BANCO DE DADOS
CREATE DATABASE IF NOT EXISTS DADOS_ESTACIONAMENTO;

-- CONECTANDO AO BANCO DE DADOS
USE DADOS_ESTACIONAMENTO;

-- ====================================================================
				-- TABELAS
-- ====================================================================
-- TABELA ENTRADA
-- ====================================================================

CREATE TABLE IF NOT EXISTS ENTRADA (
IDENTRADA INT AUTO_INCREMENT PRIMARY KEY,
NOMECLIENTE VARCHAR(120) NOT NULL,
PLACA VARCHAR(10) NOT NULL,
TIPOVEICULO VARCHAR(10) NOT NULL,
NUMEROVAGA INT,
DTENTRADA DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
STATUSVEICULO VARCHAR(20) DEFAULT 'ABERTO'
);

-- ===================================================================
-- TABELA SAIDA
-- ===================================================================

CREATE TABLE IF NOT EXISTS SAIDA (
IDSAIDA INT AUTO_INCREMENT PRIMARY KEY,
IDENTRADA INT NOT NULL,
DTSAIDA DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
TEMPO_MINUTOS INT NOT NULL,
VALOR_TOTAL DECIMAL(10,2) NOT NULL,

CONSTRAINT FK_SAIDA_ENTRADA
FOREIGN KEY (IDENTRADA)
REFERENCES ENTRADA(IDENTRADA)

);

-- =====================================================================
-- TABELA VAGAS
-- =====================================================================

CREATE TABLE IF NOT EXISTS VAGA (
IDVAGA INT AUTO_INCREMENT PRIMARY KEY,
NUMEROVAGA INT NOT NULL UNIQUE,
STATUSVAGA VARCHAR(20) DEFAULT 'LIVRE'
);

-- =====================================================================
-- PROCEDURE TARIFA
-- =====================================================================

CREATE TABLE IF NOT EXISTS TARIFA (
IDTARIFA INT AUTO_INCREMENT PRIMARY KEY,
DESCRICAO VARCHAR(100),
VALORHORA DECIMAL(10,2)
);

-- ====================================================================
				-- INSERT
-- ====================================================================

INSERT INTO TARIFA (DESCRICAO, VALORHORA) VALUES
('PADRAO', 10.00);

-- ====================================================================
-- CONTROLE DE VAGAS
-- ====================================================================

INSERT INTO VAGA (NUMEROVAGA)
SELECT
    1
WHERE NOT EXISTS (
    SELECT *
    FROM VAGA
    WHERE NUMEROVAGA = 1
);

INSERT INTO VAGA (NUMEROVAGA)
SELECT
    2
WHERE NOT EXISTS (
    SELECT *
    FROM VAGA
    WHERE NUMEROVAGA = 2
);

INSERT INTO VAGA (NUMEROVAGA)
SELECT
    3
WHERE NOT EXISTS (
    SELECT *
    FROM VAGA
    WHERE NUMEROVAGA = 3
);

INSERT INTO VAGA (NUMEROVAGA)
SELECT
    4
WHERE NOT EXISTS (
    SELECT *
    FROM VAGA
    WHERE NUMEROVAGA = 4
);

INSERT INTO VAGA (NUMEROVAGA)
SELECT
    5
WHERE NOT EXISTS (
    SELECT *
    FROM VAGA
    WHERE NUMEROVAGA = 5
);

INSERT INTO VAGA (NUMEROVAGA)
SELECT
    6
WHERE NOT EXISTS (
    SELECT *
    FROM VAGA
    WHERE NUMEROVAGA = 6
);

INSERT INTO VAGA (NUMEROVAGA)
SELECT
    7
WHERE NOT EXISTS (
    SELECT *
    FROM VAGA
    WHERE NUMEROVAGA = 7
);

INSERT INTO VAGA (NUMEROVAGA)
SELECT
    8
WHERE NOT EXISTS (
    SELECT *
    FROM VAGA
    WHERE NUMEROVAGA = 8
);

INSERT INTO VAGA (NUMEROVAGA)
SELECT
    9
WHERE NOT EXISTS (
    SELECT *
    FROM VAGA
    WHERE NUMEROVAGA = 9
);

INSERT INTO VAGA (NUMEROVAGA)
SELECT
    10
WHERE NOT EXISTS (
    SELECT *
    FROM VAGA
    WHERE NUMEROVAGA = 10
);

-- ====================================================================
				-- UPDATES / ALTER
-- ====================================================================

UPDATE ENTRADA 
SET 
    NOMECLIENTE = UPPER(NOMECLIENTE);
    

-- ====================================================================
				-- CONSULTAS ÚTEIS
-- ====================================================================
SELECT * FROM TARIFA;

SELECT * FROM SAIDA;

SELECT * FROM ENTRADA;

SELECT * FROM VAGA;

SELECT
    *
FROM
    ENTRADA
WHERE
    STATUSVEICULO = 'ABERTO'
ORDER BY
    NUMEROVAGA;

SELECT
      E.IDENTRADA
    , E.NOMECLIENTE
    , E.PLACA
    , E.TIPOVEICULO
    , E.NUMEROVAGA
    , E.DTENTRADA
    , E.STATUSVEICULO
    , S.DTSAIDA
    , S.TEMPO_MINUTOS
    , S.VALOR_TOTAL
FROM
    ENTRADA E
LEFT JOIN
    SAIDA S
ON
    E.IDENTRADA = S.IDENTRADA
ORDER BY
    E.IDENTRADA DESC;
    
