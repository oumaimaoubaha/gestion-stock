package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.Reception;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReceptionRepository extends JpaRepository<Reception, Long> {

    @Query("SELECT r FROM Reception r WHERE " +
            "(:produit IS NULL OR LOWER(r.produit) LIKE LOWER(CONCAT('%', :produit, '%'))) AND " +
            "(:entrepotId IS NULL OR r.entrepot.id = :entrepotId) AND " +
            "(:dateMin IS NULL OR r.dateReception >= :dateMin) AND " +
            "(:dateMax IS NULL OR r.dateReception <= :dateMax)")
    List<Reception> findByCriteria(@Param("produit") String produit,
                                   @Param("entrepotId") Long entrepotId,
                                   @Param("dateMin") LocalDate dateMin,
                                   @Param("dateMax") LocalDate dateMax);
}
