package com.ensah.gestiondestock.model;

public class Entrepot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String adresse;

    // Transferts
    @OneToMany(mappedBy = "source")
    private List<Transfert> transfertsSortants;

    @OneToMany(mappedBy = "destination")
    private List<Transfert> transfertsEntrants;

    // Réceptions
    @OneToMany(mappedBy = "entrepot")
    private List<Reception> receptions;

    // Livraisons
    @OneToMany(mappedBy = "entrepot")
    private List<Livraison> livraisons;

    // Inventaires
    @OneToMany(mappedBy = "entrepot")
    private List<Inventaire> inventaires;
}
