# Application Web de Gestion Logistique – Mini Projet Spring Boot

Cette application web permet la gestion complète d’un système logistique : **entrepôts**, **produits**, **commandes d’achat**, **commandes de livraison**, **livraisons**, **transferts de stock** et **inventaires**.

Elle a été développée avec **Java Spring Boot** et **Thymeleaf** dans le cadre d’un mini projet universitaire, avec une architecture claire et modulaire inspirée des systèmes utilisés en entreprise.

---

##  Objectifs du Projet

- Suivre les mouvements de stock entre différents entrepôts  
- Gérer les commandes d’achat (fournisseurs) et de livraison (clients)  
- Mettre à jour les stocks en temps réel lors des transferts et livraisons  
- Réaliser des inventaires avec écart de stock  
- Offrir une interface web claire, fluide et efficace  

---

##  Fonctionnalités principales

### 🏢 Gestion des Entrepôts
- Ajouter, modifier, supprimer un entrepôt  
- Lister les entrepôts existants  

### 📦 Gestion des Produits
- Création de produits avec unité, type et quantité  
- Association aux entrepôts  

### 🛒 Commandes d’Achat
- Création de commandes fournisseurs  
- Suivi des réceptions  
- Mise à jour des stocks à la réception  

### 🚚 Commandes de Livraison
- Enregistrement des **demandes de livraison** (client, entrepôt, produits)  
- Consultation des commandes par statut  

### ✅ Livraison Effective
- Interface dédiée à la **validation des livraisons**  
- Sélection des lignes livrées  
- Débit automatique du stock  
- Statut mis à jour : **livré / non livré**  

### 🔁 Transferts
- Transfert de stock d’un entrepôt à un autre  
- Suivi et historique des mouvements  

### 📊 Inventaires
- Création d’un inventaire par entrepôt  
- Saisie du stock réel  
- Calcul automatique des écarts (positifs / négatifs)  
- Validation de l’inventaire  

---

##  Technologies utilisées

| Technologie        | Rôle                                 |
|--------------------|---------------------------------------|
| Java 17            | Backend principal                     |
| Spring Boot        | Framework backend (MVC + REST)        |
| Spring Data JPA    | ORM pour l’accès aux données          |
| Thymeleaf          | Moteur de templates HTML              |
| MySQL              | Base de données relationnelle         |
| Bootstrap / CSS    | Design de l’interface utilisateur     |
| IntelliJ / VS Code | Environnement de développement        |
| Ubuntu             | Système de développement utilisé      |

---

## 🏗 Architecture du projet

L’application suit l’architecture **MVC** :

src/
├── controller/
├── service/
├── repository/
├── entity/
├── templates/
├── static/
└── application.properties

📁 **Explication des dossiers :**

- `controller/` : gère les requêtes utilisateur (Spring MVC)  
- `service/` : logique métier (traitements, calculs, gestion)  
- `repository/` : interfaces pour accéder à la base via JPA  
- `entity/` : entités représentant les tables (Produit, Entrepôt…)  
- `templates/` : pages HTML rendues avec Thymeleaf  
- `static/` : fichiers CSS, JS et médias  
- `application.properties` : paramètres de connexion et configuration  

---

##  Captures d’écran 
- 🛒 Commandes d’achat 
  ![commandes_achat](assets/commandes_achat.png)

- 🚚 Commandes de livraison  
  ![commandes_livraison](assets/commandes_livraison.png)

- ✅ Livraison effective  
  ![livraison_validation](assets/livraison_validation.png)

- 🔁 Transferts  
  ![transferts](assets/transferts.png)

- 📊 Inventaires  
  ![inventaires](assets/inventaires.png)
##  Leçons apprises

Ce projet m’a permis de mettre en pratique plusieurs aspects du développement d’applications web réelles, notamment :

- La modélisation de données complexes
- L’implémentation d’une architecture MVC complète
- La gestion de la logique métier et des interactions avec la base de données
- Le développement d’une interface utilisateur claire et ergonomique
- Le déploiement local d’un projet Java sous environnement Ubuntu

---

##  Réalisé par

- **Nom** : Oumaima  
- **Projet académique** – Année 2024/2025  
- **Encadré par** : M Tarik Boudaa

---

##  Liens utiles

-  [Lien GitHub du projet](https://github.com/ton-utilisateur/gestion-logistique)  
-  [Profil LinkedIn](https://www.linkedin.com/in/ton-lien)
