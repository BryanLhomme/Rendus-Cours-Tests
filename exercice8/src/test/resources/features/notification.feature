Feature: Notification de réservation

  Scenario: Notification envoyée en cas de succès
    Given une salle avec le code "EVOLI" de nom "Salle Evoli" et une capacité de 15 personnes
    And aucune réservation existante pour la salle "EVOLI"
    When je réserve la salle "EVOLI" pour "alice@example.com" avec 6 participants du "2026-06-12T10:00" au "2026-06-12T12:00"
    Then une notification de confirmation est envoyée à "alice@example.com" pour la salle "EVOLI"

  Scenario: Notification non envoyée en cas d'échec
    Given aucune salle avec le code "RONDOUDOU"
    When je tente de réserver la salle "RONDOUDOU" pour "bob@example.com" avec 4 participants du "2026-06-12T14:00" au "2026-06-12T16:00"
    Then aucune notification n'est envoyée
