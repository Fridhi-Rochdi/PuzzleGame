package com.designpatterns.puzzle.patterns.decorator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Decorator Pattern - Décorateur RainbowPiece
 * Ajoute un effet arc-en-ciel animé et x3 score
 */
public class RainbowPieceDecorator extends PuzzlePieceDecorator {
    
    private double hueShift = 0;
    
    public RainbowPieceDecorator(PuzzlePiece piece) {
        super(piece);
        logger.logDecoratorApplied("RainbowPiece", piece.getDescription());
    }
    
    @Override
    public void render(GraphicsContext gc, double x, double y, double blockSize) {
        // Anime le décalage de teinte
        hueShift = (hueShift + 2) % 360;
        
        // Rend avec effet arc-en-ciel
        int[][] shape = getShape();
        
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    double blockX = x + col * blockSize;
                    double blockY = y + row * blockSize;
                    
                    // Calcule une couleur arc-en-ciel basée sur la position
                    double hue = (hueShift + row * 30 + col * 30) % 360;
                    Color rainbowColor = Color.hsb(hue, 0.8, 0.9);
                    
                    gc.setFill(rainbowColor);
                    gc.fillRect(blockX, blockY, blockSize - 1, blockSize - 1);
                    
                    // Effet brillant
                    gc.setStroke(rainbowColor.brighter());
                    gc.setLineWidth(2);
                    gc.strokeRect(blockX + 2, blockY + 2, blockSize - 5, blockSize - 5);
                    gc.setLineWidth(1);
                }
            }
        }
    }
    
    @Override
    public int getScoreMultiplier() {
        return decoratedPiece.getScoreMultiplier() * 3;
    }
    
    @Override
    public boolean hasSpecialEffect() {
        return true;
    }
    
    @Override
    public String getDescription() {
        return decoratedPiece.getDescription() + " + Rainbow Power (x3 score, animated)";
    }
    
    @Override
    public Color getColor() {
        // Retourne une couleur arc-en-ciel dynamique
        return Color.hsb(hueShift, 0.8, 0.9);
    }
}
