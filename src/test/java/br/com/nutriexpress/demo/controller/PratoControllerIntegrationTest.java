package br.com.nutriexpress.demo.controller;

import br.com.nutriexpress.demo.dto.PratoRequestDTO;
import br.com.nutriexpress.demo.dto.PratoValorPatchDTO;
import br.com.nutriexpress.demo.repository.PratoRepository;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PratoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PratoRepository pratoRepository;

    @BeforeEach
    void setUp() {
        pratoRepository.deleteAll();
    }

    private PratoRequestDTO criarDTOValido(String nome, String categoria, int calorias) {
        return new PratoRequestDTO(
                nome,
                "Descrição detalhada e deliciosa do prato",
                new BigDecimal("29.90"),
                categoria,
                calorias,
                350.0,
                "g"
        );
    }

    @Test
    @DisplayName("POST /pratos deve cadastrar com 201 Created")
    void deveCriarPratoComSucesso() throws Exception {
        PratoRequestDTO dto = criarDTOValido("Prato Vegano Teste", "vegano", 250);

        mockMvc.perform(post("/pratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", is("Prato Vegano Teste")))
                .andExpect(jsonPath("$.categoria", is("vegano")))
                .andExpect(jsonPath("$.valor", is(29.90)));
    }

    @Test
    @DisplayName("GET /pratos deve retornar 200 OK com lista")
    void deveListarTodosOsPratos() throws Exception {
        mockMvc.perform(post("/pratos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criarDTOValido("Prato 1", "fitness", 300))));

        mockMvc.perform(get("/pratos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome", is("Prato 1")));
    }

    @Test
    @DisplayName("GET /pratos?categoria=vegano deve filtrar por categoria")
    void deveFiltrarPorCategoria() throws Exception {
        mockMvc.perform(post("/pratos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criarDTOValido("Salada", "vegano", 150))));

        mockMvc.perform(post("/pratos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criarDTOValido("Carne", "fitness", 500))));

        mockMvc.perform(get("/pratos?categoria=vegano"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].categoria", is("vegano")));
    }

    @Test
    @DisplayName("GET /pratos/{id} existente deve retornar 200 OK")
    void deveBuscarPratoPorId() throws Exception {
        String response = mockMvc.perform(post("/pratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarDTOValido("Prato Unico", "low carb", 200))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/pratos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.nome", is("Prato Unico")));
    }

    @Test
    @DisplayName("GET /pratos/{id} inexistente deve retornar 404 Not Found")
    void deveRetornar404AoBuscarIdInexistente() throws Exception {
        mockMvc.perform(get("/pratos/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Recurso não encontrado")));
    }

    @Test
    @DisplayName("PUT /pratos/{id} deve atualizar com 200 OK")
    void deveAtualizarPratoComSucesso() throws Exception {
        String response = mockMvc.perform(post("/pratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarDTOValido("Prato Original", "fitness", 300))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        PratoRequestDTO dtoAtualizado = new PratoRequestDTO(
                "Prato Modificado",
                "Nova descrição",
                new BigDecimal("35.00"),
                "fitness",
                320,
                380.0,
                "g"
        );

        mockMvc.perform(put("/pratos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Prato Modificado")))
                .andExpect(jsonPath("$.valor", is(35.00)));
    }

    @Test
    @DisplayName("DELETE /pratos/{id} deve remover com 204 No Content")
    void deveRemoverPratoComSucesso() throws Exception {
        String response = mockMvc.perform(post("/pratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarDTOValido("Para Deletar", "vegano", 100))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/pratos/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/pratos/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /pratos com dados inválidos deve retornar 400 Bad Request")
    void deveRetornar400ParaDadosInvalidos() throws Exception {
        PratoRequestDTO dtoInvalido = new PratoRequestDTO(
                "", // nome vazio
                "", // descrição vazia
                new BigDecimal("-10.00"), // valor negativo
                "", // categoria vazia
                -5, // calorias negativas
                -10.0, // quantidade negativa
                "" // unidade vazia
        );

        mockMvc.perform(post("/pratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", notNullValue()))
                .andExpect(jsonPath("$.errors.nome", notNullValue()))
                .andExpect(jsonPath("$.errors.valor", notNullValue()));
    }

    @Test
    @DisplayName("Regra de negócio: não permitir pratos com mesmo nome")
    void deveBloquearPratosComMesmoNome() throws Exception {
        PratoRequestDTO dto = criarDTOValido("Prato Duplicado", "vegano", 200);

        mockMvc.perform(post("/pratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/pratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Violação de regra de negócio")));
    }

    @Test
    @DisplayName("Regra de negócio: sobremesa saudável não pode ter > 350 calorias")
    void deveBloquearSobremesaSaudavelComExcessoDeCalorias() throws Exception {
        PratoRequestDTO dto = criarDTOValido("Torta Doce", "sobremesa saudável", 450);

        mockMvc.perform(post("/pratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Violação de regra de negócio")));
    }

    @Test
    @DisplayName("Desafio Extra 1: PATCH /pratos/{id}/valor atualiza somente o preço")
    void deveAtualizarApenasPrecoViaPatch() throws Exception {
        String response = mockMvc.perform(post("/pratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarDTOValido("Prato Preco", "fitness", 300))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        PratoValorPatchDTO patchDTO = new PratoValorPatchDTO(new BigDecimal("49.90"));

        mockMvc.perform(patch("/pratos/" + id + "/valor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor", is(49.90)))
                .andExpect(jsonPath("$.nome", is("Prato Preco")));
    }

    @Test
    @DisplayName("Desafio Extra 2: GET /pratos/calorias?max=X filtra por limite calórico")
    void deveFiltrarPorCaloriasMaximas() throws Exception {
        mockMvc.perform(post("/pratos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criarDTOValido("Prato Leve", "vegano", 180))));

        mockMvc.perform(post("/pratos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criarDTOValido("Prato Pesado", "fitness", 650))));

        mockMvc.perform(get("/pratos/calorias?max=300"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome", is("Prato Leve")));
    }
}
