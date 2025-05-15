package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.Entrepot;
import com.ensah.gestiondestock.repository.EntrepotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntrepotService {

    @Autowired
    private EntrepotRepository entrepotRepository;

    // 1. Lister tous les entrepôts
    public List<Entrepot> getAllEntrepots() {
        return entrepotRepository.findAll();
    }

    // 2. Ajouter ou modifier un entrepôt
    public Entrepot saveOrUpdateEntrepot(Entrepot entrepot) {
        return entrepotRepository.save(entrepot);
    }

    // 3. Obtenir un entrepôt par ID
    public Entrepot getEntrepotById(Long id) {
        return entrepotRepository.findById(id).orElse(null);
    }

    // 4. Supprimer un entrepôt
    public void deleteEntrepot(Long id) {
        entrepotRepository.deleteById(id);
    }

    // 5. Vérifier si un entrepôt existe
    public boolean exists(Long id) {
        return entrepotRepository.existsById(id);
    }

    // 6. Rechercher par nom (partiel, insensible à la casse)
    public List<Entrepot> findByNom(String nom) {
        return entrepotRepository.findByNomContainingIgnoreCase(nom);
    }
}
