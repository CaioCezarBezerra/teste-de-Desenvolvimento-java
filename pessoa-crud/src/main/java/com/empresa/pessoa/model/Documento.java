package com.empresa.pessoa.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "documento")
public class Documento implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TipoDocumento tipo;
    
    @Column(length = 50)
    private String numero;
    
    @Column(name = "orgao_emissor", length = 50)
    private String orgaoEmissor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;
    
    public enum TipoDocumento {
        CPF, RG, CNH, PASSAPORTE
    }
    

    public Documento() {}
    

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public TipoDocumento getTipo() { return tipo; }
    public void setTipo(TipoDocumento tipo) { this.tipo = tipo; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getOrgaoEmissor() { return orgaoEmissor; }
    public void setOrgaoEmissor(String orgaoEmissor) { this.orgaoEmissor = orgaoEmissor; }
    
    public Pessoa getPessoa() { return pessoa; }
    public void setPessoa(Pessoa pessoa) { this.pessoa = pessoa; }

}