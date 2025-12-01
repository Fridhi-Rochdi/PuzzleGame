package com.designpatterns.puzzle.patterns.state;

import com.designpatterns.puzzle.core.GameContext;
import com.designpatterns.puzzle.utils.GameLogger;

/**
 * State Pattern - État Game Over
 * Représente l'état quand le jeu est terminé
 */
public class GameOverState implements GameState {
    
    private static final GameLogger logger = GameLogger.getInstance();
    
    @Override
    public void enter(GameContext context) {
        logger.logStateChange("Game", context.getCurrentStateName(), getStateName());
        logger.logGameEvent("Game Over");
        logger.logFinalScore(context.getScore(), context.getLevel(), context.getLinesCleared());
    }
    
    @Override
    public void update(GameContext context, double deltaTime) {
        // Pas de mise à jour en état Game Over
    }
    
    @Override
    public void handleInput(GameContext context, String input) {
        switch (input) {
            case "RESTART":
                context.resetGame();
                context.setState(new PlayingState());
                break;
            case "MENU":
                context.resetGame();
                context.setState(new MenuState());
                break;
        }
    }
    
    @Override
    public void exit(GameContext context) {
        logger.logInfo("Exiting game over state");
    }
    
    @Override
    public String getStateName() {
        return "GAME_OVER";
    }
}
