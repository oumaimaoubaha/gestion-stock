package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.Produit;
import com.ensah.gestiondestock.model.Entrepot;
import com.ensah.gestiondestock.model.Reception;
import com.ensah.gestiondestock.repository.ReceptionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Service
public class ReceptionService {

    @Autowired
    private ReceptionRepository receptionRepository;

    // Lister toutes les réceptions
    public List<Reception> getAllReceptions() {
        return receptionRepository.findAll();
    }

    // Rechercher par date, produit ou entrepôt
    public List<Reception> search(LocalDate date, Long produitId, Long entrepotId) {
        return receptionRepository.findByCriteria(date, produitId, entrepotId);
    }

    // Ajouter ou modifier une réception
    public Reception saveOrUpdateReception(Reception reception) {
        return receptionRepository.save(reception);
    }

    // Supprimer une réception
    public void deleteReception(Long id) {
        receptionRepository.deleteById(id);
    }

    // Récupérer une réception par ID
    public Reception getReceptionById(Long id) {
        return receptionRepository.findById(id).orElse(null);
    }

    // Simuler des commandes à réceptionner (pour page /add/achat)
    public List<Reception> getCommandesAReceptions() {
        List<Reception> commandes = new ArrayList<>();
        // Tu peux ajouter d'autres lignes ici si tu veux tester plus d'exemples

        return commandes;
    }
}
