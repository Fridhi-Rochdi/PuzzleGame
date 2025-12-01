package com.designpatterns.puzzle.core;

import com.designpatterns.puzzle.model.ActivePiece;
import com.designpatterns.puzzle.model.GameGrid;
import com.designpatterns.puzzle.patterns.decorator.PuzzlePiece;
import com.designpatterns.puzzle.patterns.factory.PieceFactory;
import com.designpatterns.puzzle.patterns.factory.PowerUpFactory;
import com.designpatterns.puzzle.patterns.state.GameState;
import com.designpatterns.puzzle.utils.GameLogger;

$
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
    private static final double BASE_FALL_SPEED = 1.0; 
    
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
    
    
    public void setState(GameState newState) {
        if (currentState != null) {
            currentState.exit(this);
        }
        currentState = newState;
        currentState.enter(this);
    }
    
    
    public void update(double deltaTime) {
        currentState.update(this, deltaTime);
    }
    
   
    public void handleInput(String input) {
        currentState.handleInput(this, input);
    }
    
    
    public void initializeGame() {
        grid.clear();
        score = 0;
        level = 1;
        linesCleared = 0;
        fallSpeed = BASE_FALL_SPEED;
        fallTimer = 0;
        gameOver = false;
        
        
        spawnNewPiece();
        
        nextPiece = PowerUpFactory.applyRandomPowerUp(PieceFactory.createRandomPiece());
        
        logger.logInfo("New game initialized");
    }
    
   
    public void updateGame(double deltaTime) {
        if (currentPiece == null) {
            spawnNewPiece();
            return;
        }
        
        fallTimer += deltaTime;
        if (fallTimer >= fallSpeed) {
            fallTimer = 0;
            movePieceDown();
        }
    }
    
   
    private void spawnNewPiece() {
        PuzzlePiece piece = (nextPiece != null) ? nextPiece : 
                           PowerUpFactory.applyRandomPowerUp(PieceFactory.createRandomPiece());
        
        int startX = grid.getWidth() / 2 - 1;
        int startY = 0;
        
        currentPiece = new ActivePiece(piece, startX, startY);
        
        nextPiece = PowerUpFactory.applyRandomPowerUp(PieceFactory.createRandomPiece());
        
        if (!grid.canPlacePiece(currentPiece)) {
            gameOver = true;
            logger.logGameEvent("Game Over - Cannot spawn new piece");
        }
    }
    
    
    public void movePieceLeft() {
        if (currentPiece != null) {
            currentPiece.moveLeft();
            if (!grid.canPlacePiece(currentPiece)) {
                currentPiece.moveRight(); 
            }
        }
    }
    
    
    public void movePieceRight() {
        if (currentPiece != null) {
            currentPiece.moveRight();
            if (!grid.canPlacePiece(currentPiece)) {
                currentPiece.moveLeft(); 
            }
        }
    }
    
  
    public void movePieceDown() {
        if (currentPiece != null) {
            currentPiece.moveDown();
            if (!grid.canPlacePiece(currentPiece)) {
                currentPiece.setY(currentPiece.getY() - 1); 
                lockPiece();
            }
        }
    }
    
    public void rotatePiece() {
        if (currentPiece != null) {
            currentPiece.rotate();
            if (!grid.canPlacePiece(currentPiece)) {
                currentPiece.rotate();
                currentPiece.rotate();
                currentPiece.rotate();
            }
        }
    }
    
    
    public void dropPiece() {
        if (currentPiece != null) {
            int dropDistance = 0;
            while (grid.canPlacePiece(currentPiece)) {
                currentPiece.moveDown();
                dropDistance++;
            }
            currentPiece.setY(currentPiece.getY() - 1);
            
            score += dropDistance * 2;
            
            lockPiece();
            logger.logGameEvent("Piece dropped (distance: " + dropDistance + ")");
        }
    }
    
  
    private void lockPiece() {
        if (currentPiece != null) {
            grid.placePiece(currentPiece);
            
            int lines = grid.clearCompleteLines();
            if (lines > 0) {
                linesCleared += lines;
                int baseScore = calculateLineScore(lines);
                int multiplier = currentPiece.getPiece().getScoreMultiplier();
                score += baseScore * multiplier;
                
                logger.logGameEvent("Lines cleared: " + lines + " | Score added: " + (baseScore * multiplier));
                
                level = (linesCleared / 10) + 1;
                fallSpeed = BASE_FALL_SPEED / (1 + (level - 1) * 0.1);
            }
            
            if (grid.isGameOver()) {
                gameOver = true;
            }
            
            currentPiece = null;
        }
    }
    
    
    private int calculateLineScore(int lines) {
        switch (lines) {
            case 1: return 100;
            case 2: return 300;
            case 3: return 500;
            case 4: return 800; 
            default: return lines * 100;
        }
    }
    
    
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
