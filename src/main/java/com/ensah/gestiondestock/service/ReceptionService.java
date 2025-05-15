package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.Reception;
import com.ensah.gestiondestock.repository.ReceptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReceptionService {

    @Autowired
    private ReceptionRepository receptionRepository;

    // 2.1 Lister toutes les réceptions
    public List<Reception> getAllReceptions() {
        return receptionRepository.findAll();
    }

    // 2.2 Rechercher par date, produit ou entrepôt
    public List<Reception> search(LocalDate date, Long produitId, Long entrepotId) {
        return receptionRepository.findByCriteria(date, produitId, entrepotId);
    }

    // 2.3 Modifier une réception (même méthode que ajouter)
    public Reception saveOrUpdateReception(Reception reception) {
        return receptionRepository.save(reception);
    }

    // 2.4 Supprimer une réception
    public void deleteReception(Long id) {
        receptionRepository.deleteById(id);
    }

    // 2.5.a Ajouter une réception suite à un achat (même méthode mais contexte différent côté vue)
    public Reception ajouterReceptionSuiteAchat(Reception reception) {
        // logique spécifique si tu veux la différencier
        return receptionRepository.save(reception);
    }

    // 2.5.b Ajouter une réception indépendante (retour, don, production, etc.)
    public Reception ajouterReceptionIndependante(Reception reception) {
        // logique spécifique si tu veux la différencier
        return receptionRepository.save(reception);
    }

    // 🔎 Obtenir une réception par ID (utile pour édition)
    public Reception getReceptionById(Long id) {
        return receptionRepository.findById(id).orElse(null);
    }
}
