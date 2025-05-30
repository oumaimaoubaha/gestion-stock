package com.ensah.gestiondestock.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private int quantite;
    @Column(length = 255)
    private String remarque;


    @ManyToOne
    @JoinColumn(name = "id_source")
    private Entrepot source;

    @ManyToOne
    @JoinColumn(name = "id_destination")
    private Entrepot destination;

    @ManyToOne
    @JoinColumn(name = "id_produit")
    private Produit produit;


    public void setProduitId(Long id) {
        if (this.produit == null) this.produit = new Produit();
        this.produit.setId(id);
    }

    public void setSourceId(Long id) {
        if (this.source == null) this.source = new Entrepot();
        this.source.setId(id);
    }

    public void setDestinationId(Long id) {
        if (this.destination == null) this.destination = new Entrepot();
        this.destination.setId(id);
    }
    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public Entrepot getDestination() { return destination; }
    public void setDestination(Entrepot destination) { this.destination = destination; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }
    public Entrepot getSource() {
        return source;
    }



}
