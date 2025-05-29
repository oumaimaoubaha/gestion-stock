package com.ensah.gestiondestock.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeAchat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateAchat;
    private String numeroAchat;
    private String produit;
    private String unite;
    private int quantite;
    private String source;

    @OneToMany(mappedBy = "commandeAchat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneCommande> lignes;
    @OneToMany(mappedBy = "commandeAchat", cascade = CascadeType.ALL)
    private List<Reception> receptions;

}