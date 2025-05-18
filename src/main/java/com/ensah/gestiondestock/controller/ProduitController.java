package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/produits")
public class ProduitController {

    @Autowired
    private ProduitService produitService;

    @GetMapping
    public String listProduits(Model model) {
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("produit", new Produit());
        return "produit/list";
    }

    @PostMapping("/save")
    public String saveProduit(@Valid @ModelAttribute Produit produit, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("produits", produitService.getAllProduits());
            return "produit/list";
        }
        produitService.saveOrUpdateProduit(produit);
        return "redirect:/produits";
    }

    @GetMapping("/edit/{id}")
    public String editProduit(@PathVariable Long id, Model model) {
        model.addAttribute("produit", produitService.getProduitById(id));
        model.addAttribute("produits", produitService.getAllProduits());
        return "produit/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return "redirect:/produits";
    }
}
