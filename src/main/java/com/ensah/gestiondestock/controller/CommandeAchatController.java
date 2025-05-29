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

@Controller
@RequestMapping("/commandes")
public class CommandeAchatController {

    @Autowired
    private CommandeAchatService commandeService;

    @GetMapping
    public String listCommandes(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<CommandeAchat> commandes = commandeService.getPageCommandes(PageRequest.of(page, 5));
        model.addAttribute("commandes", commandes);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", commandes.getTotalPages());
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
        commande.setDateAchat(LocalDate.now()); // mise à jour automatique à la date du jour
        model.addAttribute("commande", commande);
        return "commande/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute CommandeAchat commande, Model model) {
        CommandeAchat existante = commandeService.findByNumeroAchat(commande.getNumeroAchat());
        boolean ajout = (commande.getId() == null);

        if (existante != null && (ajout || !existante.getId().equals(commande.getId()))) {
            model.addAttribute("commande", commande);
            model.addAttribute("erreurNumero", "Le numéro de commande existe déjà.");
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
            model.addAttribute("error", "Impossible de supprimer la commande car elle est liée à des lignes de commande.");
            return listCommandes(0, model);
        }
        return "redirect:/commandes";
    }
}
