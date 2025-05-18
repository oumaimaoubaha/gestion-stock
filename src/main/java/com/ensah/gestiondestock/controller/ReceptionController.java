package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.Reception;
import com.ensah.gestiondestock.service.ReceptionService;
import com.ensah.gestiondestock.service.ProduitService;
import com.ensah.gestiondestock.service.EntrepotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/receptions")
public class ReceptionController {

    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private ProduitService produitService;

    @Autowired
    private EntrepotService entrepotService;

    // Liste avec filtres
    @GetMapping
    public String listReceptions(Model model,
                                 @RequestParam(required = false) Long produit,
                                 @RequestParam(required = false) Long entrepot,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        model.addAttribute("receptions", receptionService.search(from, produit, entrepot));
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("produit", produit);
        model.addAttribute("entrepot", entrepot);
        return "reception/list";
    }

    // Formulaire ajout réception manuelle
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("reception", new Reception());
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "reception/form";
    }

    // Sauvegarde
    @PostMapping("/save")
    public String saveReception(@ModelAttribute Reception reception) {
        receptionService.saveOrUpdateReception(reception);
        return "redirect:/receptions";
    }

    // Modifier
    @GetMapping("/edit/{id}")
    public String editReception(@PathVariable Long id, Model model) {
        model.addAttribute("reception", receptionService.getReceptionById(id));
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "reception/form";
    }

    // Supprimer
    @GetMapping("/delete/{id}")
    public String deleteReception(@PathVariable Long id) {
        receptionService.deleteReception(id);
        return "redirect:/receptions";
    }
}
