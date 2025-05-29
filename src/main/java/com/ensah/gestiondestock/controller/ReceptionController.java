package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.CommandeAchat;
import com.ensah.gestiondestock.model.Reception;
import com.ensah.gestiondestock.service.CommandeAchatService;
import com.ensah.gestiondestock.service.EntrepotService;
import com.ensah.gestiondestock.service.ProduitService;
import com.ensah.gestiondestock.service.ReceptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Set;
import java.util.HashSet;

import java.time.LocalDate;
import java.util.List;
@Controller
@RequestMapping("/receptions")
public class ReceptionController {

    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private CommandeAchatService commandeAchatService;

    @Autowired
    private EntrepotService entrepotService;

    @Autowired
    private ProduitService produitService;

    @GetMapping("/nouvelle")
    public String afficherFormulaireReception(@RequestParam(required = false) Long id, Model model) {
        List<Long> idsReceptionnees = receptionService.getCommandeIdsReceptionnees();
        List<CommandeAchat> commandes = commandeAchatService.getByIdNotIn(idsReceptionnees);

        model.addAttribute("commandes", commandes);
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());

        // Récupérer les unités à partir des produits ET des commandes
        Set<String> unites = new HashSet<>(produitService.getUnites());
        for (CommandeAchat cmd : commandes) {
            if (cmd.getUnite() != null) {
                unites.add(cmd.getUnite());
            }
        }
        model.addAttribute("unites", unites);

        if (id != null) {
            CommandeAchat cmd = commandeAchatService.getById(id);
            if (cmd != null) {
                Reception r = new Reception();
                r.setDateReception(LocalDate.now());              // ✅ Aujourd'hui
                r.setDateAchat(cmd.getDateAchat());               // ✅ Date de la commande
                r.setNumeroAchat(cmd.getNumeroAchat());
                r.setProduit(cmd.getProduit());
                r.setUnite(cmd.getUnite());                       // ✅ Unité associée
                r.setQuantite(cmd.getQuantite());
                r.setSource(cmd.getSource());
                r.setCommandeAchat(cmd);
                model.addAttribute("reception", r);
                return "reception/form";
            }
        }

        model.addAttribute("reception", new Reception());
        return "reception/form";
    }

    @PostMapping("/valider")
    public String enregistrerReception(@Valid @ModelAttribute Reception reception,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("entrepots", entrepotService.getAllEntrepots());
            Set<String> unites = new HashSet<>(produitService.getUnites());
            model.addAttribute("unites", unites);
            return "reception/form";
        }

        receptionService.save(reception);
        redirectAttributes.addFlashAttribute("message", "✅ Réception enregistrée avec succès !");
        return "redirect:/receptions";
    }
}
