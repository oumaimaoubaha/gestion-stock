package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.Entrepot;
import com.ensah.gestiondestock.service.EntrepotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/entrepots")
public class EntrepotController {

    @Autowired
    private EntrepotService entrepotService;

    private final int PAGE_SIZE = 4;

    // 🔍 Liste paginée des entrepôts
    @GetMapping
    public String listEntrepots(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Entrepot> entrepotPage = entrepotService.getEntrepotsPaginated(page, PAGE_SIZE);
        model.addAttribute("entrepots", entrepotPage.getContent());
        model.addAttribute("hasNext", entrepotPage.hasNext());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", entrepotPage.getTotalPages()); // 🔥 C'est cette ligne qui manquait
        return "entrepot/list";
    }


    // ➕ Formulaire d'ajout (page séparée)
    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("entrepot", new Entrepot());
        return "entrepot/form";
    }

    // ✏️ Formulaire de modification (page séparée)
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Entrepot entrepot = entrepotService.getEntrepotById(id);
        model.addAttribute("entrepot", entrepot);
        return "entrepot/form";
    }

    // ✅ Enregistrement (ajout ou modification)
    @PostMapping("/save")
    public String saveEntrepot(@ModelAttribute Entrepot entrepot) {
        entrepotService.saveOrUpdateEntrepot(entrepot);
        return "redirect:/entrepots";
    }

    // 🗑️ Suppression
    @GetMapping("/delete/{id}")
    public String deleteEntrepot(@PathVariable Long id) {
        entrepotService.deleteEntrepot(id);
        return "redirect:/entrepots";
    }
}
