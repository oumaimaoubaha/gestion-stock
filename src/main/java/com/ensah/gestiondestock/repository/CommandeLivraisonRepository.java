package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.CommandeLivraison;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CommandeLivraisonRepository extends JpaRepository<CommandeLivraison, Long> {

    CommandeLivraison findByNumeroLivraison(String numeroLivraison);

    List<CommandeLivraison> findByStatut(String statut);

    @Query("SELECT c FROM CommandeLivraison c WHERE " +
            "(:numero IS NULL OR LOWER(c.numeroLivraison) LIKE LOWER(CONCAT('%', :numero, '%'))) AND " +
            "(:produit IS NULL OR LOWER(c.produit.reference) LIKE LOWER(CONCAT('%', :produit, '%'))) AND " +
            "(:date IS NULL OR c.dateLivraison = :date) AND " +
            "(:entrepotId IS NULL OR c.entrepot.id = :entrepotId) AND " +
            "(:statut IS NULL OR LOWER(c.statut) = LOWER(:statut))")
    Page<CommandeLivraison> searchCommandes(
            @Param("numero") String numero,
            @Param("produit") String produit,
            @Param("date") LocalDate date,
            @Param("entrepotId") Long entrepotId,
            @Param("statut") String statut,
            Pageable pageable
    );

    @Query("SELECT c FROM CommandeLivraison c WHERE " +
            "(:numero IS NULL OR c.numeroLivraison LIKE %:numero%) OR " +
            "(:produit IS NULL OR c.produit.reference LIKE %:produit%)")
    List<CommandeLivraison> searchByNumeroOrProduit(@Param("numero") String numero, @Param("produit") String produit);
}
