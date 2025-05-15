package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
