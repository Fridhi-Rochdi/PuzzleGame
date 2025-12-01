package com.designpatterns.puzzle.patterns.decorator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Decorator Pattern - Décorateur GoldenPiece
 * Ajoute un multiplicateur de score x2 et un effet visuel doré
 */
public class GoldenPieceDecorator extends PuzzlePieceDecorator {
    
    public GoldenPieceDecorator(PuzzlePiece piece) {
        super(piece);
        logger.logDecoratorApplied("GoldenPiece", piece.getDescription());
    }
    
    @Override
    public void render(GraphicsContext gc, double x, double y, double blockSize) {
        // Rend la pièce de base
        decoratedPiece.render(gc, x, y, blockSize);
        
        // Ajoute un effet doré brillant
        int[][] shape = getShape();
        gc.setStroke(Color.GOLD);
        gc.setLineWidth(2);
        
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    double blockX = x + col * blockSize;
                    double blockY = y + row * blockSize;
                    gc.strokeRect(blockX + 1, blockY + 1, blockSize - 3, blockSize - 3);
                }
            }
        }
        
        gc.setLineWidth(1);
    }
    
    @Override
    public int getScoreMultiplier() {
        return decoratedPiece.getScoreMultiplier() * 2;
    }
    
    @Override
    public boolean hasSpecialEffect() {
        return true;
    }
    
    @Override
    public String getDescription() {
        return decoratedPiece.getDescription() + " + Golden Boost (x2 score)";
    }
    
    @Override
    public Color getColor() {
        // Modifie légèrement la couleur pour un ton doré
        Color baseColor = decoratedPiece.getColor();
        return baseColor.interpolate(Color.GOLD, 0.3);
    }
}
