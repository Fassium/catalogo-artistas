package br.com.fassium.catalogoartistas.dominio.artista.model;

import jakarta.persistence.*;

@Entity
@Table(name = "artista")
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoArtista tipo;

    protected Artista() {
    }

    public Artista(String nome, TipoArtista tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoArtista getTipo() {
        return tipo;
    }

    public void setTipo(TipoArtista tipo) {
        this.tipo = tipo;
    }
}
