Feature: Recherche de produits

  Scenario: Recherche par mot-clé
    Given des produits contenant le mot-clé "vélo"
    When je recherche le mot-clé "vélo"
    Then je vois une liste de résultats non vide

  Scenario: Recherche par prix maximum
    Given des produits avec un prix inférieur ou égal à 50.0
    When je recherche les produits avec un prix maximum de 50.0
    Then je vois une liste de résultats non vide
