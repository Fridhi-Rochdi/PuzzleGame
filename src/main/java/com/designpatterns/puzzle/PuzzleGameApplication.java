package com.designpatterns.puzzle;

import com.designpatterns.puzzle.core.GameManager;
import com.designpatterns.puzzle.utils.GameLogger;
import com.designpatterns.puzzle.view.GameView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Application principale du jeu de Puzzle
 * Projet Design Patterns - JavaFX
 * 
 * Design Patterns implémentés:
 * 1. State Pattern - Gestion des états du jeu
 * 2. Decorator Pattern - Power-ups et effets spéciaux
 * 3. Composite Pattern - Structure hiérarchique des composants
 * 4. Factory Pattern - Création des pièces et power-ups
 * 5. Singleton Pattern - GameManager et GameLogger
 */
public class PuzzleGameApplication extends Application {
    
    private static final int WINDOW_WIDTH = 650;
    private static final int WINDOW_HEIGHT = 700;
    private static final String TITLE = "Puzzle Game - Design Patterns Project";
    
    private GameManager gameManager;
    private GameView gameView;
    private GameLogger logger;
    
    private long lastUpdate;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialise le logger (Singleton)
            logger = GameLogger.getInstance();
            logger.logInfo("=== Puzzle Game Started ===");
            logger.logInfo("Application: " + TITLE);
            
            // Initialise le game manager (Singleton)
            gameManager = GameManager.getInstance();
            gameManager.initialize();
            
            // Crée le canvas
            Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
            gameView = new GameView(canvas);
            
            // Layout
            StackPane root = new StackPane();
            root.getChildren().add(canvas);
            
            // Scène
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            scene.setOnKeyPressed(event -> handleKeyPress(event.getCode()));
            
            // Stage
            primaryStage.setTitle(TITLE);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setOnCloseRequest(event -> {
                logger.logInfo("=== Application Closed ===");
                System.exit(0);
            });
            primaryStage.show();
            
            // Boucle de jeu
            lastUpdate = System.nanoTime();
            AnimationTimer gameLoop = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                    lastUpdate = now;
                    
                    // Met à jour le jeu
                    gameManager.update(deltaTime);
                    
                    // Rend la vue
                    gameView.render();
                }
            };
            gameLoop.start();
            
            logger.logInfo("Game loop started");
            
        } catch (Exception e) {
            logger.logError("Error starting application", e);
            e.printStackTrace();
        }
    }
    
    /**
     * Gère les entrées clavier
     */
    private void handleKeyPress(KeyCode code) {
        String input = null;
        
        switch (code) {
            // Menu
            case ENTER:
                input = "START";
                break;
            case ESCAPE:
                input = "QUIT";
                break;
                
            // Jeu
            case LEFT:
                input = "LEFT";
                break;
            case RIGHT:
                input = "RIGHT";
                break;
            case DOWN:
                input = "DOWN";
                break;
            case UP:
                input = "ROTATE";
                break;
            case SPACE:
                input = "DROP";
                break;
                
            // Pause
            case P:
                input = "PAUSE";
                break;
                
            // Game Over
            case R:
                input = "RESTART";
                break;
            case M:
                input = "MENU";
                break;
                
            default:
                break;
        }
        
        if (input != null) {
            gameManager.handleInput(input);
            // Force un rendu immédiat pour feedback instantané
            gameView.render();
        }
    }
    
    /**
     * Point d'entrée de l'application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
