package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.CommandeLivraison;
import com.ensah.gestiondestock.model.Livraison;
import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.service.CommandeLivraisonService;
import com.ensah.gestiondestock.service.EntrepotService;
import com.ensah.gestiondestock.service.LivraisonService;
import com.ensah.gestiondestock.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/livraisons")
public class LivraisonController {

    @Autowired
    private LivraisonService livraisonService;

    @Autowired
    private CommandeLivraisonService commandeLivraisonService;

    @Autowired
    private ProduitService produitService;

    @Autowired
    private EntrepotService entrepotService;

    // 🧾 Liste des livraisons (figure 5)
    @GetMapping
    public String listeLivraisons(Model model,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
                                  @RequestParam(required = false) String referenceProduit,
                                  @RequestParam(required = false) Long entrepotId) {

        List<Livraison> livraisons = livraisonService.searchBetween(dateDebut, dateFin, referenceProduit, entrepotId);
        model.addAttribute("livraisons", livraisons);
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        model.addAttribute("dateDebut", dateDebut);
        model.addAttribute("dateFin", dateFin);
        model.addAttribute("referenceProduit", referenceProduit);
        model.addAttribute("entrepotId", entrepotId);
        return "livraison/list";
    }

    // 🆕 Nouvelle livraison - choix
    @GetMapping("/nouvelle")
    public String nouvelleLivraison(Model model) {
        model.addAttribute("commandeLivraisons", commandeLivraisonService.getAll());
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        model.addAttribute("livraison", new Livraison());
        return "livraison/form";
    }

    // ✅ Enregistrer une livraison (commande ou manuelle)
    @PostMapping("/save")
    public String saveLivraison(@ModelAttribute Livraison livraison) {
        livraisonService.saveOrUpdateLivraison(livraison);
        return "redirect:/livraisons";
    }

    // ✏️ Modifier une livraison
    @GetMapping("/modifier/{id}")
    public String modifier(@PathVariable Long id, Model model) {
        Livraison livraison = livraisonService.getLivraisonById(id);
        model.addAttribute("livraison", livraison);
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "livraison/form";
    }

    // ❌ Supprimer
    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        livraisonService.deleteLivraison(id);
        return "redirect:/livraisons";
    }

    // 🔍 Rechercher une commande par numéro ou produit
    @GetMapping("/rechercherCommandes")
    public String rechercherCommandes(@RequestParam(required = false) String numero,
                                      @RequestParam(required = false) String produit,
                                      Model model) {
        List<CommandeLivraison> commandes = commandeLivraisonService.getAll();
        model.addAttribute("commandeLivraisons", commandes);
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        model.addAttribute("livraison", new Livraison());
        return "livraison/form";
    }

    // Pré-remplir à partir d’une commande
    @GetMapping("/livrerCommande/{id}")
    public String livrerCommande(@PathVariable Long id, Model model) {
        CommandeLivraison commande = commandeLivraisonService.getById(id);
        Livraison livraison = new Livraison();
        livraison.setDateLivraison(LocalDate.now());
        livraison.setCommandeLivraison(commande);
        livraison.setProduit(commande.getProduit());
        livraison.setUnite(commande.getUnite());
        livraison.setQuantite(commande.getQuantite());
        livraison.setEntrepot(commande.getEntrepot());
        livraison.setRemarque(commande.getRemarque());

        model.addAttribute("livraison", livraison);
        model.addAttribute("commandeLivraisons", commandeLivraisonService.getAll());
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "livraison/form";
    }
}
