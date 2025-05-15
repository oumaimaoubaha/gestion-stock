package com.ensah.gestiondestock.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;
    private String libelle;
    private String type;
    private String unite;

    // Transferts
    @OneToMany(mappedBy = "produit")
    private List<Transfert> transferts;

    // Réceptions
    @OneToMany(mappedBy = "produit")
    private List<Reception> receptions;

    // Livraisons
    @OneToMany(mappedBy = "produit")
    private List<Livraison> livraisons;

    // Lignes d’inventaire
    @OneToMany(mappedBy = "produit")
    private List<LigneInventaire> lignesInventaire;
}
