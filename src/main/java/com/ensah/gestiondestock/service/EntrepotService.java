package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.Entrepot;
import com.ensah.gestiondestock.repository.EntrepotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EntrepotService {

    @Autowired
    private EntrepotRepository entrepotRepository;

    public Page<Entrepot> getEntrepotsPaginated(int page, int size) {
        return entrepotRepository.findAll(PageRequest.of(page, size));
    }

    public Entrepot getEntrepotById(Long id) {
        Optional<Entrepot> optional = entrepotRepository.findById(id);
        return optional.orElse(null);
    }

    public void saveOrUpdateEntrepot(Entrepot entrepot) {
        entrepotRepository.save(entrepot);
    }

    public void deleteEntrepot(Long id) {
        entrepotRepository.deleteById(id);
    }
    public List<Entrepot> getAllEntrepots() {
        return entrepotRepository.findAll();
    }

}
