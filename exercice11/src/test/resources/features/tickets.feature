Feature: Gestion des tickets de support

  Scenario: Création d'un ticket valide
    Given l'API ne contient aucun ticket
    When je crée un ticket avec le titre "Bug de connexion" et la priorité "HIGH"
    Then la réponse HTTP doit être 201
    And la réponse contient le titre "Bug de connexion"
    And la réponse contient le statut "OPEN"

  Scenario: Résolution d'un ticket existant
    Given l'API ne contient aucun ticket
    And un ticket avec le titre "Panne de messagerie" et la priorité "MEDIUM" a été créé
    When je modifie le statut du ticket vers "RESOLVED"
    Then la réponse HTTP doit être 200
    And la réponse contient le statut "RESOLVED"

  Scenario: Refus de modification d'un ticket déjà résolu
    Given l'API ne contient aucun ticket
    And un ticket résolu avec le titre "Lenteur sur le portail" et la priorité "LOW" existe
    When je modifie le statut du ticket vers "IN_PROGRESS"
    Then la réponse HTTP doit être 409

  Scenario: Consultation d'un ticket inexistant
    Given l'API ne contient aucun ticket
    When je consulte le ticket avec l'identifiant 999
    Then la réponse HTTP doit être 404
