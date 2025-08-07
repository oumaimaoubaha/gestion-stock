#  Application Web de Gestion Logistique – Mini Projet Spring Boot

Cette application web permet la gestion complète d’un système logistique : **entrepôts**, **produits**, **commandes d’achat**, **réceptions**, **commandes de livraison**, **livraisons**, **transferts de stock** et **inventaires**.

Elle a été développée avec **Java Spring Boot** et **Thymeleaf** dans le cadre d’un mini projet universitaire, avec une architecture claire et modulaire inspirée des systèmes professionnels.

---

##  Objectifs du Projet

- Suivre les mouvements de stock entre différents entrepôts  
- Gérer les commandes d’achat (fournisseurs) et de livraison (clients)  
- Réceptionner les produits et mettre à jour les stocks automatiquement  
- Réaliser des inventaires avec écart de stock  
- Offrir une interface web simple, fluide et sécurisée  

---

##  Fonctionnalités principales

### 🔐 Authentification
- Accès sécurisé à l’application  
- Interface de **connexion**  
- Gestion des sessions utilisateurs  

### 🏢 Gestion des Entrepôts
- Ajouter, modifier, supprimer un entrepôt  
- Lister les entrepôts existants  

### 📦 Gestion des Produits
- Création de produits avec unité, type, stock  
- Association des produits aux entrepôts  

### 🛒 Commandes d’Achat
- Création de commandes fournisseurs  
- Suivi des lignes de commande  
- Génération de **bon de commande**  

### 📥 Réceptions de Produits
- Interface de réception des produits commandés  
- Sélection des lignes réceptionnées  
- Mise à jour automatique des stocks  
- Statut mis à jour : **reçue / partiellement reçue / non reçue**  
- Historique des réceptions  

### 🚚 Commandes de Livraison
- Enregistrement des **demandes de livraison** (client, entrepôt, produits)  
- Consultation des commandes par statut  

### ✅ Livraison Effective
- Interface dédiée à la **validation des livraisons**  
- Sélection des lignes livrées  
- Débit automatique du stock  
- Mise à jour des statuts de livraison  

### 🔁 Transferts
- Transfert de stock d’un entrepôt à un autre  
- Suivi et historique des mouvements  

### 📊 Inventaires
- Création d’un inventaire par entrepôt  
- Saisie du stock physique  
- Calcul automatique des écarts (positifs / négatifs)  
- Validation et sauvegarde  

---

##  Technologies utilisées

| Technologie        | Rôle                                 |
|--------------------|---------------------------------------|
| Java 17            | Backend principal                     |
| Spring Boot        | Framework backend (MVC + REST)        |
| Spring Security    | Authentification et sécurisation      |
| Spring Data JPA    | ORM pour l’accès aux données          |
| Thymeleaf          | Moteur de templates HTML              |
| MySQL              | Base de données relationnelle         |
| Bootstrap / CSS    | Design de l’interface utilisateur     |
| IntelliJ / VS Code | Environnement de développement        |
| Ubuntu             | Système de développement utilisé      |

---

##  Architecture du projet

L’application suit l’architecture **MVC** :
```bash
src/
├── controller/
├── service/
├── repository/
├── entity/
├── templates/
├── static/
└── application.properties
```


📁 **Explication des dossiers :**

- `controller/` : gère les requêtes utilisateur (Spring MVC)  
- `service/` : logique métier (traitements, calculs, gestion)  
- `repository/` : accès aux données avec Spring Data  
- `entity/` : entités JPA mappées à la base de données  
- `templates/` : pages HTML rendues avec Thymeleaf  
- `static/` : ressources statiques (CSS, JS, images)  
- `application.properties` : configuration générale  

---

## 📸 Captures d’écran

### 🔐 Authentification
![Authentification](assets/authentification.png)

### 🏠 Page d’accueil
![Page d’accueil](assets/page_accueil.png)

### 🏢 Gestion des Entrepôts
![Entrepôts](assets/entrepots.png)

### 📦 Gestion des Produits
![Produits](assets/produits.png)

### 🛒 Commandes d’Achat
![Commandes d’Achat](assets/commande_achat.png)

### 📥 Réception des Produits
![Réception](assets/reception.png)

### 🚚 Commandes de Livraison
![Commandes de Livraison](assets/commande_livraison.png)

### ✅ Livraison
![Livraison](assets/livraison.png)

### 🔁 Transferts de Stock
![Transfert](assets/transfert.png)

### 📊 Inventaires
![Inventaire](assets/inventaire.png)
---

##  Leçons apprises

Ce projet m’a permis de consolider mes compétences dans :

- Le développement d’applications Spring Boot complètes (MVC + JPA + Sécurité)
- La gestion de workflows complexes (achat → réception → stock → livraison)
- L’intégration d’interfaces web avec Thymeleaf
- L’utilisation de bases de données relationnelles (MySQL)
- Le déploiement local sous environnement **Ubuntu**

---

## Réalisé par

- **Nom** : Oumaima  
- **Projet académique** – Année 2024/2025  
- **Encadré par** : M. Tarik Boudaa  

---

##  Liens utiles

- [Lien GitHub du projet](https://github.com/oumaimaoubaha/gestion-stock)  
- [Mon profil LinkedIn](https://www.linkedin.com/in/oumaima-oubaha-493b03296/)

