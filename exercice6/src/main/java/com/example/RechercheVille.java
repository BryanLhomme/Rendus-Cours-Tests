package com.example;

import java.util.ArrayList;
import java.util.List;

public class RechercheVille {

    private final List<String> villes;

    public RechercheVille(List<String> villes) {
        this.villes = villes;
    }

    public List<String> Rechercher(String mot) throws NotFoundException {
        if ("*".equals(mot)) {
            return List.copyOf(villes);
        }
        if (mot == null || mot.length() < 2) {
            throw new NotFoundException("Le texte de recherche doit contenir au moins 2 caractères");
        }
        String motLower = mot.toLowerCase();
        List<String> resultat = new ArrayList<>();
        for (String ville : villes) {
            if (ville.toLowerCase().contains(motLower)) {
                resultat.add(ville);
            }
        }
        return resultat;
    }
}
