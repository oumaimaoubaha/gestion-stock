// src/main/java/com/ensah/gestiondestock/repository/TransfertRepository.java
package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.Transfert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransfertRepository extends JpaRepository<Transfert, Long> {

    // 1) Méthode existante (retourne List) – on la conserve tel quel
    @Query("SELECT t FROM Transfert t WHERE " +
            "(:date IS NULL   OR t.date = :date) AND " +
            "(:produitId IS NULL OR (t.produit IS NOT NULL AND t.produit.id = :produitId)) AND " +
            "(:sourceId IS NULL   OR t.source.id = :sourceId) AND " +
            "(:destinationId IS NULL OR t.destination.id = :destinationId)")
    List<Transfert> findByCriteria(
            @Param("date") LocalDate date,
            @Param("produitId") Long produitId,
            @Param("sourceId") Long sourceId,
            @Param("destinationId") Long destinationId
    );

    // 2) Nouvelle méthode paginée avec filtrage sur référence du produit
    @Query("SELECT t FROM Transfert t WHERE " +
            "(:date IS NULL OR t.date = :date) AND " +
            "(:referenceProduit IS NULL OR (t.produit IS NOT NULL AND t.produit.reference LIKE %:referenceProduit%)) AND " +
            "(:sourceId IS NULL OR t.source.id = :sourceId) AND " +
            "(:destinationId IS NULL OR t.destination.id = :destinationId)")
    Page<Transfert> findByCriteriaWithReference(
            @Param("date") LocalDate date,
            @Param("referenceProduit") String referenceProduit,
            @Param("sourceId") Long sourceId,
            @Param("destinationId") Long destinationId,
            Pageable pageable
    );
}
