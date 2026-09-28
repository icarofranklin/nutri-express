package br.dtos.prato;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PratoRequestDTO(

    @NotBlank(message = "O nome do prato é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    String nome,

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    String descricao,

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser maior que zero")
    Double preco,

    @NotNull(message = "As calorias são obrigatórias")
    @Min(value = 0, message = "As calorias não podem ser negativas")
    @Max(value = 5000, message = "As calorias não podem exceder 5000")
    Integer calorias,

    @NotNull(message = "O campo 'disponível' é obrigatório")
    Boolean disponivel,

    @NotNull(message = "A categoria é obrigatória")
    Long categoriaId

) {}