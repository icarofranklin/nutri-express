package br.com.nutriexpress.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.nutriexpress.demo.model.Categoria;
import br.com.nutriexpress.demo.service.CategoriaService;
import br.dtos.categoria.CategoriaRequestDTO;
import br.dtos.categoria.CategoriaResponseDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    // O @Autowired é usado para injetar automaticamente a dependência do CategoriaService na classe CategoriaController.
    @Autowired
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        List<CategoriaResponseDTO> categorias = categoriaService.listarCategorias()
            // o stream() é usado para criar um fluxo de dados a partir da lista de categorias 
            // retornada pelo serviço. Isso permite aplicar operações funcionais,
            //  como map(), filter() e collect(), sobre os elementos da lista.    
            .stream()                       
            // o map() é usado para transformar cada objeto Categoria em um objeto CategoriaResponseDTO.
            // Ele aplica o construtor de CategoriaResponseDTO a cada elemento do fluxo,
            //  criando uma nova lista de DTOs.
            .map(CategoriaResponseDTO::new)
            // e o toList() é usado para coletar os elementos do fluxo transformado em uma nova lista. 
            // Ele converte o fluxo de CategoriaResponseDTOs de volta em uma lista concreta, 
            // que pode ser retornada na resposta da API. 
            .toList();                          
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> getCategoriaById(@PathVariable Long id) {
        // pesquisa a categoria pelo ID usando o serviço e cria um DTO de resposta a partir do objeto Categoria retornado.
        CategoriaResponseDTO categoria = new CategoriaResponseDTO(categoriaService.getCategoriaById(id));
        // retorna uma resposta HTTP 200 OK com o DTO de resposta no corpo da resposta.
        return ResponseEntity.ok(categoria);
    }


    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criarCategoria(@Valid @RequestBody CategoriaRequestDTO cat) {

        /*
            O @Valid é usado para indicar que o objeto CategoriaRequestDTO deve ser validado antes de ser processado pelo método.
            O @RequestBody é usado para indicar que o objeto CategoriaRequestDTO deve ser construído
        */

        // cria uma nova categoria usando o serviço e cria um DTO de resposta a partir do objeto Categoria criado.
        Categoria categoriaCriada = categoriaService.criarCategoria(cat);
        // cria um DTO de resposta a partir do objeto Categoria criado.
        CategoriaResponseDTO categoriaResponse = new CategoriaResponseDTO(categoriaCriada);
        // retorna uma resposta HTTP 201 Created com o DTO de resposta no corpo da resposta.
        // podemos usar ResponseEntity.created(categoriaResponse) para retornar um status HTTP 201 Created, 
        // indicando que a categoria foi criada com sucesso.
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO cat) {
        // atualiza a categoria existente usando o serviço e cria um DTO de resposta a partir do objeto Categoria atualizado.
        Categoria categoriaAtualizada = categoriaService.atualizarCategoria(id, cat);
        // cria um DTO de resposta a partir do objeto Categoria atualizado.
        CategoriaResponseDTO categoriaResponse = new CategoriaResponseDTO(categoriaAtualizada);
        // retorna uma resposta HTTP 200 OK com o DTO de resposta no corpo da resposta.
        return ResponseEntity.status(HttpStatus.OK).body(categoriaResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id) {
        // chama o serviço para deletar a categoria existente pelo ID fornecido.
        categoriaService.deletarCategoria(id);
        // retorna uma resposta HTTP 204 No Content, indicando que a operação foi bem-sucedida, mas não há conteúdo a ser retornado.
        return ResponseEntity.noContent().build();
    }

}
