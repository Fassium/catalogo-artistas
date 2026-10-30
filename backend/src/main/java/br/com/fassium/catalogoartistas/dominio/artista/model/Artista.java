package br.com.fassium.catalogoartistas.dominio.artista.model;

import br.com.fassium.catalogoartistas.dominio.album.model.Album;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "artista_album",
            joinColumns = @JoinColumn(name = "artista_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id")
    )
    @OrderBy("titulo ASC")
    private Set<Album> albuns = new HashSet<>();

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

    public Set<Album> getAlbuns() {
        return Collections.unmodifiableSet(albuns);
    }

    public void vincularAlbum(Album album) {
        this.albuns.add(album);
    }

    public void desvincularAlbum(Album album) {
        this.albuns.remove(album);
    }

    /**
     * Igualdade por id (chave do banco). Entidade ainda sem id (não salva) só é igual a ela mesma.
     * Usa getId() e instanceof, e não o campo e getClass(), porque o Hibernate pode entregar um proxy.
     * O hashCode é constante por classe: o id muda de null para um número ao salvar, e o hash não pode mudar
     * enquanto o objeto está num Set.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o instanceof Artista outro && id != null && id.equals(outro.getId());
    }

    @Override
    public int hashCode() {
        return Artista.class.hashCode();
    }
}
