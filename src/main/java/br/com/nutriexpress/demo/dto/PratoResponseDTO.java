package br.com.nutriexpress.demo.dto;

import br.com.nutriexpress.demo.model.Prato;

import java.math.BigDecimal;

public record PratoResponseDTO(
    Long id,
    String nome,
    String descricao,
    BigDecimal valor,
    String categoria,
    Integer calorias,
    Double quantidade,
    String unidadeMedida
) {
    public static PratoResponseDTO fromEntity(Prato prato) {
        return new PratoResponseDTO(
            prato.getId(),
            prato.getNome(),
            prato.getDescricao(),
            prato.getValor(),
            prato.getCategoria(),
            prato.getCalorias(),
            prato.getQuantidade(),
            prato.getUnidadeMedida()
        );
    }
}
