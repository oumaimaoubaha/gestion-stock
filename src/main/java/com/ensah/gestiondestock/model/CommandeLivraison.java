package com.ensah.gestiondestock.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeLivraison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateLivraison;

    @Column(unique = true)
    private String numeroLivraison;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private int quantite;
    private String unite;
    private String client;

    @ManyToOne
    @JoinColumn(name = "entrepot_id")
    private Entrepot entrepot;

    private String remarque;
    public Long getId() {
        return id;
    }

    public String getNumeroLivraison() {
        return numeroLivraison;
    }

    public void setDateLivraison(LocalDate dateLivraison) {
        this.dateLivraison = dateLivraison;
    }



    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }
    public int getQuantite() {
        return quantite;
    }
    public String getUnite() {
        return unite;
    }

    public Entrepot getEntrepot() {
        return entrepot;
    }

    public String getRemarque() {
        return remarque;
    }




}