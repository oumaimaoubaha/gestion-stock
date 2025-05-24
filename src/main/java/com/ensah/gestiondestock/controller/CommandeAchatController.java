package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.CommandeAchat;
import com.ensah.gestiondestock.model.LigneCommande;
import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.service.CommandeAchatService;
import com.ensah.gestiondestock.service.EntrepotService;
import com.ensah.gestiondestock.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/commandes")
public class CommandeAchatController {

    @Autowired private CommandeAchatService commandeAchatService;
    @Autowired private ProduitService produitService;
    @Autowired private EntrepotService entrepotService;

    @GetMapping("/new")
    public String showForm(Model model) {
        CommandeAchat commande = new CommandeAchat();
        commande.setLignes(List.of(new LigneCommande()));
        model.addAttribute("commande", commande);
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        model.addAttribute("isEdit", false);
        return "commande/form";
    }

    @GetMapping("/edit/{id}")
    public String editCommande(@PathVariable Long id, Model model) {
        CommandeAchat commande = commandeAchatService.getCommandeById(id);
        System.out.println(">>> [DEBUG EDIT] Commande récupérée : id=" + (commande != null ? commande.getId() : "null"));

        if (commande == null) return "redirect:/commandes";

        model.addAttribute("commande", commande);
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        model.addAttribute("isEdit", true);
        return "commande/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("commande") CommandeAchat commande, Model model) {
        System.out.println(">>> [DEBUG SAVE] ID reçu = " + commande.getId());

        boolean isModification = commande.getId() != null;
        boolean numeroExiste = commandeAchatService.numeroExisteDeja(commande.getNumero(), commande.getId());

        if (!isModification && numeroExiste) {
            model.addAttribute("commande", commande);
            model.addAttribute("entrepots", entrepotService.getAllEntrepots());
            model.addAttribute("isEdit", false);
            model.addAttribute("error", "Le numéro de commande existe déjà.");
            return "commande/form";
        }

        for (LigneCommande ligne : commande.getLignes()) {
            String libelle = ligne.getProduit().getLibelle();
            Produit produit = produitService.findOrCreateByLibelle(libelle);
            ligne.setProduit(produit);
            ligne.setCommandeAchat(commande);
        }

        commandeAchatService.save(commande);
        return "redirect:/commandes";
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("commandes", commandeAchatService.getAllCommandes());
        return "commande/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCommande(@PathVariable Long id) {
        commandeAchatService.deleteById(id);
        return "redirect:/commandes";
    }
}
