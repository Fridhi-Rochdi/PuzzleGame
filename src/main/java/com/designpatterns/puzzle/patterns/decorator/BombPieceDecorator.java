package com.designpatterns.puzzle.patterns.decorator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Decorator Pattern - Décorateur BombPiece
 * Ajoute un effet de bombe qui détruit les lignes adjacentes
 */
public class BombPieceDecorator extends PuzzlePieceDecorator {
    
    public BombPieceDecorator(PuzzlePiece piece) {
        super(piece);
        logger.logDecoratorApplied("BombPiece", piece.getDescription());
    }
    
    @Override
    public void render(GraphicsContext gc, double x, double y, double blockSize) {
        // Rend la pièce de base
        decoratedPiece.render(gc, x, y, blockSize);
        
        // Ajoute un symbole de bombe
        int[][] shape = getShape();
        gc.setFill(Color.RED);
        
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    double blockX = x + col * blockSize;
                    double blockY = y + row * blockSize;
                    double centerX = blockX + blockSize / 2;
                    double centerY = blockY + blockSize / 2;
                    
                    // Dessine un cercle rouge (symbole bombe)
                    gc.fillOval(centerX - 5, centerY - 5, 10, 10);
                }
            }
        }
    }
    
    @Override
    public boolean hasSpecialEffect() {
        return true;
    }
    
    @Override
    public String getDescription() {
        return decoratedPiece.getDescription() + " + Bomb Effect (clears adjacent lines)";
    }
}
