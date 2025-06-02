package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.CommandeLivraison;
import com.ensah.gestiondestock.repository.CommandeLivraisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommandeLivraisonService {

    @Autowired
    private CommandeLivraisonRepository commandeLivraisonRepository;

    // 🔁 Toutes les commandes
    public List<CommandeLivraison> getAll() {
        return commandeLivraisonRepository.findAll();
    }

    // 🔁 Commandes NON livrées uniquement (statut = 'non livré')
    public List<CommandeLivraison> getCommandesNonLivrees() {
        return commandeLivraisonRepository.findByStatut("non livré");
    }

    public CommandeLivraison getById(Long id) {
        return commandeLivraisonRepository.findById(id).orElse(null);
    }

    public void save(CommandeLivraison cmd) {
        commandeLivraisonRepository.save(cmd);
    }

    public void delete(Long id) {
        commandeLivraisonRepository.deleteById(id);
    }

    public CommandeLivraison findByNumero(String numero) {
        return commandeLivraisonRepository.findByNumeroLivraison(numero);
    }

    public List<CommandeLivraison> searchByNumeroOrProduit(String numero, String produit) {
        return commandeLivraisonRepository.searchByNumeroOrProduit(numero, produit);
    }

    public Page<CommandeLivraison> search(String numero, String produit, LocalDate date, Long entrepotId, String statut, Pageable pageable) {
        return commandeLivraisonRepository.searchCommandes(numero, produit, date, entrepotId, statut, pageable);
    }
}
