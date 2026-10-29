package br.com.fassium.catalogoartistas.dominio.autenticacao.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, length = 64)
    private String hash;

    @Column(name = "expira_em", nullable = false)
    private Instant expiraEm;

    @Column(nullable = false)
    private boolean revogado;

    protected RefreshToken() {
    }

    public RefreshToken(Usuario usuario, String hash, Instant expiraEm) {
        this.usuario = usuario;
        this.hash = hash;
        this.expiraEm = expiraEm;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public boolean isRevogado() {
        return revogado;
    }

    public void revogar() {
        this.revogado = true;
    }

    public boolean estaValido(Instant agora) {
        return !revogado && expiraEm.isAfter(agora);
    }

}
