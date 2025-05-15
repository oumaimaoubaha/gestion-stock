package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.Inventaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface InventaireRepository extends JpaRepository<Inventaire, Long> {

    @Query("SELECT i FROM Inventaire i WHERE "
            + "(:date IS NULL OR i.dateInventaire = :date) AND "
            + "(:entrepotId IS NULL OR i.entrepot.id = :entrepotId)")
    List<Inventaire> findByCriteria(LocalDate date, Long entrepotId);
}
