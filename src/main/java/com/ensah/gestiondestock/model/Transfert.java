package com.ensah.gestiondestock.model;

public class Transfert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private int quantite;

    @ManyToOne
    @JoinColumn(name = "id_source")
    private Entrepot source;

    @ManyToOne
    @JoinColumn(name = "id_destination")
    private Entrepot destination;

    @ManyToOne
    @JoinColumn(name = "id_produit")
    private Produit produit;
}
