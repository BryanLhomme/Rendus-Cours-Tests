package com.example.bdd;

import com.example.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.hamcrest.Matchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Step definitions Cucumber pour l'API bancaire.
 *
 * Chaque méthode Java est reliée à une phrase du fichier .feature.
 * Les expressions {string} et {double} permettent de récupérer des valeurs du scénario.
 *
 * MockMvc simule des requêtes HTTP sans démarrer de vrai serveur.
 * Le repository est injecté pour remettre l'état à zéro entre les scénarios.
 */
public class AccountSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions lastResult;

    @Given("aucun compte n existe dans l API")
    public void noAccountExists() {
        accountRepository.deleteAll();
    }

    @Given("un compte {string} au nom de {string} a ete cree")
    public void anAccountExists(String number, String holder) throws Exception {
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"number\":\"" + number + "\",\"holder\":\"" + holder + "\"}"));
    }

    @Given("le compte {string} a ete credite de {double}")
    public void accountIsCreditedWith(String number, double amount) throws Exception {
        mockMvc.perform(post("/api/accounts/" + number + "/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":" + amount + "}"));
    }

    @When("je cree un compte avec le numero {string} et le titulaire {string}")
    public void createAccount(String number, String holder) throws Exception {
        lastResult = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"number\":\"" + number + "\",\"holder\":\"" + holder + "\"}"));
    }

    @When("je consulte le compte {string}")
    public void getAccount(String number) throws Exception {
        lastResult = mockMvc.perform(get("/api/accounts/" + number));
    }

    @When("je depose {double} sur le compte {string}")
    public void deposit(double amount, String number) throws Exception {
        lastResult = mockMvc.perform(post("/api/accounts/" + number + "/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":" + amount + "}"));
    }

    @When("je retire {double} du compte {string}")
    public void withdraw(double amount, String number) throws Exception {
        lastResult = mockMvc.perform(post("/api/accounts/" + number + "/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":" + amount + "}"));
    }

    @When("je vire {double} de {string} vers {string}")
    public void transfer(double amount, String fromNumber, String toNumber) throws Exception {
        lastResult = mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fromNumber\":\"" + fromNumber + "\",\"toNumber\":\"" + toNumber + "\",\"amount\":" + amount + "}"));
    }

    @Then("la reponse HTTP doit etre {int}")
    public void responseStatusShouldBe(int expectedStatus) throws Exception {
        lastResult.andExpect(status().is(expectedStatus));
    }

    @And("le compte a le numero {string}")
    public void accountShouldHaveNumber(String expectedNumber) throws Exception {
        lastResult.andExpect(jsonPath("$.number").value(expectedNumber));
    }

    @And("le compte a le titulaire {string}")
    public void accountShouldHaveHolder(String expectedHolder) throws Exception {
        lastResult.andExpect(jsonPath("$.holder").value(expectedHolder));
    }

    @And("le solde du compte est {double}")
    public void accountBalanceShouldBe(double expectedBalance) throws Exception {
        lastResult.andExpect(jsonPath("$.balance", Matchers.closeTo(expectedBalance, 0.001)));
    }

    @And("la reponse contient un message d erreur")
    public void responseShouldContainErrorMessage() throws Exception {
        lastResult.andExpect(jsonPath("$.message").exists());
    }
}
