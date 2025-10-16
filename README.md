# 👨‍💻 Teste de Desenvolvimento Java — Pessoa CRUD

## 💻 Sobre o Projeto

Este projeto foi desenvolvido como parte de um **teste técnico de desenvolvimento Java**, com o objetivo de demonstrar conhecimentos práticos nas principais tecnologias do ecossistema **Java EE (Jakarta EE)**, aplicando boas práticas de desenvolvimento, arquitetura em camadas e persistência com JPA/Hibernate.

A aplicação consiste em um **CRUD completo de Pessoas**, com suporte a **endereços, telefones e documentos** relacionados.

---

## 🧠 Funcionalidades

✅ **Listagem de Pessoas** — Mostra todas as pessoas cadastradas, com opções de editar e excluir.  
✅ **Cadastro de Pessoa** — Permite criar novas pessoas.  
✅ **Edição de Pessoa** — Atualiza dados de uma pessoa existente.  
✅ **Remoção de Pessoa** — Exclui um registro.  
✅ **Gerenciamento dinâmico** de endereços, telefones e documentos na mesma tela de cadastro.  

---

## 🧩 Estrutura do Projeto

pessoa-crud/
 ├── 📁 src/main/java/com/empresa/pessoa/

│├── 🎮 controller/

│ │ └── PessoaController.java

│ ├── ⚙️ service/

│ │ └── PessoaService.java

│ ├── 🗄️ repository/

│ │ └── PessoaRepository.java

│ └── 🏷️ model/

│ ├── Pessoa.java

│ ├── Endereco.java

│ ├── Telefone.java

│ └── Documento.java

├── 📁 src/main/resources/META-INF/

│ └── persistence.xml

├── 📁 src/main/webapp/

│ ├── listagem.xhtml

│ ├── cadastro.xhtml

│ └── WEB-INF/

│ └── web.xml

├── 📄 pom.xml

└── 📄 README.md




---
## 📊 Fluxo da aplicação:
Frontend (JSF/XHTML) → Controller → Service → Repository → Model → Banco de Dados

## 📱 URLs Principais
Página	Descrição	URL
🏠 Home/Listagem	Lista todas as pessoas	http://localhost:8080/pessoa-crud/index.xhtml

➕ Cadastro	Formulário para nova pessoa	http://localhost:8080/pessoa-crud/cadastro.xhtml




---

## 🚀 Tecnologias Utilizadas
🔧 Backend

✅ **JAVA 11**

✅ **Jakarta EE 9**

✅ **Hibernate 6.2 (JPA)**

✅ **CDI 3.0 — Injeção de dependência**

✅ **JTA — Controle transacional**  

## 🎨 Frontend

✅ **PrimeFaces 12**

✅ **JSF 3.0**

✅ **CSS3 + JavaScript**


## 🗄️ Infraestrutura

✅ **WildFly 26**

✅ **MySQL 8**

✅ **Maven 3.8t**


## ⚙️ Instalação e Configuração
✅ Pré-requisitos

✅ **Java 11 ou superior**

✅ **WildFly 26+**

✅ **MySQL 8+**

✅ **Maven 3.6+**

## 1️⃣ Criar o Banco de Dados
CREATE DATABASE pessoa_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER 'pessoa_user'@'localhost' IDENTIFIED BY 'pessoa123';
GRANT ALL PRIVILEGES ON pessoa_db.* TO 'pessoa_user'@'localhost';
FLUSH PRIVILEGES;
## 2️⃣ Configurar o DataSource no WildFly

Adicione no arquivo standalone.xml (ou via CLI):

<datasource jndi-name="java:jboss/datasources/MySQLDS"
            pool-name="MySQLDS" 
            enabled="true">
    <connection-url>jdbc:mysql://localhost:3306/pessoa_db</connection-url>
    <driver>mysql</driver>
    <security>
        <user-name>pessoa_user</user-name>
        <password>pessoa123</password>
    </security>
    <validation>
        <check-valid-connection-sql>SELECT 1</check-valid-connection-sql>
        <background-validation>true</background-validation>
        <background-validation-millis>60000</background-validation-millis>
    </validation>
</datasource>

Adicione o driver MySQL:

module add --name=com.mysql --resources=/path/to/mysql-connector-java-8.0.33.jar --dependencies=javax.api,javax.transaction.api
/subsystem=datasources/jdbc-driver=mysql:add(driver-name=mysql,driver-module-name=com.mysql,driver-class-name=com.mysql.cj.jdbc.Driver)

## 3️⃣ Clonar e Rodar o Projeto
 Clone o repositório
git clone https://github.com/seu-usuario/pessoa-crud.git
cd pessoa-crud

## Build e deploy automático
mvn clean package wildfly:deploy

# Ou manualmente
mvn clean package
cp target/pessoa-crud.war $WILDFLY_HOME/standalone/deployments/

## 🔧 Configurações do Hibernate
Desenvolvimento
jakarta.persistence.schema-generation.database.action = drop-and-create
hibernate.show_sql = true
hibernate.format_sql = true

## Produção
jakarta.persistence.schema-generation.database.action = validate
hibernate.show_sql = false

## 🌐 Variáveis de Ambiente
Variável	Valor Padrão	Descrição
DB_HOST	localhost	Host do banco de dados
DB_PORT	3306	Porta MySQL
DB_NAME	pessoa_db	Nome do banco
DB_USER	pessoa_user	Usuário do banco
DB_PASS	pessoa123	Senha do banco
## 🧪 Testes

## Testes manuais realizados via interface PrimeFaces (CRUD completo).

Banco gerado automaticamente pelo Hibernate com base nas entidades JPA.

## 🧱 Modelo de Dados (Entidades)

Pessoa → possui listas de Endereço, Telefone e Documento

Endereço → atributos como CEP, cidade, estado, etc.

Telefone → tipo, DDD e número

Documento → tipo (RG/CPF) e número

Relações:

@OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)

## 🧾 Licença

Projeto desenvolvido apenas para fins de avaliação técnica.
Todos os direitos reservados © 2025.

💬 Autor: Caio Cezar Bezerra
📧 caiocezar@example.com
 (substitua pelo seu real)
🌐 LinkedIn
 | GitHub
--
## 🏗️ Arquitetura

### 📐 Diagrama de Camadas

```mermaid
graph TB
    A[🎨 Frontend<br>JSF/XHTML] --> B[🎮 Controller<br>PessoaController]
    B --> C[⚙️ Service<br>PessoaService]
    C --> D[🗄️ Repository<br>PessoaRepository]
    D --> E[🏷️ Model<br>Entidades JPA]
    E --> F[📊 Database<br>MySQL]
    
    style A fill:#4CAF50,color:white
    style B fill:#2196F3,color:white
    style C fill:#FF9800,color:white
    style D fill:#9C27B0,color:white
    style E fill:#607D8B,color:white
    style F fill:#F44336,color:white```


