package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.Transfert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TransfertRepository extends JpaRepository<Transfert, Long> {

    @Query("SELECT t FROM Transfert t WHERE "
            + "(:date IS NULL OR t.date = :date) AND "
            + "(:produitId IS NULL OR (t.produit IS NOT NULL AND t.produit.id = :produitId)) AND "
            + "(:sourceId IS NULL OR t.source.id = :sourceId) AND "
            + "(:destinationId IS NULL OR t.destination.id = :destinationId)")
    List<Transfert> findByCriteria(LocalDate date, Long produitId, Long sourceId, Long destinationId);

    @Query("SELECT t FROM Transfert t WHERE "
            + "(:date IS NULL OR t.date = :date) AND "
            + "(:referenceProduit IS NULL OR (t.produit IS NOT NULL AND t.produit.reference LIKE %:referenceProduit%)) AND "
            + "(:sourceId IS NULL OR t.source.id = :sourceId) AND "
            + "(:destinationId IS NULL OR t.destination.id = :destinationId)")
    List<Transfert> findByCriteriaWithReference(LocalDate date, String referenceProduit, Long sourceId, Long destinationId);


}
