Feature: Commande acceptée

  Scenario: Client STANDARD passe une commande sans remise
    Given un produit avec la référence "IPHONE17" au prix de 1000.0 avec un stock de 10
    And une commande pour le client "alice@example.com" avec la référence "IPHONE17" et une quantité de 2
    When je passe la commande en tant que client STANDARD
    Then la commande est acceptée
    And le montant total est 2000.0
    And le message de confirmation est "Commande confirmée"

  Scenario: Client PREMIUM passe une commande avec 10% de remise
    Given un produit avec la référence "MACBOOK" au prix de 500.0 avec un stock de 5
    And une commande pour le client "bob@example.com" avec la référence "MACBOOK" et une quantité de 1
    When je passe la commande en tant que client PREMIUM
    Then la commande est acceptée
    And le montant total est 450.0
    And le message de confirmation est "Commande confirmée"

  Scenario: Client VIP passe une commande avec 20% de remise
    Given un produit avec la référence "TELEDUSEIGNEUR" au prix de 800.0 avec un stock de 3
    And une commande pour le client "charlie@example.com" avec la référence "TELEDUSEIGNEUR" et une quantité de 1
    When je passe la commande en tant que client VIP
    Then la commande est acceptée
    And le montant total est 640.0
    And le message de confirmation est "Commande confirmée"
