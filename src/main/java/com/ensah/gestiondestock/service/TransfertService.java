package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.model.Transfert;
import com.ensah.gestiondestock.repository.ProduitRepository;
import com.ensah.gestiondestock.repository.TransfertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransfertService {

    @Autowired
    private TransfertRepository transfertRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private ProduitService produitService;

    // 1. Lister tous les transferts (sans pagination)
    public List<Transfert> getAllTransferts() {
        return transfertRepository.findAll();
    }

    // 2. Rechercher par critères (sans pagination) – on conserve :
    public List<Transfert> search(LocalDate date, Long produitId, Long sourceId, Long destinationId) {
        return transfertRepository.findByCriteria(date, produitId, sourceId, destinationId);
    }

    // 2bis. Nouvelle méthode paginée (ajoutée) :
    public Page<Transfert> search(LocalDate date, String referenceProduit, Long sourceId, Long destinationId, int page, int size) {
        return transfertRepository.findByCriteriaWithReference(
                date,
                (referenceProduit == null || referenceProduit.isBlank()) ? null : referenceProduit,
                sourceId,
                destinationId,
                PageRequest.of(page, size, Sort.by("date").descending())
        );
    }

    // 3. Ajouter un nouveau transfert (avec transaction sécurisée) :
    @Transactional(rollbackFor = Exception.class)
    public Transfert addTransfert(Transfert transfert) {
        if (transfert.getSource().getId().equals(transfert.getDestination().getId())) {
            throw new IllegalArgumentException("L’entrepôt source et l’entrepôt destination doivent être différents.");
        }

        Produit produitSource = produitService.getProduitById(transfert.getProduit().getId());
        if (!produitSource.getEntrepot().getId().equals(transfert.getSource().getId())) {
            throw new IllegalArgumentException("Le produit ne se trouve pas dans l'entrepôt source.");
        }

        int quantiteDemandee = transfert.getQuantite();
        int quantiteDisponible = produitSource.getQuantiteStock();
        if (quantiteDemandee > quantiteDisponible) {
            throw new IllegalArgumentException("Quantité demandée supérieure au stock disponible.");
        }

        // Rechercher un produit dans la destination avec la même référence
        Produit produitDestination = produitRepository
                .findByReferenceAndEntrepotId(produitSource.getReference(), transfert.getDestination().getId());

        // Diminuer la quantité dans la source
        produitSource.setQuantiteStock(quantiteDisponible - quantiteDemandee);
        produitRepository.save(produitSource);

        if (produitDestination != null) {
            // Produit déjà présent → on ajoute la quantité
            produitDestination.setQuantiteStock(produitDestination.getQuantiteStock() + quantiteDemandee);
            produitRepository.save(produitDestination);
        } else {
            // Produit absent → on le crée dans l'entrepôt destination
            Produit nouveau = new Produit();
            nouveau.setReference(produitSource.getReference());
            nouveau.setLibelle(produitSource.getLibelle());
            nouveau.setType(produitSource.getType());
            nouveau.setUnite(produitSource.getUnite());
            nouveau.setQuantiteStock(quantiteDemandee);
            nouveau.setEntrepot(transfert.getDestination());
            produitRepository.save(nouveau);
        }

        return transfertRepository.save(transfert);
    }

    // 4. Modifier un transfert existant avec la même méthode que l'ajout :
    public Transfert saveOrUpdateTransfert(Transfert transfert) {
        return transfertRepository.save(transfert);
    }

    // 5. Supprimer un transfert :
    public void deleteTransfert(Long id) {
        transfertRepository.deleteById(id);
    }

    // 6. Obtenir un transfert par ID :
    public Transfert getTransfertById(Long id) {
        return transfertRepository.findById(id).orElse(null);
    }

    // 6bis. Recherche par référence seule (conserve la méthode existante) :
    public List<Transfert> searchByReference(LocalDate date, String referenceProduit, Long sourceId, Long destinationId) {
        return transfertRepository.findByCriteriaWithReference(date, referenceProduit, sourceId, destinationId, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
    }
}
