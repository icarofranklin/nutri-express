package br.com.nutriexpress.demo.controller;

import br.com.nutriexpress.demo.model.Prato;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pratos")
public class PratoController {
    private final List<Prato> pratos = new ArrayList<>();
    private Long proximoId = 1L;

    @GeMapping 
    public ResponseEntity<List<Pratos>> listarTodos(){
        return ResponseEntity.ok(pratos);
    }

    @PostMapping
    public ResponseEntity<Pratos> criar(@RequestBody Prato prato) {
        prato.setId(proximoId++);
        pratos.add(prato);
        return ResponseEntity.status(HttpStatus.CREATED).body(prato);
    }
}
