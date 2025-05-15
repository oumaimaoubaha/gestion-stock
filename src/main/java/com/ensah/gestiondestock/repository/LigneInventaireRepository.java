package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.LigneInventaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneInventaireRepository extends JpaRepository<LigneInventaire, Long> {
}
