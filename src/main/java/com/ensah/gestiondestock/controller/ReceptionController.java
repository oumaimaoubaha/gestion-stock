package com.ensah.gestiondestock.controller;

import com.ensah.gestiondestock.model.Reception;
import com.ensah.gestiondestock.service.EntrepotService;
import com.ensah.gestiondestock.service.ReceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/receptions")
public class ReceptionController {

    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private EntrepotService entrepotService;

    @GetMapping
    public String listReceptions(Model model,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                 @RequestParam(required = false) String produit,
                                 @RequestParam(required = false) Long entrepot) {

        List<Reception> receptions = receptionService.search(from, to, produit, entrepot);
        model.addAttribute("receptions", receptions);
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "reception/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteReception(@PathVariable Long id) {
        receptionService.deleteById(id);
        return "redirect:/receptions";
    }
    @GetMapping("/new")
    public String newReception(Model model) {
        model.addAttribute("reception", new Reception());
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "reception/form";
    }

    @GetMapping("/edit/{id}")
    public String editReception(@PathVariable Long id, Model model) {
        Reception reception = receptionService.getById(id);
        model.addAttribute("reception", reception);
        model.addAttribute("entrepots", entrepotService.getAllEntrepots());
        return "reception/form";
    }

    @PostMapping("/save")
    public String saveReception(@ModelAttribute Reception reception) {
        receptionService.save(reception);
        return "redirect:/receptions";
    }

}
