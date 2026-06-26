Feature: Annulation de réservation

  Background:
    Given les données sont réinitialisées

  Scenario: Annulation d'une réservation confirmée
    Given une salle "Salle Fusion" de capacité 10 a été créée
    And une réservation a été faite pour "Marie Martin" du "2024-06-15T10:00:00" au "2024-06-15T12:00:00"
    When j'annule cette réservation
    Then la réponse HTTP doit être 200
    And la réservation a le statut "CANCELLED"

  Scenario: Annulation d'une réservation déjà annulée
    Given une salle "Salle Fusion" de capacité 10 a été créée
    And une réservation a été faite pour "Marie Martin" du "2024-06-15T10:00:00" au "2024-06-15T12:00:00"
    And cette réservation a été annulée
    When j'annule cette réservation
    Then la réponse HTTP doit être 409
