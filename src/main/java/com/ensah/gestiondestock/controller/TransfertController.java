package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.Entrepot;
import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.model.Transfert;
import com.ensah.gestiondestock.service.EntrepotService;
import com.ensah.gestiondestock.service.ProduitService;
import com.ensah.gestiondestock.service.TransfertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/transferts")
public class TransfertController {

    @Autowired private TransfertService transfertService;
    @Autowired private ProduitService produitService;
    @Autowired private EntrepotService entrepotService;

    @GetMapping
    public String showList(Model model,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
                           @RequestParam(required = false) String produit) {

        List<Transfert> transferts = transfertService.getAllTransferts(); // à affiner si nécessaire
        model.addAttribute("transferts", transferts);
        model.addAttribute("produits", produitService.getAllProduits());
        return "transfert/list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("transfert", new Transfert());
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "transfert/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Transfert transfert) {
        transfertService.saveOrUpdateTransfert(transfert);
        return "redirect:/transferts";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        transfertService.deleteTransfert(id);
        return "redirect:/transferts";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Transfert transfert = transfertService.getTransfertById(id);
        model.addAttribute("transfert", transfert);
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "transfert/form";
    }
}
