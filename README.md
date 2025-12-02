# Puzzle Game - Design Patterns Project

![Java](https://img.shields.io/badge/Java-17-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue)
![Maven](https://img.shields.io/badge/Maven-3.6+-red)
![License](https://img.shields.io/badge/License-Educational-green)

## 📋 Description

Jeu de puzzle de type Tetris développé dans le cadre du module **Design Patterns**. Ce projet démontre l'application pratique et professionnelle de **5 patrons de conception** majeurs dans le développement d'un jeu vidéo complet avec interface graphique JavaFX.

**Enseignant** : Haythem Ghazouani  
**Année universitaire** : 2025-2026

---

## 🎯 Objectifs Pédagogiques Atteints

Ce projet démontre la maîtrise de :
- ✅ **5 Design Patterns** correctement implémentés
- ✅ Architecture logicielle **maintenable et extensible**
- ✅ Interface graphique **JavaFX** complète et interactive
- ✅ Système de **logging** avec traçabilité complète
- ✅ Gestion de versions avec **Git**
- ✅ Documentation technique **UML**

---

## 🎮 Gameplay

### Fonctionnalités Principales
- **7 types de pièces** différentes (I, O, T, S, Z, J, L)
- **Power-ups aléatoires** (15% de chance) :
  - 🥇 **Golden Piece** : Score multiplié par 2
  - 💣 **Bomb Piece** : Effet de bombe spécial
  - 🌈 **Rainbow Piece** : Score multiplié par 3 + animation
- **Système de niveaux** progressifs
- **Effets visuels** modernes et animés
- **Score** avec multiplicateurs
- **Interface HUD** complète

### Contrôles
| Touche | Action |
|--------|--------|
| `←` `→` | Déplacer la pièce |
| `↓` | Descente rapide |
| `↑` | Rotation |
| `ESPACE` | Chute instantanée |
| `P` | Pause |
| `ENTER` | Démarrer/Redémarrer |
| `M` | Menu principal |
| `ESC` | Quitter |

---

## 🏗️ Design Patterns Implémentés

### 1. **State Pattern** ⭐ (Obligatoire)
**Localisation** : `com.designpatterns.puzzle.patterns.state`

Gestion des états du jeu avec transitions fluides et tracées.

**États implémentés** :
- `MenuState` : Menu principal
- `PlayingState` : Jeu en cours
- `PausedState` : Pause
- `GameOverState` : Fin de partie

**Exemple de transition** :
```java
public void enter(GameContext context) {
    logger.logStateChange("Game", context.getCurrentStateName(), getStateName());
    logger.logGameEvent("Game started");
    context.initializeGame();
}
```

**Avantages** :
- ✅ Ajout facile de nouveaux états
- ✅ Logique séparée par état
- ✅ Transitions traçables

---

### 2. **Decorator Pattern** ⭐ (Obligatoire)
**Localisation** : `com.designpatterns.puzzle.patterns.decorator`

Ajout dynamique de capacités aux pièces via des décorateurs empilables.

**Décorateurs implémentés** :
- `GoldenPieceDecorator` : ×2 score + effet doré
- `BombPieceDecorator` : Effet bombe
- `RainbowPieceDecorator` : ×3 score + animation arc-en-ciel

**Exemple d'utilisation** :
```java
PuzzlePiece basicPiece = new BasicPuzzlePiece(Color.CYAN, shape, "I");
PuzzlePiece goldPiece = new GoldenPieceDecorator(basicPiece);
PuzzlePiece superPiece = new RainbowPieceDecorator(goldPiece);
// Multiplicateur final : 2 × 3 = ×6 !
```

**Avantages** :
- ✅ Power-ups empilables
- ✅ Extension sans modification
- ✅ Chaque décoration est loggée

---

### 3. **Composite Pattern** ⭐ (Obligatoire)
**Localisation** : `com.designpatterns.puzzle.patterns.composite`

Structure hiérarchique pour gérer des groupes de composants de jeu.

**Classes** :
- `GameComponent` : Interface de base
- `GameLeaf` : Composant simple (feuille)
- `GameComposite` : Composant composite (contient d'autres composants)

**Utilité** :
- Organisation hiérarchique des éléments de jeu
- Gestion uniforme des objets simples et composés
- Facilite l'extension du jeu (niveaux, scènes complexes)

---

### 4. **Factory Pattern** ⭐ (Additionnel)
**Localisation** : `com.designpatterns.puzzle.patterns.factory`

Création centralisée et traçée des pièces et power-ups.

**Factories implémentées** :
- `PieceFactory` : Crée les 7 types de pièces
- `PowerUpFactory` : Applique les décorateurs power-up

**Exemple** :
```java
// Création d'une pièce aléatoire
PuzzlePiece piece = PieceFactory.createRandomPiece();

// Application d'un power-up (15% de chance)
piece = PowerUpFactory.applyRandomPowerUp(piece);

// Log automatique : [FACTORY] Created PuzzlePiece: T
```

**Avantages** :
- ✅ Création centralisée
- ✅ Ajout facile de nouveaux types
- ✅ Logging automatique de chaque création

---

### 5. **Singleton Pattern** ⭐ (Additionnel)
**Localisation** : `com.designpatterns.puzzle.utils` & `com.designpatterns.puzzle.core`

Garantit une seule instance des gestionnaires critiques.

**Singletons implémentés** :
- `GameLogger` : Système de logging centralisé
- `GameManager` : Gestionnaire principal du jeu

**Exemple** :
```java
// Thread-safe avec synchronisation
public static synchronized GameManager getInstance() {
    if (instance == null) {
        instance = new GameManager();
        logger.logInfo("GameManager instance created");
    }
    return instance;
}
```

**Avantages** :
- ✅ Instance unique garantie
- ✅ Accès global contrôlé
- ✅ Thread-safe

---

## 📊 Système de Logging (Log4j2)

### Configuration
Fichier : `src/main/resources/log4j2.xml`
- Sortie : Console + Fichier `game.log`
- Format : `[TIMESTAMP] [LEVEL] MESSAGE`

### Événements Tracés

#### 1. **Changements d'États** (State Pattern)
```
[2025-11-28 14:23:47] [INFO] [STATE] Game: MENU -> PLAYING
[2025-11-28 14:23:50] [INFO] [STATE] Game: PLAYING -> PAUSED
[2025-11-28 14:24:10] [INFO] [STATE] Game: PAUSED -> PLAYING
[2025-11-28 14:25:05] [INFO] [STATE] Game: PLAYING -> GAME_OVER
```

#### 2. **Décorateurs** (Decorator Pattern)
```
[2025-11-28 14:23:52] [INFO] [DECORATOR] GoldenPiece applied to Basic I piece
[2025-11-28 14:24:05] [INFO] [DECORATOR] RainbowPiece applied to Basic T piece + Golden Boost
```

#### 3. **Création d'Objets** (Factory Pattern)
```
[2025-11-28 14:23:48] [INFO] [FACTORY] Created PuzzlePiece: T
[2025-11-28 14:23:49] [INFO] [FACTORY] Created PowerUp: GOLDEN
```

#### 4. **Événements de Jeu**
```
[2025-11-28 14:23:45] [INFO] Game started
[2025-11-28 14:23:55] [INFO] [EVENT] Piece rotated (rotation: 1)
[2025-11-28 14:24:02] [INFO] [EVENT] Piece placed at (4, 18)
[2025-11-28 14:24:03] [INFO] [EVENT] Line cleared: 19
[2025-11-28 14:24:03] [INFO] [EVENT] Total lines cleared: 1
[2025-11-28 14:25:05] [INFO] [EVENT] Game Over - Cannot spawn new piece
[2025-11-28 14:25:05] [INFO] [SCORE] Final score: 2450 | Level: 3 | Lines cleared: 15
```

---

## 🏛️ Architecture du Projet

```
puzzle/
├── src/
│   ├── main/
│   │   ├── java/com/designpatterns/puzzle/
│   │   │   ├── PuzzleGameApplication.java    # Application principale
│   │   │   ├── core/
│   │   │   │   ├── GameContext.java          # Contexte State Pattern
│   │   │   │   └── GameManager.java          # Singleton Manager
│   │   │   ├── model/
│   │   │   │   ├── GameGrid.java             # Grille de jeu
│   │   │   │   └── ActivePiece.java          # Pièce active
│   │   │   ├── patterns/
│   │   │   │   ├── state/                    # State Pattern
│   │   │   │   │   ├── GameState.java
│   │   │   │   │   ├── MenuState.java
│   │   │   │   │   ├── PlayingState.java
│   │   │   │   │   ├── PausedState.java
│   │   │   │   │   └── GameOverState.java
│   │   │   │   ├── decorator/                # Decorator Pattern
│   │   │   │   │   ├── PuzzlePiece.java
│   │   │   │   │   ├── BasicPuzzlePiece.java
│   │   │   │   │   ├── PuzzlePieceDecorator.java
│   │   │   │   │   ├── GoldenPieceDecorator.java
│   │   │   │   │   ├── BombPieceDecorator.java
│   │   │   │   │   └── RainbowPieceDecorator.java
│   │   │   │   ├── composite/                # Composite Pattern
│   │   │   │   │   ├── GameComponent.java
│   │   │   │   │   ├── GameLeaf.java
│   │   │   │   │   └── GameComposite.java
│   │   │   │   └── factory/                  # Factory Pattern
│   │   │   │       ├── PieceFactory.java
│   │   │   │       └── PowerUpFactory.java
│   │   │   ├── utils/
│   │   │   │   └── GameLogger.java           # Singleton Logger
│   │   │   └── view/
│   │   │       └── GameView.java             # Interface JavaFX
│   │   └── resources/
│   │       └── log4j2.xml                    # Config logging
│   └── test/
│       └── java/                             # Tests unitaires
├── docs/
│   ├── class-diagram.puml                    # Diagramme UML source
│   └── class-diagram.png                     # Diagramme UML exporté
├── pom.xml                                   # Configuration Maven
├── .gitignore                                # Git ignore
├── README.md                                 # Ce fichier
└── game.log                                  # Fichier de log généré
```

---

## 🚀 Installation et Exécution

### Prérequis
- **JDK 17** ou supérieur
- **Maven 3.6+**
- **JavaFX 21** (inclus dans les dépendances Maven)

### Étapes d'Installation

#### 1. Cloner le dépôt
```powershell
git clone <URL_DU_DEPOT>
cd puzzle
```

#### 2. Compiler le projet
```powershell
mvn clean compile
```

#### 3. Exécuter le jeu
```powershell
mvn javafx:run
```

#### 4. Créer un JAR exécutable
```powershell
mvn clean package
```
Le JAR sera généré dans `target/puzzle-game-1.0.0.jar`

### Exécution Alternative
Si vous avez des problèmes avec JavaFX, utilisez :
```powershell
mvn clean javafx:run
```

---

## 📦 Technologies Utilisées

| Technologie | Version | Utilisation |
|-------------|---------|-------------|
| **Java** | 17 | Langage principal |
| **JavaFX** | 21.0.1 | Interface graphique |
| **Log4j2** | 2.22.0 | Système de logging |
| **Maven** | 3.6+ | Gestion de build |
| **PlantUML** | - | Diagrammes UML |

---

## 📈 Diagramme de Classes UML

Le diagramme complet est disponible dans :
- **Source** : `docs/class-diagram.puml`
- **Export PNG** : `docs/class-diagram.png`

### Génération du Diagramme
Avec PlantUML installé :
```powershell
java -jar plantuml.jar docs/class-diagram.puml
```

---

## 🧪 Tests

### Exécuter les tests unitaires
```powershell
mvn test
```

### Vérifier le build complet
```powershell
mvn clean verify
```

---

## 📝 Exemple de Session de Jeu (Log)

```log
[2025-11-28 14:23:45] [INFO] === Puzzle Game Started ===
[2025-11-28 14:23:45] [INFO] Application: Puzzle Game - Design Patterns Project
[2025-11-28 14:23:45] [INFO] Logger initialized
[2025-11-28 14:23:45] [INFO] GameManager instance created
[2025-11-28 14:23:45] [INFO] GameManager initialized
[2025-11-28 14:23:45] [INFO] [STATE] Game: NONE -> MENU
[2025-11-28 14:23:45] [INFO] Entering menu state
[2025-11-28 14:23:45] [INFO] Game loop started
[2025-11-28 14:23:47] [INFO] [STATE] Game: MENU -> PLAYING
[2025-11-28 14:23:47] [INFO] Exiting menu state
[2025-11-28 14:23:47] [INFO] [EVENT] Game started
[2025-11-28 14:23:47] [INFO] New game initialized
[2025-11-28 14:23:47] [INFO] [FACTORY] Created PuzzlePiece: T
[2025-11-28 14:23:48] [INFO] [FACTORY] Created PuzzlePiece: L
[2025-11-28 14:23:48] [INFO] [FACTORY] Created PowerUp: GOLDEN
[2025-11-28 14:23:48] [INFO] [DECORATOR] GoldenPiece applied to Basic L piece
[2025-11-28 14:23:52] [INFO] [EVENT] Piece rotated (rotation: 1)
[2025-11-28 14:23:55] [INFO] [EVENT] Piece placed at (4, 18)
[2025-11-28 14:23:55] [INFO] [FACTORY] Created PuzzlePiece: I
[2025-11-28 14:24:02] [INFO] [EVENT] Piece dropped (distance: 15)
[2025-11-28 14:24:02] [INFO] [EVENT] Piece placed at (2, 19)
[2025-11-28 14:24:02] [INFO] [EVENT] Line cleared: 19
[2025-11-28 14:24:02] [INFO] [EVENT] Total lines cleared: 1
[2025-11-28 14:24:02] [INFO] [EVENT] Lines cleared: 1 | Score added: 200
[2025-11-28 14:24:10] [INFO] [STATE] Game: PLAYING -> PAUSED
[2025-11-28 14:24:10] [INFO] [EVENT] Game paused
[2025-11-28 14:24:15] [INFO] [STATE] Game: PAUSED -> PLAYING
[2025-11-28 14:25:05] [INFO] [EVENT] Game Over - Cannot spawn new piece
[2025-11-28 14:25:05] [INFO] [STATE] Game: PLAYING -> GAME_OVER
[2025-11-28 14:25:05] [INFO] [EVENT] Game Over
[2025-11-28 14:25:05] [INFO] [SCORE] Final score: 2450 | Level: 3 | Lines cleared: 15
```

---

## 🎓 Points Forts du Projet

### Architecture
- ✅ **5 Design Patterns** implémentés professionnellement
- ✅ **Séparation des préoccupations** (MVC-like)
- ✅ **Extensibilité** : Ajout facile de nouveaux patterns, états, power-ups
- ✅ **Maintenabilité** : Code bien structuré et commenté

### Qualité du Code
- ✅ **Commentaires JavaDoc** sur toutes les classes publiques
- ✅ **Conventions de nommage** respectées
- ✅ **Gestion d'erreurs** appropriée
- ✅ **Logging complet** de tous les événements importants

### Interface Utilisateur
- ✅ **Interface JavaFX** moderne et réactive
- ✅ **HUD** avec score, niveau, lignes
- ✅ **Preview** de la pièce suivante
- ✅ **Effets visuels** pour les power-ups
- ✅ **Animations** fluides

### Documentation
- ✅ **README complet** avec exemples
- ✅ **Diagramme UML** détaillé avec tous les patterns
- ✅ **Instructions d'installation** claires
- ✅ **Fichier de log** d'exemple

---

## 🤝 Contribution / Équipe

Ce projet a été développé dans le cadre du module Design Patterns.

**Note** : Pour un projet en équipe, ajoutez ici :
- Noms des membres
- Répartition des tâches
- Contributions de chacun

---

## 📞 Contact

**Enseignant** : Haythem Ghazouani  
**Cours** : Design Patterns  
**Année** : 2025-2026

---

## 📄 Licence

Ce projet est à usage éducatif uniquement dans le cadre du module Design Patterns.

---

## ✅ Checklist de Validation

- [x] Jeu fonctionnel et démarre sans erreur
- [x] 5 design patterns correctement implémentés
- [x] Système de logging fonctionnel et trace tous les événements requis
- [x] Diagramme UML complet et à jour
- [x] Code commenté et bien structuré
- [x] Interface graphique fonctionnelle et attractive
- [x] README.md complet avec instructions
- [x] Gestion Git avec commits réguliers
- [x] Architecture maintenable et extensible

---

## 🎯 Démonstration des Patterns

### State Pattern en Action
```java
// Transition automatique avec logging
context.setState(new PlayingState());
// Log: [STATE] Game: MENU -> PLAYING
```

### Decorator Pattern en Action
```java
PuzzlePiece piece = PieceFactory.createRandomPiece();
piece = PowerUpFactory.applyRandomPowerUp(piece);
// Si power-up obtenu:
// Log: [DECORATOR] GoldenPiece applied to Basic T piece
// Multiplicateur de score: x2
```

### Factory Pattern en Action
```java
PuzzlePiece piece = PieceFactory.createPiece(PieceType.T);
// Log: [FACTORY] Created PuzzlePiece: T
```

### Singleton Pattern en Action
```java
GameManager manager = GameManager.getInstance();
GameLogger logger = GameLogger.getInstance();
// Une seule instance garantie pour toute l'application
```

### Composite Pattern en Action
```java
GameComposite scene = new GameComposite("MainScene");
scene.add(gridComponent);
scene.add(hudComponent);
scene.update(deltaTime); // Met à jour tous les enfants
```

---

**Version** : 1.0.0  
**Date** : Novembre 2025  
**Status** : ✅ Production Ready
