package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de RechercheVille")
public class RechercheVilleTest {

    private static final List<String> VILLES = List.of(
            "Paris", "Budapest", "Skopje", "Rotterdam", "Valence", "Vancouver",
            "Amsterdam", "Vienne", "Sydney", "New York", "Londres", "Bangkok",
            "Hong Kong", "Dubaï", "Rome", "Istanbul"
    );

    private RechercheVille rechercheVille;

    @BeforeEach
    void setUp() {
        rechercheVille = new RechercheVille(VILLES);
    }

    // --- Etape 1 : NotFoundException si texte < 2 caractères ---

    @Test
    @DisplayName("Doit lever NotFoundException si le texte est vide")
    void shouldThrowNotFoundExceptionWhenSearchTextIsEmpty() {
        assertThrows(NotFoundException.class, () -> rechercheVille.Rechercher(""));
    }

    @Test
    @DisplayName("Doit lever NotFoundException si le texte fait 1 caractère")
    void shouldThrowNotFoundExceptionWhenSearchTextIsOneChar() {
        assertThrows(NotFoundException.class, () -> rechercheVille.Rechercher("P"));
    }

    // --- Etape 2 : villes commençant par le texte ---

    @Test
    @DisplayName("Doit retourner les villes commençant par 'Va'")
    void shouldReturnCitiesStartingWithVa() throws NotFoundException {
        List<String> result = rechercheVille.Rechercher("Va");
        assertEquals(List.of("Valence", "Vancouver"), result);
    }

    // --- Etape 3 : insensible à la casse ---

    @Test
    @DisplayName("Doit être insensible à la casse (minuscules)")
    void shouldBeCaseInsensitiveWithLowercase() throws NotFoundException {
        List<String> result = rechercheVille.Rechercher("va");
        assertEquals(List.of("Valence", "Vancouver"), result);
    }

    @Test
    @DisplayName("Doit être insensible à la casse (majuscules)")
    void shouldBeCaseInsensitiveWithUppercase() throws NotFoundException {
        List<String> result = rechercheVille.Rechercher("VA");
        assertEquals(List.of("Valence", "Vancouver"), result);
    }

    // --- Etape 4 : correspondance partielle ---

    @Test
    @DisplayName("Doit trouver Budapest via la correspondance partielle 'ape'")
    void shouldFindBudapestWithPartialMatch() throws NotFoundException {
        List<String> result = rechercheVille.Rechercher("ape");
        assertEquals(List.of("Budapest"), result);
    }

    @Test
    @DisplayName("Doit trouver des villes via correspondance partielle insensible à la casse")
    void shouldFindCitiesWithPartialMatchCaseInsensitive() throws NotFoundException {
        List<String> result = rechercheVille.Rechercher("APE");
        assertEquals(List.of("Budapest"), result);
    }

    // --- Etape 5 : '*' retourne toutes les villes ---

    @Test
    @DisplayName("Doit retourner toutes les villes si le texte est '*'")
    void shouldReturnAllCitiesWhenSearchTextIsAsterisk() throws NotFoundException {
        List<String> result = rechercheVille.Rechercher("*");
        assertEquals(VILLES, result);
    }
}
