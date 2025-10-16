package com.empresa.pessoa.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "endereco")
public class Endereco implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 200)
    private String logradouro;
    
    @Column(length = 20)
    private String numero;
    
    @Column(length = 100)
    private String bairro;
    
    @Column(length = 100)
    private String cidade;
    
    @Column(length = 2)
    private String estado;
    
    @Column(length = 9)
    private String cep;
    
    @Column(length = 200)
    private String complemento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;
    

    public Endereco() {}
    
    public Endereco(String logradouro, String numero, String bairro, String cidade, String estado) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    
    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }
    
    public Pessoa getPessoa() { return pessoa; }
    public void setPessoa(Pessoa pessoa) { this.pessoa = pessoa; }
    
    @Override
    public String toString() {
        return "Endereco{" +
                "logradouro='" + logradouro + '\'' +
                ", numero='" + numero + '\'' +
                ", bairro='" + bairro + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}