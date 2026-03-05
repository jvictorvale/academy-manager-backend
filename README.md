# Academy Manager API 🥋

Uma API RESTful desenvolvida para resolver um problema real de gestão em academias de artes marciais (como Jiu-Jitsu), automatizando o controle de alunos, faixas e, principalmente, a frequência (check-in) nas aulas.

Este projeto foi construído com foco em performance e baixo consumo de recursos, projetado para rodar de forma otimizada em ambientes de hospedagem em nuvem (Free Tier).

## 🚀 Tecnologias e Arquitetura

O ecossistema do projeto foi montado com as seguintes tecnologias:

* **Linguagem/Framework:** Java 21 + Spring Boot 3
* **Segurança:** Spring Security + JWT (JSON Web Tokens)
* **Banco de Dados:** PostgreSQL (Serverless via Neon Tech)
* **Migrations:** Flyway (Controle de versão do banco de dados)
* **Documentação:** OpenAPI 3 / Swagger UI
* **Containerização:** Docker
* **CI/CD:** GitHub Actions + Webhooks (Deploy automatizado no Render)

## ⚙️ Principais Funcionalidades

* **Autenticação:** Criação de usuários administradores (Mestres/Recepção) e login via token JWT.
* **Gestão de Alunos:** CRUD completo de alunos, incluindo dados de graduação (faixas).
* **Controle de Frequência (Check-in):** * Registro de presença individual com data retroativa.
  * **Bulk Sync (Sincronização em Massa):** Endpoint otimizado com `@Transactional` para salvar e editar a lista de chamada de uma turma inteira em uma única requisição.

## 🔄 Fluxo de CI/CD (Deploy Contínuo)

O projeto possui um pipeline automatizado configurado no `.github/workflows`. O fluxo funciona da seguinte maneira:
1. Um `git push` na branch `main` aciona o GitHub Actions.
2. O código é compilado e os testes são executados via Maven.
3. Uma imagem Docker é gerada e enviada para o DockerHub.
4. Um Webhook notifica o Render.com, que baixa a nova imagem e reinicia a aplicação com zero downtime manual.
5. O Flyway roda automaticamente na inicialização para aplicar qualquer nova alteração no banco de dados.

## 🛠️ Como rodar o projeto localmente

**Pré-requisitos:** Java 21, Maven e um banco PostgreSQL rodando (pode ser via Docker).

1. Clone o repositório:
   ```bash
   git clone [https://github.com/seu-usuario/academy-manager-backend.git](https://github.com/seu-usuario/academy-manager-backend.git)
