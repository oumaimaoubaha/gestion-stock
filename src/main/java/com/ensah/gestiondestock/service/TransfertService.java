package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.model.Transfert;
import com.ensah.gestiondestock.repository.ProduitRepository;
import com.ensah.gestiondestock.repository.TransfertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransfertService {

    @Autowired
    private TransfertRepository transfertRepository;

    @Autowired
    private ProduitService produitService;

    @Autowired
    private ProduitRepository produitRepository;

    // 1. Lister tous les transferts
    public List<Transfert> getAllTransferts() {
        return transfertRepository.findAll();
    }

    // 2. Rechercher par critères (date, produit, source, destination)
    public List<Transfert> search(LocalDate date, Long produitId, Long sourceId, Long destinationId) {
        return transfertRepository.findByCriteria(date, produitId, sourceId, destinationId);
    }

    // 3. Ajouter un nouveau transfert (avec transaction sécurisée)
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

        // Rechercher un produit dans la destination avec la MÊME référence
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


    // 4. Modifier un transfert existant
    public Transfert saveOrUpdateTransfert(Transfert transfert) {
        return transfertRepository.save(transfert);
    }

    // 5. Supprimer un transfert
    public void deleteTransfert(Long id) {
        transfertRepository.deleteById(id);
    }

    // 6. Obtenir un transfert par ID
    public Transfert getTransfertById(Long id) {
        return transfertRepository.findById(id).orElse(null);
    }

    public List<Transfert> searchByReference(LocalDate date, String referenceProduit, Long sourceId, Long destinationId) {
        return transfertRepository.findByCriteriaWithReference(date, referenceProduit, sourceId, destinationId);
    }

}
