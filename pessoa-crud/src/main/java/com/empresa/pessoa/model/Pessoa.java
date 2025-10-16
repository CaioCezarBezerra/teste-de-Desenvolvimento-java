package com.empresa.pessoa.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pessoa")
public class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(name = "data_nascimento")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    
    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Endereco> enderecos = new ArrayList<>();
    
    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Telefone> telefones = new ArrayList<>();
    
    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Documento> documentos = new ArrayList<>();
    

    public Pessoa() {}
    
    public Pessoa(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }
    

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Date getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }
    

    public List<Endereco> getEnderecos() { 
        if (enderecos == null) {
            enderecos = new ArrayList<>();
        }
        return enderecos; 
    }
    
    public void setEnderecos(List<Endereco> enderecos) { 
        this.enderecos = enderecos; 
    }
    
    public List<Telefone> getTelefones() { 
        if (telefones == null) {
            telefones = new ArrayList<>();
        }
        return telefones; 
    }
    
    public void setTelefones(List<Telefone> telefones) { 
        this.telefones = telefones; 
    }
    
    public List<Documento> getDocumentos() { 
        if (documentos == null) {
            documentos = new ArrayList<>();
        }
        return documentos; 
    }
    
    public void setDocumentos(List<Documento> documentos) { 
        this.documentos = documentos; 
    }
    

    public void adicionarEndereco(Endereco endereco) {
        if (this.enderecos == null) {
            this.enderecos = new ArrayList<>();
        }
        this.enderecos.add(endereco);
        endereco.setPessoa(this);
    }
    
    public void removerEndereco(Endereco endereco) {
        if (this.enderecos != null) {
            this.enderecos.remove(endereco);
            endereco.setPessoa(null);
        }
    }
    
    public void adicionarTelefone(Telefone telefone) {
        if (this.telefones == null) {
            this.telefones = new ArrayList<>();
        }
        this.telefones.add(telefone);
        telefone.setPessoa(this);
    }
    
    public void removerTelefone(Telefone telefone) {
        if (this.telefones != null) {
            this.telefones.remove(telefone);
            telefone.setPessoa(null);
        }
    }
    
    public void adicionarDocumento(Documento documento) {
        if (this.documentos == null) {
            this.documentos = new ArrayList<>();
        }
        this.documentos.add(documento);
        documento.setPessoa(this);
    }
    
    public void removerDocumento(Documento documento) {
        if (this.documentos != null) {
            this.documentos.remove(documento);
            documento.setPessoa(null);
        }
    }
    

    public String toString() {
        return "Pessoa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", enderecos=" + (enderecos != null ? enderecos.size() : 0) +
                ", telefones=" + (telefones != null ? telefones.size() : 0) +
                ", documentos=" + (documentos != null ? documentos.size() : 0) +
                '}';
    }
}