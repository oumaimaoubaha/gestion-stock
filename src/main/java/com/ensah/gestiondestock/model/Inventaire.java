package com.ensah.gestiondestock.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String effectuePar;
    private String validePar;
    private String remarque;
    private LocalDate dateInventaire;

    @ManyToOne
    @JoinColumn(name = "entrepot_id")
    private Entrepot entrepot;

    @OneToMany(mappedBy = "inventaire", cascade = CascadeType.ALL)
    private List<LigneInventaire> lignes;
    public void setEffectuePar(String effectuePar) {
        this.effectuePar = effectuePar;
    }

    public void setValidePar(String validePar) {
        this.validePar = validePar;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public void setDateInventaire(LocalDate dateInventaire) {
        this.dateInventaire = dateInventaire;
    }

    public void setEntrepot(Entrepot entrepot) {
        this.entrepot = entrepot;
    }

    public void setLignes(List<LigneInventaire> lignes) {
        this.lignes = lignes;
    }
    public List<LigneInventaire> getLignes() {
        return lignes;
    }

    @Override
    public String toString() {
        return "Inventaire{id=" + id + ", dateInventaire=" + dateInventaire + "}";
    }


}