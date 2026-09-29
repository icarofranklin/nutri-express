package br.com.nutriexpress.demo.repository;

import br.com.nutriexpress.demo.model.Prato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PratoRepository extends JpaRepository<Prato, Long> {

    List<Prato> findByCategoria(String categoria);

    List<Prato> findByCategoriaIgnoreCase(String categoria);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    List<Prato> findByCaloriasLessThanEqual(Integer maxCalorias);
}
