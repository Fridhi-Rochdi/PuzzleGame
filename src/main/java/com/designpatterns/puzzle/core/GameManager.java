package com.designpatterns.puzzle.core;

import com.designpatterns.puzzle.patterns.state.MenuState;
import com.designpatterns.puzzle.utils.GameLogger;

/**
 * Singleton Pattern - GameManager
 * Gestionnaire principal du jeu, garantit une seule instance
 */
public class GameManager {
    
    private static GameManager instance;
    private static final GameLogger logger = GameLogger.getInstance();
    
    private GameContext gameContext;
    private boolean initialized;
    
    /**
     * Constructeur privé (Singleton)
     */
    private GameManager() {
        this.initialized = false;
    }
    
    /**
     * Obtient l'instance unique du GameManager (Singleton)
     * Thread-safe avec synchronisation
     */
    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
            logger.logInfo("GameManager instance created");
        }
        return instance;
    }
    
    /**
     * Initialise le gestionnaire de jeu
     */
    public void initialize() {
        if (!initialized) {
            gameContext = new GameContext(new MenuState());
            initialized = true;
            logger.logInfo("GameManager initialized");
        }
    }
    
    /**
     * Démarre une nouvelle partie
     */
    public void startNewGame() {
        if (gameContext != null) {
            gameContext.handleInput("START");
            logger.logInfo("New game started via GameManager");
        }
    }
    
    /**
     * Met en pause le jeu
     */
    public void pauseGame() {
        if (gameContext != null) {
            gameContext.handleInput("PAUSE");
        }
    }
    
    /**
     * Reprend le jeu
     */
    public void resumeGame() {
        if (gameContext != null) {
            gameContext.handleInput("RESUME");
        }
    }
    
    /**
     * Retourne au menu
     */
    public void returnToMenu() {
        if (gameContext != null) {
            gameContext.handleInput("MENU");
        }
    }
    
    /**
     * Redémarre la partie
     */
    public void restartGame() {
        if (gameContext != null) {
            gameContext.handleInput("RESTART");
        }
    }
    
    /**
     * Met à jour le jeu
     */
    public void update(double deltaTime) {
        if (gameContext != null) {
            gameContext.update(deltaTime);
        }
    }
    
    /**
     * Gère les entrées
     */
    public void handleInput(String input) {
        if (gameContext != null) {
            gameContext.handleInput(input);
        }
    }
    
    /**
     * Quitte le jeu
     */
    public void quitGame() {
        logger.logInfo("Game quit requested");
        System.exit(0);
    }
    
    // Getters
    public GameContext getGameContext() {
        return gameContext;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
}
