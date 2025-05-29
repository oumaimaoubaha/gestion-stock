package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.CommandeAchat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List; // ✅ Manquait

public interface CommandeAchatRepository extends JpaRepository<CommandeAchat, Long> {

    CommandeAchat findByNumeroAchat(String numeroAchat);

    // ✅ méthode dérivée correcte pour trier par date décroissante
    Page<CommandeAchat> findAllByOrderByDateAchatDesc(Pageable pageable);

    @Query("SELECT c FROM CommandeAchat c WHERE c.id NOT IN :ids")
    List<CommandeAchat> findByIdNotIn(@Param("ids") List<Long> ids);

    List<CommandeAchat> findAll();

}
