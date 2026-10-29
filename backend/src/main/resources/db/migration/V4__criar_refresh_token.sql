CREATE TABLE refresh_token
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id BIGINT                   NOT NULL,
    hash       VARCHAR(64)              NOT NULL,
    expira_em  TIMESTAMP WITH TIME ZONE NOT NULL,
    revogado   BOOLEAN                  NOT NULL DEFAULT FALSE,
    CONSTRAINT uk_refresh_token_hash UNIQUE (hash),
    CONSTRAINT fk_refresh_token_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

CREATE INDEX ix_refresh_token_usuario_id ON refresh_token (usuario_id);
