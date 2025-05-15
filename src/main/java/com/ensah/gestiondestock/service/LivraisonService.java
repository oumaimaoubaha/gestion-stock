package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.Livraison;
import com.ensah.gestiondestock.repository.LivraisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LivraisonService {

    @Autowired
    private LivraisonRepository livraisonRepository;

    // 3.1 Lister toutes les livraisons
    public List<Livraison> getAllLivraisons() {
        return livraisonRepository.findAll();
    }

    // 3.2 Rechercher par date, produit ou entrepôt
    public List<Livraison> search(LocalDate date, Long produitId, Long entrepotId) {
        return livraisonRepository.findByCriteria(date, produitId, entrepotId);
    }

    // 3.3 Modifier une livraison (même que ajouter)
    public Livraison saveOrUpdateLivraison(Livraison livraison) {
        return livraisonRepository.save(livraison);
    }

    // 3.4 Supprimer une livraison
    public void deleteLivraison(Long id) {
        livraisonRepository.deleteById(id);
    }

    // 3.5.a Livraison via commande client (même logique pour l’instant)
    public Livraison livraisonViaCommande(Livraison livraison) {
        return livraisonRepository.save(livraison);
    }

    // 3.5.b Livraison interne (production, transfert, etc.)
    public Livraison livraisonInterne(Livraison livraison) {
        return livraisonRepository.save(livraison);
    }

    // 🔎 Obtenir une livraison par ID
    public Livraison getLivraisonById(Long id) {
        return livraisonRepository.findById(id).orElse(null);
    }
}
