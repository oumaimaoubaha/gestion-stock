package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.CommandeAchat;
import com.ensah.gestiondestock.model.Livraison;
import com.ensah.gestiondestock.service.CommandeAchatService;
import com.ensah.gestiondestock.service.EntrepotService;
import com.ensah.gestiondestock.service.LivraisonService;
import com.ensah.gestiondestock.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private CommandeAchatService commandeAchatService;

    @Autowired
    private ProduitService produitService;

    @Autowired
    private EntrepotService entrepotService;

    // ✅ Afficher les commandes livrables
    @GetMapping("/commandes")
    public String showCommandesLivrables(@RequestParam(required = false) String numero,
                                         @RequestParam(required = false) String produit,
                                         Model model) {

        List<CommandeAchat> commandes = commandeAchatService.search(numero, produit);
        model.addAttribute("commandes", commandes);
        model.addAttribute("numero", numero);
        model.addAttribute("produit", produit);
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "livraison/form_commande";
    }

    // ✅ Afficher le formulaire prérempli après clic sur "Livrer"
    @GetMapping("/form-commande/{id}")
    public String showFormLivraison(@PathVariable Long id, Model model) {
        CommandeAchat cmd = commandeAchatService.getById(id);

        Livraison livraison = new Livraison();
        livraison.setDateLivraison(LocalDate.now());
        livraison.setCommandeAchat(cmd);
        livraison.setProduit(cmd.getProduit() != null ? cmd.getProduit() : null);
        livraison.setQuantite(cmd.getQuantite());
        livraison.setEntrepot(null); // À choisir manuellement
        livraison.setRemarque("");

        model.addAttribute("livraison", livraison);
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "livraison/form_commande";
    }

    // ✅ Enregistrer la livraison liée à une commande
    @PostMapping("/save-commande")
    public String saveLivraisonDepuisCommande(@ModelAttribute Livraison livraison) {
        livraisonService.saveOrUpdateLivraison(livraison);
        return "redirect:/livraisons";
    }
}
