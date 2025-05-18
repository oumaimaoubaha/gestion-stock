package com.ensah.gestiondestock.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;
    private String libelle;
    private String type;
    private String unite;
    private int quantiteStock;

    @ManyToOne
    @JoinColumn(name = "entrepot_id")
    private Entrepot entrepot;

    @OneToMany(mappedBy = "produit")
    private List<Transfert> transferts;

    @OneToMany(mappedBy = "produit")
    private List<Reception> receptions;

    @OneToMany(mappedBy = "produit")
    private List<Livraison> livraisons;

    @OneToMany(mappedBy = "produit")
    private List<LigneInventaire> lignesInventaire;

    // ======= Getters & Setters manuels =======

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public Entrepot getEntrepot() {
        return entrepot;
    }

    public void setEntrepot(Entrepot entrepot) {
        this.entrepot = entrepot;
    }

    public List<Transfert> getTransferts() {
        return transferts;
    }

    public void setTransferts(List<Transfert> transferts) {
        this.transferts = transferts;
    }
    public int getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(int quantiteStock) {
        this.quantiteStock = quantiteStock;
    }
    public List<Reception> getReceptions() {
        return receptions;
    }

    public void setReceptions(List<Reception> receptions) {
        this.receptions = receptions;
    }

    public List<Livraison> getLivraisons() {
        return livraisons;
    }

    public void setLivraisons(List<Livraison> livraisons) {
        this.livraisons = livraisons;
    }

    public List<LigneInventaire> getLignesInventaire() {
        return lignesInventaire;
    }

    public void setLignesInventaire(List<LigneInventaire> lignesInventaire) {
        this.lignesInventaire = lignesInventaire;
    }
}
