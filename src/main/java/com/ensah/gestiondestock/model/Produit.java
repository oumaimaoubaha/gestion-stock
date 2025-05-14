package com.ensah.gestiondestock.model;

public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;
    private String libelle;
    private String type;

    // Transferts
    @OneToMany(mappedBy = "produit")
    private List<Transfert> transferts;

    // Réceptions
    @OneToMany(mappedBy = "produit")
    private List<Reception> receptions;

    // Livraisons
    @OneToMany(mappedBy = "produit")
    private List<Livraison> livraisons;

    // LigneInventaire
    @OneToMany(mappedBy = "produit")
    private List<LigneInventaire> lignesInventaire;
}
