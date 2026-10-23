CREATE TABLE artista (
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    tipo VARCHAR(10)  NOT NULL,
    CONSTRAINT ck_artista_tipo CHECK (tipo IN ('CANTOR', 'BANDA'))
);

CREATE TABLE album (
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    titulo         VARCHAR(200) NOT NULL,
    ano_lancamento INTEGER
);

CREATE TABLE artista_album (
    artista_id BIGINT NOT NULL,
    album_id   BIGINT NOT NULL,
    CONSTRAINT pk_artista_album PRIMARY KEY (artista_id, album_id),
    CONSTRAINT fk_artista_album_artista FOREIGN KEY (artista_id) REFERENCES artista (id),
    CONSTRAINT fk_artista_album_album FOREIGN KEY (album_id) REFERENCES album (id)
);

CREATE INDEX ix_artista_nome ON artista (nome);
CREATE INDEX ix_artista_album_album_id ON artista_album (album_id);
