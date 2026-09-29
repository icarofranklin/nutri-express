# Nutri Express - API REST Delivery

API REST desenvolvida em Spring Boot para gestao de pratos de um delivery de comida saudavel. O projeto segue arquitetura em camadas (Controller, Service, Repository, Database), utilizando DTOs com Java Records e persistencia com Spring Data JPA e PostgreSQL.

## Tecnologias

- Java 17+
- Spring Boot
- Spring Data JPA
- PostgreSQL (e H2 para testes)
- Hibernate Validator (Bean Validation)

## Como rodar o projeto

1. Subir o banco de dados PostgreSQL (via Docker):

```bash
docker run -d --name postgres-nutri -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres
```

2. Executar a aplicacao:

```bash
./mvnw spring-boot:run
```

A API ficara disponivel em `http://localhost:8080`.

Para rodar os testes automatizados:

```bash
./mvnw test
```

### Endpoints da API

- GET /pratos - Lista todos os pratos cadastrados (200 OK)
- GET /pratos/{id} - Busca um prato pelo id (200 OK ou 404 Not Found)
- GET /pratos?categoria={nome} - Filtra pratos por categoria via query param (200 OK)
- POST /pratos - Cria um novo prato com validacao de campos (201 Created)
- PUT /pratos/{id} - Atualiza um prato existente (200 OK ou 404 Not Found)
- DELETE /pratos/{id} - Remove um prato pelo id (204 No Content ou 404 Not Found)
- PATCH /pratos/{id}/valor - Atualiza somente o preco do prato (200 OK ou 404 Not Found)
- GET /pratos/calorias?max={valor} - Filtra pratos com valor calorico menor ou igual ao parametro (200 OK)
- Tratamento global de erros com GlobalExceptionHandler:
  - 400 Bad Request com mapa de campos invalidos (MethodArgumentNotValidException)
  - 404 Not Found com mensagem descritiva (PratoNaoEncontradoException)
  - 400 Bad Request para violacao de regra de negocio (RegraNegocioException)

## Regras de Negocio Implementadas

- Unicidade de nome: nao e permitido cadastrar ou atualizar dois pratos com o mesmo nome (ignorando maiusculas e minusculas).
- Compatibilidade calorica: pratos da categoria "sobremesa saudavel" nao podem exceder 350 calorias por porcao.

Ambas as regras estao implementadas e comentadas na classe PratoService.

## DTOs e Arquitetura

- PratoRequestDTO: record com validacoes Bean Validation (@NotBlank, @NotNull, @DecimalMin, @Positive, etc.).
- PratoResponseDTO: record de saida. Mantem todos os campos do prato (incluindo calorias, peso e unidade) para que o cliente tenha acesso a ficha nutricional completa no delivery.
- As conversoes toEntity() e toDTO() sao feitas de forma manual e privada no PratoService, isolando a entidade Prato do PratoController.
