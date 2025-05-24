// ✅ CommandeAchatService.java
package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.CommandeAchat;
import com.ensah.gestiondestock.repository.CommandeAchatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandeAchatService {

    @Autowired
    private CommandeAchatRepository commandeAchatRepository;

    public void save(CommandeAchat commande) {
        commandeAchatRepository.save(commande);
    }

    public List<CommandeAchat> getAllCommandes() {
        return commandeAchatRepository.findAll();
    }

    public CommandeAchat getCommandeById(Long id) {
        return commandeAchatRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        commandeAchatRepository.deleteById(id);
    }

    public CommandeAchat getCommandeByNumero(String numero) {
        return commandeAchatRepository.findByNumero(numero);
    }

    public boolean numeroExisteDeja(String numero, Long idCommandeEnCours) {
        CommandeAchat existante = commandeAchatRepository.findByNumero(numero);
        if (existante == null) return false;
        return idCommandeEnCours == null || !existante.getId().equals(idCommandeEnCours);
    }
}
