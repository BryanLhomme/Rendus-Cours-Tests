Feature: Réservations refusées

  Background:
    Given les données sont réinitialisées

  Scenario: Salle inexistante
    When je réserve la salle 999 pour "Marie Martin" du "2024-06-15T10:00:00" au "2024-06-15T12:00:00"
    Then la réponse HTTP doit être 404

  Scenario: Créneau déjà réservé
    Given une salle "Salle Fusion" de capacité 10 a été créée
    And une réservation a été faite pour "Jean Dupont" du "2024-06-15T10:00:00" au "2024-06-15T12:00:00"
    When je réserve cette salle pour "Marie Martin" du "2024-06-15T11:00:00" au "2024-06-15T13:00:00"
    Then la réponse HTTP doit être 409

  Scenario: Date de fin avant la date de début
    Given une salle "Salle Fusion" de capacité 10 a été créée
    When je réserve cette salle pour "Marie Martin" du "2024-06-15T12:00:00" au "2024-06-15T10:00:00"
    Then la réponse HTTP doit être 400
