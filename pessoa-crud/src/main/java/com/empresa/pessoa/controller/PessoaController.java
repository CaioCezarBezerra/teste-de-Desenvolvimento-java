package com.empresa.pessoa.controller;

import com.empresa.pessoa.model.Pessoa;
import com.empresa.pessoa.model.Endereco;
import com.empresa.pessoa.model.Telefone;
import com.empresa.pessoa.model.Documento;
import com.empresa.pessoa.service.PessoaService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Named("pessoaController")
@ViewScoped
public class PessoaController implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private PessoaService pessoaService;


    private Pessoa pessoa = new Pessoa();
    private List<Pessoa> pessoas;
    private String filtroNome;


    private Endereco novoEndereco = new Endereco();
    private Telefone novoTelefone = new Telefone();
    private Documento novoDocumento = new Documento();


    private Endereco enderecoSelecionado;
    private Telefone telefoneSelecionado;
    private Documento documentoSelecionado;


    private Pessoa pessoaEditando = new Pessoa();

    @PostConstruct
    public void init() {
        System.out.println("=== INICIALIZANDO PESSOA CONTROLLER ===");
        carregarPessoas();
    }



    public void prepararEdicao(Long pessoaId) {
        try {
            System.out.println("=== PREPARANDO EDIÇÃO DA PESSOA ID: " + pessoaId + " ===");
            
            Optional<Pessoa> pessoaOpt = pessoaService.buscarPorId(pessoaId);
            if (pessoaOpt.isPresent()) {
                this.pessoaEditando = pessoaOpt.get();
                

                if (this.pessoaEditando.getEnderecos() == null) {
                    this.pessoaEditando.setEnderecos(new ArrayList<>());
                }
                if (this.pessoaEditando.getTelefones() == null) {
                    this.pessoaEditando.setTelefones(new ArrayList<>());
                }
                if (this.pessoaEditando.getDocumentos() == null) {
                    this.pessoaEditando.setDocumentos(new ArrayList<>());
                }
                
                System.out.println("✅ PESSOA CARREGADA: " + this.pessoaEditando.getNome());
                System.out.println("✅ ENDEREÇOS: " + this.pessoaEditando.getEnderecos().size());
                System.out.println("✅ TELEFONES: " + this.pessoaEditando.getTelefones().size());
                System.out.println("✅ DOCUMENTOS: " + this.pessoaEditando.getDocumentos().size());
                
            } else {
                addErrorMessage("Erro", "Pessoa não encontrada!");
                this.pessoaEditando = new Pessoa();
            }
        } catch (Exception e) {
            addErrorMessage("Erro", "Erro ao carregar pessoa: " + e.getMessage());
            e.printStackTrace();
            this.pessoaEditando = new Pessoa();
        }
    }

 
    public void carregarParaEdicao(Long pessoaId) {
        prepararEdicao(pessoaId);
    }

    public void salvarEdicao() {
        try {
            System.out.println("=== SALVANDO EDIÇÃO DA MODAL ===");
            System.out.println("Pessoa ID: " + pessoaEditando.getId());
            System.out.println("Nome: " + pessoaEditando.getNome());
            
            pessoaService.salvarOuAtualizar(pessoaEditando);
            addMessage("Sucesso", "Pessoa atualizada com sucesso!");
            carregarPessoas();
            
        } catch (Exception e) {
            addErrorMessage("Erro", "Erro ao atualizar pessoa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void excluirEditando() {
        try {
            if (pessoaEditando.getId() != null) {
                pessoaService.excluir(pessoaEditando.getId());
                addMessage("Sucesso", "Pessoa excluída com sucesso!");
                carregarPessoas();
            }
        } catch (Exception e) {
            addErrorMessage("Erro", "Erro ao excluir pessoa: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public void salvar() {
        try {
            System.out.println("🎯🎯🎯 MÉTODO SALVAR INICIADO 🎯🎯🎯");
            System.out.println("📝 Nome: " + pessoa.getNome());
            System.out.println("📧 Email: " + pessoa.getEmail());
            System.out.println("🆔 Pessoa ID: " + pessoa.getId());
            

            System.out.println("🏠 Endereços: " + (pessoa.getEnderecos() != null ? pessoa.getEnderecos().size() : "null"));
            System.out.println("📞 Telefones: " + (pessoa.getTelefones() != null ? pessoa.getTelefones().size() : "null"));
            System.out.println("📄 Documentos: " + (pessoa.getDocumentos() != null ? pessoa.getDocumentos().size() : "null"));
            

            if (pessoa.getNome() == null || pessoa.getNome().trim().isEmpty()) {
                System.out.println("❌ VALIDAÇÃO: Nome vazio");
                addErrorMessage("Erro", "Nome é obrigatório!");
                return;
            }
            
            if (pessoa.getEmail() == null || pessoa.getEmail().trim().isEmpty()) {
                System.out.println("❌ VALIDAÇÃO: Email vazio");
                addErrorMessage("Erro", "Email é obrigatório!");
                return;
            }
            
            System.out.println("✅ VALIDAÇÕES PASSARAM - CHAMANDO SERVICE");
            

            if (pessoa.getEnderecos() == null) {
                pessoa.setEnderecos(new ArrayList<>());
                System.out.println("⚠️ Lista de endereços inicializada");
            }
            if (pessoa.getTelefones() == null) {
                pessoa.setTelefones(new ArrayList<>());
                System.out.println("⚠️ Lista de telefones inicializada");
            }
            if (pessoa.getDocumentos() == null) {
                pessoa.setDocumentos(new ArrayList<>());
                System.out.println("⚠️ Lista de documentos inicializada");
            }
            
            pessoaService.salvarOuAtualizar(pessoa);
            addMessage("Sucesso", "Pessoa salva com sucesso!");
            limpar();
            carregarPessoas();
            
            System.out.println("🎉 MÉTODO SALVAR CONCLUÍDO COM SUCESSO");
            
        } catch (Exception e) {
            System.out.println("💥 ERRO NO MÉTODO SALVAR: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Erro", "Erro ao salvar pessoa: " + e.getMessage());
        }
    }

    public void editar(Pessoa pessoa) {
        try {
            System.out.println("=== EDITANDO PESSOA ===");
            System.out.println("ID: " + pessoa.getId());
            System.out.println("Nome: " + pessoa.getNome());
            

            Optional<Pessoa> pessoaCompleta = pessoaService.buscarPorIdComRelacionamentos(pessoa.getId());
            
            if (pessoaCompleta.isPresent()) {

                this.pessoa = pessoaCompleta.get();
                
                System.out.println("✅ DADOS CARREGADOS NO FORMULÁRIO:");
                System.out.println("   Nome: " + this.pessoa.getNome());
                System.out.println("   Email: " + this.pessoa.getEmail());
                System.out.println("   Data Nasc: " + this.pessoa.getDataNascimento());
                System.out.println("   Endereços: " + this.pessoa.getEnderecos().size());
                System.out.println("   Telefones: " + this.pessoa.getTelefones().size());
                

                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Editando", "Pessoa carregada: " + this.pessoa.getNome()));
                    
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Pessoa não encontrada!"));
            }
        } catch (Exception e) {
            System.out.println("❌ ERRO: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar pessoa"));
        }
    }
    public void excluir(Pessoa pessoa) {
        try {
            pessoaService.excluir(pessoa.getId());
            addMessage("Sucesso", "Pessoa excluída com sucesso!");
            carregarPessoas();
        } catch (Exception e) {
            addErrorMessage("Erro", "Erro ao excluir pessoa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void pesquisar() {
        try {
            if (filtroNome != null && !filtroNome.trim().isEmpty()) {
                pessoas = pessoaService.pesquisarPorNome(filtroNome);
                addMessage("Info", "Encontradas " + pessoas.size() + " pessoa(s)");
            } else {
                carregarPessoas();
            }
        } catch (Exception e) {
            addErrorMessage("Erro", "Erro ao pesquisar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void limparPesquisa() {
        filtroNome = null;
        carregarPessoas();
        addMessage("Info", "Pesquisa limpa");
    }

    public void limpar() {
        try {
            System.out.println("=== EXECUTANDO LIMPAR ===");
            

            this.pessoa = new Pessoa();
            this.novoEndereco = new Endereco();
            this.novoTelefone = new Telefone();
            this.novoDocumento = new Documento();
            

            this.enderecoSelecionado = null;
            this.telefoneSelecionado = null;
            this.documentoSelecionado = null;
            
            System.out.println("✅ FORMULÁRIO LIMPO COM SUCESSO");
            addMessage("Info", "Formulário limpo");
            
        } catch (Exception e) {
            System.out.println("❌ ERRO NO LIMPAR: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Erro", "Erro ao limpar formulário: " + e.getMessage());
        }
    }

 

    public void salvarOuAtualizarEndereco() {
        try {
            System.out.println("=== SALVAR/ATUALIZAR ENDEREÇO ===");
            System.out.println("Editando: " + isEnderecoEditando());
            
            if (isEnderecoEditando()) {
                atualizarEndereco();
            } else {
                adicionarEndereco();
            }
        } catch (Exception e) {
            System.out.println("❌ ERRO em salvarOuAtualizarEndereco: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Erro", "Erro ao processar endereço: " + e.getMessage());
        }
    }

    public void adicionarEndereco() {
        try {
            System.out.println("=== ADICIONANDO ENDEREÇO ===");
            System.out.println("Logradouro: " + novoEndereco.getLogradouro());
            System.out.println("Número: " + novoEndereco.getNumero());
            

            if (novoEndereco.getLogradouro() == null || novoEndereco.getLogradouro().trim().isEmpty()) {
                addErrorMessage("Erro", "Logradouro é obrigatório!");
                return;
            }
            if (novoEndereco.getNumero() == null || novoEndereco.getNumero().trim().isEmpty()) {
                addErrorMessage("Erro", "Número é obrigatório!");
                return;
            }
            if (novoEndereco.getBairro() == null || novoEndereco.getBairro().trim().isEmpty()) {
                addErrorMessage("Erro", "Bairro é obrigatório!");
                return;
            }
            if (novoEndereco.getCidade() == null || novoEndereco.getCidade().trim().isEmpty()) {
                addErrorMessage("Erro", "Cidade é obrigatória!");
                return;
            }
            if (novoEndereco.getEstado() == null || novoEndereco.getEstado().trim().isEmpty()) {
                addErrorMessage("Erro", "Estado é obrigatório!");
                return;
            }
            

            if (pessoa.getEnderecos() == null) {
                pessoa.setEnderecos(new ArrayList<>());
                System.out.println("Lista de endereços inicializada");
            }
            

            novoEndereco.setPessoa(pessoa);
            

            pessoa.getEnderecos().add(novoEndereco);
            
            System.out.println("✅ Endereço adicionado com sucesso!");
            System.out.println("Total de endereços: " + pessoa.getEnderecos().size());
            

            novoEndereco = new Endereco();
            
            addMessage("Sucesso", "Endereço adicionado com sucesso!");
            
        } catch (Exception e) {
            System.out.println("❌ ERRO ao adicionar endereço: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Erro", "Erro ao adicionar endereço: " + e.getMessage());
        }
    }

    public void editarEndereco(Endereco endereco) {
        try {
            System.out.println("=== EDITANDO ENDEREÇO ===");
            System.out.println("Endereço selecionado: " + endereco.getLogradouro());
            
            this.enderecoSelecionado = endereco;
            

            this.novoEndereco = new Endereco();
            this.novoEndereco.setLogradouro(endereco.getLogradouro());
            this.novoEndereco.setNumero(endereco.getNumero());
            this.novoEndereco.setBairro(endereco.getBairro());
            this.novoEndereco.setCidade(endereco.getCidade());
            this.novoEndereco.setEstado(endereco.getEstado());
            this.novoEndereco.setCep(endereco.getCep());
            this.novoEndereco.setComplemento(endereco.getComplemento());
            
            System.out.println("✅ Endereço preparado para edição");
            addMessage("Info", "Editando endereço...");
            
        } catch (Exception e) {
            System.out.println("❌ ERRO ao editar endereço: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Erro", "Erro ao editar endereço: " + e.getMessage());
        }
    }

    public void atualizarEndereco() {
        try {
            System.out.println("=== ATUALIZANDO ENDEREÇO ===");
            
            if (enderecoSelecionado != null) {

                enderecoSelecionado.setLogradouro(novoEndereco.getLogradouro());
                enderecoSelecionado.setNumero(novoEndereco.getNumero());
                enderecoSelecionado.setBairro(novoEndereco.getBairro());
                enderecoSelecionado.setCidade(novoEndereco.getCidade());
                enderecoSelecionado.setEstado(novoEndereco.getEstado());
                enderecoSelecionado.setCep(novoEndereco.getCep());
                enderecoSelecionado.setComplemento(novoEndereco.getComplemento());
                
                System.out.println("✅ Endereço atualizado: " + enderecoSelecionado.getLogradouro());
                
                cancelarEdicaoEndereco();
                addMessage("Sucesso", "Endereço atualizado com sucesso!");
            }
        } catch (Exception e) {
            System.out.println("❌ ERRO ao atualizar endereço: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Erro", "Erro ao atualizar endereço: " + e.getMessage());
        }
    }

    public void removerEndereco(Endereco endereco) {
        try {
            System.out.println("=== REMOVENDO ENDEREÇO ===");
            pessoa.removerEndereco(endereco);
            System.out.println("✅ Endereço removido");
            addMessage("Sucesso", "Endereço removido com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ ERRO ao remover endereço: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Erro", "Erro ao remover endereço: " + e.getMessage());
        }
    }

    public void cancelarEdicaoEndereco() {
        enderecoSelecionado = null;
        novoEndereco = new Endereco();
        System.out.println("✅ Edição de endereço cancelada");
    }



    public void salvarOuAtualizarTelefone() {
        try {
            System.out.println("=== SALVAR/ATUALIZAR TELEFONE ===");
            
            if (isTelefoneEditando()) {
                atualizarTelefone();
            } else {
                adicionarTelefone();
            }
        } catch (Exception e) {
            System.out.println("❌ ERRO em salvarOuAtualizarTelefone: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Erro", "Erro ao processar telefone: " + e.getMessage());
        }
    }

    public void adicionarTelefone() {
        try {
            System.out.println("=== ADICIONANDO TELEFONE ===");
            System.out.println("Número: " + novoTelefone.getNumero());
            System.out.println("Tipo: " + novoTelefone.getTipo());
            
            if (novoTelefone.getNumero() == null || novoTelefone.getNumero().trim().isEmpty()) {
                addErrorMessage("Erro", "Número do telefone é obrigatório!");
                return;
            }
            if (novoTelefone.getTipo() == null) {
                addErrorMessage("Erro", "Tipo de telefone é obrigatório!");
                return;
            }
            
  
            if (pessoa.getTelefones() == null) {
                pessoa.setTelefones(new ArrayList<>());
            }
            

            novoTelefone.setPessoa(pessoa);
            pessoa.getTelefones().add(novoTelefone);
            
            System.out.println("✅ Telefone adicionado com sucesso!");
            novoTelefone = new Telefone();
            
            addMessage("Sucesso", "Telefone adicionado com sucesso!");
            
        } catch (Exception e) {
            System.out.println("❌ ERRO ao adicionar telefone: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Erro", "Erro ao adicionar telefone: " + e.getMessage());
        }
    }

    public void editarTelefone(Telefone telefone) {
        try {
            this.telefoneSelecionado = telefone;
            this.novoTelefone = new Telefone();
            this.novoTelefone.setTipo(telefone.getTipo());
            this.novoTelefone.setDdd(telefone.getDdd());
            this.novoTelefone.setNumero(telefone.getNumero());
            addMessage("Info", "Editando telefone...");
        } catch (Exception e) {
            addErrorMessage("Erro", "Erro ao editar telefone: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void atualizarTelefone() {
        try {
            if (telefoneSelecionado != null) {
                telefoneSelecionado.setTipo(novoTelefone.getTipo());
                telefoneSelecionado.setDdd(novoTelefone.getDdd());
                telefoneSelecionado.setNumero(novoTelefone.getNumero());
                
                cancelarEdicaoTelefone();
                addMessage("Sucesso", "Telefone atualizado com sucesso!");
            }
        } catch (Exception e) {
            addErrorMessage("Erro", "Erro ao atualizar telefone: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removerTelefone(Telefone telefone) {
        try {
            pessoa.removerTelefone(telefone);
            addMessage("Sucesso", "Telefone removido com sucesso!");
        } catch (Exception e) {
            addErrorMessage("Erro", "Erro ao remover telefone: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void cancelarEdicaoTelefone() {
        telefoneSelecionado = null;
        novoTelefone = new Telefone();
    }



    public void salvarOuAtualizarDocumento() {
        try {
            System.out.println("=== SALVAR/ATUALIZAR DOCUMENTO ===");
            
            if (isDocumentoEditando()) {
                atualizarDocumento();
            } else {
                adicionarDocumento();
            }
        } catch (Exception e) {
            System.out.println("❌ ERRO em salvarOuAtualizarDocumento: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Erro", "Erro ao processar documento: " + e.getMessage());
        }
    }

    public void adicionarDocumento() {
        try {
            System.out.println("=== ADICIONANDO DOCUMENTO ===");
            System.out.println("Número: " + novoDocumento.getNumero());
            System.out.println("Tipo: " + novoDocumento.getTipo());
            
            if (novoDocumento.getNumero() == null || novoDocumento.getNumero().trim().isEmpty()) {
                addErrorMessage("Erro", "Número do documento é obrigatório!");
                return;
            }
            if (novoDocumento.getTipo() == null) {
                addErrorMessage("Erro", "Tipo de documento é obrigatório!");
                return;
            }
            

            if (pessoa.getDocumentos() == null) {
                pessoa.setDocumentos(new ArrayList<>());
            }
            

            novoDocumento.setPessoa(pessoa);
            pessoa.getDocumentos().add(novoDocumento);
            
            System.out.println("✅ Documento adicionado com sucesso!");
            novoDocumento = new Documento();
            
            addMessage("Sucesso", "Documento adicionado com sucesso!");
            
        } catch (Exception e) {
            System.out.println("❌ ERRO ao adicionar documento: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Erro", "Erro ao adicionar documento: " + e.getMessage());
        }
    }

    public void editarDocumento(Documento documento) {
        try {
            this.documentoSelecionado = documento;
            this.novoDocumento = new Documento();
            this.novoDocumento.setTipo(documento.getTipo());
            this.novoDocumento.setNumero(documento.getNumero());
            this.novoDocumento.setOrgaoEmissor(documento.getOrgaoEmissor());
            addMessage("Info", "Editando documento...");
        } catch (Exception e) {
            addErrorMessage("Erro", "Erro ao editar documento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void atualizarDocumento() {
        try {
            if (documentoSelecionado != null) {
                documentoSelecionado.setTipo(novoDocumento.getTipo());
                documentoSelecionado.setNumero(novoDocumento.getNumero());
                documentoSelecionado.setOrgaoEmissor(novoDocumento.getOrgaoEmissor());
                
                cancelarEdicaoDocumento();
                addMessage("Sucesso", "Documento atualizado com sucesso!");
            }
        } catch (Exception e) {
            addErrorMessage("Erro", "Erro ao atualizar documento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removerDocumento(Documento documento) {
        try {
            pessoa.removerDocumento(documento);
            addMessage("Sucesso", "Documento removido com sucesso!");
        } catch (Exception e) {
            addErrorMessage("Erro", "Erro ao remover documento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void cancelarEdicaoDocumento() {
        documentoSelecionado = null;
        novoDocumento = new Documento();
    }



    private void carregarPessoas() {
        try {
            System.out.println("=== CARREGANDO PESSOAS DO BANCO ===");
            pessoas = pessoaService.listarTodas();
            System.out.println("✅ Pessoas carregadas: " + pessoas.size());
            
        } catch (Exception e) {
            System.out.println("❌ ERRO AO CARREGAR PESSOAS: " + e.getMessage());
            e.printStackTrace();
            pessoas = new ArrayList<>();
        }
    }

    private void addMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }

    private void addErrorMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    }



    public Pessoa getPessoa() { return pessoa; }
    public void setPessoa(Pessoa pessoa) { this.pessoa = pessoa; }

    public List<Pessoa> getPessoas() { 
        if (pessoas == null) {
            carregarPessoas();
        }
        return pessoas; 
    }

    public String getFiltroNome() { return filtroNome; }
    public void setFiltroNome(String filtroNome) { this.filtroNome = filtroNome; }

    public Endereco getNovoEndereco() { return novoEndereco; }
    public void setNovoEndereco(Endereco novoEndereco) { this.novoEndereco = novoEndereco; }

    public Telefone getNovoTelefone() { return novoTelefone; }
    public void setNovoTelefone(Telefone novoTelefone) { this.novoTelefone = novoTelefone; }

    public Documento getNovoDocumento() { return novoDocumento; }
    public void setNovoDocumento(Documento novoDocumento) { this.novoDocumento = novoDocumento; }

    public Endereco getEnderecoSelecionado() { return enderecoSelecionado; }
    public void setEnderecoSelecionado(Endereco enderecoSelecionado) { this.enderecoSelecionado = enderecoSelecionado; }

    public Telefone getTelefoneSelecionado() { return telefoneSelecionado; }
    public void setTelefoneSelecionado(Telefone telefoneSelecionado) { this.telefoneSelecionado = telefoneSelecionado; }

    public Documento getDocumentoSelecionado() { return documentoSelecionado; }
    public void setDocumentoSelecionado(Documento documentoSelecionado) { this.documentoSelecionado = documentoSelecionado; }

    public Pessoa getPessoaEditando() { 
        if (pessoaEditando == null) {
            pessoaEditando = new Pessoa();
        }
        return pessoaEditando; 
    }
    
    public void setPessoaEditando(Pessoa pessoaEditando) { 
        this.pessoaEditando = pessoaEditando; 
    }

    public boolean isEnderecoEditando() { 
        boolean editando = enderecoSelecionado != null;
        System.out.println("isEnderecoEditando: " + editando);
        return editando;
    }
    
    public boolean isTelefoneEditando() { 
        return telefoneSelecionado != null; 
    }
    
    public boolean isDocumentoEditando() { 
        return documentoSelecionado != null; 
    }
}