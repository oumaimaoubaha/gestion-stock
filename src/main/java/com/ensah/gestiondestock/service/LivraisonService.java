package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.CommandeLivraison;
import com.ensah.gestiondestock.model.Livraison;
import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.repository.CommandeLivraisonRepository;
import com.ensah.gestiondestock.repository.LivraisonRepository;
import com.ensah.gestiondestock.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LivraisonService {

    @Autowired
    private LivraisonRepository livraisonRepository;

    @Autowired
    private CommandeLivraisonRepository commandeLivraisonRepository;

    @Autowired
    private ProduitRepository produitRepository;

    public List<Livraison> getAllLivraisons() {
        return livraisonRepository.findAll();
    }

    public List<Livraison> search(LocalDate date, Long produitId, Long entrepotId) {
        return livraisonRepository.findByCriteria(date, produitId, entrepotId);
    }

    public Livraison getLivraisonById(Long id) {
        return livraisonRepository.findById(id).orElse(null);
    }

    public void deleteLivraison(Long id) {
        livraisonRepository.deleteById(id);
    }

    public List<Livraison> searchBetween(LocalDate dateDebut, LocalDate dateFin, String referenceProduit, Long entrepotId) {
        return livraisonRepository.findByFilters(dateDebut, dateFin, referenceProduit, entrepotId);
    }

    public Livraison saveOrUpdateLivraison(Livraison livraison) {
        Livraison saved = livraisonRepository.save(livraison);

        // ⚠️ Met à jour le statut que si c’est une nouvelle livraison (pas une modification)
        if (livraison.getId() == null && livraison.getCommandeLivraison() != null) {
            CommandeLivraison commande = livraison.getCommandeLivraison();
            commande.setStatut("livré");
            commandeLivraisonRepository.save(commande);
        }

        return saved;
    }
}
