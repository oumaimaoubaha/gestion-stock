package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.CommandeAchat;
import com.ensah.gestiondestock.repository.CommandeAchatRepository;
import com.ensah.gestiondestock.repository.LigneCommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommandeAchatService {

    @Autowired
    private CommandeAchatRepository commandeAchatRepository;

    @Autowired
    private LigneCommandeRepository ligneCommandeRepository;

    public List<CommandeAchat> getAllCommandes() {
        return commandeAchatRepository.findAll();
    }

    public CommandeAchat getById(Long id) {
        return commandeAchatRepository.findById(id).orElse(null);
    }

    public void save(CommandeAchat commande) {
        commandeAchatRepository.save(commande);
    }

    @Transactional
    public void delete(Long id) {
        ligneCommandeRepository.deleteByCommandeAchatId(id);
        commandeAchatRepository.deleteById(id);
    }

    public CommandeAchat findByNumeroAchat(String numero) {
        return commandeAchatRepository.findByNumeroAchat(numero);
    }

    public Page<CommandeAchat> getPageCommandes(Pageable pageable) {
        return commandeAchatRepository.findAllByOrderByDateAchatDesc(pageable);
    }

    public Page<CommandeAchat> searchCommandes(String numero, String produit, LocalDate date, Pageable pageable) {
        return commandeAchatRepository.searchCommandes(numero, produit, date, pageable);
    }

    public List<CommandeAchat> getByIdNotIn(List<Long> ids) {
        return commandeAchatRepository.findByIdNotIn(ids);
    }
}
