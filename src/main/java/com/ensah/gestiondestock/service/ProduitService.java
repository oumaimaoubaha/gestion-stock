package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.model.Transfert;
import com.ensah.gestiondestock.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProduitService {

    @Autowired
    private ProduitRepository produitRepository;

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Produit getProduitById(Long id) {
        return produitRepository.findById(id).orElse(null);
    }

    public Produit saveOrUpdateProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    public void deleteProduit(Long id) {
        produitRepository.deleteById(id);
    }

    public boolean exists(Long id) {
        return produitRepository.existsById(id);
    }

    public List<Produit> searchByReference(String ref) {
        return produitRepository.findByReferenceContainingIgnoreCase(ref);
    }

    public List<Produit> searchByLibelle(String libelle) {
        return produitRepository.findByLibelleContainingIgnoreCase(libelle);
    }

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

    public Page<Produit> getProduitsFiltresParPage(String ref, Long entrepotId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if ((ref == null || ref.isBlank()) && entrepotId == null) {
            return produitRepository.findAll(pageable);
        } else if (ref != null && !ref.isBlank() && entrepotId == null) {
            return produitRepository.findAllByReferenceContainingIgnoreCase(ref, pageable);
        } else if ((ref == null || ref.isBlank()) && entrepotId != null) {
            return produitRepository.findAllByEntrepotId(entrepotId, pageable);
        } else {
            return produitRepository.findAllByReferenceContainingIgnoreCaseAndEntrepotId(ref, entrepotId, pageable);
        }
    }

    public Produit getProduitByReference(String reference) {
        List<Produit> produits = produitRepository.findByReference(reference);
        return produits.isEmpty() ? null : produits.get(0);
    }

    public boolean produitExisteDansEntrepot(String reference, Long entrepotId, Long produitId) {
        List<Produit> produits = produitRepository.findByReference(reference);
        return produits.stream().anyMatch(p ->
                p.getEntrepot() != null &&
                        p.getEntrepot().getId().equals(entrepotId) &&
                        (produitId == null || !p.getId().equals(produitId))
        );
    }

    public Produit findOrCreateByLibelle(String libelle) {
        Produit produit = produitRepository.findByLibelle(libelle);
        if (produit == null) {
            produit = new Produit();
            produit.setLibelle(libelle);
            produitRepository.save(produit);
        }
        return produit;
    }

    @Autowired
    public List<String> getUnites() {
        return produitRepository.findDistinctUnites();
    }

    public List<Produit> getProduitsByEntrepot(Long entrepotId) {
        return produitRepository.findByEntrepotId(entrepotId);
    }

    public Produit getProduitByLibelleAndEntrepot(String libelle, Long entrepotId) {
        return produitRepository.findByLibelleAndEntrepotId(libelle, entrepotId);
    }

    // ✅ Transfert transactionnel : tout ou rien
    @Transactional(rollbackFor = Exception.class)
    public void transfererProduit(Transfert transfert) {
        Produit produitSource = produitRepository.findById(transfert.getProduit().getId()).orElse(null);
        if (produitSource == null || produitSource.getEntrepot() == null) {
            throw new IllegalArgumentException("Produit ou entrepôt source invalide.");
        }

        if (!produitSource.getEntrepot().getId().equals(transfert.getSource().getId())) {
            throw new IllegalArgumentException("Le produit n'appartient pas à l'entrepôt source.");
        }

        int quantiteDemandee = transfert.getQuantite();
        int quantiteDisponible = produitSource.getQuantiteStock();

        if (quantiteDemandee > quantiteDisponible) {
            throw new IllegalArgumentException("Quantité demandée supérieure au stock disponible.");
        }

        // Mise à jour stock source
        produitSource.setQuantiteStock(quantiteDisponible - quantiteDemandee);
        produitRepository.save(produitSource);

        // Mise à jour ou création dans l'entrepôt destination
        Produit produitDestination = produitRepository.findByLibelleAndEntrepotId(
                produitSource.getLibelle(),
                transfert.getDestination().getId()
        );

        if (produitDestination != null) {
            produitDestination.setQuantiteStock(produitDestination.getQuantiteStock() + quantiteDemandee);
            produitRepository.save(produitDestination);
        } else {
            Produit nouveau = new Produit();
            nouveau.setLibelle(produitSource.getLibelle());
            nouveau.setReference(produitSource.getReference());
            nouveau.setType(produitSource.getType());
            nouveau.setUnite(produitSource.getUnite());
            nouveau.setQuantiteStock(quantiteDemandee);
            nouveau.setEntrepot(transfert.getDestination()); // ✅ ESSENTIEL
            produitRepository.save(nouveau);
        }
    }

    public Produit findByReference(String reference) {
        List<Produit> produits = produitRepository.findByReference(reference);
        if (produits.isEmpty()) {
            return null; // ✅ évite le crash
        }
        return produits.get(0);
    }

    public Produit getById(Long id) {
        return produitRepository.findById(id).orElse(null);
    }

    public Produit save(Produit produit) {
        return produitRepository.save(produit);
    }
}
