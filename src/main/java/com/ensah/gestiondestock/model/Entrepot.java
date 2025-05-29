package com.ensah.gestiondestock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entrepot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;

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
    @OneToMany(mappedBy = "entrepot")
    @JsonIgnore
    private List<Produit> produits;
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Entrepot{id=" + id + ", code='" + code + "', nom='" + nom + "'}";
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setId(Long id) {
        this.id = id;
    }





}
