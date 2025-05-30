package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.CommandeLivraison;
import com.ensah.gestiondestock.repository.CommandeLivraisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandeLivraisonService {

    @Autowired
    private CommandeLivraisonRepository commandeLivraisonRepository;

    public List<CommandeLivraison> getAll() {
        return commandeLivraisonRepository.findAll();
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
}
