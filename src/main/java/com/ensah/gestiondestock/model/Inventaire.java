package com.ensah.gestiondestock.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateInventaire;

    @ManyToOne
    @JoinColumn(name = "entrepot_id")
    private Entrepot entrepot;

    @OneToMany(mappedBy = "inventaire", cascade = CascadeType.ALL)
    private List<LigneInventaire> lignes;
}
