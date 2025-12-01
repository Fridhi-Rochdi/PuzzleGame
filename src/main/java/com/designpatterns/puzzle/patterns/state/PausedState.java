package com.designpatterns.puzzle.patterns.state;

import com.designpatterns.puzzle.core.GameContext;
import com.designpatterns.puzzle.utils.GameLogger;

/**
 * State Pattern - État Pause
 * Représente l'état quand le jeu est en pause
 */
public class PausedState implements GameState {
    
    private static final GameLogger logger = GameLogger.getInstance();
    
    @Override
    public void enter(GameContext context) {
        logger.logStateChange("Game", context.getCurrentStateName(), getStateName());
        logger.logGameEvent("Game paused");
    }
    
    @Override
    public void update(GameContext context, double deltaTime) {
        // Pas de mise à jour pendant la pause
    }
    
    @Override
    public void handleInput(GameContext context, String input) {
        switch (input) {
            case "RESUME":
            case "PAUSE":
                context.setState(new PlayingState());
                break;
            case "MENU":
                context.setState(new MenuState());
                break;
        }
    }
    
    @Override
    public void exit(GameContext context) {
        logger.logInfo("Exiting paused state");
    }
    
    @Override
    public String getStateName() {
        return "PAUSED";
    }
}
