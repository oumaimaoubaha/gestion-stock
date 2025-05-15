package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
}
