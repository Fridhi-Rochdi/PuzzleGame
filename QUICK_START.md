# 🚀 Guide de Démarrage Rapide - Puzzle Game

## ⚡ Installation Express (5 minutes)

### Prérequis
✅ **JDK 17** installé  
✅ **Maven 3.6+** installé  
✅ **Git** installé (optionnel)

### Vérification des Prérequis

```powershell
# Vérifier Java
java -version
# Attendu: java version "17.x.x" ou supérieur

# Vérifier Maven
mvn -version
# Attendu: Apache Maven 3.6.x ou supérieur
```

---

## 📥 Installation

### Option 1 : Depuis Git
```powershell
# 1. Cloner le projet
git clone <URL_DU_DEPOT>
cd puzzle

# 2. Compiler
mvn clean compile

# 3. Lancer le jeu
mvn javafx:run
```

### Option 2 : Depuis ZIP
```powershell
# 1. Extraire le ZIP
# 2. Ouvrir PowerShell dans le dossier

# 3. Compiler
mvn clean compile

# 4. Lancer le jeu
mvn javafx:run
```

---

## 🎮 Contrôles du Jeu

| Touche | Action |
|--------|--------|
| **ENTER** | Démarrer le jeu |
| **← →** | Déplacer la pièce horizontalement |
| **↓** | Accélérer la descente |
| **↑** | Faire tourner la pièce |
| **ESPACE** | Chute instantanée (Hard Drop) |
| **P** | Mettre en pause |
| **R** | Redémarrer (Game Over) |
| **M** | Retour au menu |
| **ESC** | Quitter |

---

## 🎯 Objectif du Jeu

### But
Accumuler le maximum de points en complétant des lignes horizontales.

### Système de Score

#### Score de Base (par ligne complétée)
- **1 ligne** : 100 points
- **2 lignes** : 300 points  
- **3 lignes** : 500 points
- **4 lignes (Tetris!)** : 800 points

#### Power-Ups (15% de chance)
- 🥇 **Golden Piece** : Score ×2
- 💣 **Bomb Piece** : Effet spécial bombe
- 🌈 **Rainbow Piece** : Score ×3 + animation

#### Multiplicateurs Empilables
Les power-ups peuvent s'empiler !
- **Golden + Rainbow** : Score ×6 (2 × 3)
- **Exemple** : 4 lignes + Golden + Rainbow = 800 × 6 = **4800 points !**

### Progression
- Le niveau augmente tous les **10 lignes** complétées
- La vitesse de chute augmente à chaque niveau
- Score requis pour battre votre record !

---

## 📊 Fichier de Log

### Consulter les Logs
Le fichier `game.log` est créé automatiquement à la racine du projet.

```powershell
# Voir les logs en temps réel
Get-Content game.log -Wait -Tail 20
```

### Exemple de Log
```log
[2025-11-28 14:23:45] [INFO] === Puzzle Game Started ===
[2025-11-28 14:23:47] [INFO] [STATE] Game: MENU -> PLAYING
[2025-11-28 14:23:48] [INFO] [FACTORY] Created PuzzlePiece: T
[2025-11-28 14:23:50] [INFO] [FACTORY] Created PowerUp: GOLDEN
[2025-11-28 14:23:50] [INFO] [DECORATOR] GoldenPiece applied to Basic T piece
[2025-11-28 14:24:02] [INFO] [EVENT] Lines cleared: 2 | Score added: 600
[2025-11-28 14:25:05] [INFO] [STATE] Game: PLAYING -> GAME_OVER
[2025-11-28 14:25:05] [INFO] [SCORE] Final score: 2450 | Level: 3 | Lines: 15
```

---

## 🛠️ Commandes Utiles

### Compilation
```powershell
# Compilation simple
mvn compile

# Compilation propre (efface target/)
mvn clean compile

# Compilation + tests
mvn clean test
```

### Exécution
```powershell
# Lancer le jeu
mvn javafx:run

# Alternative si problème
mvn clean javafx:run
```

### Packaging
```powershell
# Créer un JAR exécutable
mvn clean package

# Le JAR est dans: target/puzzle-game-1.0.0.jar
```

### Nettoyage
```powershell
# Nettoyer les fichiers compilés
mvn clean

# Nettoyer + supprimer les logs
mvn clean; Remove-Item game.log -ErrorAction SilentlyContinue
```

---

## 🐛 Résolution de Problèmes

