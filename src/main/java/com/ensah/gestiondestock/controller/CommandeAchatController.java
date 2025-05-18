package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.CommandeAchat;
import com.ensah.gestiondestock.model.LigneCommande;
import com.ensah.gestiondestock.service.CommandeAchatService;
import com.ensah.gestiondestock.service.ProduitService;
import com.ensah.gestiondestock.service.EntrepotService;

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

    // 🔹 Formulaire pour nouvelle commande
    @GetMapping("/new")
    public String showForm(Model model) {
        CommandeAchat commande = new CommandeAchat();
        commande.setLignes(List.of(new LigneCommande())); // Une ligne vide par défaut

        model.addAttribute("commande", commande);
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "commande/form";
    }

    // 🔹 Enregistrement commande avec ses lignes
    @PostMapping("/save")
    public String save(@ModelAttribute CommandeAchat commande) {
        for (LigneCommande ligne : commande.getLignes()) {
            ligne.setCommandeAchat(commande); // liaison importante
        }
        commandeAchatService.save(commande);
        return "redirect:/commandes"; // 🔄 Aller directement à réceptionner
    }

    // (optionnel)
    @GetMapping
    public String list(Model model) {
        model.addAttribute("commandes", commandeAchatService.getAllCommandes());
        return "commande/list"; // tu peux créer cette page si besoin
    }
    // 🔹 Modifier une commande existante
    @GetMapping("/edit/{id}")
    public String editCommande(@PathVariable Long id, Model model) {
        CommandeAchat commande = commandeAchatService.getCommandeById(id);
        if (commande == null) {
            return "redirect:/commandes";
        }

        model.addAttribute("commande", commande);
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "commande/form";
    }

    // 🔹 Supprimer une commande existante
    @GetMapping("/delete/{id}")
    public String deleteCommande(@PathVariable Long id) {
        commandeAchatService.deleteById(id);
        return "redirect:/commandes";
    }

}
