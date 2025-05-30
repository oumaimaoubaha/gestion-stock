package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    // Pour l'inventaire ou le transfert : récupérer tous les produits d’un entrepôt
    List<Produit> findByEntrepotId(Long entrepotId);

    // Recherche floue sur la référence ou le libellé
    List<Produit> findByReferenceContainingIgnoreCase(String ref);
    List<Produit> findByLibelleContainingIgnoreCase(String libelle);

    // Recherche exacte
    List<Produit> findByReference(String reference);
    Produit findByLibelle(String libelle);
    @Query("SELECT DISTINCT p.unite FROM Produit p WHERE p.unite IS NOT NULL")
    List<String> findDistinctUnites();

    // Utile si besoin d’un seul résultat
    Produit findFirstByReference(String reference);

    // ✅ Corrigé : libelle au lieu de nom
    Produit findByLibelleAndEntrepotId(String libelle, Long entrepotId);
    Produit findByReferenceAndEntrepotId(String reference, Long entrepotId);


}
