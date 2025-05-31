package com.ensah.gestiondestock.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class CommandeLivraison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateLivraison;

    @Column(unique = true)
    private String numeroLivraison;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    private int quantite;
    private String unite;
    private String client;
    private String statut = "non livré";

    @ManyToOne(optional = false)
    @JoinColumn(name = "entrepot_id", nullable = false)
    private Entrepot entrepot;

    private String remarque;

    // Getters & setters uniquement utiles
    public Long getId() { return id; }

    public LocalDate getDateLivraison() { return dateLivraison; }
    public void setDateLivraison(LocalDate dateLivraison) { this.dateLivraison = dateLivraison; }

    public String getNumeroLivraison() { return numeroLivraison; }
    public void setNumeroLivraison(String numeroLivraison) { this.numeroLivraison = numeroLivraison; }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public String getUnite() { return unite; }
    public void setUnite(String unite) { this.unite = unite; }

    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Entrepot getEntrepot() { return entrepot; }
    public void setEntrepot(Entrepot entrepot) { this.entrepot = entrepot; }

    public String getRemarque() { return remarque; }
    public void setRemarque(String remarque) { this.remarque = remarque; }
    public void setId(Long id) {
        this.id = id;
    }

}
