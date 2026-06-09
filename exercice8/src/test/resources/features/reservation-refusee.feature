Feature: Réservation refusée

  Scenario: Réservation refusée si la salle est inconnue
    Given aucune salle avec le code "MEWTWO"
    When je tente de réserver la salle "MEWTWO" pour "alice@example.com" avec 5 participants du "2026-06-10T09:00" au "2026-06-10T11:00"
    Then la réservation est refusée avec le message "Salle inconnue : MEWTWO"

  Scenario: Réservation refusée si la capacité est insuffisante
    Given une salle avec le code "CARAPUCE" de nom "Salle Carapuce" et une capacité de 4 personnes
    And aucune réservation existante pour la salle "CARAPUCE"
    When je tente de réserver la salle "CARAPUCE" pour "bob@example.com" avec 10 participants du "2026-06-10T09:00" au "2026-06-10T11:00"
    Then la réservation est refusée avec le message "Capacité insuffisante"

  Scenario: Réservation refusée si la période est invalide
    Given une salle avec le code "BULBIZARRE" de nom "Salle Bulbizarre" et une capacité de 10 personnes
    And aucune réservation existante pour la salle "BULBIZARRE"
    When je tente de réserver la salle "BULBIZARRE" pour "charlie@example.com" avec 3 participants du "2026-06-10T11:00" au "2026-06-10T09:00"
    Then la réservation est refusée avec le message "Période invalide"

  Scenario: Réservation refusée si conflit de réservation
    Given une salle avec le code "MAGICARPE" de nom "Salle Magicarpe" et une capacité de 20 personnes
    And une réservation existante pour la salle "MAGICARPE" du "2026-06-11T09:00" au "2026-06-11T11:00"
    When je tente de réserver la salle "MAGICARPE" pour "dave@example.com" avec 5 participants du "2026-06-11T10:00" au "2026-06-11T12:00"
    Then la réservation est refusée avec le message "Salle déjà réservée sur ce créneau"
