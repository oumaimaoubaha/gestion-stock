package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService {

    @Autowired
    private ProduitRepository produitRepository;

    // 1. Lister tous les produits
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    // 2. Obtenir un produit par ID
    public Produit getProduitById(Long id) {
        return produitRepository.findById(id).orElse(null);
    }

    // 3. Ajouter ou modifier un produit
    public Produit saveOrUpdateProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    // 4. Supprimer un produit
    public void deleteProduit(Long id) {
        produitRepository.deleteById(id);
    }

    // 5. Vérifier si un produit existe
    public boolean exists(Long id) {
        return produitRepository.existsById(id);
    }

    // 6. Rechercher par référence
    public List<Produit> searchByReference(String ref) {
        return produitRepository.findByReferenceContainingIgnoreCase(ref);
    }

    // 7. Rechercher par libellé
    public List<Produit> searchByLibelle(String libelle) {
        return produitRepository.findByLibelleContainingIgnoreCase(libelle);
    }

    // 8. Obtenir un produit par référence exacte
    public Produit getProduitByReference(String reference) {
        return produitRepository.findByReference(reference);
    }

    // 🔹 9. Méthode à ajouter : chercher ou créer par libellé
    public Produit findOrCreateByLibelle(String libelle) {
        Produit produit = produitRepository.findByLibelle(libelle);
        if (produit == null) {
            produit = new Produit();
            produit.setLibelle(libelle);
            produit.setReference(libelle); // ou autre logique
            produit.setQuantiteStock(0);
            produit.setType("Nouveau");
            produit.setUnite("Unité");
            produit = produitRepository.save(produit);
        }
        return produit;
    }
}
