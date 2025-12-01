package com.designpatterns.puzzle.patterns.state;

import com.designpatterns.puzzle.core.GameContext;
import com.designpatterns.puzzle.utils.GameLogger;

/**
 * State Pattern - État En Jeu
 * Représente l'état quand le jeu est en cours
 */
public class PlayingState implements GameState {
    
    private static final GameLogger logger = GameLogger.getInstance();
    
    @Override
    public void enter(GameContext context) {
        logger.logStateChange("Game", context.getCurrentStateName(), getStateName());
        logger.logGameEvent("Game started");
        context.initializeGame();
    }
    
    @Override
    public void update(GameContext context, double deltaTime) {
        // Met à jour la logique du jeu
        context.updateGame(deltaTime);
        
        // Vérifie si le jeu est terminé
        if (context.isGameOver()) {
            context.setState(new GameOverState());
        }
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
            case "RIGHT":
                context.movePieceRight();
                break;
            case "DOWN":
                context.movePieceDown();
                break;
            case "ROTATE":
                context.rotatePiece();
                break;
            case "DROP":
                context.dropPiece();
                break;
        }
    }
    
    @Override
    public void exit(GameContext context) {
        logger.logInfo("Exiting playing state");
    }
    
    @Override
    public String getStateName() {
        return "PLAYING";
    }
}
