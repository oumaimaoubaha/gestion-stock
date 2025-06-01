package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.CommandeAchat;
import com.ensah.gestiondestock.service.CommandeAchatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Controller
@RequestMapping("/commandes")
public class CommandeAchatController {

    @Autowired
    private CommandeAchatService commandeService;

    @GetMapping
    public String listCommandes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) String produit,
            @RequestParam(required = false) String date,
            Model model) {

        LocalDate dateAchat = null;
        if (date != null && !date.isEmpty()) {
            try {
                dateAchat = LocalDate.parse(date);
            } catch (DateTimeParseException e) {
                model.addAttribute("error", "⚠️ Date invalide. Format attendu : yyyy-MM-dd");
            }
        }

        Page<CommandeAchat> commandes = commandeService.searchCommandes(
                numero != null && !numero.isEmpty() ? numero : null,
                produit != null && !produit.isEmpty() ? produit : null,
                dateAchat,
                PageRequest.of(page, 5)
        );

        model.addAttribute("commandes", commandes);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", commandes.getTotalPages());
        model.addAttribute("numero", numero);
        model.addAttribute("produit", produit);
        model.addAttribute("date", date);

        if (commandes.isEmpty()) {
            model.addAttribute("noResults", "⚠️ Aucun résultat trouvé pour cette recherche.");
        }

        return "commande/list";
    }

    @GetMapping("/nouveau")
    public String nouveau(Model model) {
        CommandeAchat commande = new CommandeAchat();
        commande.setDateAchat(LocalDate.now());
        model.addAttribute("commande", commande);
        return "commande/form";
    }

    @GetMapping("/modifier/{id}")
    public String modifier(@PathVariable Long id, Model model) {
        CommandeAchat commande = commandeService.getById(id);
        model.addAttribute("commande", commande);
        return "commande/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute CommandeAchat commande, Model model) {
        CommandeAchat existante = commandeService.findByNumeroAchat(commande.getNumeroAchat());
        boolean ajout = (commande.getId() == null);

        if (existante != null && (ajout || !existante.getId().equals(commande.getId()))) {
            model.addAttribute("commande", commande);
            model.addAttribute("erreurNumero", "❌ Le numéro de commande existe déjà.");
            return "commande/form";
        }

        commandeService.save(commande);
        return "redirect:/commandes";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id, Model model) {
        try {
            commandeService.delete(id);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "❌ Suppression impossible : commande liée à des lignes.");
            return listCommandes(0, null, null, null, model);
        }
        return "redirect:/commandes";
    }
}
