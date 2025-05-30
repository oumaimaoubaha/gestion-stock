package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {

    @Query("SELECT l FROM Livraison l WHERE "
            + "(:date IS NULL OR l.dateLivraison = :date) AND "
            + "(:produitId IS NULL OR l.produit.id = :produitId) AND "
            + "(:entrepotId IS NULL OR l.entrepot.id = :entrepotId)")
    List<Livraison> findByCriteria(LocalDate date, Long produitId, Long entrepotId);

    @Query("SELECT l FROM Livraison l WHERE "
            + "(:dateDebut IS NULL OR l.dateLivraison >= :dateDebut) AND "
            + "(:dateFin IS NULL OR l.dateLivraison <= :dateFin) AND "
            + "(:referenceProduit IS NULL OR l.produit.reference LIKE %:referenceProduit%) AND "
            + "(:entrepotId IS NULL OR l.entrepot.id = :entrepotId)")
    List<Livraison> findByFilters(LocalDate dateDebut, LocalDate dateFin, String referenceProduit, Long entrepotId);

}
