package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.Entrepot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntrepotRepository extends JpaRepository<Entrepot, Long> {

    // Recherche d'entrepôts par nom (partiel, insensible à la casse)
    List<Entrepot> findByNomContainingIgnoreCase(String nom);
}
