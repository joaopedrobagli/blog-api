# Blog API

API REST de Blog com autenticação JWT, construída com Java e Spring Boot.

## 🚀 Demo

- **Documentação:** https://blog-api-production-fbbd.up.railway.app/swagger-ui/index.html
- **Base URL:** https://blog-api-production-fbbd.up.railway.app

## 🛠️ Stack

- Java 25 + Spring Boot 4
- Spring Security + JWT
- PostgreSQL
- Docker
- Swagger/OpenAPI

## ⚙️ Como rodar localmente

### Pré-requisitos
- Java 25
- Docker

### Passos

```bash
# Clone o repositório
git clone https://github.com/joaopedrobagli/blog-api.git
cd blog-api/blog-api

# Suba o banco
docker-compose up -d

# Rode a aplicação
./mvnw spring-boot:run
```

Acesse: http://localhost:8080/swagger-ui/index.html

## 📌 Endpoints

### Auth
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | /api/auth/register | Criar conta |
| POST | /api/auth/login | Autenticar |

### Posts
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | /api/posts | Listar posts |
| POST | /api/posts | Criar post |
| GET | /api/posts/{id} | Buscar post |
| PUT | /api/posts/{id} | Editar post |
| DELETE | /api/posts/{id} | Deletar post |

### Comments
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | /api/posts/{id}/comments | Listar comentários |
| POST | /api/posts/{id}/comments | Criar comentário |
| DELETE | /api/posts/{id}/comments/{commentId} | Deletar comentário |

## 🔐 Como testar no Swagger

1. Acesse a documentação
2. Use `POST /api/auth/register` para criar uma conta
3. Copie o token da resposta
4. Clique em **Authorize** e cole o token
5. Teste os endpoints

## 🧪 Testes

```bash
./mvnw test
```

## 👨‍💻 Autor

João Pedro Bagli — [joaopedrobagli.netlify.app](https://joaopedrobagli.netlify.app)