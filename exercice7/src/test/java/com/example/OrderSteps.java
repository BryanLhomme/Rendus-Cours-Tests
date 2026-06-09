package com.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderSteps {
    private ProductRepository productRepository;
    private OrderService orderService;
    private Order order;
    private OrderReceipt receipt;
    private Exception thrownException;

    @Given("un produit avec la référence {string} au prix de {double} avec un stock de {int}")
    public void unProduitAvecLaReference(String ref, double price, int stock) {
        productRepository = mock(ProductRepository.class);
        orderService = new OrderService(productRepository);

        Product product = new Product(ref, "Produit " + ref, price, stock);
        when(productRepository.exists(ref)).thenReturn(true);
        when(productRepository.findByReference(ref)).thenReturn(product);
    }

    @Given("aucun produit avec la référence {string}")
    public void aucunProduitAvecLaReference(String ref) {
        productRepository = mock(ProductRepository.class);
        orderService = new OrderService(productRepository);

        when(productRepository.exists(ref)).thenReturn(false);
    }

    @And("une commande pour le client {string} avec la référence {string} et une quantité de {int}")
    public void uneCommandePourLeClient(String email, String ref, int quantity) {
        order = new Order(email, ref, quantity);
    }

    @When("je passe la commande en tant que client {word}")
    public void jePasseLaCommandeEnTantQueClient(String clientTypeStr) {
        ClientType clientType = ClientType.valueOf(clientTypeStr);
        receipt = orderService.placeOrder(order, clientType);
    }

    @When("je tente de passer la commande en tant que client {word}")
    public void jeTenteDePasLaCommandeEnTantQueClient(String clientTypeStr) {
        ClientType clientType = ClientType.valueOf(clientTypeStr);
        try {
            orderService.placeOrder(order, clientType);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("la commande est acceptée")
    public void laCommandeEstAcceptee() {
        assertNotNull(receipt);
        assertNull(thrownException);
    }

    @And("le montant total est {double}")
    public void leMontantTotalEst(double expectedTotal) {
        assertEquals(expectedTotal, receipt.getTotalAmount(), 0.01);
    }

    @And("le message de confirmation est {string}")
    public void leMessageDeConfirmationEst(String expectedMessage) {
        assertEquals(expectedMessage, receipt.getConfirmationMessage());
    }

    @Then("la commande est refusée avec le message {string}")
    public void laCommandeEstRefuseeAvecLeMessage(String expectedMessage) {
        assertNotNull(thrownException);
        assertEquals(expectedMessage, thrownException.getMessage());
    }
}
