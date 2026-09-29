package br.com.nutriexpress.demo.service;

import br.com.nutriexpress.demo.dto.PratoRequestDTO;
import br.com.nutriexpress.demo.dto.PratoResponseDTO;
import br.com.nutriexpress.demo.exception.PratoNaoEncontradoException;
import br.com.nutriexpress.demo.exception.RegraNegocioException;
import br.com.nutriexpress.demo.model.Prato;
import br.com.nutriexpress.demo.repository.PratoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PratoService {

    private final PratoRepository pratoRepository;

    // Injeção de dependência via construtor conforme solicitado
    public PratoService(PratoRepository pratoRepository) {
        this.pratoRepository = pratoRepository;
    }

    /**
     * REGRA DE NEGÓCIO PRÓPRIA:
     * 1. Não é permitido cadastrar dois pratos com o mesmo nome (ignorando maiúsculas e minúsculas).
     *    Isso evita redundâncias no cardápio de delivery e inconsistência de pedidos para a cozinha.
     * 2. Pratos da categoria 'sobremesa saudável' não podem exceder 350 calorias por porção,
     *    garantindo a proposta fitness do delivery saudável.
     */
    private void validarRegrasDeNegocio(PratoRequestDTO dto, Long pratoIdAtual) {
        // Validação de unicidade de nome
        boolean nomeJaExiste;
        if (pratoIdAtual == null) {
            nomeJaExiste = pratoRepository.existsByNomeIgnoreCase(dto.nome().trim());
        } else {
            nomeJaExiste = pratoRepository.existsByNomeIgnoreCaseAndIdNot(dto.nome().trim(), pratoIdAtual);
        }

        if (nomeJaExiste) {
            throw new RegraNegocioException("Já existe um prato cadastrado com o nome: '" + dto.nome() + "'.");
        }

        // Validação de calorias para sobremesa saudável
        if ("sobremesa saudável".equalsIgnoreCase(dto.categoria().trim()) && dto.calorias() > 350) {
            throw new RegraNegocioException(
                "Pratos na categoria 'sobremesa saudável' não podem ter mais de 350 calorias. Calorias informadas: " + dto.calorias()
            );
        }
    }

    @Transactional
    public PratoResponseDTO criar(PratoRequestDTO dto) {
        validarRegrasDeNegocio(dto, null);
        Prato prato = toEntity(dto);
        Prato salvo = pratoRepository.save(prato);
        return toDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<PratoResponseDTO> listarTodos() {
        return pratoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PratoResponseDTO buscarPorId(Long id) {
        Prato prato = buscarEntidadePorId(id);
        return toDTO(prato);
    }

    @Transactional(readOnly = true)
    public List<PratoResponseDTO> listarPorCategoria(String categoria) {
        return pratoRepository.findByCategoriaIgnoreCase(categoria.trim())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PratoResponseDTO> listarPorCaloriasMax(Integer maxCalorias) {
        return pratoRepository.findByCaloriasLessThanEqual(maxCalorias)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public PratoResponseDTO atualizar(Long id, PratoRequestDTO dto) {
        Prato pratoExistente = buscarEntidadePorId(id);
        validarRegrasDeNegocio(dto, id);

        pratoExistente.setNome(dto.nome().trim());
        pratoExistente.setDescricao(dto.descricao().trim());
        pratoExistente.setValor(dto.valor());
        pratoExistente.setCategoria(dto.categoria().trim());
        pratoExistente.setCalorias(dto.calorias());
        pratoExistente.setQuantidade(dto.quantidade());
        pratoExistente.setUnidadeMedida(dto.unidadeMedida().trim());

        Prato atualizado = pratoRepository.save(pratoExistente);
        return toDTO(atualizado);
    }

    @Transactional
    public PratoResponseDTO atualizarValor(Long id, BigDecimal novoValor) {
        Prato pratoExistente = buscarEntidadePorId(id);
        pratoExistente.setValor(novoValor);
        Prato atualizado = pratoRepository.save(pratoExistente);
        return toDTO(atualizado);
    }

    @Transactional
    public void remover(Long id) {
        Prato prato = buscarEntidadePorId(id);
        pratoRepository.delete(prato);
    }

    private Prato buscarEntidadePorId(Long id) {
        return pratoRepository.findById(id)
                .orElseThrow(() -> new PratoNaoEncontradoException(id));
    }

    // Métodos privados de conversão exigidos pela especificação
    private Prato toEntity(PratoRequestDTO dto) {
        return Prato.builder()
                .nome(dto.nome().trim())
                .descricao(dto.descricao().trim())
                .valor(dto.valor())
                .categoria(dto.categoria().trim())
                .calorias(dto.calorias())
                .quantidade(dto.quantidade())
                .unidadeMedida(dto.unidadeMedida().trim())
                .build();
    }

    private PratoResponseDTO toDTO(Prato prato) {
        return PratoResponseDTO.fromEntity(prato);
    }
}
