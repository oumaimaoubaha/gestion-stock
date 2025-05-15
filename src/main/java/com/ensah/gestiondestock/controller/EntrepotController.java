package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.Entrepot;
import com.ensah.gestiondestock.service.EntrepotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/entrepots")
public class EntrepotController {

    @Autowired
    private EntrepotService entrepotService;

    // 1.1 Liste des entrepôts
    @GetMapping
    public String listEntrepots(Model model) {
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        model.addAttribute("entrepot", new Entrepot()); // 👈 important pour le formulaire
        return "entrepot/list";
    }

    // 1.2 Affichage du formulaire d'ajout
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("entrepot", new Entrepot());
        return "entrepot/form"; // correspond à templates/entrepot/form.html
    }

    // 1.3 Enregistrement (ajout ou modification)
    @PostMapping("/save")
    public String saveEntrepot(@ModelAttribute Entrepot entrepot) {
        entrepotService.saveOrUpdateEntrepot(entrepot);
        return "redirect:/entrepots";
    }

    // 1.3 Suite : afficher formulaire de modification
    @GetMapping("/edit/{id}")
    public String editEntrepot(@PathVariable Long id, Model model) {
        Entrepot e = entrepotService.getEntrepotById(id);
        model.addAttribute("entrepot", e);
        return "entrepot/form";
    }

    // 1.4 Supprimer un entrepôt
    @GetMapping("/delete/{id}")
    public String deleteEntrepot(@PathVariable Long id) {
        entrepotService.deleteEntrepot(id);
        return "redirect:/entrepots";
    }
}
