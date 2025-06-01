package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.model.Reception;
import com.ensah.gestiondestock.repository.ReceptionRepository;
import com.ensah.gestiondestock.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceptionService {

    @Autowired
    private ReceptionRepository receptionRepository;

    @Autowired
    private ProduitService produitService;

    public Page<Reception> search(LocalDate dateMin, LocalDate dateMax, String produit, String entrepot, int page, int size) {
        boolean produitVide = (produit == null || produit.isBlank());
        boolean entrepotVide = (entrepot == null || entrepot.isBlank());

        return receptionRepository.findByCriteria(
                dateMin,
                dateMax,
                produitVide ? null : produit,
                entrepotVide ? null : entrepot,
                PageRequest.of(page, size, Sort.by("dateReception").descending())
        );
    }

    public Reception getById(Long id) {
        return receptionRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Reception reception) {
        if (reception.getCommandeAchat() != null && reception.getCommandeAchat().getNumeroAchat() != null) {
            reception.setNumeroAchat(reception.getCommandeAchat().getNumeroAchat());
        }

        // Enregistrer la réception
        receptionRepository.save(reception);

        // 🔁 Ajouter ou mettre à jour le produit dans l'entrepôt
        if (reception.getProduit() != null && reception.getEntrepot() != null) {
            Produit produit = produitService.getProduitByLibelleAndEntrepot(
                    reception.getProduit(),
                    reception.getEntrepot().getId()
            );

            if (produit != null) {
                // 🔁 Mise à jour stock si le produit existe
                produit.setQuantiteStock(produit.getQuantiteStock() + reception.getQuantite());
                produitService.saveOrUpdateProduit(produit);
            } else {
                // ➕ Nouveau produit si non trouvé
                Produit nouveau = new Produit();
                nouveau.setLibelle(reception.getProduit());
                nouveau.setReference(reception.getNumeroAchat());
                nouveau.setType("inconnu");
                nouveau.setUnite(reception.getUnite());
                nouveau.setQuantiteStock(reception.getQuantite());
                nouveau.setEntrepot(reception.getEntrepot());
                produitService.saveOrUpdateProduit(nouveau);
            }
        }
    }

    public void delete(Long id) {
        Reception reception = getById(id);
        if (reception != null) {
            reception.setCommandeAchat(null);
            reception.setEntrepot(null);
            receptionRepository.save(reception);
            receptionRepository.deleteById(id);
        }
    }

    public List<Reception> getAll() {
        return receptionRepository.findAll(Sort.by("dateReception").descending());
    }

    public List<Long> getCommandeIdsReceptionnees() {
        return receptionRepository.findAll()
                .stream()
                .filter(r -> r.getCommandeAchat() != null)
                .map(r -> r.getCommandeAchat().getId())
                .collect(Collectors.toList());
    }
}
