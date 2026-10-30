package br.com.fassium.catalogoartistas.dominio.album.model;

import jakarta.persistence.*;

@Entity
@Table(name = "album")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(name = "ano_lancamento")
    private Integer anoLancamento;

    protected Album() {
    }

    public Album(String titulo, Integer anoLancamento) {
        this.titulo = titulo;
        this.anoLancamento = anoLancamento;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(Integer anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    /**
     * Igualdade por id (chave do banco). Entidade ainda sem id (não salva) só é igual a ela mesma.
     * Usa getId() e instanceof, e não o campo e getClass(), porque o Hibernate pode entregar um proxy.
     * O hashCode é constante por classe: o id muda de null para um número ao salvar, e o hash não pode mudar
     * enquanto o objeto está num Set (o Artista guarda os álbuns num Set).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o instanceof Album outro && id != null && id.equals(outro.getId());
    }

    @Override
    public int hashCode() {
        return Album.class.hashCode();
    }
}
