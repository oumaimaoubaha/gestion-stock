package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.Inventaire;
import com.ensah.gestiondestock.model.LigneInventaire;
import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.repository.InventaireRepository;
import com.ensah.gestiondestock.repository.LigneInventaireRepository;
import com.ensah.gestiondestock.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.List;

@Service
public class InventaireService {

    @Autowired
    private InventaireRepository inventaireRepository;

    @Autowired
    private LigneInventaireRepository ligneInventaireRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private ProduitService produitService;

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

    // 5.4 Rechercher par période et entrepôt
    public List<Inventaire> searchInventairesParPeriodeEtEntrepot(LocalDate dateMin, LocalDate dateMax, Long entrepotId) {
        return inventaireRepository.findByPeriodeAndEntrepot(dateMin, dateMax, entrepotId);
    }

    // 5.5.c Valider l’inventaire (placeholder)
    public void validerInventaire(Long inventaireId) {
        // Tu peux enrichir cette méthode plus tard
    }

    // ✅ Méthode pour appliquer le dernier inventaire corrigé
    public void appliquerDernierInventaire() {
        File fichier = new File("dernier_inventaire.csv");
        if (!fichier.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            boolean first = true;

            while ((ligne = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }

                String[] champs = ligne.split(",");
                if (champs.length >= 5) {
                    String reference = champs[0].trim();
                    int quantitePhysique = Integer.parseInt(champs[3].trim());

                    Produit produit = produitService.getProduitByReference(reference);
                    if (produit != null) {
                        produit.setQuantiteStock(quantitePhysique);
                        produitRepository.save(produit);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
