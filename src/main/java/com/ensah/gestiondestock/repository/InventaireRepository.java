package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.Inventaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InventaireRepository extends JpaRepository<Inventaire, Long> {

    // 🔎 Recherche simple (non utilisée actuellement)
    @Query("SELECT i FROM Inventaire i WHERE " +
            "(:date IS NULL OR i.dateInventaire = :date) AND " +
            "(:entrepotId IS NULL OR i.entrepot.id = :entrepotId)")
    List<Inventaire> findByCriteria(@Param("date") LocalDate date,
                                    @Param("entrepotId") Long entrepotId);

    // 🔍 Recherche avec période + entrepôt (non paginée)
    @Query("SELECT i FROM Inventaire i WHERE " +
            "(:entrepotId IS NULL OR i.entrepot.id = :entrepotId) AND " +
            "(:dateMin IS NULL OR i.dateInventaire >= :dateMin) AND " +
            "(:dateMax IS NULL OR i.dateInventaire <= :dateMax)")
    List<Inventaire> findByPeriodeAndEntrepot(@Param("dateMin") LocalDate dateMin,
                                              @Param("dateMax") LocalDate dateMax,
                                              @Param("entrepotId") Long entrepotId);

    // ✅ Version paginée
    @Query("SELECT i FROM Inventaire i WHERE " +
            "(:entrepotId IS NULL OR i.entrepot.id = :entrepotId) AND " +
            "(:dateMin IS NULL OR i.dateInventaire >= :dateMin) AND " +
            "(:dateMax IS NULL OR i.dateInventaire <= :dateMax)")
    Page<Inventaire> findByPeriodeAndEntrepot(@Param("dateMin") LocalDate dateMin,
                                              @Param("dateMax") LocalDate dateMax,
                                              @Param("entrepotId") Long entrepotId,
                                              Pageable pageable);
}
