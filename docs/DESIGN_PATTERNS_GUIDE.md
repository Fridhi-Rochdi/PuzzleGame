# Guide Complet des Design Patterns Implémentés

## Table des Matières
1. [State Pattern](#1-state-pattern)
2. [Decorator Pattern](#2-decorator-pattern)
3. [Composite Pattern](#3-composite-pattern)
4. [Factory Pattern](#4-factory-pattern)
5. [Singleton Pattern](#5-singleton-pattern)

---

## 1. State Pattern

### 📖 Définition
Le pattern State permet à un objet de modifier son comportement lorsque son état interne change. L'objet semblera avoir changé de classe.

### 🎯 Problème Résolu
Sans State Pattern, le code du jeu aurait de nombreux `if/else` ou `switch` pour gérer les différents états (menu, jeu, pause, game over), rendant le code difficile à maintenir et étendre.

### ✅ Implémentation dans le Projet

#### Structure
```
GameState (interface)
├── MenuState
├── PlayingState
├── PausedState
└── GameOverState

GameContext (utilise GameState)
```

#### Exemple de Code

**Interface GameState**
```java
public interface GameState {
    void enter(GameContext context);
    void update(GameContext context, double deltaTime);
    void handleInput(GameContext context, String input);
    void exit(GameContext context);
    String getStateName();
}
```

**État Concret - PlayingState**
```java
public class PlayingState implements GameState {
    @Override
    public void enter(GameContext context) {
        logger.logStateChange("Game", context.getCurrentStateName(), getStateName());
        logger.logGameEvent("Game started");
        context.initializeGame();
    }
    
    @Override
    public void handleInput(GameContext context, String input) {
        switch (input) {
            case "PAUSE":
                context.setState(new PausedState());
                break;
            case "LEFT":
                context.movePieceLeft();
                break;
            // ...
        }
    }
}
```

**Contexte - GameContext**
```java
public class GameContext {
    private GameState currentState;
    
    public void setState(GameState newState) {
        if (currentState != null) {
            currentState.exit(this);
        }
        currentState = newState;
        currentState.enter(this);
    }
    
    public void handleInput(String input) {
        currentState.handleInput(this, input);
    }
}
```

### 📊 Diagramme de Transitions d'États
```
MENU ──(START)──> PLAYING ──(PAUSE)──> PAUSED
                     │                    │
                     │                    │
                (GAME_OVER)           (RESUME)
                     │                    │
                     ▼                    │
                 GAME_OVER ◄──────────────┘
                     │
                 (RESTART)
                     │
                     └────────────> PLAYING
```

### 🎯 Avantages Démontrés
- ✅ **Extensibilité** : Ajout de nouveaux états sans modifier le code existant
- ✅ **Maintenabilité** : Chaque état est encapsulé dans sa propre classe
- ✅ **Traçabilité** : Toutes les transitions sont loggées
- ✅ **Clarté** : Comportement spécifique à chaque état bien séparé

### 📝 Logging Exemple
```log
[2025-11-28 14:23:45] [INFO] [STATE] Game: NONE -> MENU
[2025-11-28 14:23:47] [INFO] [STATE] Game: MENU -> PLAYING
[2025-11-28 14:24:10] [INFO] [STATE] Game: PLAYING -> PAUSED
[2025-11-28 14:24:15] [INFO] [STATE] Game: PAUSED -> PLAYING
[2025-11-28 14:25:05] [INFO] [STATE] Game: PLAYING -> GAME_OVER
```

---

## 2. Decorator Pattern

### 📖 Définition
Le pattern Decorator permet d'ajouter dynamiquement des responsabilités à un objet. Les décorateurs offrent une alternative flexible à l'héritage pour étendre les fonctionnalités.

### 🎯 Problème Résolu
Sans Decorator, créer toutes les combinaisons possibles de pièces avec power-ups nécessiterait une explosion de sous-classes :
- `GoldenIPiece`, `GoldenTPiece`, `RainbowIPiece`, etc.
- Pire : `GoldenRainbowIPiece` pour les combinaisons !

### ✅ Implémentation dans le Projet

#### Structure
```
PuzzlePiece (interface)
├── BasicPuzzlePiece (composant concret)
└── PuzzlePieceDecorator (décorateur abstrait)
    ├── GoldenPieceDecorator (×2 score)
    ├── BombPieceDecorator (effet bombe)
    └── RainbowPieceDecorator (×3 score + animation)
```

#### Exemple de Code

**Interface Component**
```java
public interface PuzzlePiece {
    void render(GraphicsContext gc, double x, double y, double blockSize);
    Color getColor();
    int[][] getShape();
    int getScoreMultiplier();
    boolean hasSpecialEffect();
    String getDescription();
}
```

**Composant Concret**
```java
public class BasicPuzzlePiece implements PuzzlePiece {
    protected Color color;
    protected int[][] shape;
    protected String type;
    
    @Override
    public int getScoreMultiplier() {
        return 1; // Score de base
    }
    
    @Override
    public boolean hasSpecialEffect() {
        return false;
    }
    
    @Override
    public String getDescription() {
        return "Basic " + type + " piece";
    }
}
```

**Décorateur Abstrait**
```java
public abstract class PuzzlePieceDecorator implements PuzzlePiece {
    protected PuzzlePiece decoratedPiece;
    
    public PuzzlePieceDecorator(PuzzlePiece piece) {
        this.decoratedPiece = piece;
    }
    
    @Override
    public int getScoreMultiplier() {
        return decoratedPiece.getScoreMultiplier();
    }
}
```

**Décorateur Concret - GoldenPieceDecorator**
```java
public class GoldenPieceDecorator extends PuzzlePieceDecorator {
    public GoldenPieceDecorator(PuzzlePiece piece) {
        super(piece);
        logger.logDecoratorApplied("GoldenPiece", piece.getDescription());
    }
    
    @Override
    public int getScoreMultiplier() {
        return decoratedPiece.getScoreMultiplier() * 2; // Double le score !
    }
    
    @Override
    public void render(GraphicsContext gc, double x, double y, double blockSize) {
        decoratedPiece.render(gc, x, y, blockSize);
        // Ajoute effet doré par-dessus
        gc.setStroke(Color.GOLD);
        gc.setLineWidth(2);
        // ... dessine bordure dorée
    }
    
    @Override
    public String getDescription() {
        return decoratedPiece.getDescription() + " + Golden Boost (x2 score)";
    }
}
```

### 🎨 Empilement de Décorateurs

**Exemple Puissant**
```java
// Crée une pièce de base
PuzzlePiece piece = new BasicPuzzlePiece(Color.CYAN, shape, "I");
// Description: "Basic I piece"
// Multiplicateur: x1

// Ajoute l'effet Golden
piece = new GoldenPieceDecorator(piece);
// Description: "Basic I piece + Golden Boost (x2 score)"
// Multiplicateur: x2

// Ajoute l'effet Rainbow par-dessus !
piece = new RainbowPieceDecorator(piece);
// Description: "Basic I piece + Golden Boost (x2 score) + Rainbow Power (x3 score)"
// Multiplicateur: x6 (2 × 3) !

// Le joueur marque 600 points au lieu de 100 !
```

### 📊 Diagramme d'Empilement
```
┌─────────────────────────────┐
│  RainbowPieceDecorator      │ ×3
│  ┌───────────────────────┐  │
│  │ GoldenPieceDecorator  │  │ ×2
│  │  ┌─────────────────┐  │  │
│  │  │ BasicPuzzlePiece│  │  │ ×1
│  │  │   (I piece)     │  │  │
│  │  └─────────────────┘  │  │
│  └───────────────────────┘  │
└─────────────────────────────┘
Multiplicateur total : 1 × 2 × 3 = ×6
```

### 🎯 Avantages Démontrés
- ✅ **Flexibilité** : Combinaisons infinies de power-ups
- ✅ **Open/Closed Principle** : Ajout de nouveaux décorateurs sans modifier les existants
- ✅ **Composition** : Empilement dynamique à l'exécution
- ✅ **Traçabilité** : Chaque application de décorateur est loggée

### 📝 Logging Exemple
```log
[2025-11-28 14:23:52] [INFO] [DECORATOR] GoldenPiece applied to Basic I piece
[2025-11-28 14:24:05] [INFO] [DECORATOR] RainbowPiece applied to Basic T piece + Golden Boost (x2 score)
[2025-11-28 14:24:30] [INFO] [DECORATOR] BombPiece applied to Basic L piece
```

---

## 3. Composite Pattern

### 📖 Définition
Le pattern Composite permet de composer des objets en structures arborescentes pour représenter des hiérarchies partie-tout. Il permet aux clients de traiter uniformément les objets individuels et les compositions d'objets.

### 🎯 Problème Résolu
Gérer uniformément des éléments simples et des groupes d'éléments sans code conditionnel complexe.

### ✅ Implémentation dans le Projet

#### Structure
```
GameComponent (interface)
├── GameLeaf (composant simple)
└── GameComposite (composant composite)
    └── children: List<GameComponent>
```

#### Exemple de Code

**Interface Component**
```java
public interface GameComponent {
    void update(double deltaTime);
    void render(GraphicsContext gc);
    void add(GameComponent component);
    void remove(GameComponent component);
    GameComponent getChild(int index);
    int getChildCount();
}
```

**Leaf (Feuille)**
```java
public abstract class GameLeaf implements GameComponent {
    @Override
    public void add(GameComponent component) {
        throw new UnsupportedOperationException("Cannot add to a leaf");
    }
    
    @Override
    public void remove(GameComponent component) {
        throw new UnsupportedOperationException("Cannot remove from a leaf");
    }
    
    @Override
    public int getChildCount() {
        return 0;
    }
    
    // update() et render() sont implémentés par les sous-classes
}
```

**Composite**
```java
public class GameComposite implements GameComponent {
    private List<GameComponent> children = new ArrayList<>();
    private String name;
    
    @Override
    public void update(double deltaTime) {
        // Propage l'update à tous les enfants
        for (GameComponent child : children) {
            child.update(deltaTime);
        }
    }
    
    @Override
    public void render(GraphicsContext gc) {
        // Propage le render à tous les enfants
        for (GameComponent child : children) {
            child.render(gc);
        }
    }
    
    @Override
    public void add(GameComponent component) {
        children.add(component);
    }
    
    @Override
    public void remove(GameComponent component) {
        children.remove(component);
    }
}
```

### 🌳 Exemple d'Utilisation

```java
// Crée la scène principale (composite)
GameComposite mainScene = new GameComposite("MainScene");

// Crée les composants de jeu
GameComposite gridGroup = new GameComposite("GridGroup");
GameLeaf background = new BackgroundLeaf();
GameLeaf grid = new GridLeaf();
GameLeaf currentPiece = new PieceLeaf();

// Structure hiérarchique
gridGroup.add(background);
gridGroup.add(grid);
gridGroup.add(currentPiece);

mainScene.add(gridGroup);
mainScene.add(new HUDLeaf());

// Un seul appel met à jour TOUT
mainScene.update(deltaTime);
mainScene.render(gc);
```

### 📊 Hiérarchie de Composition
```
MainScene (Composite)
├── GridGroup (Composite)
│   ├── Background (Leaf)
│   ├── Grid (Leaf)
│   └── CurrentPiece (Leaf)
├── HUD (Leaf)
└── NextPiecePreview (Leaf)
```

### 🎯 Avantages Démontrés
- ✅ **Uniformité** : Traitement identique des objets simples et composés
- ✅ **Hiérarchie** : Organisation naturelle en arbre
- ✅ **Simplicité** : Un appel propage l'opération à toute la structure
- ✅ **Extensibilité** : Ajout facile de nouveaux composants

---

## 4. Factory Pattern

### 📖 Définition
Le pattern Factory définit une interface pour créer un objet, mais laisse les sous-classes décider quelle classe instancier. Il permet de reporter l'instanciation aux sous-classes.

### 🎯 Problème Résolu
Centraliser et simplifier la création d'objets complexes, tout en traçant leur création.

### ✅ Implémentation dans le Projet

#### Structure
```
PieceFactory (Factory statique)
└── createPiece(type) : PuzzlePiece
    └── createIPiece(), createTPiece(), etc.

PowerUpFactory (Factory statique)
└── applyPowerUp(piece, type) : PuzzlePiece
```

#### Exemple de Code

**Factory pour les Pièces**
```java
public class PieceFactory {
    private static final GameLogger logger = GameLogger.getInstance();
    
    public enum PieceType {
        I, O, T, S, Z, J, L
    }
    
    public static PuzzlePiece createPiece(PieceType type) {
        PuzzlePiece piece = null;
        
        switch (type) {
            case I:
                piece = createIPiece();
                break;
            case T:
                piece = createTPiece();
                break;
            // ... autres types
        }
        
        logger.logObjectCreation("PuzzlePiece", type.toString());
        return piece;
    }
    
    public static PuzzlePiece createRandomPiece() {
        PieceType[] types = PieceType.values();
        PieceType randomType = types[random.nextInt(types.length)];
        return createPiece(randomType);
    }
    
    private static PuzzlePiece createIPiece() {
        int[][] shape = {{1, 1, 1, 1}};
        return new BasicPuzzlePiece(Color.CYAN, shape, "I");
    }
}
```

**Factory pour les Power-Ups**
```java
public class PowerUpFactory {
    public enum PowerUpType {
        GOLDEN, BOMB, RAINBOW
    }
    
    public static PuzzlePiece applyRandomPowerUp(PuzzlePiece piece) {
        // 15% de chance d'avoir un power-up
        if (random.nextDouble() < 0.15) {
            PowerUpType[] types = PowerUpType.values();
            PowerUpType randomType = types[random.nextInt(types.length)];
            return applyPowerUp(piece, randomType);
        }
        return piece;
    }
    
    public static PuzzlePiece applyPowerUp(PuzzlePiece piece, PowerUpType type) {
        logger.logObjectCreation("PowerUp", type.toString());
        
        switch (type) {
            case GOLDEN:
                return new GoldenPieceDecorator(piece);
            case BOMB:
                return new BombPieceDecorator(piece);
            case RAINBOW:
                return new RainbowPieceDecorator(piece);
            default:
                return piece;
        }
    }
    
    // Empilement de plusieurs power-ups
    public static PuzzlePiece applyMultiplePowerUps(PuzzlePiece piece, PowerUpType... types) {
        PuzzlePiece decorated = piece;
        for (PowerUpType type : types) {
            decorated = applyPowerUp(decorated, type);
        }
        return decorated;
    }
}
```

### 🔧 Utilisation

```java
// Création simple
PuzzlePiece piece = PieceFactory.createPiece(PieceType.T);

// Création aléatoire
PuzzlePiece randomPiece = PieceFactory.createRandomPiece();

// Application de power-up aléatoire
randomPiece = PowerUpFactory.applyRandomPowerUp(randomPiece);

// Application de power-ups multiples
PuzzlePiece superPiece = PowerUpFactory.applyMultiplePowerUps(
    piece,
    PowerUpType.GOLDEN,
    PowerUpType.RAINBOW
);
```

### 🎯 Avantages Démontrés
- ✅ **Centralisation** : Un seul endroit pour créer les objets
- ✅ **Traçabilité** : Logging automatique de chaque création
- ✅ **Simplification** : Le client n'a pas besoin de connaître les détails de construction
- ✅ **Extensibilité** : Ajout facile de nouveaux types

### 📝 Logging Exemple
```log
[2025-11-28 14:23:48] [INFO] [FACTORY] Created PuzzlePiece: T
[2025-11-28 14:23:49] [INFO] [FACTORY] Created PuzzlePiece: I
[2025-11-28 14:23:50] [INFO] [FACTORY] Created PowerUp: GOLDEN
[2025-11-28 14:23:51] [INFO] [FACTORY] Created PowerUp: RAINBOW
```

---

## 5. Singleton Pattern

### 📖 Définition
Le pattern Singleton garantit qu'une classe n'a qu'une seule instance et fournit un point d'accès global à cette instance.

### 🎯 Problème Résolu
Garantir qu'il n'existe qu'une seule instance critique dans toute l'application (logger, gestionnaire de jeu).

### ✅ Implémentation dans le Projet

#### Structure
```
GameLogger (Singleton)
└── instance: GameLogger (static, privée)

GameManager (Singleton)
└── instance: GameManager (static, privée)
```

#### Exemple de Code

**GameLogger Singleton**
```java
public class GameLogger {
    private static GameLogger instance;
    private static final Logger logger = LogManager.getLogger(GameLogger.class);
    
    // Constructeur privé empêche l'instanciation directe
    private GameLogger() {
        logInfo("Logger initialized");
    }
    
    // Thread-safe avec synchronisation
    public static synchronized GameLogger getInstance() {
        if (instance == null) {
            instance = new GameLogger();
        }
        return instance;
    }
    
    // Méthodes de logging
    public void logInfo(String message) {
        logger.info(message);
    }
    
    public void logStateChange(String context, String from, String to) {
        String message = String.format("[STATE] %s: %s -> %s", context, from, to);
        logger.info(message);
    }
    
    public void logDecoratorApplied(String decoratorType, String target) {
        String message = String.format("[DECORATOR] %s applied to %s", decoratorType, target);
        logger.info(message);
    }
    
    // ... autres méthodes
}
```

**GameManager Singleton**
```java
public class GameManager {
    private static GameManager instance;
    private static final GameLogger logger = GameLogger.getInstance();
    
    private GameContext gameContext;
    private boolean initialized;
    
    // Constructeur privé
    private GameManager() {
        this.initialized = false;
    }
    
    // Thread-safe avec synchronisation
    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
            logger.logInfo("GameManager instance created");
        }
        return instance;
    }
    
    public void initialize() {
        if (!initialized) {
            gameContext = new GameContext(new MenuState());
            initialized = true;
            logger.logInfo("GameManager initialized");
        }
    }
    
    // Méthodes de gestion du jeu
    public void startNewGame() {
        if (gameContext != null) {
            gameContext.handleInput("START");
        }
    }
    
    // ... autres méthodes
}
```

### 🔧 Utilisation

```java
// Première utilisation - crée l'instance
GameLogger logger1 = GameLogger.getInstance();

// Deuxième utilisation - retourne la MÊME instance
GameLogger logger2 = GameLogger.getInstance();

// logger1 === logger2 (même référence)
assert logger1 == logger2; // true !

// Utilisation depuis n'importe où dans le code
GameLogger.getInstance().logInfo("Message depuis n'importe quelle classe");
GameManager.getInstance().startNewGame();
```

### 🧵 Thread-Safety

**Version Thread-Safe (Utilisée)**
```java
public static synchronized GameLogger getInstance() {
    if (instance == null) {
        instance = new GameLogger();
    }
    return instance;
}
```

**Alternative : Double-Checked Locking (Plus performant)**
```java
public static GameLogger getInstance() {
    if (instance == null) {
        synchronized (GameLogger.class) {
            if (instance == null) {
                instance = new GameLogger();
            }
        }
    }
    return instance;
}
```

### 🎯 Avantages Démontrés
- ✅ **Instance unique** garantie dans toute l'application
- ✅ **Accès global** contrôlé
- ✅ **Initialisation paresseuse** (lazy initialization)
- ✅ **Thread-safe** avec synchronisation
- ✅ **Économie de ressources** (une seule instance)

### ⚠️ Considérations
- ⚠️ Peut compliquer les tests unitaires
- ⚠️ Crée un couplage global
- ✅ Justifié ici car : Logger et GameManager sont vraiment uniques et globaux

---

## 🎓 Conclusion

### Interactions entre les Patterns

```
Application
    │
    ├─► Singleton: GameManager
    │       │
    │       └─► Singleton: GameLogger
    │
    ├─► State: GameContext
    │       └─► GameState (Menu, Playing, Paused, GameOver)
    │
    ├─► Factory: PieceFactory + PowerUpFactory
    │       └─► Decorator: PuzzlePiece + Decorators
    │
    └─► Composite: GameComponent
            └─► GameComposite / GameLeaf
```

### Patterns et Logging
Tous les patterns sont tracés dans le fichier de log :
- **State** : Transitions d'états
- **Decorator** : Application/retrait de décorateurs
- **Factory** : Création d'objets
- **Singleton** : Initialisation des instances

### Qualité Professionnelle
✅ Tous les patterns sont :
- Correctement implémentés selon les définitions classiques
- Documentés avec JavaDoc
- Tracés dans les logs
- Illustrés dans le diagramme UML
- Démontrés dans le code fonctionnel

---

**Projet** : Puzzle Game - Design Patterns  
**Enseignant** : Haythem Ghazouani  
**Année** : 2025-2026
