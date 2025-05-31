package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.CommandeLivraison;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeLivraisonRepository extends JpaRepository<CommandeLivraison, Long> {
    CommandeLivraison findByNumeroLivraison(String numeroLivraison);
}