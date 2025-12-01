package com.designpatterns.puzzle.patterns.state;

import com.designpatterns.puzzle.core.GameContext;
import com.designpatterns.puzzle.utils.GameLogger;

/**
 * State Pattern - État Menu Principal
 * Représente l'état quand le joueur est dans le menu principal
 */
public class MenuState implements GameState {
    
    private static final GameLogger logger = GameLogger.getInstance();
    
    @Override
    public void enter(GameContext context) {
        logger.logStateChange("Game", context.getCurrentStateName(), getStateName());
        logger.logInfo("Entering menu state");
    }
    
    @Override
    public void update(GameContext context, double deltaTime) {
        // Le menu n'a pas besoin de mise à jour continue
    }
    
    @Override
    public void handleInput(GameContext context, String input) {
        switch (input) {
            case "START":
                context.setState(new PlayingState());
                break;
            case "QUIT":
                logger.logInfo("Quit game requested from menu");
                System.exit(0);
                break;
        }
    }
    
    @Override
    public void exit(GameContext context) {
        logger.logInfo("Exiting menu state");
    }
    
    @Override
    public String getStateName() {
        return "MENU";
    }
}
