# 🥗 Nutri Express — API REST de Delivery de Comida Saudável

API REST desenvolvida em **Java** com **Spring Boot** para o gerenciamento de pratos de um aplicativo de delivery de comida saudável, seguindo estritamente a arquitetura em camadas (`Controller -> Service -> Repository -> PostgreSQL`) e DTOs com Java Records.

---

## 🛠️ Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot 3 / 4**
- **Spring Data JPA & Hibernate**
- **PostgreSQL** (com suporte a H2 para testes automatizados)
- **Bean Validation** (Hibernate Validator)
- **Lombok**
- **Maven**

---

## 🏗️ Arquitetura do Projeto

```
src/main/java/br/com/nutriexpress/demo/
├── model/
│   └── Prato.java                     # Entidade JPA mapeada para a tabela 'pratos'
├── repository/
│   └── PratoRepository.java           # Interface Spring Data JPA (sem SQL manual)
├── dto/
│   ├── PratoRequestDTO.java           # Java Record com validações Bean Validation
│   ├── PratoResponseDTO.java          # Java Record retornado nas respostas
│   └── PratoValorPatchDTO.java        # Record para atualização parcial de valor
├── service/
│   └── PratoService.java              # Regras de negócio, conversões toEntity() e toDTO()
├── controller/
│   └── PratoController.java           # Endpoints REST e códigos HTTP sem regras de negócio
├── exception/
│   ├── GlobalExceptionHandler.java    # Interceptador global com @RestControllerAdvice
│   ├── PratoNaoEncontradoException.java
│   └── RegraNegocioException.java
└── config/
    └── LoadDatabase.java              # Seed opcional com dados iniciais de exemplo
```

---

## 📋 Regras de Negócio Implementadas

1. **Unicidade de Nome (PratoService)**:
   - Não é permitido cadastrar dois pratos com o mesmo nome (comparação que ignora maiúsculas e minúsculas).
   - Caso tente cadastrar ou atualizar para um nome já existente, a API retorna `400 Bad Request` com mensagem explicativa.
2. **Compatibilidade Calórica por Categoria**:
   - Pratos cadastrados na categoria `'sobremesa saudável'` não podem ultrapassar 350 calorias por porção.

---

## 🚀 Como Executar o Projeto

### 1. Iniciar o PostgreSQL via Docker
Execute o comando abaixo no terminal para subir um contêiner do PostgreSQL:

```bash
docker run -d --name postgres-nutri -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres
```

### 2. Rodar a Aplicação
No diretório raiz do projeto, execute:

```bash
./mvnw spring-boot:run
```

A aplicação iniciará na porta `8080`.

---

## 📡 Endpoints da API

| Verbo | Rota | Descrição | Status de Sucesso | Status de Erro |
|---|---|---|---|---|
| `GET` | `/pratos` | Lista todos os pratos cadastrados | `200 OK` | - |
| `GET` | `/pratos/{id}` | Busca um prato pelo seu ID | `200 OK` | `404 Not Found` |
| `GET` | `/pratos?categoria=vegano` | Filtra pratos por categoria | `200 OK` | - |
| `POST` | `/pratos` | Cadastra um novo prato | `201 Created` | `400 Bad Request` |
| `PUT` | `/pratos/{id}` | Atualiza todos os dados de um prato | `200 OK` | `400 Bad Request` / `404 Not Found` |
| `DELETE` | `/pratos/{id}` | Remove um prato do cardápio | `204 No Content` | `404 Not Found` |
| `PATCH` | `/pratos/{id}/valor` | **(Bônus 1)** Atualiza apenas o preço | `200 OK` | `400 Bad Request` / `404 Not Found` |
| `GET` | `/pratos/calorias?max=500` | **(Bônus 2)** Filtra pratos até X calorias | `200 OK` | `400 Bad Request` |

---

## 🧪 Exemplos de Requisição (Roteiro de Testes)

### 1. Criar Prato (`POST /pratos`)
**Corpo da requisição (JSON):**
```json
{
  "nome": "Bowl Tropical de Quinoa",
  "descricao": "Quinoa real com legumes salteados, castanhas e molho cítrico",
  "valor": 34.90,
  "categoria": "vegano",
  "calorias": 280,
  "quantidade": 380.0,
  "unidadeMedida": "g"
}
```

### 2. Listar Todos (`GET /pratos`)
Retorna a lista em JSON com status `200 OK`.

### 3. Filtrar por Categoria (`GET /pratos?categoria=vegano`)
Retorna apenas pratos cuja categoria seja vegano.

### 4. Buscar por ID (`GET /pratos/1`)
Retorna o prato correspondente. Se o ID não existir (ex: `/pratos/999`), retorna `404 Not Found`.

### 5. Atualizar Prato (`PUT /pratos/1`)
```json
{
  "nome": "Salada Tropical Especial com Tofu",
  "descricao": "Mix de folhas verdes nobres, manga, tomate cereja e tofu defumado grelhado",
  "valor": 36.00,
  "categoria": "vegano",
  "calorias": 240,
  "quantidade": 360.0,
  "unidadeMedida": "g"
}
```

### 6. Atualizar Apenas Valor (`PATCH /pratos/1/valor`)
```json
{
  "valor": 38.50
}
```

### 7. Filtrar por Calorias Máximas (`GET /pratos/calorias?max=300`)
Retorna apenas pratos que possuam até 300 calorias.

### 8. Remover Prato (`DELETE /pratos/1`)
Retorna status `204 No Content`.

### 9. Teste de Validação (`POST /pratos` com dados inválidos)
Enviar payload com nome em branco ou valor negativo retornará `400 Bad Request` com o mapa detalhado de erros de cada campo:
```json
{
  "timestamp": "2026-09-28T19:50:00",
  "status": 400,
  "error": "Erro de validação nos campos informados",
  "errors": {
    "nome": "O nome do prato é obrigatório",
    "valor": "O valor deve ser maior que zero"
  }
}
```
