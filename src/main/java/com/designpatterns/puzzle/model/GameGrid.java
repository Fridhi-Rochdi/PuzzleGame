package com.designpatterns.puzzle.model;

import com.designpatterns.puzzle.utils.GameLogger;
import javafx.scene.paint.Color;

/**
 * Représente la grille de jeu (le plateau)
 * Gère l'état des blocs placés
 */
public class GameGrid {
    
    private static final int DEFAULT_WIDTH = 10;
    private static final int DEFAULT_HEIGHT = 20;
    private static final GameLogger logger = GameLogger.getInstance();
    
    private Color[][] grid;
    private int width;
    private int height;
    
    public GameGrid() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    public GameGrid(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Color[height][width];
        clear();
    }
    
    /**
     * Efface la grille
     */
    public void clear() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = null;
            }
        }
    }
    
    /**
     * Vérifie si une position est vide
     */
    public boolean isEmpty(int row, int col) {
        if (row < 0 || row >= height || col < 0 || col >= width) {
            return false;
        }
        return grid[row][col] == null;
    }
    
    /**
     * Place un bloc à une position
     */
    public void setBlock(int row, int col, Color color) {
        if (row >= 0 && row < height && col >= 0 && col < width) {
            grid[row][col] = color;
        }
    }
    
    /**
     * Obtient la couleur d'un bloc
     */
    public Color getBlock(int row, int col) {
        if (row < 0 || row >= height || col < 0 || col >= width) {
            return null;
        }
        return grid[row][col];
    }
    
    /**
     * Vérifie si une pièce peut être placée à une position
     */
    public boolean canPlacePiece(ActivePiece piece) {
        int[][] shape = piece.getCurrentShape();
        int pieceX = piece.getX();
        int pieceY = piece.getY();
        
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    int gridX = pieceX + col;
                    int gridY = pieceY + row;
                    
                    // Vérifie les limites
                    if (gridX < 0 || gridX >= width || gridY >= height) {
                        return false;
                    }
                    
                    // Vérifie si la position est occupée
                    if (gridY >= 0 && !isEmpty(gridY, gridX)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * Place une pièce sur la grille
     */
    public void placePiece(ActivePiece piece) {
        int[][] shape = piece.getCurrentShape();
        int pieceX = piece.getX();
        int pieceY = piece.getY();
        Color color = piece.getPiece().getColor();
        
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    int gridX = pieceX + col;
                    int gridY = pieceY + row;
                    if (gridY >= 0 && gridY < height) {
                        setBlock(gridY, gridX, color);
                    }
                }
            }
        }
        
        logger.logGameEvent("Piece placed at (" + pieceX + ", " + pieceY + ")");
    }
    
    /**
     * Vérifie si une ligne est complète
     */
    public boolean isLineComplete(int row) {
        for (int col = 0; col < width; col++) {
            if (isEmpty(row, col)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Supprime une ligne et fait descendre les lignes au-dessus
     */
    public void clearLine(int row) {
        for (int r = row; r > 0; r--) {
            for (int col = 0; col < width; col++) {
                grid[r][col] = grid[r - 1][col];
            }
        }
        // Efface la ligne du haut
        for (int col = 0; col < width; col++) {
            grid[0][col] = null;
        }
        logger.logGameEvent("Line cleared: " + row);
    }
    
    /**
     * Efface toutes les lignes complètes et retourne le nombre de lignes effacées
     */
    public int clearCompleteLines() {
        int linesCleared = 0;
        for (int row = height - 1; row >= 0; row--) {
            if (isLineComplete(row)) {
                clearLine(row);
                linesCleared++;
                row++; // Revérifie la même ligne car elle a descendu
            }
        }
        
        if (linesCleared > 0) {
            logger.logGameEvent("Total lines cleared: " + linesCleared);
        }
        
        return linesCleared;
    }
    
    /**
     * Vérifie si le jeu est terminé (blocs en haut)
     */
    public boolean isGameOver() {
        for (int col = 0; col < width; col++) {
            if (!isEmpty(0, col)) {
                return true;
            }
        }
        return false;
    }
    
    // Getters
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public Color[][] getGrid() {
        return grid;
    }
}
