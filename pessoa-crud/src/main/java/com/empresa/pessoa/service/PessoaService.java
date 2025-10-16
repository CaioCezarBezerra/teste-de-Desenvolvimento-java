package com.empresa.pessoa.service;

import com.empresa.pessoa.model.Pessoa;
import com.empresa.pessoa.model.Endereco;
import com.empresa.pessoa.model.Telefone;
import com.empresa.pessoa.model.Documento;
import com.empresa.pessoa.repository.PessoaRepository;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Stateless
@Transactional
public class PessoaService {

    @Inject 
    private PessoaRepository pessoaRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    public List<Pessoa> listarTodas() {
        try {
            System.out.println("=== SERVICE: LISTANDO TODAS AS PESSOAS ===");
            
            List<Pessoa> pessoas = pessoaRepository.findAll();
            System.out.println("✅ SERVICE: " + pessoas.size() + " pessoas encontradas");
            
            for (Pessoa p : pessoas) {
                System.out.println("   👤 " + p.getId() + " - " + p.getNome() + " - " + p.getEmail());
            }
            
            return pessoas;
            
        } catch (Exception e) {
            System.out.println("❌ SERVICE: Erro ao listar pessoas: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public Optional<Pessoa> buscarPorId(Long id) {
        try {
            System.out.println("=== SERVICE: Buscando pessoa ID: " + id + " ===");
            
            if (id == null) {
                System.out.println("❌ SERVICE: ID não pode ser nulo");
                return Optional.empty();
            }
            

            Optional<Pessoa> pessoaOpt = pessoaRepository.findById(id);
            
            if (pessoaOpt.isPresent()) {
                Pessoa pessoa = pessoaOpt.get();
                System.out.println("✅ SERVICE: Pessoa encontrada - " + pessoa.getNome());
                

                if (pessoa.getEnderecos() == null) {
                    pessoa.setEnderecos(new ArrayList<>());
                }
                if (pessoa.getTelefones() == null) {
                    pessoa.setTelefones(new ArrayList<>());
                }
                if (pessoa.getDocumentos() == null) {
                    pessoa.setDocumentos(new ArrayList<>());
                }
                
                return Optional.of(pessoa);
            } else {
                System.out.println("❌ SERVICE: Pessoa não encontrada no banco");
                return Optional.empty();
            }
            
        } catch (Exception e) {
            System.out.println("❌ SERVICE: Erro ao buscar pessoa: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Pessoa> buscarPorIdComRelacionamentos(Long id) {
        try {
            System.out.println("=== BUSCANDO PESSOA COM RELACIONAMENTOS ===");
            System.out.println("ID: " + id);
            

            Optional<Pessoa> pessoaOpt = pessoaRepository.findById(id);
            
            if (pessoaOpt.isPresent()) {
                Pessoa pessoa = pessoaOpt.get();
                System.out.println("✅ Pessoa básica encontrada: " + pessoa.getNome());
                

                carregarEnderecos(pessoa);
                carregarTelefones(pessoa);
                carregarDocumentos(pessoa);
                
                System.out.println("✅ Endereços: " + pessoa.getEnderecos().size());
                System.out.println("✅ Telefones: " + pessoa.getTelefones().size());
                System.out.println("✅ Documentos: " + pessoa.getDocumentos().size());
                
                return Optional.of(pessoa);
            } else {
                System.out.println("❌ Pessoa não encontrada");
                return Optional.empty();
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao buscar pessoa com relacionamentos: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private void carregarEnderecos(Pessoa pessoa) {
        try {
            String jpql = "SELECT e FROM Endereco e WHERE e.pessoa.id = :pessoaId";
            List<Endereco> enderecos = entityManager.createQuery(jpql, Endereco.class)
                .setParameter("pessoaId", pessoa.getId())
                .getResultList();
            

            if (pessoa.getEnderecos() == null) {
                pessoa.setEnderecos(new ArrayList<>());
            } else {
                pessoa.getEnderecos().clear();
            }
            pessoa.getEnderecos().addAll(enderecos);
            

            for (Endereco endereco : enderecos) {
                endereco.setPessoa(pessoa);
            }
            
            System.out.println("✅ Endereços carregados: " + enderecos.size());
        } catch (Exception e) {
            System.out.println("❌ Erro ao carregar endereços: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void carregarTelefones(Pessoa pessoa) {
        try {
            String jpql = "SELECT t FROM Telefone t WHERE t.pessoa.id = :pessoaId";
            List<Telefone> telefones = entityManager.createQuery(jpql, Telefone.class)
                .setParameter("pessoaId", pessoa.getId())
                .getResultList();
            

            if (pessoa.getTelefones() == null) {
                pessoa.setTelefones(new ArrayList<>());
            } else {
                pessoa.getTelefones().clear();
            }
            pessoa.getTelefones().addAll(telefones);
            

            for (Telefone telefone : telefones) {
                telefone.setPessoa(pessoa);
            }
            
            System.out.println("✅ Telefones carregados: " + telefones.size());
        } catch (Exception e) {
            System.out.println("❌ Erro ao carregar telefones: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void carregarDocumentos(Pessoa pessoa) {
        try {
            String jpql = "SELECT d FROM Documento d WHERE d.pessoa.id = :pessoaId";
            List<Documento> documentos = entityManager.createQuery(jpql, Documento.class)
                .setParameter("pessoaId", pessoa.getId())
                .getResultList();
            

            if (pessoa.getDocumentos() == null) {
                pessoa.setDocumentos(new ArrayList<>());
            } else {
                pessoa.getDocumentos().clear();
            }
            pessoa.getDocumentos().addAll(documentos);
            

            for (Documento documento : documentos) {
                documento.setPessoa(pessoa);
            }
            
            System.out.println("✅ Documentos carregados: " + documentos.size());
        } catch (Exception e) {
            System.out.println("❌ Erro ao carregar documentos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Pessoa salvarOuAtualizar(Pessoa pessoa) {
        try {
            System.out.println("=== SERVICE: Salvando/Atualizando pessoa ===");
            System.out.println("ID: " + pessoa.getId());
            System.out.println("Nome: " + pessoa.getNome());
            System.out.println("Email: " + pessoa.getEmail());
            
            if (pessoa.getNome() == null || pessoa.getNome().trim().isEmpty()) {
                throw new RuntimeException("Nome é obrigatório");
            }
            
            if (pessoa.getEmail() == null || pessoa.getEmail().trim().isEmpty()) {
                throw new RuntimeException("Email é obrigatório");
            }
            
            if (pessoa.getEnderecos() == null) {
                pessoa.setEnderecos(new ArrayList<>());
            }
            if (pessoa.getTelefones() == null) {
                pessoa.setTelefones(new ArrayList<>());
            }
            if (pessoa.getDocumentos() == null) {
                pessoa.setDocumentos(new ArrayList<>());
            }
            
            configurarRelacionamentos(pessoa);

            System.out.println("✅ VAI SALVAR NO BANCO...");
            Pessoa saved = pessoaRepository.save(pessoa);
            System.out.println("✅ SERVICE: Pessoa salva com sucesso - ID: " + saved.getId());
            return saved;
            
        } catch (Exception e) {
            System.out.println("❌ SERVICE: Erro ao salvar pessoa: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar pessoa: " + e.getMessage(), e);
        }
    }

    private void configurarRelacionamentos(Pessoa pessoa) {
        System.out.println("=== SERVICE: Configurando relacionamentos ===");
        
        if (pessoa.getEnderecos() != null) {
            for (Endereco endereco : pessoa.getEnderecos()) {
                if (endereco != null) {
                    endereco.setPessoa(pessoa);
                    System.out.println("✅ Endereço vinculado: " + endereco.getLogradouro());
                }
            }
        }
        
        if (pessoa.getTelefones() != null) {
            for (Telefone telefone : pessoa.getTelefones()) {
                if (telefone != null) {
                    telefone.setPessoa(pessoa);
                    System.out.println("✅ Telefone vinculado: " + telefone.getNumero());
                }
            }
        }
        
        if (pessoa.getDocumentos() != null) {
            for (Documento documento : pessoa.getDocumentos()) {
                if (documento != null) {
                    documento.setPessoa(pessoa);
                    System.out.println("✅ Documento vinculado: " + documento.getNumero());
                }
            }
        }
    }

    public void excluir(Long id) {
        try {
            System.out.println("=== SERVICE: Excluindo pessoa ID: " + id + " ===");
            
            if (id == null) {
                throw new RuntimeException("ID não pode ser nulo");
            }
            
            Optional<Pessoa> pessoaOpt = pessoaRepository.findById(id);
            if (pessoaOpt.isPresent()) {
                pessoaRepository.delete(pessoaOpt.get());
                System.out.println("✅ SERVICE: Pessoa excluída com sucesso");
            } else {
                throw new RuntimeException("Pessoa não encontrada com ID: " + id);
            }
            
        } catch (Exception e) {
            System.out.println("❌ SERVICE: Erro ao excluir pessoa: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao excluir pessoa: " + e.getMessage(), e);
        }
    }

    public boolean existeEmail(String email, Long excludeId) {
        try {
            System.out.println("=== SERVICE: Verificando email: " + email + " ===");
            boolean existe = pessoaRepository.existsByEmail(email, excludeId);
            System.out.println("✅ SERVICE: Email existe? " + existe);
            return existe;
        } catch (Exception e) {
            System.out.println("❌ SERVICE: Erro ao verificar email: " + e.getMessage());
            throw new RuntimeException("Erro ao verificar email: " + e.getMessage(), e);
        }
    }

    public Long contarPessoas() {
        try {
            System.out.println("=== SERVICE: Contando pessoas ===");
            Long count = pessoaRepository.count();
            System.out.println("✅ SERVICE: Total de pessoas: " + count);
            return count;
        } catch (Exception e) {
            System.out.println("❌ SERVICE: Erro ao contar pessoas: " + e.getMessage());
            throw new RuntimeException("Erro ao contar pessoas: " + e.getMessage(), e);
        }
    }

    public List<Pessoa> pesquisarPorNome(String nome) {
        try {
            System.out.println("=== SERVICE: Pesquisando por nome: " + nome + " ===");
            
            if (nome == null || nome.trim().isEmpty()) {
                System.out.println("⚠️ SERVICE: Nome vazio, retornando todas as pessoas");
                return listarTodas();
            }
            
            List<Pessoa> resultado = pessoaRepository.pesquisarPorNome(nome.trim());
            System.out.println("✅ SERVICE: Encontradas " + resultado.size() + " pessoas");
            return resultado;
            
        } catch (Exception e) {
            System.out.println("❌ SERVICE: Erro na pesquisa: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao pesquisar por nome: " + e.getMessage(), e);
        }
    }

    public List<Pessoa> buscarPorEmail(String email) {
        try {
            System.out.println("=== SERVICE: Buscando por email: " + email + " ===");
            
            if (email == null || email.trim().isEmpty()) {
                return new ArrayList<>();
            }
            
            String jpql = "SELECT p FROM Pessoa p WHERE p.email = :email";
            List<Pessoa> resultado = entityManager.createQuery(jpql, Pessoa.class)
                .setParameter("email", email.trim())
                .getResultList();
                
            System.out.println("✅ SERVICE: " + resultado.size() + " pessoas encontradas com o email");
            return resultado;
            
        } catch (Exception e) {
            System.out.println("❌ SERVICE: Erro ao buscar por email: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar por email: " + e.getMessage(), e);
        }
    }

    public void testarConexao() {
        try {
            System.out.println("=== SERVICE: Testando conexão ===");
            Long count = contarPessoas();
            System.out.println("✅ SERVICE: Conexão OK - " + count + " pessoas no banco");
        } catch (Exception e) {
            System.out.println("❌ SERVICE: Falha na conexão: " + e.getMessage());
            throw e;
        }
    }

    public void limparTodosDados() {
        try {
            System.out.println("=== SERVICE: Limpando todos os dados ===");
            List<Pessoa> todasPessoas = listarTodas();
            for (Pessoa pessoa : todasPessoas) {
                excluir(pessoa.getId());
            }
            System.out.println("✅ SERVICE: Todos os dados foram removidos");
        } catch (Exception e) {
            System.out.println("❌ SERVICE: Erro ao limpar dados: " + e.getMessage());
            throw e;
        }
    }
}