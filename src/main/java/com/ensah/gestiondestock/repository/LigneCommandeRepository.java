package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.LigneCommande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {
}
