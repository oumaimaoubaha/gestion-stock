package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.Inventaire;
import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.repository.ProduitRepository;
import com.ensah.gestiondestock.service.EntrepotService;
import com.ensah.gestiondestock.service.InventaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/inventaires")
public class InventaireController {

    @Autowired
    private InventaireService inventaireService;

    @Autowired
    private EntrepotService entrepotService;

    @Autowired
    private ProduitRepository produitRepository;

    @GetMapping
    public String listerInventaires(
            Model model,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMax,
            @RequestParam(required = false) Long entrepotId) {

        List<Inventaire> inventaires = inventaireService.searchInventairesParPeriodeEtEntrepot(dateMin, dateMax, entrepotId);
        model.addAttribute("inventaires", inventaires);
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

        StringBuilder contenu = new StringBuilder("Référence,Libellé,Type,Unité,Quantité\n");
        for (Produit p : produits) {
            contenu.append(p.getReference()).append(",")
                    .append(p.getLibelle()).append(",")
                    .append(p.getType()).append(",")
                    .append(p.getUnite()).append(",")
                    .append(p.getQuantiteStock()).append("\n");
        }

        byte[] fichier = contenu.toString().getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stock.csv");

        return ResponseEntity.ok().headers(headers).body(fichier);
    }

    @GetMapping("/import")
    public String afficherImportPage(Model model) {
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "inventaire/import";
    }

    @PostMapping("/import")
    public String importerFichierCorrige(@RequestParam("file") MultipartFile file, Model model) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String ligne;
            List<String[]> ecarts = new ArrayList<>();
            boolean first = true;

            while ((ligne = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] champs = ligne.split(",");
                if (champs.length >= 5) {
                    ecarts.add(champs); // Produit, Unité, QuantitéAvant, QuantitéAprès, Justif
                }
            }

            model.addAttribute("ecarts", ecarts);
            return "inventaire/ecarts";

        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du traitement du fichier.");
            return "inventaire/import";
        }
    }

    // ✅ Nouveau : validation de l'inventaire
    @PostMapping("/valider")
    public String validerInventaire() {
        inventaireService.appliquerDernierInventaire();
        return "redirect:/inventaires?valide=true";
    }
}
