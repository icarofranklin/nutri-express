package br.com.nutriexpress.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PratoRequestDTO(
    @NotBlank(message = "O nome do prato é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    String nome,

    @NotBlank(message = "A descrição do prato é obrigatória")
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    String descricao,

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    BigDecimal valor,

    @NotBlank(message = "A categoria é obrigatória (ex: 'vegano', 'low carb', 'fitness', 'sobremesa saudável')")
    String categoria,

    @NotNull(message = "As calorias são obrigatórias")
    @Min(value = 0, message = "As calorias não podem ser negativas")
    Integer calorias,

    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    Double quantidade,

    @NotBlank(message = "A unidade de medida é obrigatória (ex: 'g' ou 'ml')")
    String unidadeMedida
) {}
