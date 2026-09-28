package br.dtos.prato;

public record PratoResponseDTO(
    Long id,
    String nome,
    String descricao,
    Double preco,
    Integer calorias,
    Boolean disponivel,
    Long categoriaId
) {}
