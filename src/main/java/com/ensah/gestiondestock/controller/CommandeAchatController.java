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

    @GetMapping
    public String list(Model model) {
        model.addAttribute("commandes", commandeAchatService.getAllCommandes());
        return "commande/list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        LigneCommande ligne = new LigneCommande();
        ligne.setProduit(new Produit());
        CommandeAchat commande = new CommandeAchat();
        commande.setLignes(List.of(ligne));
        model.addAttribute("commande", commande);
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        model.addAttribute("isEdit", false);
        return "commande/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("commande") CommandeAchat commande, Model model) {
        if (commandeAchatService.numeroExisteDeja(commande.getNumero(), null)) {
            model.addAttribute("error", "Le numéro de commande existe déjà.");
            model.addAttribute("commande", commande);
            model.addAttribute("entrepots", entrepotService.getAllEntrepots());
            model.addAttribute("isEdit", false);
            return "commande/form";
        }

        for (LigneCommande ligne : commande.getLignes()) {
            Produit produit = produitService.findOrCreateByLibelle(ligne.getProduit().getLibelle());
            ligne.setProduit(produit);
            ligne.setCommandeAchat(commande);
        }

        commandeAchatService.save(commande);
        return "redirect:/commandes";
    }

    @GetMapping("/edit/{id}")
    public String editCommande(@PathVariable Long id, Model model) {
        CommandeAchat commande = commandeAchatService.getCommandeById(id);
        if (commande == null) return "redirect:/commandes";

        for (LigneCommande ligne : commande.getLignes()) {
            if (ligne.getProduit() == null) {
                ligne.setProduit(new Produit());
            }
        }

        model.addAttribute("commande", commande);
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        model.addAttribute("isEdit", true);
        return "commande/form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("commande") CommandeAchat commande, Model model) {
        if (commandeAchatService.numeroExisteDeja(commande.getNumero(), commande.getId())) {
            model.addAttribute("error", "Le numéro de commande existe déjà.");
            model.addAttribute("commande", commande);
            model.addAttribute("entrepots", entrepotService.getAllEntrepots());
            model.addAttribute("isEdit", true);
            return "commande/form";
        }

        for (LigneCommande ligne : commande.getLignes()) {
            Produit produit = produitService.findOrCreateByLibelle(ligne.getProduit().getLibelle());
            ligne.setProduit(produit);
            ligne.setCommandeAchat(commande);
        }

        commandeAchatService.save(commande);
        return "redirect:/commandes";
    }

    @GetMapping("/delete/{id}")
    public String deleteCommande(@PathVariable Long id) {
        commandeAchatService.deleteById(id);
        return "redirect:/commandes";
    }
}
