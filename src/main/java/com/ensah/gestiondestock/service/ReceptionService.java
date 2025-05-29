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

    public List<Reception> search(LocalDate dateMin, LocalDate dateMax, String produit, Long entrepotId) {
        return receptionRepository.findByCriteria(produit, entrepotId, dateMin, dateMax);
    }

    public List<Reception> getAll() {
        return receptionRepository.findAll();
    }

    public void deleteById(Long id) {
        receptionRepository.deleteById(id);
    }

    public Reception getById(Long id) {
        return receptionRepository.findById(id).orElse(null);
    }
    public void save(Reception reception) {
        receptionRepository.save(reception);
    }

}
