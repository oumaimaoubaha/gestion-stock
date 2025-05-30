package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.Reception;
import com.ensah.gestiondestock.repository.ReceptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceptionService {

    @Autowired
    private ReceptionRepository receptionRepository;

    public Page<Reception> search(LocalDate dateMin, LocalDate dateMax, String produit, String entrepot, int page, int size) {
        return receptionRepository.findByCriteria(
                dateMin,
                dateMax,
                (produit == null || produit.isBlank()) ? null : produit,
                (entrepot == null || entrepot.isBlank()) ? null : entrepot,
                PageRequest.of(page, size, Sort.by("dateReception").descending())
        );
    }

    public Reception getById(Long id) {
        return receptionRepository.findById(id).orElse(null);
    }

    public void save(Reception reception) {
        if (reception.getCommandeAchat() != null && reception.getCommandeAchat().getNumeroAchat() != null) {
            reception.setNumeroAchat(reception.getCommandeAchat().getNumeroAchat());
        }
        receptionRepository.save(reception);
    }

    public void delete(Long id) {
        Reception reception = getById(id);
        if (reception != null) {
            reception.setCommandeAchat(null);
            reception.setEntrepot(null);
            receptionRepository.save(reception);
            receptionRepository.deleteById(id);
        }
    }

    public List<Reception> getAll() {
        return receptionRepository.findAll(Sort.by("dateReception").descending());
    }

    public List<Long> getCommandeIdsReceptionnees() {
        return receptionRepository.findAll()
                .stream()
                .filter(r -> r.getCommandeAchat() != null)
                .map(r -> r.getCommandeAchat().getId())
                .collect(Collectors.toList());
    }
}
