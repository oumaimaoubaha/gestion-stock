package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.Transfert;
import com.ensah.gestiondestock.repository.TransfertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransfertService {

    @Autowired
    private TransfertRepository transfertRepository;

    // 4.1 Lister tous les transferts
    public List<Transfert> getAllTransferts() {
        return transfertRepository.findAll();
    }

    // 4.2 Rechercher par date, produit, source ou destination
    public List<Transfert> search(LocalDate date, Long produitId, Long sourceId, Long destinationId) {
        return transfertRepository.findByCriteria(date, produitId, sourceId, destinationId);
    }

    // 4.3 Modifier un transfert (même méthode que save)
    public Transfert saveOrUpdateTransfert(Transfert transfert) {
        return transfertRepository.save(transfert);
    }

    // 4.4 Supprimer un transfert
    public void deleteTransfert(Long id) {
        transfertRepository.deleteById(id);
    }

    // 4.5 Ajouter un nouveau transfert
    public Transfert addTransfert(Transfert transfert) {
        return transfertRepository.save(transfert);
    }

    // Obtenir un transfert par ID
    public Transfert getTransfertById(Long id) {
        return transfertRepository.findById(id).orElse(null);
    }
}
