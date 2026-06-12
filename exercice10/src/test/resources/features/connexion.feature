Feature: Connexion

  Scenario: Connexion réussie
    Given un utilisateur avec l'identifiant "lea" et le mot de passe "Montagne42"
    When je me connecte avec l'identifiant "lea" et le mot de passe "Montagne42"
    Then je suis connecté avec le message "Connexion réussie"

  Scenario: Connexion échouée avec mauvais mot de passe
    Given un utilisateur avec l'identifiant "lea" et le mot de passe "Montagne42"
    When je me connecte avec l'identifiant "lea" et le mot de passe "mauvaisMotDePasse"
    Then je vois le message d'erreur "Identifiants incorrects"
