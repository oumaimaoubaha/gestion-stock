package com.ensah.gestiondestock.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reception {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull(message = "La date de réception est obligatoire")
        private LocalDate dateReception;

        @NotNull(message = "La date d'achat est obligatoire")
        private LocalDate dateAchat;

        private String numeroAchat;

        @NotBlank(message = "Le produit est obligatoire")
        private String produit;

        @NotBlank(message = "L’unité est obligatoire")
        private String unite;

        @Min(value = 1, message = "La quantité doit être au minimum 1")
        private int quantite;

        @NotBlank(message = "La source est obligatoire")
        private String source;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "entrepot_id")
        private Entrepot entrepot;

        private String remarque;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "commande_achat_id")
        private CommandeAchat commandeAchat;
}
