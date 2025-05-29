package com.ensah.gestiondestock.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livraison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateLivraison;

    private int quantite;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @ManyToOne
    @JoinColumn(name = "entrepot_id")
    private Entrepot entrepot;

    @Override
    public String toString() {
        return "Livraison{id=" + id + ", date=" + dateLivraison + "}";
    }
}
