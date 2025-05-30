package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.CommandeAchat;
import com.ensah.gestiondestock.model.Reception;
import com.ensah.gestiondestock.service.CommandeAchatService;
import com.ensah.gestiondestock.service.EntrepotService;
import com.ensah.gestiondestock.service.ProduitService;
import com.ensah.gestiondestock.service.ReceptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/receptions")
public class ReceptionController {

    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private CommandeAchatService commandeAchatService;

    @Autowired
    private EntrepotService entrepotService;

    @Autowired
    private ProduitService produitService;

    @GetMapping
    public String afficherListe(Model model,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMin,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMax,
                                @RequestParam(required = false) String produit,
                                @RequestParam(required = false) String entrepot,
                                @RequestParam(defaultValue = "0") int page) {

        int size = 4;
        var pageResult = receptionService.search(dateMin, dateMax, produit, entrepot, page, size);

        model.addAttribute("receptions", pageResult.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageResult.getTotalPages());

        model.addAttribute("dateMin", dateMin);
        model.addAttribute("dateMax", dateMax);
        model.addAttribute("produit", produit);
        model.addAttribute("entrepot", entrepot);
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());

        return "reception/list";
    }

    @GetMapping("/nouvelle")
    public String afficherFormulaireReception(@RequestParam(required = false) Long id,
                                              @RequestParam(required = false) String numeroAchat,
                                              @RequestParam(required = false) String produit,
                                              Model model) {
        List<Long> idsReceptionnees = receptionService.getCommandeIdsReceptionnees();
        List<CommandeAchat> commandes = commandeAchatService.getByIdNotIn(idsReceptionnees);

        if (numeroAchat != null && !numeroAchat.isBlank()) {
            commandes = commandes.stream()
                    .filter(c -> c.getNumeroAchat() != null && c.getNumeroAchat().toLowerCase().contains(numeroAchat.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (produit != null && !produit.isBlank()) {
            commandes = commandes.stream()
                    .filter(c -> c.getProduit() != null && c.getProduit().toLowerCase().contains(produit.toLowerCase()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("commandes", commandes);
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        model.addAttribute("numeroAchat", numeroAchat);
        model.addAttribute("produit", produit);
        model.addAttribute("aucuneCommande", commandes.stream().noneMatch(c -> c.getProduit() != null)); // ✅ Ajouté

        Set<String> unites = new HashSet<>(produitService.getUnites());
        for (CommandeAchat cmd : commandes) {
            if (cmd.getUnite() != null) unites.add(cmd.getUnite());
        }
        model.addAttribute("unites", unites);

        Reception r = new Reception();
        r.setDateReception(LocalDate.now());

        if (id != null) {
            CommandeAchat cmd = commandeAchatService.getById(id);
            if (cmd != null) {
                r.setDateAchat(cmd.getDateAchat());
                r.setNumeroAchat(cmd.getNumeroAchat());
                r.setProduit(cmd.getProduit());
                r.setUnite(cmd.getUnite());
                r.setQuantite(cmd.getQuantite());
                r.setSource(cmd.getSource());
                r.setCommandeAchat(cmd);
            }
        }

        model.addAttribute("reception", r);
        return "reception/form";
    }

    @PostMapping("/valider")
    public String enregistrerReception(@Valid @ModelAttribute Reception reception,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("entrepots", entrepotService.getAllEntrepots());
            model.addAttribute("unites", new HashSet<>(produitService.getUnites()));
            return "reception/form";
        }

        receptionService.save(reception);
        redirectAttributes.addFlashAttribute("message", "✅ Réception enregistrée !");
        return "redirect:/receptions";
    }

    @GetMapping("/modifier/{id}")
    public String modifier(@PathVariable Long id, Model model) {
        Reception reception = receptionService.getById(id);
        model.addAttribute("reception", reception);
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());

        Set<String> unites = new HashSet<>(produitService.getUnites());
        model.addAttribute("unites", unites);

        return "reception/form";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        receptionService.delete(id);
        return "redirect:/receptions";
    }
}
