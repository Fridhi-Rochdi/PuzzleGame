package com.designpatterns.puzzle.model;

import com.designpatterns.puzzle.patterns.decorator.PuzzlePiece;
import com.designpatterns.puzzle.utils.GameLogger;

/**
 * Représente une pièce active dans le jeu
 * Gère la position et la rotation de la pièce
 */
public class ActivePiece {
    
    private PuzzlePiece piece;
    private int x;
    private int y;
    private int rotation;
    private static final GameLogger logger = GameLogger.getInstance();
    
    public ActivePiece(PuzzlePiece piece, int startX, int startY) {
        this.piece = piece;
        this.x = startX;
        this.y = startY;
        this.rotation = 0;
    }
    
    /**
     * Déplace la pièce vers la gauche
     */
    public void moveLeft() {
        x--;
    }
    
    /**
     * Déplace la pièce vers la droite
     */
    public void moveRight() {
        x++;
    }
    
    /**
     * Déplace la pièce vers le bas
     */
    public void moveDown() {
        y++;
    }
    
    /**
     * Fait tourner la pièce (rotation horaire)
     */
    public void rotate() {
        rotation = (rotation + 1) % 4;
        logger.logGameEvent("Piece rotated (rotation: " + rotation + ")");
    }
    
    /**
     * Obtient la forme actuelle de la pièce avec rotation appliquée
     */
    public int[][] getCurrentShape() {
        int[][] baseShape = piece.getShape();
        return rotateMatrix(baseShape, rotation);
    }
    
    /**
     * Fait tourner une matrice selon le nombre de rotations (90° chaque)
     */
    private int[][] rotateMatrix(int[][] matrix, int times) {
        int[][] result = matrix;
        for (int i = 0; i < times; i++) {
            result = rotate90Clockwise(result);
        }
        return result;
    }
    
    /**
     * Fait tourner une matrice de 90° dans le sens horaire
     */
    private int[][] rotate90Clockwise(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] rotated = new int[cols][rows];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - 1 - i] = matrix[i][j];
            }
        }
        return rotated;
    }
    
    // Getters et setters
    public PuzzlePiece getPiece() {
        return piece;
    }
    
    public void setPiece(PuzzlePiece piece) {
        this.piece = piece;
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getRotation() {
        return rotation;
    }
}
