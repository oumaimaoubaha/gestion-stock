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
