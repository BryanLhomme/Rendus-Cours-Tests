Feature: Gestion des comptes bancaires
  Cette feature decrit le comportement attendu lors de la creation et consultation des comptes.

  Background:
    Given aucun compte n existe dans l API

  Scenario: Creation d un nouveau compte
    When je cree un compte avec le numero "COMPTE1-ANANAS" et le titulaire "Bob Leponge"
    Then la reponse HTTP doit etre 201
    And le compte a le numero "COMPTE1-ANANAS"
    And le compte a le titulaire "Bob Leponge"
    And le solde du compte est 0.0

  Scenario: Refus de creation d un compte avec un numero deja existant
    Given un compte "COMPTE1-ANANAS" au nom de "Bob Leponge" a ete cree
    When je cree un compte avec le numero "COMPTE1-ANANAS" et le titulaire "Patrick Etoile"
    Then la reponse HTTP doit etre 409
    And la reponse contient un message d erreur

  Scenario: Consultation d un compte existant
    Given un compte "COMPTE1-ANANAS" au nom de "Bob Leponge" a ete cree
    When je consulte le compte "COMPTE1-ANANAS"
    Then la reponse HTTP doit etre 200
    And le compte a le numero "COMPTE1-ANANAS"

  Scenario: Consultation d un compte inexistant
    When je consulte le compte "COMPTE0-FANTOME"
    Then la reponse HTTP doit etre 404
    And la reponse contient un message d erreur
