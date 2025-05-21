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

    @Autowired private InventaireRepository inventaireRepository;
    @Autowired private LigneInventaireRepository ligneInventaireRepository;
    @Autowired private ProduitRepository produitRepository;

    // 🔁 Tous les inventaires
    public List<Inventaire> getAllInventaires() {
        return inventaireRepository.findAll();
    }

    // 🔍 Recherche simple (non utilisée ici)
    public List<Inventaire> searchInventaires(LocalDate date, Long entrepotId) {
        return inventaireRepository.findByCriteria(date, entrepotId);
    }

    // 🔍 Recherche avec filtres combinés
    public List<Inventaire> searchInventairesParPeriodeEtEntrepot(LocalDate dateMin, LocalDate dateMax, Long entrepotId) {
        return inventaireRepository.findByPeriodeAndEntrepot(dateMin, dateMax, entrepotId);
    }

    // 📦 Récupérer un inventaire par ID
    public Inventaire getInventaireById(Long id) {
        return inventaireRepository.findById(id).orElse(null);
    }

    // ✅ Enregistrer un inventaire complet
    public Inventaire enregistrerInventaire(Inventaire inventaire) {
        return inventaireRepository.save(inventaire);
    }

    // ➕ Ajouter une ligne à un inventaire
    public LigneInventaire ajouterLigneInventaire(LigneInventaire ligne) {
        int ecart = ligne.getQuantitePhysique() - ligne.getQuantiteTheorique();
        ligne.setEcart(ecart);
        return ligneInventaireRepository.save(ligne);
    }

    // 📂 Fonction utilitaire si tu veux appliquer un fichier .csv de correction à froid
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
                if (champs.length >= 4) {
                    String reference = champs[0].trim();
                    String quantiteStr = champs[3].trim();

                    if (!quantiteStr.isEmpty()) {
                        int quantite = Integer.parseInt(quantiteStr);
                        Produit produit = produitRepository.findByReference(reference);
                        if (produit != null) {
                            produit.setQuantiteStock(quantite);
                            produitRepository.save(produit);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
