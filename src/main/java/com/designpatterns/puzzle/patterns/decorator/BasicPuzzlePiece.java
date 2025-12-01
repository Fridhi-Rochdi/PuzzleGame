package com.designpatterns.puzzle.patterns.decorator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Decorator Pattern - Pièce de base (ConcreteComponent)
 * Représente une pièce standard sans décoration
 */
public class BasicPuzzlePiece implements PuzzlePiece {
    
    protected Color color;
    protected int[][] shape;
    protected String type;
    
    public BasicPuzzlePiece(Color color, int[][] shape, String type) {
        this.color = color;
        this.shape = shape;
        this.type = type;
    }
    
    @Override
    public void render(GraphicsContext gc, double x, double y, double blockSize) {
        gc.setFill(color);
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    double blockX = x + col * blockSize;
                    double blockY = y + row * blockSize;
                    gc.fillRect(blockX, blockY, blockSize - 1, blockSize - 1);
                    
                    // Effet 3D
                    gc.setStroke(color.brighter());
                    gc.strokeLine(blockX, blockY, blockX + blockSize - 1, blockY);
                    gc.strokeLine(blockX, blockY, blockX, blockY + blockSize - 1);
                    
                    gc.setStroke(color.darker());
                    gc.strokeLine(blockX + blockSize - 1, blockY, blockX + blockSize - 1, blockY + blockSize - 1);
                    gc.strokeLine(blockX, blockY + blockSize - 1, blockX + blockSize - 1, blockY + blockSize - 1);
                }
            }
        }
    }
    
    @Override
    public Color getColor() {
        return color;
    }
    
    @Override
    public int[][] getShape() {
        return shape;
    }
    
    @Override
    public int getScoreMultiplier() {
        return 1;
    }
    
    @Override
    public boolean hasSpecialEffect() {
        return false;
    }
    
    @Override
    public String getDescription() {
        return "Basic " + type + " piece";
    }
    
    public String getType() {
        return type;
    }
}
