package com.designpatterns.puzzle.core;

import com.designpatterns.puzzle.model.ActivePiece;
import com.designpatterns.puzzle.model.GameGrid;
import com.designpatterns.puzzle.patterns.decorator.PuzzlePiece;
import com.designpatterns.puzzle.patterns.factory.PieceFactory;
import com.designpatterns.puzzle.patterns.factory.PowerUpFactory;
import com.designpatterns.puzzle.patterns.state.GameState;
import com.designpatterns.puzzle.utils.GameLogger;

/**
 * Contexte du jeu - Utilise le State Pattern
 * Gère l'état actuel du jeu et délègue les actions à l'état actif
 */
public class GameContext {
    
    private GameState currentState;
    private GameGrid grid;
    private ActivePiece currentPiece;
    private PuzzlePiece nextPiece;
    private int score;
    private int level;
    private int linesCleared;
    private double fallSpeed;
    private double fallTimer;
    private boolean gameOver;
    
    private static final GameLogger logger = GameLogger.getInstance();
    private static final double BASE_FALL_SPEED = 1.0; // secondes
    
    public GameContext(GameState initialState) {
        this.currentState = initialState;
        this.grid = new GameGrid();
        this.score = 0;
        this.level = 1;
        this.linesCleared = 0;
        this.fallSpeed = BASE_FALL_SPEED;
        this.fallTimer = 0;
        this.gameOver = false;
        
        currentState.enter(this);
    }
    
    /**
     * Change l'état du jeu (State Pattern)
     */
    public void setState(GameState newState) {
        if (currentState != null) {
            currentState.exit(this);
        }
        currentState = newState;
        currentState.enter(this);
    }
    
    /**
     * Met à jour le jeu
     */
    public void update(double deltaTime) {
        currentState.update(this, deltaTime);
    }
    
    /**
     * Gère les entrées utilisateur
     */
    public void handleInput(String input) {
        currentState.handleInput(this, input);
    }
    
    /**
     * Initialise une nouvelle partie
     */
    public void initializeGame() {
        grid.clear();
        score = 0;
        level = 1;
        linesCleared = 0;
        fallSpeed = BASE_FALL_SPEED;
        fallTimer = 0;
        gameOver = false;
        
        // Crée la première pièce
        spawnNewPiece();
        
        // Prépare la pièce suivante
        nextPiece = PowerUpFactory.applyRandomPowerUp(PieceFactory.createRandomPiece());
        
        logger.logInfo("New game initialized");
    }
    
    /**
     * Met à jour la logique du jeu (appelé quand en état PLAYING)
     */
    public void updateGame(double deltaTime) {
        if (currentPiece == null) {
            spawnNewPiece();
            return;
        }
        
        // Gère la chute automatique
        fallTimer += deltaTime;
        if (fallTimer >= fallSpeed) {
            fallTimer = 0;
            movePieceDown();
        }
    }
    
    /**
     * Fait apparaître une nouvelle pièce
     */
    private void spawnNewPiece() {
        PuzzlePiece piece = (nextPiece != null) ? nextPiece : 
                           PowerUpFactory.applyRandomPowerUp(PieceFactory.createRandomPiece());
        
        int startX = grid.getWidth() / 2 - 1;
        int startY = 0;
        
        currentPiece = new ActivePiece(piece, startX, startY);
        
        // Prépare la pièce suivante
        nextPiece = PowerUpFactory.applyRandomPowerUp(PieceFactory.createRandomPiece());
        
        // Vérifie si le jeu est terminé
        if (!grid.canPlacePiece(currentPiece)) {
            gameOver = true;
            logger.logGameEvent("Game Over - Cannot spawn new piece");
        }
    }
    
    /**
     * Déplace la pièce vers la gauche
     */
    public void movePieceLeft() {
        if (currentPiece != null) {
            currentPiece.moveLeft();
            if (!grid.canPlacePiece(currentPiece)) {
                currentPiece.moveRight(); // Annule le mouvement
            }
        }
    }
    
    /**
     * Déplace la pièce vers la droite
     */
    public void movePieceRight() {
        if (currentPiece != null) {
            currentPiece.moveRight();
            if (!grid.canPlacePiece(currentPiece)) {
                currentPiece.moveLeft(); // Annule le mouvement
            }
        }
    }
    
    /**
     * Déplace la pièce vers le bas
     */
    public void movePieceDown() {
        if (currentPiece != null) {
            currentPiece.moveDown();
            if (!grid.canPlacePiece(currentPiece)) {
                currentPiece.setY(currentPiece.getY() - 1); // Annule le mouvement
                lockPiece();
            }
        }
    }
    
    /**
     * Fait tourner la pièce
     */
    public void rotatePiece() {
        if (currentPiece != null) {
            currentPiece.rotate();
            if (!grid.canPlacePiece(currentPiece)) {
                // Annule la rotation 3 fois (retour à l'état initial)
                currentPiece.rotate();
                currentPiece.rotate();
                currentPiece.rotate();
            }
        }
    }
    
    /**
     * Fait tomber la pièce instantanément
     */
    public void dropPiece() {
        if (currentPiece != null) {
            int dropDistance = 0;
            while (grid.canPlacePiece(currentPiece)) {
                currentPiece.moveDown();
                dropDistance++;
            }
            currentPiece.setY(currentPiece.getY() - 1);
            
            // Bonus de points pour le drop
            score += dropDistance * 2;
            
            lockPiece();
            logger.logGameEvent("Piece dropped (distance: " + dropDistance + ")");
        }
    }
    
    /**
     * Verrouille la pièce sur la grille
     */
    private void lockPiece() {
        if (currentPiece != null) {
            grid.placePiece(currentPiece);
            
            // Efface les lignes complètes
            int lines = grid.clearCompleteLines();
            if (lines > 0) {
                linesCleared += lines;
                int baseScore = calculateLineScore(lines);
                int multiplier = currentPiece.getPiece().getScoreMultiplier();
                score += baseScore * multiplier;
                
                logger.logGameEvent("Lines cleared: " + lines + " | Score added: " + (baseScore * multiplier));
                
                // Augmente le niveau tous les 10 lignes
                level = (linesCleared / 10) + 1;
                fallSpeed = BASE_FALL_SPEED / (1 + (level - 1) * 0.1);
            }
            
            // Vérifie game over
            if (grid.isGameOver()) {
                gameOver = true;
            }
            
            currentPiece = null;
        }
    }
    
    /**
     * Calcule le score pour les lignes effacées
     */
    private int calculateLineScore(int lines) {
        switch (lines) {
            case 1: return 100;
            case 2: return 300;
            case 3: return 500;
            case 4: return 800; // Tetris!
            default: return lines * 100;
        }
    }
    
    /**
     * Réinitialise le jeu
     */
    public void resetGame() {
        grid.clear();
        score = 0;
        level = 1;
        linesCleared = 0;
        fallSpeed = BASE_FALL_SPEED;
        fallTimer = 0;
        gameOver = false;
        currentPiece = null;
        nextPiece = null;
        
        logger.logInfo("Game reset");
    }
    
    // Getters
    public String getCurrentStateName() {
        return currentState != null ? currentState.getStateName() : "NONE";
    }
    
    public GameState getCurrentState() {
        return currentState;
    }
    
    public GameGrid getGrid() {
        return grid;
    }
    
    public ActivePiece getCurrentPiece() {
        return currentPiece;
    }
    
    public PuzzlePiece getNextPiece() {
        return nextPiece;
    }
    
    public int getScore() {
        return score;
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getLinesCleared() {
        return linesCleared;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public double getFallSpeed() {
        return fallSpeed;
    }
}
