Feature: Réservations acceptées

  Background:
    Given les données sont réinitialisées

  Scenario: Réservation valide sur une salle disponible
    Given une salle "Salle Fusion" de capacité 10 a été créée
    When je réserve cette salle pour "Marie Martin" du "2024-06-15T10:00:00" au "2024-06-15T12:00:00"
    Then la réponse HTTP doit être 201
    And la réservation a le statut "CONFIRMED"
    And la réservation est au nom de "Marie Martin"
