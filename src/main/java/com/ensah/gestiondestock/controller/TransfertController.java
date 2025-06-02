package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.model.Transfert;
import com.ensah.gestiondestock.service.EntrepotService;
import com.ensah.gestiondestock.service.ProduitService;
import com.ensah.gestiondestock.service.TransfertService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/transferts")
public class TransfertController {

    @Autowired
    private TransfertService transfertService;

    @Autowired
    private ProduitService produitService;

    @Autowired
    private EntrepotService entrepotService;

    @GetMapping
    public String showList(Model model,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                           @RequestParam(required = false) String referenceProduit,
                           @RequestParam(required = false) Long sourceId,
                           @RequestParam(required = false) Long destinationId,
                           @RequestParam(defaultValue = "0") int page) {

        // Taille fixe de page :
        int size = 4;

        // Nouvelle surcharge qui renvoie Page<Transfert> :
        var pageResult = transfertService.search(date, referenceProduit, sourceId, destinationId, page, size);

        model.addAttribute("transferts", pageResult.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("referenceProduit", referenceProduit);
        model.addAttribute("sourceId", sourceId);
        model.addAttribute("destinationId", destinationId);
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());

        return "transfert/list";
    }

    @GetMapping("/new")
    public String showForm(Model model,
                           @RequestParam(name = "error", required = false) String errorMessage) {
        model.addAttribute("transfert", new Transfert());
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        model.addAttribute("errorMessage", errorMessage);
        return "transfert/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Transfert transfert, Model model) {
        Produit p = transfert.getProduit();
        if (transfert.getSource().getId().equals(transfert.getDestination().getId())) {
            // Même entrepôt source et destination → erreur affichée dans le formulaire
            model.addAttribute("transfert", transfert);
            model.addAttribute("produits", produitService.getAllProduits());
            model.addAttribute("entrepots", entrepotService.getAllEntrepots());
            model.addAttribute("errorMessage", "L’entrepôt source et destination doivent être différents.");
            return "transfert/form";
        }

        if (p != null) {
            Produit produitEnBase = produitService.getProduitById(p.getId());
            if (produitEnBase.getQuantiteStock() < transfert.getQuantite()) {
                model.addAttribute("transfert", transfert);
                model.addAttribute("produits", produitService.getAllProduits());
                model.addAttribute("entrepots", entrepotService.getAllEntrepots());
                model.addAttribute("errorMessage", "Quantité insuffisante dans l'entrepôt source.");
                return "transfert/form";
            }
        }

        transfertService.addTransfert(transfert);
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
