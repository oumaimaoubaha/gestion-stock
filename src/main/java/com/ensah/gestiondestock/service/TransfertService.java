// src/main/java/com/ensah/gestiondestock/service/TransfertService.java
package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.model.Transfert;
import com.ensah.gestiondestock.repository.ProduitRepository;
import com.ensah.gestiondestock.repository.TransfertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
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

    /**
     * 1. Lister tous les transferts (sans pagination)
     */
    public List<Transfert> getAllTransferts() {
        return transfertRepository.findAll();
    }

    /**
     * 2. Recherche par critères (sans pagination) – conserve cette méthode existante.
     */
    public List<Transfert> search(LocalDate date, Long produitId, Long sourceId, Long destinationId) {
        return transfertRepository.findByCriteria(date, produitId, sourceId, destinationId);
    }

    /**
     * 2bis. Recherche paginée + triée par date décroissante.
     *     On appelle la méthode paginée dans le repository, en lui passant PageRequest.
     */
    public Page<Transfert> search(LocalDate date,
                                  String referenceProduit,
                                  Long sourceId,
                                  Long destinationId,
                                  int page,
                                  int size) {
        // PageRequest.of(page, size, Sort.by("date").descending()) → tri par date décroissante
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return transfertRepository.findByCriteriaWithReference(
                date,
                (referenceProduit == null || referenceProduit.isBlank()) ? null : referenceProduit,
                sourceId,
                destinationId,
                pageable
        );
    }

    /**
     * 2ter. Si vous voulez surcharger avec un Pageable direct (optionnel),
     *      ajoutez ceci :
     */
    public Page<Transfert> search(LocalDate date,
                                  String referenceProduit,
                                  Long sourceId,
                                  Long destinationId,
                                  Pageable pageable) {
        return transfertRepository.findByCriteriaWithReference(
                date,
                (referenceProduit == null || referenceProduit.isBlank()) ? null : referenceProduit,
                sourceId,
                destinationId,
                pageable
        );
    }

    /**
     * 3. Ajouter un transfert (transactionnel)
     */
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

        // Vérification / mise à jour du stock en source
        produitSource.setQuantiteStock(quantiteDisponible - quantiteDemandee);
        produitRepository.save(produitSource);

        // Gestion du produit dans l’entrepôt destination
        Produit produitDestination =
                produitRepository.findByReferenceAndEntrepotId(
                        produitSource.getReference(), transfert.getDestination().getId()
                );

        if (produitDestination != null) {
            // Si le produit existe déjà en destination, on ajoute la quantité
            produitDestination.setQuantiteStock(
                    produitDestination.getQuantiteStock() + quantiteDemandee
            );
            produitRepository.save(produitDestination);
        } else {
            // Sinon, on recrée un nouveau produit dans l’entrepôt destination
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

    /**
     * 4. Modifier un transfert existant
     */
    public Transfert saveOrUpdateTransfert(Transfert transfert) {
        return transfertRepository.save(transfert);
    }

    /**
     * 5. Supprimer un transfert
     */
    public void deleteTransfert(Long id) {
        transfertRepository.deleteById(id);
    }

    /**
     * 6. Obtenir un transfert par ID
     */
    public Transfert getTransfertById(Long id) {
        return transfertRepository.findById(id).orElse(null);
    }

    /**
     * 6bis. Recherche par référence seule (sans pagination) – on la conserve si besoin
     */
    public List<Transfert> searchByReference(LocalDate date,
                                             String referenceProduit,
                                             Long sourceId,
                                             Long destinationId) {
        return transfertRepository
                .findByCriteriaWithReference(
                        date,
                        (referenceProduit == null || referenceProduit.isBlank()) ? null : referenceProduit,
                        sourceId,
                        destinationId,
                        PageRequest.of(0, Integer.MAX_VALUE, Sort.by("date").descending())
                )
                .getContent();
    }
}
