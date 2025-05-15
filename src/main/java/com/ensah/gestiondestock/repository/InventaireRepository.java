package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.Inventaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventaireRepository extends JpaRepository<Inventaire, Long> {
}
