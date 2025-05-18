package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.Inventaire;
import com.ensah.gestiondestock.service.InventaireService;
import com.ensah.gestiondestock.service.EntrepotService;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/inventaires")
public class InventaireController {

    @Autowired private InventaireService inventaireService;
    @Autowired private EntrepotService entrepotService;
    @Autowired private ProduitRepository produitRepository;

    @GetMapping
    public String listerInventaires(
            Model model,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMax,
            @RequestParam(required = false) Long entrepotId) {

        List<Inventaire> inventaires = inventaireService.searchInventairesParPeriodeEtEntrepot(dateMin, dateMax, entrepotId);
        model.addAttribute("inventaires", inventaireService.searchInventairesParPeriodeEtEntrepot(dateMin, dateMax, entrepotId));
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "inventaire/list";
    }

    @GetMapping("/form")
    public String afficherFormulaireNouveauInventaire(Model model) {
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "inventaire/form";
    }

    @PostMapping("/generer")
    public ResponseEntity<byte[]> genererFichierStock(@RequestParam Long entrepotId) {
        List<Produit> produits = produitRepository.findByEntrepotId(entrepotId);

        StringBuilder contenu = new StringBuilder("Référence,Libellé,Type,Unité\n");
        for (Produit p : produits) {
            contenu.append(p.getReference()).append(",")
                    .append(p.getLibelle()).append(",")
                    .append(p.getType()).append(",")
                    .append(p.getUnite()).append("\n");
        }

        byte[] fichier = contenu.toString().getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stock.csv");

        return ResponseEntity.ok().headers(headers).body(fichier);
    }
}
