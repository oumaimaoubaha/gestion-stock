package com.ensah.gestiondestock.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ensah.gestiondestock.model.CommandeLivraison;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeLivraisonRepository extends JpaRepository<CommandeLivraison, Long> {

    CommandeLivraison findByNumeroLivraison(String numeroLivraison);

    List<CommandeLivraison> findByStatut(String statut);
    @Query("SELECT c FROM CommandeLivraison c WHERE c.statut <> 'livré' " +
            "AND (:numero IS NULL OR c.numeroLivraison LIKE %:numero%) " +
            "AND (:produit IS NULL OR c.produit.reference LIKE %:produit%)")
    List<CommandeLivraison> searchByNumeroOrProduit(@Param("numero") String numero,
                                                    @Param("produit") String produit);

    @Query("SELECT c FROM CommandeLivraison c WHERE c.statut <> 'livré'")
    List<CommandeLivraison> getCommandesNonLivrees();
}
