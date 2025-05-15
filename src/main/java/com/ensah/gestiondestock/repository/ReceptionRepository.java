package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.Reception;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReceptionRepository extends JpaRepository<Reception, Long> {

    // Recherche personnalisée : date, produit, entrepôt
    @Query("SELECT r FROM Reception r WHERE "
            + "(:date IS NULL OR r.dateReception = :date) AND "
            + "(:produitId IS NULL OR r.produit.id = :produitId) AND "
            + "(:entrepotId IS NULL OR r.entrepot.id = :entrepotId)")
    List<Reception> findByCriteria(LocalDate date, Long produitId, Long entrepotId);
}
