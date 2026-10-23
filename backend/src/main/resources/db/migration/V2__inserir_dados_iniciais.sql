INSERT INTO artista (nome, tipo) VALUES
    ('Serj Tankian', 'CANTOR'),
    ('Mike Shinoda', 'CANTOR'),
    ('Michel Teló', 'CANTOR'),
    ('Guns N'' Roses', 'BANDA');

INSERT INTO album (titulo) VALUES
    ('Harakiri'),
    ('Black Blooms'),
    ('The Rough Dog'),
    ('The Rising Tied'),
    ('Post Traumatic'),
    ('Post Traumatic EP'),
    ('Where''d You Go'),
    ('Bem Sertanejo'),
    ('Bem Sertanejo - O Show (Ao Vivo)'),
    ('Bem Sertanejo - (1ª Temporada) - EP'),
    ('Use Your Illusion I'),
    ('Use Your Illusion II'),
    ('Greatest Hits');

INSERT INTO artista_album (artista_id, album_id)
SELECT a.id, al.id
FROM (VALUES
    ('Serj Tankian', 'Harakiri'),
    ('Serj Tankian', 'Black Blooms'),
    ('Serj Tankian', 'The Rough Dog'),
    ('Mike Shinoda', 'The Rising Tied'),
    ('Mike Shinoda', 'Post Traumatic'),
    ('Mike Shinoda', 'Post Traumatic EP'),
    ('Mike Shinoda', 'Where''d You Go'),
    ('Michel Teló', 'Bem Sertanejo'),
    ('Michel Teló', 'Bem Sertanejo - O Show (Ao Vivo)'),
    ('Michel Teló', 'Bem Sertanejo - (1ª Temporada) - EP'),
    ('Guns N'' Roses', 'Use Your Illusion I'),
    ('Guns N'' Roses', 'Use Your Illusion II'),
    ('Guns N'' Roses', 'Greatest Hits')
) AS v (artista_nome, album_titulo)
JOIN artista a ON a.nome = v.artista_nome
JOIN album al ON al.titulo = v.album_titulo;
