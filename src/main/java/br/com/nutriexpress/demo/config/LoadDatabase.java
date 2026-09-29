package br.com.nutriexpress.demo.config;

import br.com.nutriexpress.demo.model.Prato;
import br.com.nutriexpress.demo.repository.PratoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(PratoRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                Prato p1 = Prato.builder()
                        .nome("Salada Tropical com Tofu")
                        .descricao("Mix de folhas verdes, manga, tomate cereja e cubos de tofu grelhado")
                        .valor(new BigDecimal("32.50"))
                        .categoria("vegano")
                        .calorias(220)
                        .quantidade(350.0)
                        .unidadeMedida("g")
                        .build();

                Prato p2 = Prato.builder()
                        .nome("Frango Grelhado com Legumes ao Vapor")
                        .descricao("Filé de frango marinado acompanhado de brócolis, cenoura e abobrinha")
                        .valor(new BigDecimal("38.90"))
                        .categoria("fitness")
                        .calorias(410)
                        .quantidade(400.0)
                        .unidadeMedida("g")
                        .build();

                Prato p3 = Prato.builder()
                        .nome("Omelete de Cogumelos e Espinafre")
                        .descricao("Três ovos caipiras recheados com cogumelos frescos e espinafre salteado")
                        .valor(new BigDecimal("29.00"))
                        .categoria("low carb")
                        .calorias(320)
                        .quantidade(300.0)
                        .unidadeMedida("g")
                        .build();

                Prato p4 = Prato.builder()
                        .nome("Mousse de Cacau com Abacate")
                        .descricao("Sobremesa funcional rica em antioxidantes e sem adição de açúcares refinados")
                        .valor(new BigDecimal("18.00"))
                        .categoria("sobremesa saudável")
                        .calorias(210)
                        .quantidade(150.0)
                        .unidadeMedida("g")
                        .build();

                log.info("Pré-carregando: {}", repository.save(p1));
                log.info("Pré-carregando: {}", repository.save(p2));
                log.info("Pré-carregando: {}", repository.save(p3));
                log.info("Pré-carregando: {}", repository.save(p4));
            }
        };
    }
}
