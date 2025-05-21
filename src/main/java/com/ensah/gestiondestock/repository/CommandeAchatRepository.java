package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.CommandeAchat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeAchatRepository extends JpaRepository<CommandeAchat, Long> {
    CommandeAchat findByNumero(String numero);
}
