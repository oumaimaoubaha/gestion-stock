package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.CommandeLivraison;
import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.service.CommandeLivraisonService;
import com.ensah.gestiondestock.service.EntrepotService;
import com.ensah.gestiondestock.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public String liste(Model model) {
        model.addAttribute("commandes", commandeLivraisonService.getAll());
        return "commandeLivraison/list";
    }

    @GetMapping("/nouveau")
    public String nouveau(Model model) {
        CommandeLivraison commande = new CommandeLivraison();
        commande.setDateLivraison(LocalDate.now());
        model.addAttribute("commande", commande);
        model.addAttribute("produits", produitService.getAllProduits()); // utile au démarrage
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
        if (commande.getQuantite() > produit.getQuantiteStock()) {
            model.addAttribute("commande", commande);
            model.addAttribute("produits", produitService.getAllProduits());
            model.addAttribute("entrepots", entrepotService.getAllEntrepots());
            model.addAttribute("error", "❌ Quantité demandée (" + commande.getQuantite() + ") > stock disponible (" + produit.getQuantiteStock() + ").");
            return "commandeLivraison/form";
        }

        produit.setQuantiteStock(produit.getQuantiteStock() - commande.getQuantite());
        produitService.save(produit);
        commandeLivraisonService.save(commande);
        return "redirect:/commandeLivraison";
    }

    @GetMapping("/supprimer/{id}")
    public String delete(@PathVariable Long id) {
        commandeLivraisonService.delete(id);
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

    // ✅ Méthode supplémentaire pour filtrage dynamique JS (ne change pas les URLs)
    @GetMapping("/produits-par-entrepot")
    @ResponseBody
    public List<Produit> getProduitsParEntrepot(@RequestParam("entrepotId") Long entrepotId) {
        return produitService.getProduitsByEntrepot(entrepotId);
    }
}
