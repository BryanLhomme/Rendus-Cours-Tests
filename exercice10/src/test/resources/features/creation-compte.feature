Feature: Création de compte

  Scenario: Création d'un nouveau compte réussie
    Given aucun compte existant avec l'identifiant "tristan"
    When je crée un compte avec l'email "tristan@ynov.com", l'identifiant "tristan" et le mot de passe "Soleil2026"
    Then je reçois la confirmation "Compte créé avec succès"

  Scenario: Création d'un compte avec un identifiant déjà existant
    Given un compte existant avec l'identifiant "tristan"
    When je crée un compte avec l'email "autre@ynov.com", l'identifiant "tristan" et le mot de passe "PluieFine7"
    Then je reçois l'erreur "Identifiant déjà utilisé"
