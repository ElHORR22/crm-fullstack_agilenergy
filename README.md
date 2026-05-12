# 🏭 CRM Web App — SNDP Agil Energy

> Application CRM full-stack développée en PFE pour **SNDP Agil Energy**  
> **Note : 14/20 | Superviseur : Dr. Marouene Chaieb | Esprit School of Business 2025**

---

## 🔐 Comptes de démonstration

| Email | Mot de passe | Rôle |
|-------|-------------|------|
| admin@agil.com.tn | admin123 | Administrateur |
| commercial@agil.com.tn | com123 | Commercial |
| logistique@agil.com.tn | log123 | Logistique |

---

## 🛠️ Stack technique

| Couche | Technologie | Version |
|--------|------------|---------|
| Frontend | Angular | 19.2.5 |
| Backend | Spring Boot | 3.4.4 |
| Langage | Java | 17 |
| Sécurité | Spring Security + JWT | jjwt 0.12.6 |
| Base de données | MySQL | 8.0 |
| PDF | iTextPDF | 5.5.13.3 |
| Mail | Spring Mail | — |
| Déploiement | Docker + Nginx | — |

---

## ✨ Fonctionnalités

- 🔐 **Auth JWT** — Login sécurisé, rôles multiples (Admin, Commercial, Logistique, Client)
- 👥 **Prospects → Clients** — Conversion en 1 clic avec transfert automatique des données
- 📄 **Devis PDF** — Génération automatique avec calcul TTC, poids, éco-zit
- 📊 **Dashboard** — Statistiques temps réel (CA, prospects, clients, devis par mois)
- 🏭 **Catalogue** — Produits & emballages avec codes, prix multiples, poids
- 👤 **Gestion utilisateurs** — CRUD avec rôles, activation/désactivation
- 📧 **Reset password** — Par email avec token temporaire (Spring Mail)
- ⚙️ **Paramétrage** — TVA configurable, secteurs, sources de prospection
- 🐳 **Docker** — Déploiement complet en 1 commande

---

## 🏃 Lancer en local

### Prérequis
Java 17+ · Node.js 20+ · MySQL 8.0 · Maven 3.9+

### Backend
```bash
cd BStagePFE
# Copier et remplir les variables d'environnement
cp .env.example .env
./mvnw spring-boot:run
# API disponible sur http://localhost:8080
```

### Frontend
```bash
cd AgilEnergyCRM
npm install
ng serve
# App disponible sur http://localhost:4200
```

### Docker (tout en une commande)
```bash
cp .env.example .env   # Remplir les valeurs
docker-compose up --build
```

---

## 📁 Structure du projet
CRM/
├── BStagePFE/                    # Backend Spring Boot
│   ├── src/main/java/com/example/bstagepfe/
│   │   ├── controllers/          # 13 controllers REST
│   │   ├── services/             # Logique métier
│   │   ├── entities/             # Entités JPA (11)
│   │   ├── DTO/                  # Data Transfer Objects
│   │   ├── repos/                # Spring Data repositories
│   │   ├── JwtAuthFilter.java    # Filtre JWT
│   │   └── SecurityConfig.java   # Spring Security
│   └── src/test/                 # Tests JUnit + Mockito
├── AgilEnergyCRM/                # Frontend Angular 19
│   ├── src/app/
│   │   ├── components/           # 12 composants
│   │   ├── service/              # Services HTTP
│   │   ├── guards/               # Auth guards
│   │   ├── interceptors/         # JWT interceptor
│   │   └── environments/         # Config dev/prod
│   └── nginx.conf                # Config Nginx production
├── docker-compose.yml
├── .env.example
└── README.md---

## 🔒 Sécurité

- JWT stateless (HS256, expiration 24h)
- BCrypt pour les mots de passe
- CORS configuré explicitement
- Variables d'environnement pour tous les secrets
- Soft delete sur toutes les entités sensibles

---

*Développé par **[Haroun Dorbez](https://linkedin.com/in/haroun-dorbez)***  
*GitHub : [@ElHORR22](https://github.com/ElHORR22)*
