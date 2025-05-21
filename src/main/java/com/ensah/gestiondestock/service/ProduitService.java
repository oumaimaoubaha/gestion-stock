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

    // 6. Rechercher par référence (partielle)
    public List<Produit> searchByReference(String ref) {
        return produitRepository.findByReferenceContainingIgnoreCase(ref);
    }

    // 7. Rechercher par libellé
    public List<Produit> searchByLibelle(String libelle) {
        return produitRepository.findByLibelleContainingIgnoreCase(libelle);
    }

    // 8. Recherche combinée
    public List<Produit> getProduitsFiltres(String ref, Long entrepotId) {
        if ((ref == null || ref.isBlank()) && entrepotId == null) {
            return produitRepository.findAll();
        }

        return produitRepository.findAll().stream()
                .filter(p ->
                        (ref == null || p.getReference().toLowerCase().contains(ref.toLowerCase())) &&
                                (entrepotId == null || (p.getEntrepot() != null && p.getEntrepot().getId().equals(entrepotId)))
                )
                .toList();
    }

    // 9. Obtenir un produit par référence exacte
    // 9. Obtenir un produit par référence exacte (le premier de la liste)
    public Produit getProduitByReference(String reference) {
        List<Produit> produits = produitRepository.findByReference(reference);
        return produits.isEmpty() ? null : produits.get(0);
    }

    // 10. Vérifier si un produit du même code existe dans le même entrepôt
    public boolean produitExisteDansEntrepot(String reference, Long entrepotId, Long produitId) {
        List<Produit> produits = produitRepository.findByReference(reference);

        return produits.stream().anyMatch(p ->
                p.getEntrepot() != null &&
                        p.getEntrepot().getId().equals(entrepotId) &&
                        (produitId == null || !p.getId().equals(produitId))
        );
    }

}
