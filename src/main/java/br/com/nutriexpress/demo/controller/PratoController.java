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

    public PratoController(PratoService pratoService) {
        this.pratoService = pratoService;
    }

    // filtro opcional por categoria via query string
    @GetMapping
    public ResponseEntity<List<PratoResponseDTO>> listarPratos(
            @RequestParam(required = false) String categoria) {
        if (categoria != null && !categoria.isBlank()) {
            return ResponseEntity.ok(pratoService.listarPorCategoria(categoria));
        }
        return ResponseEntity.ok(pratoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PratoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pratoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PratoResponseDTO> criar(@Valid @RequestBody PratoRequestDTO dto) {
        PratoResponseDTO novoPrato = pratoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPrato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PratoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PratoRequestDTO dto) {
        return ResponseEntity.ok(pratoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        pratoService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/valor")
    public ResponseEntity<PratoResponseDTO> atualizarValor(
            @PathVariable Long id,
            @Valid @RequestBody PratoValorPatchDTO dto) {
        return ResponseEntity.ok(pratoService.atualizarValor(id, dto.valor()));
    }

    @GetMapping("/calorias")
    public ResponseEntity<List<PratoResponseDTO>> listarPorCaloriasMax(
            @RequestParam(name = "max") Integer maxCalorias) {
        return ResponseEntity.ok(pratoService.listarPorCaloriasMax(maxCalorias));
    }
}
