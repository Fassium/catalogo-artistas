CREATE TABLE usuario
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(200) NOT NULL,
    senha VARCHAR(100) NOT NULL,
    ativo BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_usuario_email UNIQUE (email),
    CONSTRAINT ck_usuario_email_minusculo CHECK (email = LOWER(email))
);

INSERT INTO usuario (email, senha)
VALUES ('thiago@fassium.com', '$2a$10$Y7zuYshgxcVaYWITxjtqTukY4qKh40BN77YkdKn9MK7Gsm/CSai8O');
