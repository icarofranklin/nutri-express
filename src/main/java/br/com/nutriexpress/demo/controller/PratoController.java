package br.com.nutriexpress.demo.controller;

import br.com.nutriexpress.demo.dto.PratoRequestDTO;
import br.com.nutriexpress.demo.dto.PratoResponseDTO;
import br.com.nutriexpress.demo.dto.PratoValorPatchDTO;
import br.com.nutriexpress.demo.service.PratoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pratos")
public class PratoController {

    private final PratoService pratoService;

    // Injeção de dependência via construtor
    public PratoController(PratoService pratoService) {
        this.pratoService = pratoService;
    }

    /**
     * GET /pratos ou GET /pratos?categoria=vegano
     * Lista todos os pratos ou filtra pela categoria se o parâmetro for informado.
     */
    @GetMapping
    public ResponseEntity<List<PratoResponseDTO>> listarPratos(
            @RequestParam(required = false) String categoria) {
        if (categoria != null && !categoria.isBlank()) {
            return ResponseEntity.ok(pratoService.listarPorCategoria(categoria));
        }
        return ResponseEntity.ok(pratoService.listarTodos());
    }

    /**
     * GET /pratos/{id}
     * Busca um prato pelo seu identificador.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PratoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pratoService.buscarPorId(id));
    }

    /**
     * POST /pratos
     * Cria um novo prato após validação com Bean Validation.
     */
    @PostMapping
    public ResponseEntity<PratoResponseDTO> criar(@Valid @RequestBody PratoRequestDTO dto) {
        PratoResponseDTO novoPrato = pratoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPrato);
    }

    /**
     * PUT /pratos/{id}
     * Atualiza os dados completos de um prato existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PratoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PratoRequestDTO dto) {
        return ResponseEntity.ok(pratoService.atualizar(id, dto));
    }

    /**
     * DELETE /pratos/{id}
     * Remove um prato pelo ID, retornando 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        pratoService.remover(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH /pratos/{id}/valor
     * Atualiza exclusivamente o valor do prato.
     */
    @PatchMapping("/{id}/valor")
    public ResponseEntity<PratoResponseDTO> atualizarValor(
            @PathVariable Long id,
            @Valid @RequestBody PratoValorPatchDTO dto) {
        return ResponseEntity.ok(pratoService.atualizarValor(id, dto.valor()));
    }

    /**
     * GET /pratos/calorias?max=500
     * Filtra e retorna pratos com valor calórico menor ou igual ao valor especificado.
     */
    @GetMapping("/calorias")
    public ResponseEntity<List<PratoResponseDTO>> listarPorCaloriasMax(
            @RequestParam(name = "max") Integer maxCalorias) {
        return ResponseEntity.ok(pratoService.listarPorCaloriasMax(maxCalorias));
    }
}
