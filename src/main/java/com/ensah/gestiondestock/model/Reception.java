package com.ensah.gestiondestock.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;


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

        @NotBlank(message = "Le numéro d'achat est obligatoire")
        private String numeroAchat;

        @NotBlank(message = "Le produit est obligatoire")
        private String produit;

        @NotBlank(message = "L’unité est obligatoire")
        private String unite;

        @Min(value = 1, message = "La quantité doit être au minimum 1")
        private int quantite;

        @NotBlank(message = "La source est obligatoire")
        private String source;

        @NotBlank(message = "L'entrepôt est obligatoire")
        private String entrepot;

        private String remarque; // ✅ champ corrigé

        @ManyToOne
        @JoinColumn(name = "commande_achat_id")
        private CommandeAchat commandeAchat;

        // ✅ getters et setters générés automatiquement avec Lombok (@Data) ou manuellement si nécessaire
    }
