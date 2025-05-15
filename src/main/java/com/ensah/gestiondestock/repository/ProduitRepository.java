package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByReferenceContainingIgnoreCase(String reference);
    List<Produit> findByLibelleContainingIgnoreCase(String libelle);
}
