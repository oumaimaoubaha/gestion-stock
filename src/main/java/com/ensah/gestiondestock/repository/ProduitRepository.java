package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    // ✅ pour inventaire : récupérer les produits d’un entrepôt
    List<Produit> findByEntrepotId(Long entrepotId);

    // (déjà existant dans ton code)
    List<Produit> findByReferenceContainingIgnoreCase(String ref);
    List<Produit> findByLibelleContainingIgnoreCase(String libelle);
    List<Produit> findByReference(String reference);
    Produit findByLibelle(String libelle);
    @Query("SELECT DISTINCT p.unite FROM Produit p WHERE p.unite IS NOT NULL")
    List<String> findDistinctUnites();

    Produit findFirstByReference(String reference);

}
