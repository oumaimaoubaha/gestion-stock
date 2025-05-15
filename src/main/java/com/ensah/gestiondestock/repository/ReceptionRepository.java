package com.ensah.gestiondestock.repository;

import com.ensah.gestiondestock.model.Reception;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceptionRepository extends JpaRepository<Reception, Long> {
}
