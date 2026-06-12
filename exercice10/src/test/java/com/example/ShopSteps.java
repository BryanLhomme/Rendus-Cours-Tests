package com.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShopSteps {

    private UserRepository userRepository;
    private ProductRepository productRepository;

    private UserService userService;
    private ProductService productService;

    private String lastMessage;
    private List<Product> lastProductList;

    @Given("aucun compte existant avec l'identifiant {string}")
    public void aucunCompteExistant(String username) {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
        when(userRepository.existsByUsername(username)).thenReturn(false);
    }

    @Given("un compte existant avec l'identifiant {string}")
    public void unCompteExistant(String username) {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
        when(userRepository.existsByUsername(username)).thenReturn(true);
    }

    @When("je crée un compte avec l'email {string}, l'identifiant {string} et le mot de passe {string}")
    public void jeCreeUnCompte(String email, String username, String password) {
        lastMessage = userService.createAccount(email, username, password);
    }

    @Given("un utilisateur avec l'identifiant {string} et le mot de passe {string}")
    public void unUtilisateur(String username, String password) {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
        User user = new User("user@example.com", username, password);
        when(userRepository.findByUsername(username)).thenReturn(user);
    }

    @When("je me connecte avec l'identifiant {string} et le mot de passe {string}")
    public void jeMeConnecte(String username, String password) {
        lastMessage = userService.login(username, password);
    }

    @Then("je suis connecté avec le message {string}")
    public void jeSuisConnecte(String expected) {
        assertEquals(expected, lastMessage);
    }

    @Then("je vois le message d'erreur {string}")
    public void jeVoisMessageErreur(String expected) {
        assertEquals(expected, lastMessage);
    }

    @Given("des produits contenant le mot-clé {string}")
    public void desProduitsMotCle(String keyword) {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
        List<Product> products = List.of(
                new Product("P1", "Vélo de route", "Sport", 299.0),
                new Product("P2", "Vélo VTT", "Sport", 499.0)
        );
        when(productRepository.findByKeyword(keyword)).thenReturn(products);
    }

    @Given("des produits avec un prix inférieur ou égal à {double}")
    public void desProduitsAvecPrix(double maxPrice) {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
        List<Product> products = List.of(
                new Product("P3", "Stylo", "Papeterie", 2.0),
                new Product("P4", "Cahier", "Papeterie", 5.0)
        );
        when(productRepository.findByMaxPrice(maxPrice)).thenReturn(products);
    }

    @When("je recherche le mot-clé {string}")
    public void jeRechercheMotCle(String keyword) {
        lastProductList = productService.search(keyword);
    }

    @When("je recherche les produits avec un prix maximum de {double}")
    public void jeRecherchePrixMax(double maxPrice) {
        lastProductList = productService.searchByMaxPrice(maxPrice);
    }

    @Then("je vois une liste de résultats non vide")
    public void jeVoisListeNonVide() {
        assertFalse(lastProductList.isEmpty());
    }

    @Then("je reçois la confirmation {string}")
    public void jeRecoisConfirmation(String expected) {
        assertEquals(expected, lastMessage);
    }

    @Then("je reçois l'erreur {string}")
    public void jeRecoisErreur(String expected) {
        assertEquals(expected, lastMessage);
    }
}
