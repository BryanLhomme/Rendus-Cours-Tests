Feature: Operations bancaires
  Cette feature decrit le comportement attendu pour les operations sur les comptes.

  Background:
    Given aucun compte n existe dans l API
    And un compte "COMPTE1-ANANAS" au nom de "Bob Leponge" a ete cree

  Scenario: Depot d argent sur un compte
    When je depose 100.0 sur le compte "COMPTE1-ANANAS"
    Then la reponse HTTP doit etre 200
    And le solde du compte est 100.0

  Scenario: Retrait avec fonds suffisants
    Given le compte "COMPTE1-ANANAS" a ete credite de 200.0
    When je retire 80.0 du compte "COMPTE1-ANANAS"
    Then la reponse HTTP doit etre 200
    And le solde du compte est 120.0

  Scenario: Retrait avec fonds insuffisants
    When je retire 9999.99 du compte "COMPTE1-ANANAS"
    Then la reponse HTTP doit etre 409
    And la reponse contient un message d erreur

  Scenario: Virement entre deux comptes
    Given le compte "COMPTE1-ANANAS" a ete credite de 500.0
    And un compte "COMPTE2-ROCHER" au nom de "Patrick Etoile" a ete cree
    When je vire 200.0 de "COMPTE1-ANANAS" vers "COMPTE2-ROCHER"
    Then la reponse HTTP doit etre 200

  Scenario: Virement refuse pour solde insuffisant
    Given un compte "COMPTE2-ROCHER" au nom de "Patrick Etoile" a ete cree
    When je vire 9999.99 de "COMPTE1-ANANAS" vers "COMPTE2-ROCHER"
    Then la reponse HTTP doit etre 409
    And la reponse contient un message d erreur
