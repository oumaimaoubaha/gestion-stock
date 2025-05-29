package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.LigneCommande;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {
    @Modifying
    @Query("DELETE FROM LigneCommande l WHERE l.commandeAchat.id = :commandeId")
    void deleteByCommandeAchatId(@Param("commandeId") Long commandeId);
}
