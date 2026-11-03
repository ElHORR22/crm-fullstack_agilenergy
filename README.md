# 🏭 CRM Web App — SNDP Agil Energy

> Application CRM full-stack développée en PFE pour **SNDP Agil Energy**  
> **Note : 14/20 | Superviseur : Dr. Marouene Chaieb | Esprit School of Business 2025**

---

## 🔐 Premier compte administrateur

Au premier démarrage, si la base ne contient aucun utilisateur, le backend crée un administrateur
à partir des variables d'environnement `ADMIN_EMAIL` et `ADMIN_PASSWORD`.
Les autres comptes (commercial, logisticien, client) se créent ensuite depuis « Comptes utilisateurs ».

---

## 🛠️ Stack technique

| Couche | Technologie | Version |
|--------|------------|---------|
| Frontend | Angular | 19.2.5 |
| Composants UI | Angular Material (dialogues, notifications) | 19.2 |
| Backend | Spring Boot | 3.4.4 |
| Langage | Java | 17 |
| Sécurité | Spring Security + JWT | jjwt 0.12.6 |
| Base de données | MySQL | 8.0 |
| PDF | iTextPDF | 5.5.13.3 |
| Mail | Spring Mail | — |
| Déploiement | Docker + Nginx | — |

---

## ✨ Fonctionnalités

- 🔐 **Auth JWT** — Login sécurisé, rôles (Administrateur, Commercial, Logisticien, Client) contrôlés côté serveur
- 👥 **Prospects → Clients** — Conversion en 1 clic : le client est créé avec les données du prospect, qui passe au statut « Converti »
- 📄 **Devis PDF** — Calcul TTC, poids et éco-zit recalculés côté serveur, exonération de TVA par client ; un client ne télécharge que ses propres devis
- 🔄 **Cycle de vie des devis** — Brouillon → Envoyé → Accepté ou Refusé ; seul un brouillon est modifiable, un devis accepté n'est jamais supprimé, le client accepte ou refuse ses devis depuis « Mes devis » et ne voit pas les brouillons ; les devis envoyés dont l'échéance est passée sont signalés
- 📊 **Dashboard** — Chiffre d'affaires des devis acceptés et son évolution mensuelle, devis par statut et taux de conversion, clients par gouvernorat/secteur, devis par mois, top produits
- 🏭 **Catalogue** — Produits & emballages avec codes, prix multiples, poids
- 👤 **Gestion utilisateurs** — CRUD avec rôles, activation/désactivation (effet immédiat)
- 📧 **Mot de passe oublié** — Lien par email valable 15 minutes (Spring Mail)
- ⚙️ **Paramétrage** — TVA configurable, secteurs, sources de prospection
- 📱 **Interface responsive** — Utilisable sur téléphone (menu latéral au bouton, panneaux et popups pleine largeur), confirmations et notifications Angular Material
- 🐳 **Docker** — Déploiement complet en 1 commande

---

## 🏃 Lancer en local

### Prérequis
Java 17+ · Node.js 20+ · MySQL 8.0 (Maven est fourni par `mvnw`)

### Variables d'environnement
Copier `.env.example` en `.env` à la racine et remplir les valeurs.
`JWT_SECRET` doit faire au moins 32 caractères.

### Backend
Spring Boot ne lit pas le fichier `.env` : il faut exporter les variables avant le lancement.
```bash
set -a; source .env; set +a
cd BStagePFE
./mvnw spring-boot:run
# API disponible sur http://localhost:8080
```

### Frontend
```bash
cd AgilEnergyCRM
npm ci
npx ng serve
# App disponible sur http://localhost:4200 (API attendue sur http://localhost:8080)
```

### Docker (tout en une commande)
```bash
cp .env.example .env   # Remplir les valeurs
docker compose up --build
```
Application disponible sur http://localhost:4200 (API sur http://localhost:8080).
Pour un déploiement en ligne, mettre l'URL publique de l'API dans
`AgilEnergyCRM/src/app/environments/environment.prod.ts` et celle du frontend dans les origines CORS de `SecurityConfig`.

### Tests
```bash
cd BStagePFE && ./mvnw test                                  # JUnit, base H2 en mémoire
cd AgilEnergyCRM && npx ng test --watch=false --browsers=ChromeHeadless
```

---

## 📁 Structure du projet
```
CRM/
├── BStagePFE/                    # Backend Spring Boot
│   ├── src/main/java/com/example/bstagepfe/
│   │   ├── controllers/          # 15 contrôleurs REST (+ RoleInitializer : rôles et 1er admin)
│   │   ├── services/             # Logique métier (calcul des devis, PDF, auth…)
│   │   ├── entities/             # 13 entités JPA
│   │   ├── DTO/                  # Data Transfer Objects
│   │   ├── repos/                # Spring Data repositories
│   │   ├── JwtAuthFilter.java    # Filtre JWT
│   │   └── SecurityConfig.java   # Règles d'accès par rôle, CORS
│   └── src/test/                 # Tests JUnit + Mockito (profil test sur H2)
├── AgilEnergyCRM/                # Frontend Angular 19
│   ├── src/app/
│   │   ├── <page>/               # Composants standalone (client, devis, dashboard…)
│   │   ├── service/              # Services HTTP
│   │   ├── login/auth.guard.ts   # Guard de navigation par rôle
│   │   ├── interceptors/         # Intercepteur JWT (401 → connexion, 403 → accès refusé)
│   │   └── environments/         # Config dev/prod
│   └── nginx.conf                # Config Nginx production
├── docker-compose.yml
├── .env.example
└── README.md
```

---

## 🔒 Sécurité

- JWT stateless (HS256, expiration 24h) ; le compte est revérifié à chaque requête
- Autorisation par rôle côté serveur (`SecurityConfig`), le guard Angular n'est qu'une aide à la navigation
- BCrypt pour les mots de passe, jamais renvoyés par l'API ; jeton de réinitialisation stocké haché (SHA-256)
- CORS configuré explicitement
- Variables d'environnement pour tous les secrets
- Soft delete sur les clients, prospects, contacts, produits et emballages (les devis et utilisateurs sont supprimés définitivement)

---

*Développé par **[Haroun Dorbez](https://linkedin.com/in/haroun-dorbez)***  
*GitHub : [@ElHORR22](https://github.com/ElHORR22)*
