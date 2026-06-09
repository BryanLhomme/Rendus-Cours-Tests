Feature: Commande refusée

  Scenario: Commande refusée si le produit est inconnu
    Given aucun produit avec la référence "INCONNU"
    And une commande pour le client "alice@example.com" avec la référence "INCONNU" et une quantité de 1
    When je tente de passer la commande en tant que client STANDARD
    Then la commande est refusée avec le message "Produit introuvable : INCONNU"

  Scenario: Commande refusée si le stock est insuffisant
    Given un produit avec la référence "POKEMON-67" au prix de 20.0 avec un stock de 2
    And une commande pour le client "dave@example.com" avec la référence "POKEMON-67" et une quantité de 5
    When je tente de passer la commande en tant que client STANDARD
    Then la commande est refusée avec le message "Stock insuffisant pour le produit POKEMON-67"