### Problème 1 : "Java version not supported"
```powershell
# Vérifier la version Java
java -version

# Si Java < 17, installer JDK 17+
# Télécharger depuis: https://adoptium.net/
```

### Problème 2 : "Maven command not found"
```powershell
# Installer Maven
# Windows: choco install maven
# Ou télécharger depuis: https://maven.apache.org/download.cgi
```

### Problème 3 : "JavaFX runtime components are missing"
```powershell
# Solution : Utiliser mvn javafx:run au lieu de java -jar
mvn clean javafx:run
```

### Problème 4 : Le jeu est lent ou saccadé
```powershell
# Vérifier les ressources système
# Fermer les applications gourmandes
# Relancer avec plus de mémoire :
$env:MAVEN_OPTS="-Xmx1024m"
mvn javafx:run
```

### Problème 5 : "Port already in use" ou erreur réseau
```powershell
# Pas de problème réseau pour ce jeu !
# Il s'exécute localement sans serveur
```

---

## 📁 Structure du Projet

```
puzzle/
├── src/
│   ├── main/
│   │   ├── java/              # Code source Java
│   │   └── resources/         # Ressources (log4j2.xml)
│   └── test/                  # Tests unitaires
├── docs/                      # Documentation
│   ├── class-diagram.puml     # Diagramme UML
│   └── DESIGN_PATTERNS_GUIDE.md
├── pom.xml                    # Configuration Maven
├── README.md                  # Documentation principale
├── .gitignore                 # Git ignore
└── game.log                   # Log généré (auto)
```

---

## 🎓 Pour la Soutenance

### Fichiers Importants à Montrer
1. ✅ **game.log** : Démonstration du système de logging
2. ✅ **docs/class-diagram.png** : Architecture UML
3. ✅ **README.md** : Documentation complète
4. ✅ **Code source** : Patterns bien commentés

### Démonstration Suggérée
1. **Lancer le jeu** (mvn javafx:run)
2. **Montrer le menu** (State Pattern)
3. **Jouer quelques lignes** (Decorator Pattern avec power-ups)
4. **Mettre en pause** (State Pattern)
5. **Montrer le Game Over** (State Pattern)
6. **Ouvrir game.log** : Montrer la traçabilité complète

### Points Clés à Mentionner
- ✅ **5 Design Patterns** implémentés professionnellement
- ✅ **Logging complet** de tous les événements importants
- ✅ **Architecture extensible** et maintenable
- ✅ **JavaFX moderne** avec interface attractive
- ✅ **Code commenté** et documenté

---

## 📞 Support

### Problème Technique
1. Vérifier les logs dans `game.log`
2. Consulter `DESIGN_PATTERNS_GUIDE.md` pour les patterns
3. Vérifier les prérequis (JDK 17, Maven 3.6+)

### Questions sur les Patterns
Consulter `docs/DESIGN_PATTERNS_GUIDE.md` pour :
- Explications détaillées de chaque pattern
- Exemples de code
- Diagrammes
- Cas d'utilisation

---

## 🎯 Checklist Avant Soutenance

- [ ] Le jeu se lance sans erreur (`mvn javafx:run`)
- [ ] Tous les contrôles fonctionnent
- [ ] Le fichier `game.log` est généré et contient les événements
- [ ] Le diagramme UML est à jour
- [ ] Le README est complet
- [ ] Le code compile sans erreur (`mvn clean compile`)
- [ ] Les 5 patterns sont clairement identifiables dans le code
- [ ] La présentation est préparée (15-20 minutes)

---

## 🚀 Commandes One-Liner

### Démarrage Rapide Complet
```powershell
# Clone + Compile + Run (tout en une fois)
git clone <URL>; cd puzzle; mvn clean compile; mvn javafx:run
```

### Rebuild Complet
```powershell
# Nettoyage total + Recompilation + Lancement
mvn clean; mvn compile; mvn javafx:run
```

### Vérification Complète
```powershell
# Compile + Tests + Package
mvn clean verify package
```

---

## 🎮 Bon Jeu !

**N'oubliez pas** : Ce projet démontre l'application professionnelle de 5 Design Patterns dans un contexte réel. Profitez du jeu tout en apprenant ! 🎓

---

**Version** : 1.0.0  
**Projet** : Design Patterns - Puzzle Game  
**Enseignant** : Haythem Ghazouani  
**Année** : 2025-2026
