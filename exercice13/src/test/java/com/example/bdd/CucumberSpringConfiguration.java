package com.example.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Configuration Spring pour Cucumber.
 *
 * @CucumberContextConfiguration indique à Cucumber d'utiliser le contexte Spring.
 * @SpringBootTest charge le contexte Spring complet pour les tests d'intégration.
 * @AutoConfigureMockMvc configure MockMvc automatiquement pour simuler les requêtes HTTP.
 */
@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class CucumberSpringConfiguration {
}
