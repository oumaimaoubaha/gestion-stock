package com.ensah.gestiondestock.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Reception {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateReception;
    private LocalDate dateAchat;
    private String numeroAchat;
    private String produit;
    private String unite;
    private int quantite;
    private String source;
    private String remarque;

    @ManyToOne
    private Entrepot entrepot;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDateReception() { return dateReception; }
    public void setDateReception(LocalDate dateReception) { this.dateReception = dateReception; }

    public LocalDate getDateAchat() { return dateAchat; }
    public void setDateAchat(LocalDate dateAchat) { this.dateAchat = dateAchat; }

    public String getNumeroAchat() { return numeroAchat; }
    public void setNumeroAchat(String numeroAchat) { this.numeroAchat = numeroAchat; }

    public String getProduit() { return produit; }
    public void setProduit(String produit) { this.produit = produit; }

    public String getUnite() { return unite; }
    public void setUnite(String unite) { this.unite = unite; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getRemarque() { return remarque; }
    public void setRemarque(String remarque) { this.remarque = remarque; }

    public Entrepot getEntrepot() { return entrepot; }
    public void setEntrepot(Entrepot entrepot) { this.entrepot = entrepot; }
}
