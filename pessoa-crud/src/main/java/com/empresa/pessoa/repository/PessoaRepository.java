package com.empresa.pessoa.repository;

import com.empresa.pessoa.model.Pessoa;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Stateless
public class PessoaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Pessoa> findAll() {
        try {
            System.out.println("=== REPOSITORY: FIND ALL ===");
            

            List<Pessoa> pessoas = entityManager.createQuery("SELECT p FROM Pessoa p", Pessoa.class)
                    .getResultList();
            
            System.out.println("✅ REPOSITORY: " + pessoas.size() + " pessoas encontradas");
            return pessoas;
            
        } catch (Exception e) {
            System.out.println("❌ REPOSITORY ERRO: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    public Optional<Pessoa> findById(Long id) {
        try {
            System.out.println("=== REPOSITORY: FIND BY ID " + id + " ===");
            

            Pessoa pessoa = entityManager.find(Pessoa.class, id);
            
            if (pessoa != null) {

                if (pessoa.getEnderecos() != null) {
                    pessoa.getEnderecos().size();
                }
                if (pessoa.getTelefones() != null) {
                    pessoa.getTelefones().size();
                }
                if (pessoa.getDocumentos() != null) {
                    pessoa.getDocumentos().size();
                }
                
                System.out.println("✅ REPOSITORY: Pessoa encontrada - " + pessoa.getNome());
                return Optional.of(pessoa);
            }
            
            System.out.println("❌ REPOSITORY: Pessoa não encontrada");
            return Optional.empty();
            
        } catch (Exception e) {
            System.out.println("❌ REPOSITORY ERRO: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Pessoa save(Pessoa pessoa) {
        try {
            System.out.println("=== REPOSITORY: SAVE ===");
            System.out.println("ID: " + pessoa.getId() + ", Nome: " + pessoa.getNome());
            
            if (pessoa.getId() == null) {
                System.out.println("Persistindo nova pessoa...");
                entityManager.persist(pessoa);
                entityManager.flush();
                System.out.println("✅ Nova pessoa persistida - ID: " + pessoa.getId());
                return pessoa;
            } else {
                System.out.println("Atualizando pessoa existente...");
                Pessoa merged = entityManager.merge(pessoa);
                entityManager.flush();
                System.out.println("✅ Pessoa atualizada");
                return merged;
            }
        } catch (Exception e) {
            System.out.println("❌ REPOSITORY ERRO AO SALVAR: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar pessoa: " + e.getMessage(), e);
        }
    }

    public void delete(Pessoa pessoa) {
        try {
            System.out.println("=== REPOSITORY: DELETE ID " + pessoa.getId() + " ===");
            
            if (entityManager.contains(pessoa)) {
                entityManager.remove(pessoa);
            } else {
                Pessoa managedPessoa = entityManager.merge(pessoa);
                entityManager.remove(managedPessoa);
            }
            
            entityManager.flush();
            System.out.println("✅ Pessoa excluída");
            
        } catch (Exception e) {
            System.out.println("❌ REPOSITORY ERRO AO EXCLUIR: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao excluir pessoa: " + e.getMessage(), e);
        }
    }

    public boolean existsByEmail(String email, Long excludeId) {
        try {
            String jpql = "SELECT COUNT(p) FROM Pessoa p WHERE p.email = :email";
            
            if (excludeId != null && excludeId > 0) {
                jpql += " AND p.id != :excludeId";
            }
            
            TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class)
                    .setParameter("email", email);
            
            if (excludeId != null && excludeId > 0) {
                query.setParameter("excludeId", excludeId);
            }
            
            Long count = query.getSingleResult();
            return count > 0;
            
        } catch (Exception e) {
            System.out.println("❌ REPOSITORY ERRO AO VERIFICAR EMAIL: " + e.getMessage());
            throw new RuntimeException("Erro ao verificar email: " + e.getMessage(), e);
        }
    }

    public Long count() {
        try {
            return entityManager.createQuery("SELECT COUNT(p) FROM Pessoa p", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println("❌ REPOSITORY ERRO AO CONTAR: " + e.getMessage());
            throw new RuntimeException("Erro ao contar pessoas: " + e.getMessage(), e);
        }
    }

    public List<Pessoa> pesquisarPorNome(String nome) {
        try {
            return entityManager.createQuery(
                "SELECT p FROM Pessoa p WHERE LOWER(p.nome) LIKE LOWER(:nome)", Pessoa.class)
                .setParameter("nome", "%" + nome + "%")
                .getResultList();
        } catch (Exception e) {
            System.out.println("❌ REPOSITORY ERRO NA PESQUISA: " + e.getMessage());
            throw new RuntimeException("Erro ao pesquisar por nome: " + e.getMessage(), e);
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}