package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.CommandeAchat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CommandeAchatRepository extends JpaRepository<CommandeAchat, Long> {

    CommandeAchat findByNumeroAchat(String numeroAchat);

    Page<CommandeAchat> findAllByOrderByDateAchatDesc(Pageable pageable);

    @Query("SELECT c FROM CommandeAchat c WHERE c.id NOT IN :ids")
    List<CommandeAchat> findByIdNotIn(@Param("ids") List<Long> ids);

    List<CommandeAchat> findAll();

    // 🔍 Recherche avec filtres dynamiques + tri par date décroissante
    @Query("SELECT c FROM CommandeAchat c WHERE " +
            "(:numero IS NULL OR LOWER(c.numeroAchat) LIKE LOWER(CONCAT('%', :numero, '%'))) AND " +
            "(:produit IS NULL OR LOWER(c.produit) LIKE LOWER(CONCAT('%', :produit, '%'))) AND " +
            "(:date IS NULL OR c.dateAchat = :date) " +
            "ORDER BY c.dateAchat DESC")
    Page<CommandeAchat> searchCommandes(
            @Param("numero") String numero,
            @Param("produit") String produit,
            @Param("date") LocalDate date,
            Pageable pageable);
}
