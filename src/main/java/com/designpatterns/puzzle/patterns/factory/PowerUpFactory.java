package com.designpatterns.puzzle.patterns.factory;

import com.designpatterns.puzzle.patterns.decorator.*;
import com.designpatterns.puzzle.utils.GameLogger;

import java.util.Random;

/**
 * Factory Pattern - Factory pour créer des décorateurs
 * Crée des power-ups aléatoires ou spécifiques
 */
public class PowerUpFactory {
    
    private static final GameLogger logger = GameLogger.getInstance();
    private static final Random random = new Random();
    
    /**
     * Types de power-ups disponibles
     */
    public enum PowerUpType {
        GOLDEN,    // x2 score
        BOMB,      // Détruit lignes adjacentes
        RAINBOW    // x3 score + animation
    }
    
    /**
     * Applique un power-up aléatoire à une pièce
     * Probabilité: 15% de recevoir un power-up
     */
    public static PuzzlePiece applyRandomPowerUp(PuzzlePiece piece) {
        // 15% de chance d'avoir un power-up
        if (random.nextDouble() < 0.15) {
            PowerUpType[] types = PowerUpType.values();
            PowerUpType randomType = types[random.nextInt(types.length)];
            return applyPowerUp(piece, randomType);
        }
        return piece;
    }
    
    /**
     * Applique un power-up spécifique à une pièce
     */
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
    
    /**
     * Applique plusieurs power-ups (stacking de décorateurs)
     */
    public static PuzzlePiece applyMultiplePowerUps(PuzzlePiece piece, PowerUpType... types) {
        PuzzlePiece decorated = piece;
        for (PowerUpType type : types) {
            decorated = applyPowerUp(decorated, type);
        }
        return decorated;
    }
}
