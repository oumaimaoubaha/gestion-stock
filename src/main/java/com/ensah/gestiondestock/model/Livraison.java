package com.ensah.gestiondestock.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Livraison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateLivraison;
    private int quantite;
    private String remarque;
    private String unite;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @ManyToOne(optional = false)
    @JoinColumn(name = "entrepot_id", nullable = false)
    private Entrepot entrepot;

    @ManyToOne
    @JoinColumn(name = "commande_livraison_id")
    private CommandeLivraison commandeLivraison;

    // Getters et Setters
    public Long getId() { return id; }

    public LocalDate getDateLivraison() { return dateLivraison; }
    public void setDateLivraison(LocalDate dateLivraison) { this.dateLivraison = dateLivraison; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public String getRemarque() { return remarque; }
    public void setRemarque(String remarque) { this.remarque = remarque; }

    public String getUnite() { return unite; }
    public void setUnite(String unite) { this.unite = unite; }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public Entrepot getEntrepot() { return entrepot; }
    public void setEntrepot(Entrepot entrepot) { this.entrepot = entrepot; }

    public CommandeLivraison getCommandeLivraison() { return commandeLivraison; }
    public void setCommandeLivraison(CommandeLivraison commandeLivraison) { this.commandeLivraison = commandeLivraison; }

    @Override
    public String toString() {
        return "Livraison{id=" + id + ", date=" + dateLivraison + "}";
    }
}
