package com.ensah.gestiondestock.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String produit;
    private int quantite;
    private String unite;

    @ManyToOne
    @JoinColumn(name = "commande_achat_id")
    private CommandeAchat commandeAchat;
}
