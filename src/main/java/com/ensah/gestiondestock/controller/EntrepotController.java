package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.Entrepot;
import com.ensah.gestiondestock.service.EntrepotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/entrepots")
public class EntrepotController {

    @Autowired
    private EntrepotService entrepotService;

    private final int PAGE_SIZE = 4;

    // 1.1 Liste paginée des entrepôts
    @GetMapping
    public String listEntrepots(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Entrepot> entrepotPage = entrepotService.getEntrepotsPaginated(page, PAGE_SIZE);
        model.addAttribute("entrepots", entrepotPage.getContent());
        model.addAttribute("hasNext", entrepotPage.hasNext());
        model.addAttribute("currentPage", page);
        model.addAttribute("entrepot", new Entrepot());
        return "entrepot/list";
    }

    // 1.2 Sauvegarde (ajout ou modification)
    @PostMapping("/save")
    public String saveEntrepot(@ModelAttribute Entrepot entrepot) {
        entrepotService.saveOrUpdateEntrepot(entrepot);
        return "redirect:/entrepots"; // retourne à la première page par défaut
    }

    // 1.3 Modifier
    @GetMapping("/edit/{id}")
    public String editEntrepot(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, Model model) {
        Entrepot e = entrepotService.getEntrepotById(id);
        Page<Entrepot> entrepotPage = entrepotService.getEntrepotsPaginated(page, PAGE_SIZE);

        model.addAttribute("entrepot", e);
        model.addAttribute("entrepots", entrepotPage.getContent());
        model.addAttribute("hasNext", entrepotPage.hasNext());
        model.addAttribute("currentPage", page);

        return "entrepot/list";
    }

    // 1.4 Supprimer
    @GetMapping("/delete/{id}")
    public String deleteEntrepot(@PathVariable Long id) {
        entrepotService.deleteEntrepot(id);
        return "redirect:/entrepots";
    }
}
