package com.designpatterns.puzzle.patterns.decorator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Decorator Pattern - Interface pour les pièces décorables
 * Définit le contrat de base pour les pièces du jeu
 */
public interface PuzzlePiece {
    
    /**
     * Rend la pièce graphiquement
     */
    void render(GraphicsContext gc, double x, double y, double blockSize);
    
    /**
     * Obtient la couleur de la pièce
     */
    Color getColor();
    
    /**
     * Obtient la forme de la pièce (matrice de blocs)
     */
    int[][] getShape();
    
    /**
     * Obtient le score bonus de la pièce
     */
    int getScoreMultiplier();
    
    /**
     * Vérifie si la pièce a un effet spécial
     */
    boolean hasSpecialEffect();
    
    /**
     * Description de la pièce et ses effets
     */
    String getDescription();
}
