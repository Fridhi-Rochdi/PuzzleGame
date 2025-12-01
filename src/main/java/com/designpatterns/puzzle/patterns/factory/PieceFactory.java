package com.designpatterns.puzzle.patterns.factory;

import com.designpatterns.puzzle.patterns.decorator.BasicPuzzlePiece;
import com.designpatterns.puzzle.patterns.decorator.PuzzlePiece;
import com.designpatterns.puzzle.utils.GameLogger;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Factory Pattern - Factory pour créer des pièces de puzzle
 * Crée différents types de pièces selon le type demandé
 */
public class PieceFactory {
    
    private static final GameLogger logger = GameLogger.getInstance();
    private static final Random random = new Random();
    
    /**
     * Types de pièces disponibles (comme Tetris)
     */
    public enum PieceType {
        I, O, T, S, Z, J, L
    }
    
    /**
     * Crée une pièce selon le type spécifié
     */
    public static PuzzlePiece createPiece(PieceType type) {
        PuzzlePiece piece = null;
        
        switch (type) {
            case I:
                piece = createIPiece();
                break;
            case O:
                piece = createOPiece();
                break;
            case T:
                piece = createTPiece();
                break;
            case S:
                piece = createSPiece();
                break;
            case Z:
                piece = createZPiece();
                break;
            case J:
                piece = createJPiece();
                break;
            case L:
                piece = createLPiece();
                break;
        }
        
        logger.logObjectCreation("PuzzlePiece", type.toString());
        return piece;
    }
    
    /**
     * Crée une pièce aléatoire
     */
    public static PuzzlePiece createRandomPiece() {
        PieceType[] types = PieceType.values();
        PieceType randomType = types[random.nextInt(types.length)];
        return createPiece(randomType);
    }
    
    // Pièce I (ligne droite de 4 blocs)
    private static PuzzlePiece createIPiece() {
        int[][] shape = {
            {1, 1, 1, 1}
        };
        return new BasicPuzzlePiece(Color.CYAN, shape, "I");
    }
    
    // Pièce O (carré 2x2)
    private static PuzzlePiece createOPiece() {
        int[][] shape = {
            {1, 1},
            {1, 1}
        };
        return new BasicPuzzlePiece(Color.YELLOW, shape, "O");
    }
    
    // Pièce T (en forme de T)
    private static PuzzlePiece createTPiece() {
        int[][] shape = {
            {0, 1, 0},
            {1, 1, 1}
        };
        return new BasicPuzzlePiece(Color.PURPLE, shape, "T");
    }
    
    // Pièce S (en forme de S)
    private static PuzzlePiece createSPiece() {
        int[][] shape = {
            {0, 1, 1},
            {1, 1, 0}
        };
        return new BasicPuzzlePiece(Color.GREEN, shape, "S");
    }
    
    // Pièce Z (en forme de Z)
    private static PuzzlePiece createZPiece() {
        int[][] shape = {
            {1, 1, 0},
            {0, 1, 1}
        };
        return new BasicPuzzlePiece(Color.RED, shape, "Z");
    }
    
    // Pièce J (en forme de J)
    private static PuzzlePiece createJPiece() {
        int[][] shape = {
            {1, 0, 0},
            {1, 1, 1}
        };
        return new BasicPuzzlePiece(Color.BLUE, shape, "J");
    }
    
    // Pièce L (en forme de L)
    private static PuzzlePiece createLPiece() {
        int[][] shape = {
            {0, 0, 1},
            {1, 1, 1}
        };
        return new BasicPuzzlePiece(Color.ORANGE, shape, "L");
    }
}
