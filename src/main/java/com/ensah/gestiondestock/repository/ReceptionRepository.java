package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.Reception;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ReceptionRepository extends JpaRepository<Reception, Long> {

    @Query("SELECT r FROM Reception r WHERE " +
            "(:produit IS NULL OR LOWER(r.produit) LIKE LOWER(CONCAT('%', :produit, '%'))) AND " +
            "(:entrepot IS NULL OR LOWER(r.entrepot.nom) LIKE LOWER(CONCAT('%', :entrepot, '%'))) AND " +
            "(:dateMin IS NULL OR r.dateReception >= :dateMin) AND " +
            "(:dateMax IS NULL OR r.dateReception <= :dateMax)")
    Page<Reception> findByCriteria(@Param("dateMin") LocalDate dateMin,
                                   @Param("dateMax") LocalDate dateMax,
                                   @Param("produit") String produit,
                                   @Param("entrepot") String entrepot,
                                   Pageable pageable);
}
