package com.designpatterns.puzzle.core;

import com.designpatterns.puzzle.patterns.state.MenuState;
import com.designpatterns.puzzle.utils.GameLogger;


public class GameManager {
    
    private static GameManager instance;
    private static final GameLogger logger = GameLogger.getInstance();
    
    private GameContext gameContext;
    private boolean initialized;
    
    
    private GameManager() {
        this.initialized = false;
    }
    
    
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
    
    
    public void startNewGame() {
        if (gameContext != null) {
            gameContext.handleInput("START");
            logger.logInfo("New game started via GameManager");
        }
    }
    
    
    public void pauseGame() {
        if (gameContext != null) {
            gameContext.handleInput("PAUSE");
        }
    }
    
    
    public void resumeGame() {
        if (gameContext != null) {
            gameContext.handleInput("RESUME");
        }
    }
    
    
    public void returnToMenu() {
        if (gameContext != null) {
            gameContext.handleInput("MENU");
        }
    }
    
   
    public void restartGame() {
        if (gameContext != null) {
            gameContext.handleInput("RESTART");
        }
    }
    
    
    public void update(double deltaTime) {
        if (gameContext != null) {
            gameContext.update(deltaTime);
        }
    }
    
    
    public void handleInput(String input) {
        if (gameContext != null) {
            gameContext.handleInput(input);
        }
    }
    
    
    public void quitGame() {
        logger.logInfo("Game quit requested");
        System.exit(0);
    }
    
    public GameContext getGameContext() {
        return gameContext;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
}
