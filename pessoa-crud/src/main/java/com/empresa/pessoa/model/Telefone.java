package com.empresa.pessoa.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "telefone")
public class Telefone implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TipoTelefone tipo;
    
    @Column(length = 3)
    private String ddd;
    
    @Column(length = 20)
    private String numero;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;
    
    public enum TipoTelefone {
        CELULAR, RESIDENCIAL, COMERCIAL, WHATSAPP
    }
    

    public Telefone() {}
    

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public TipoTelefone getTipo() { return tipo; }
    public void setTipo(TipoTelefone tipo) { this.tipo = tipo; }
    
    public String getDdd() { return ddd; }
    public void setDdd(String ddd) { this.ddd = ddd; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public Pessoa getPessoa() { return pessoa; }
    public void setPessoa(Pessoa pessoa) { this.pessoa = pessoa; }
}
