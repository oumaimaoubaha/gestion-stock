package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.CommandeAchat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeAchatRepository extends JpaRepository<CommandeAchat, Long> {

    CommandeAchat findByNumeroAchat(String numeroAchat);

    // ✅ méthode dérivée correcte pour trier par date décroissante
    Page<CommandeAchat> findAllByOrderByDateAchatDesc(Pageable pageable);

}
