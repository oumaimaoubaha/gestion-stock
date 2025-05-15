package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.Inventaire;
import com.ensah.gestiondestock.model.LigneInventaire;
import com.ensah.gestiondestock.repository.InventaireRepository;
import com.ensah.gestiondestock.repository.LigneInventaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InventaireService {

    @Autowired
    private InventaireRepository inventaireRepository;

    @Autowired
    private LigneInventaireRepository ligneInventaireRepository;

    // 5.1 Lister tous les inventaires
    public List<Inventaire> getAllInventaires() {
        return inventaireRepository.findAll();
    }

    // 5.2 Rechercher un inventaire par date et/ou entrepôt
    public List<Inventaire> searchInventaires(LocalDate date, Long entrepotId) {
        return inventaireRepository.findByCriteria(date, entrepotId);
    }

    // 5.3 Afficher un inventaire (par ID)
    public Inventaire getInventaireById(Long id) {
        return inventaireRepository.findById(id).orElse(null);
    }

    // 5.5.a Enregistrer un nouvel inventaire
    public Inventaire enregistrerInventaire(Inventaire inventaire) {
        return inventaireRepository.save(inventaire);
    }

    // 5.5.b Ajouter une ligne avec calcul d’écart
    public LigneInventaire ajouterLigneInventaire(LigneInventaire ligne) {
        int ecart = ligne.getQuantitePhysique() - ligne.getQuantiteTheorique();
        ligne.setEcart(ecart);
        return ligneInventaireRepository.save(ligne);
    }

    // 5.5.c Valider l’inventaire → ici, ça peut être vide ou enrichi plus tard
    public void validerInventaire(Long inventaireId) {
        // Pour l’instant, rien de spécial — juste un placeholder
        // Tu peux mettre à jour les produits plus tard si besoin
    }
}
