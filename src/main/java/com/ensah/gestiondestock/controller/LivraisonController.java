package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.Entrepot;
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

    @GetMapping("/nouvelle")
    public String nouvelleLivraison(Model model) {
        model.addAttribute("commandeLivraisons", commandeLivraisonService.getCommandesNonLivrees());
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        model.addAttribute("livraison", new Livraison());
        return "livraison/form";
    }

    @PostMapping("/save")
    public String saveLivraison(@RequestParam(required = false) Long commandeId,
                                @ModelAttribute Livraison livraison,
                                Model model) {

        boolean isModification = livraison.getId() != null;

        if (isModification) {
            Livraison ancienne = livraisonService.getLivraisonById(livraison.getId());

            if (ancienne == null) {
                model.addAttribute("error", "❌ Livraison introuvable.");
                model.addAttribute("edition", true); // Ajouté pour l'affichage correct du formulaire
                return "livraison/form";
            }

            // ✅ NE PAS recharger commandeId, NE PAS toucher au produit/stock
            livraison.setProduit(ancienne.getProduit());
            livraison.setEntrepot(ancienne.getEntrepot());
            livraison.setUnite(ancienne.getUnite());
            livraison.setQuantite(ancienne.getQuantite());
            livraison.setCommandeLivraison(ancienne.getCommandeLivraison());

            livraisonService.saveOrUpdateLivraison(livraison);
            return "redirect:/livraisons";
        }

        // Cas ajout : on s'attend à un commandeId valide
        if (commandeId == null) {
            model.addAttribute("error", "❌ Aucun identifiant de commande reçu.");
            return "livraison/form";
        }

        CommandeLivraison cmd = commandeLivraisonService.getById(commandeId);

        if (cmd == null) {
            model.addAttribute("error", "❌ Commande non trouvée.");
            return "livraison/form";
        }

        if ("livré".equalsIgnoreCase(cmd.getStatut())) {
            model.addAttribute("error", "❌ Cette commande a déjà été livrée.");
            model.addAttribute("commandeLivraisons", commandeLivraisonService.getCommandesNonLivrees());
            model.addAttribute("produits", produitService.getAllProduits());
            model.addAttribute("entrepots", entrepotService.getAllEntrepots());
            model.addAttribute("livraison", livraison);
            return "livraison/form";
        }

        Produit produit = produitService.getById(cmd.getProduit().getId());
        Entrepot entrepot = entrepotService.getEntrepotById(cmd.getEntrepot().getId());
        int stockActuel = produit.getQuantiteStock();
        int quantiteALivrer = cmd.getQuantite();

        if (quantiteALivrer > stockActuel) {
            model.addAttribute("error", "❌ Stock insuffisant !");
            model.addAttribute("commandeLivraisons", commandeLivraisonService.getCommandesNonLivrees());
            model.addAttribute("produits", produitService.getAllProduits());
            model.addAttribute("entrepots", entrepotService.getAllEntrepots());
            model.addAttribute("livraison", livraison);
            return "livraison/form";
        }

        livraison.setCommandeLivraison(cmd);
        livraison.setProduit(produit);
        livraison.setEntrepot(entrepot);
        livraison.setUnite(cmd.getUnite());
        livraison.setQuantite(quantiteALivrer);

        produit.setQuantiteStock(stockActuel - quantiteALivrer);
        produitService.save(produit);

        cmd.setStatut("livré");
        commandeLivraisonService.save(cmd);

        livraisonService.saveOrUpdateLivraison(livraison);
        return "redirect:/livraisons";
    }




    @GetMapping("/modifier/{id}")
    public String modifier(@PathVariable Long id, Model model) {
        Livraison livraison = livraisonService.getLivraisonById(id);
        model.addAttribute("livraison", livraison);
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        model.addAttribute("edition", true); // 🟣 Pour signaler qu'on est en mode édition
        return "livraison/form";
    }


    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        livraisonService.deleteLivraison(id);
        return "redirect:/livraisons";
    }

    @GetMapping("/rechercherCommandes")
    public String rechercherCommandes(@RequestParam(required = false) String numero,
                                      @RequestParam(required = false) String produit,
                                      Model model) {
        List<CommandeLivraison> commandes = commandeLivraisonService.searchByNumeroOrProduit(numero, produit);
        model.addAttribute("commandeLivraisons", commandes);
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        model.addAttribute("livraison", new Livraison());
        return "livraison/form";
    }


    @GetMapping("/livrerCommande/{id}")
    public String livrerCommande(@PathVariable Long id, Model model) {
        CommandeLivraison commande = commandeLivraisonService.getById(id);
        Livraison livraison = new Livraison();
        livraison.setDateLivraison(LocalDate.now());
        livraison.setCommandeLivraison(commande);
        livraison.setRemarque(commande.getRemarque());
        livraison.setProduit(commande.getProduit());
        livraison.setEntrepot(commande.getEntrepot());
        livraison.setUnite(commande.getUnite());
        livraison.setQuantite(commande.getQuantite()); // 🛑 AJOUTE CECI

        model.addAttribute("livraison", livraison);
        model.addAttribute("commandeLivraisons", commandeLivraisonService.getCommandesNonLivrees());
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "livraison/form";
    }

}
