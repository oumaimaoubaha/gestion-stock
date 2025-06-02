package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.CommandeLivraison;
import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.service.CommandeLivraisonService;
import com.ensah.gestiondestock.service.EntrepotService;
import com.ensah.gestiondestock.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/commandeLivraison")
public class CommandeLivraisonController {

    @Autowired
    private CommandeLivraisonService commandeLivraisonService;

    @Autowired
    private ProduitService produitService;

    @Autowired
    private EntrepotService entrepotService;

    @GetMapping
    public String liste(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) String produit,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Long entrepotId,
            @RequestParam(required = false) String statut,
            Model model
    ) {
        // Normalisation des filtres ("" → null)
        if (numero != null && numero.trim().isEmpty()) numero = null;
        if (produit != null && produit.trim().isEmpty()) produit = null;
        if (statut != null && statut.trim().isEmpty()) statut = null;

        // Conversion de la date
        LocalDate dateLivraison = null;
        if (date != null && !date.isEmpty()) {
            try {
                dateLivraison = LocalDate.parse(date);
            } catch (DateTimeParseException e) {
                model.addAttribute("error", "❌ Date invalide");
                model.addAttribute("entrepots", entrepotService.getAllEntrepots());
                return "commandeLivraison/list";
            }
        }

        // Recherche paginée triée par dateLivraison décroissante
        Page<CommandeLivraison> commandes = commandeLivraisonService.search(
                numero,
                produit,
                dateLivraison,
                entrepotId,
                statut,
                PageRequest.of(page, 5, Sort.by("dateLivraison").descending())
        );

        // Redirection à la première page si on dépasse la dernière
        if (commandes.getTotalPages() > 0 && page >= commandes.getTotalPages()) {
            return "redirect:/commandeLivraison?page=0"
                    + (numero != null ? "&numero=" + numero : "")
                    + (produit != null ? "&produit=" + produit : "")
                    + (date != null ? "&date=" + date : "")
                    + (entrepotId != null ? "&entrepotId=" + entrepotId : "")
                    + (statut != null ? "&statut=" + statut : "");
        }

        model.addAttribute("commandes", commandes);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", commandes.getTotalPages());
        model.addAttribute("numero", numero);
        model.addAttribute("produit", produit);
        model.addAttribute("date", date);
        model.addAttribute("statut", statut);
        model.addAttribute("entrepotId", entrepotId);
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());

        if (commandes.getTotalElements() == 0) {
            model.addAttribute("noResults", "⚠️ Aucun résultat trouvé pour cette recherche.");
        }

        return "commandeLivraison/list";
    }

    @GetMapping("/nouveau")
    public String nouveau(Model model) {
        CommandeLivraison commande = new CommandeLivraison();
        commande.setDateLivraison(LocalDate.now());
        commande.setStatut("non livré");
        model.addAttribute("commande", commande);
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "commandeLivraison/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute CommandeLivraison commande, Model model) {
        boolean ajout = (commande.getId() == null);
        CommandeLivraison existante = commandeLivraisonService.findByNumero(commande.getNumeroLivraison());

        if (existante != null && (ajout || !existante.getId().equals(commande.getId()))) {
            model.addAttribute("commande", commande);
            model.addAttribute("produits", produitService.getAllProduits());
            model.addAttribute("entrepots", entrepotService.getAllEntrepots());
            model.addAttribute("error", "❌ Le numéro de livraison existe déjà.");
            return "commandeLivraison/form";
        }

        Produit produit = produitService.getById(commande.getProduit().getId());
        commande.setUnite(produit.getUnite());

        if (commande.getQuantite() > produit.getQuantiteStock()) {
            model.addAttribute("commande", commande);
            model.addAttribute("produits", produitService.getAllProduits());
            model.addAttribute("entrepots", entrepotService.getAllEntrepots());
            model.addAttribute("error", "❌ Quantité demandée (" + commande.getQuantite() + ") > stock disponible (" + produit.getQuantiteStock() + ").");
            return "commandeLivraison/form";
        }

        if (ajout) {
            commande.setStatut("non livré");
        } else {
            CommandeLivraison enBase = commandeLivraisonService.getById(commande.getId());
            if (enBase != null) {
                commande.setStatut(enBase.getStatut());
            }
        }

        commandeLivraisonService.save(commande);
        return "redirect:/commandeLivraison";
    }

    @GetMapping("/modifier/{id}")
    public String modifier(@PathVariable Long id, Model model) {
        CommandeLivraison commande = commandeLivraisonService.getById(id);
        model.addAttribute("commande", commande);
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "commandeLivraison/form";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        commandeLivraisonService.delete(id);
        return "redirect:/commandeLivraison";
    }

    @GetMapping("/produits-par-entrepot")
    @ResponseBody
    public List<Produit> getProduitsParEntrepot(@RequestParam("entrepotId") Long entrepotId) {
        return produitService.getProduitsByEntrepot(entrepotId);
    }
}
