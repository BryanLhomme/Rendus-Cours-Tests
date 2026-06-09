Feature: Réservation acceptée

  Scenario: Réservation simple acceptée
    Given une salle avec le code "PIKACHU" de nom "Salle Pikachu" et une capacité de 10 personnes
    And aucune réservation existante pour la salle "PIKACHU"
    When je réserve la salle "PIKACHU" pour "alice@example.com" avec 5 participants du "2026-06-10T09:00" au "2026-06-10T11:00"
    Then la réservation est acceptée
    And le message de confirmation est "Réservation confirmée"

  Scenario: Réservation acceptée à capacité maximale
    Given une salle avec le code "RONFLEX" de nom "Salle Ronflex" et une capacité de 8 personnes
    And aucune réservation existante pour la salle "RONFLEX"
    When je réserve la salle "RONFLEX" pour "bob@example.com" avec 8 participants du "2026-06-10T14:00" au "2026-06-10T16:00"
    Then la réservation est acceptée
    And le message de confirmation est "Réservation confirmée"

  Scenario: Réservation acceptée si le créneau commence après une réservation existante
    Given une salle avec le code "DRACAUFEU" de nom "Salle Dracaufeu" et une capacité de 6 personnes
    And une réservation existante pour la salle "DRACAUFEU" du "2026-06-11T08:00" au "2026-06-11T10:00"
    When je réserve la salle "DRACAUFEU" pour "charlie@example.com" avec 3 participants du "2026-06-11T10:00" au "2026-06-11T12:00"
    Then la réservation est acceptée
    And le message de confirmation est "Réservation confirmée"
