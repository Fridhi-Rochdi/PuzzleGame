package com.designpatterns.puzzle.patterns.decorator;

import com.designpatterns.puzzle.utils.GameLogger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Decorator Pattern - Décorateur abstrait
 * Classe de base pour tous les décorateurs de pièces
 */
public abstract class PuzzlePieceDecorator implements PuzzlePiece {
    
    protected PuzzlePiece decoratedPiece;
    protected static final GameLogger logger = GameLogger.getInstance();
    
    public PuzzlePieceDecorator(PuzzlePiece piece) {
        this.decoratedPiece = piece;
    }
    
    @Override
    public void render(GraphicsContext gc, double x, double y, double blockSize) {
        decoratedPiece.render(gc, x, y, blockSize);
    }
    
    @Override
    public Color getColor() {
        return decoratedPiece.getColor();
    }
    
    @Override
    public int[][] getShape() {
        return decoratedPiece.getShape();
    }
    
    @Override
    public int getScoreMultiplier() {
        return decoratedPiece.getScoreMultiplier();
    }
    
    @Override
    public boolean hasSpecialEffect() {
        return decoratedPiece.hasSpecialEffect();
    }
    
    @Override
    public String getDescription() {
        return decoratedPiece.getDescription();
    }
    
    protected PuzzlePiece getDecoratedPiece() {
        return decoratedPiece;
    }
}
