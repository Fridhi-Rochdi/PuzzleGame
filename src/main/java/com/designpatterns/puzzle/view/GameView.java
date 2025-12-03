package com.designpatterns.puzzle.view;

import com.designpatterns.puzzle.core.GameContext;
import com.designpatterns.puzzle.core.GameManager;
import com.designpatterns.puzzle.model.ActivePiece;
import com.designpatterns.puzzle.model.GameGrid;
import com.designpatterns.puzzle.patterns.decorator.PuzzlePiece;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vue principale du jeu utilisant le Composite Pattern
 * Affiche la grille, la pièce active, et le HUD
 */
public class GameView {
    
    private Canvas canvas;
    private GraphicsContext gc;
    private GameManager gameManager;
    
    private static final double BLOCK_SIZE = 32;
    private static final double GRID_X = 60;
    private static final double GRID_Y = 60;
    private static final double HUD_X = 420;
    
    // Animation variables
    private double menuPulseTime = 0;
    private double starRotation = 0;
    private double glowIntensity = 0;
    
    public GameView(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.gameManager = GameManager.getInstance();
    }
    
    /**
     * Rend la vue complète avec animations
     */
    public void render() {
        // Update animations
        menuPulseTime += 0.05;
        starRotation += 2;
        glowIntensity = Math.abs(Math.sin(menuPulseTime));
        
        // Efface le canvas avec fond noir
        renderGradientBackground();
        
        GameContext context = gameManager.getGameContext();
        if (context == null) return;
        
        String stateName = context.getCurrentStateName();
        
        switch (stateName) {
            case "MENU":
                renderMenu();
                break;
            case "PLAYING":
                renderGame(context);
                break;
            case "PAUSED":
                renderGame(context);
                renderPauseOverlay();
                break;
            case "GAME_OVER":
                renderGame(context);
                renderGameOverOverlay(context);
                break;
        }
    }
    
    /**
     * Rend le menu principal professionnel minimaliste de niveau international
     */
    private void renderMenu() {
        // Grille subtile très discrète
        gc.setStroke(Color.rgb(15, 15, 15));
        gc.setLineWidth(0.5);
        for (int i = 0; i < canvas.getWidth(); i += 40) {
            gc.strokeLine(i, 0, i, canvas.getHeight());
        }
        for (int i = 0; i < canvas.getHeight(); i += 40) {
            gc.strokeLine(0, i, canvas.getWidth(), i);
        }
        
        // Particules subtiles animées
        for (int i = 0; i < 15; i++) {
            double particleX = (i * 117 + menuPulseTime * 3) % canvas.getWidth();
            double particleY = (i * 143) % canvas.getHeight();
            double particleAlpha = (Math.sin(menuPulseTime * 0.5 + i) + 1) / 2 * 0.15;
            gc.setFill(Color.rgb(100, 150, 200, particleAlpha));
            gc.fillOval(particleX, particleY, 3, 3);
        }
        
        // === TITRE PRINCIPAL ÉLÉGANT ===
        double centerX = canvas.getWidth() / 2;
        double titleY = 150;
        
        // Titre principal en blanc pur
        String title = "PUZZLE GAME";
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 64));
        
        // Ombre subtile
        gc.setFill(Color.rgb(50, 50, 50, 0.5));
        gc.fillText(title, centerX - 195, titleY + 3);
        
        // Texte principal blanc
        gc.setFill(Color.WHITE);
        gc.fillText(title, centerX - 195, titleY);
        
        // Accent subtil cyan sous le titre
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(3);
        gc.strokeLine(centerX - 120, titleY + 20, centerX + 120, titleY + 20);
        
        // Sous-titre discret
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        gc.setFill(Color.rgb(150, 150, 150));
        String subtitle = "Design Patterns Project";
        gc.fillText(subtitle, centerX - 90, titleY + 50);
        
        // === BOUTONS MINIMALISTES ===
        double buttonWidth = 280;
        double buttonHeight = 50;
        double buttonSpacing = 25;
        double buttonsStartY = 320;
        
        // Bouton START élégant
        double startY = buttonsStartY;
        double startX = centerX - buttonWidth / 2;
        
        // Ombre portée subtile
        gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillRoundRect(startX + 3, startY + 3, buttonWidth, buttonHeight, 8, 8);
        
        // Fond du bouton avec léger gradient simulé
        gc.setFill(Color.rgb(25, 25, 30));
        gc.fillRoundRect(startX, startY, buttonWidth, buttonHeight, 8, 8);
        
        // Bordure cyan élégante avec pulse subtil
        double borderAlpha = 0.7 + glowIntensity * 0.3;
        gc.setStroke(Color.rgb(0, 200, 255, borderAlpha));
        gc.setLineWidth(2);
        gc.strokeRoundRect(startX, startY, buttonWidth, buttonHeight, 8, 8);
        
        // Texte START centré
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        gc.setFill(Color.WHITE);
        gc.fillText("START GAME", startX + 90, startY + 32);
        
        // Bouton QUIT élégant
        double quitY = buttonsStartY + buttonHeight + buttonSpacing;
        double quitX = centerX - buttonWidth / 2;
        
        // Ombre portée
        gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillRoundRect(quitX + 3, quitY + 3, buttonWidth, buttonHeight, 8, 8);
        
        // Fond du bouton
        gc.setFill(Color.rgb(25, 25, 30));
        gc.fillRoundRect(quitX, quitY, buttonWidth, buttonHeight, 8, 8);
        
        // Bordure grise élégante
        gc.setStroke(Color.rgb(100, 100, 100));
        gc.setLineWidth(2);
        gc.strokeRoundRect(quitX, quitY, buttonWidth, buttonHeight, 8, 8);
        
        // Texte QUIT centré
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        gc.setFill(Color.rgb(200, 200, 200));
        gc.fillText("EXIT", quitX + 120, quitY + 32);
        
        // === FOOTER DISCRET ===
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        gc.setFill(Color.rgb(80, 80, 80));
        gc.fillText("© 2025 - Professional Edition", centerX - 95, canvas.getHeight() - 30);
    }
    

    
    /**
     * Rend un fond noir pur style Tetris classique
     */
    private void renderGradientBackground() {
        // Fond noir pur comme le vrai Tetris
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    /**
     * Rend le jeu en cours
     */
    private void renderGame(GameContext context) {
        renderGrid(context.getGrid());
        renderCurrentPiece(context.getCurrentPiece());
        renderHUD(context);
        renderNextPiece(context.getNextPiece());
    }
    
    /**
     * Rend la grille de jeu style Tetris classique amélioré
     */
    private void renderGrid(GameGrid grid) {
        double gridWidth = grid.getWidth() * BLOCK_SIZE;
        double gridHeight = grid.getHeight() * BLOCK_SIZE;
        
        // Ombre portée de la grille
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRect(GRID_X + 4, GRID_Y + 4, gridWidth, gridHeight);
        
        // Fond noir pur de la grille
        gc.setFill(Color.BLACK);
        gc.fillRect(GRID_X, GRID_Y, gridWidth, gridHeight);
        
        // Grille subtile en gris très foncé avec effet de profondeur
        gc.setStroke(Color.rgb(25, 25, 25));
        gc.setLineWidth(1);
        for (int i = 0; i <= grid.getHeight(); i++) {
            double y = GRID_Y + i * BLOCK_SIZE;
            gc.strokeLine(GRID_X, y, GRID_X + gridWidth, y);
        }
        for (int i = 0; i <= grid.getWidth(); i++) {
            double x = GRID_X + i * BLOCK_SIZE;
            gc.strokeLine(x, GRID_Y, x, GRID_Y + gridHeight);
        }
        
        // Bordure minimaliste élégante
        gc.setStroke(Color.rgb(60, 60, 60));
        gc.setLineWidth(2);
        gc.strokeRect(GRID_X, GRID_Y, gridWidth, gridHeight);
        
        // Accent subtil cyan en haut
        gc.setStroke(Color.rgb(0, 150, 200, 0.6));
        gc.setLineWidth(2);
        gc.strokeLine(GRID_X, GRID_Y, GRID_X + gridWidth, GRID_Y);
        
        // Blocs placés
        Color[][] gridData = grid.getGrid();
        for (int row = 0; row < grid.getHeight(); row++) {
            for (int col = 0; col < grid.getWidth(); col++) {
                Color color = gridData[row][col];
                if (color != null) {
                    double x = GRID_X + col * BLOCK_SIZE;
                    double y = GRID_Y + row * BLOCK_SIZE;
                    renderBlock(x, y, BLOCK_SIZE, color);
                }
            }
        }
    }
    
    /**
     * Rend un bloc individuel avec design plat minimaliste
     */
    private void renderBlock(double x, double y, double size, Color color) {
        // Ombre portée subtile
        gc.setFill(Color.rgb(0, 0, 0, 0.2));
        gc.fillRect(x + 1.5, y + 1.5, size - 1, size - 1);
        
        // Fond principal
        gc.setFill(color);
        gc.fillRect(x + 1, y + 1, size - 2, size - 2);
        
        // Highlight discret en haut
        gc.setFill(color.brighter().deriveColor(0, 1, 1, 0.3));
        gc.fillRect(x + 2, y + 2, size - 4, 2);
        
        // Bordure élégante
        gc.setStroke(Color.rgb(0, 0, 0, 0.4));
        gc.setLineWidth(1);
        gc.strokeRect(x + 0.5, y + 0.5, size - 1, size - 1);
    }
    
    /**
     * Rend la pièce active avec rotation
     */
    private void renderCurrentPiece(ActivePiece piece) {
        if (piece == null) return;
        
        // Obtient la forme avec rotation appliquée
        int[][] shape = piece.getCurrentShape();
        Color color = piece.getPiece().getColor();
        
        // Rend chaque bloc de la pièce
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    double blockX = GRID_X + (piece.getX() + col) * BLOCK_SIZE;
                    double blockY = GRID_Y + (piece.getY() + row) * BLOCK_SIZE;
                    renderBlock(blockX, blockY, BLOCK_SIZE, color);
                }
            }
        }
    }
    
    /**
     * Rend le HUD (score, niveau, etc.) avec design moderne néon
     */
    private void renderHUD(GameContext context) {
        double y = GRID_Y;
        
        // Panel Score minimaliste
        renderModernHUDPanel("SCORE", String.valueOf(context.getScore()), HUD_X, y, Color.rgb(0, 200, 255));
        
        y += 95;
        // Panel Level minimaliste
        renderModernHUDPanel("LEVEL", String.valueOf(context.getLevel()), HUD_X, y, Color.rgb(100, 150, 200));
        
        y += 95;
        // Panel Lines minimaliste
        renderModernHUDPanel("LINES", String.valueOf(context.getLinesCleared()), HUD_X, y, Color.rgb(150, 150, 150));
        
        // Panel Contrôles minimaliste
        y += 105;
        double controlsWidth = 210;
        double controlsHeight = 145;
        
        // Ombre portée
        gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillRoundRect(HUD_X - 3, y + 2, controlsWidth, controlsHeight, 8, 8);
        
        // Fond sombre
        gc.setFill(Color.rgb(20, 20, 25));
        gc.fillRoundRect(HUD_X - 4, y, controlsWidth, controlsHeight, 8, 8);
        
        // Bordure simple élégante
        gc.setStroke(Color.rgb(80, 80, 80));
        gc.setLineWidth(2);
        gc.strokeRoundRect(HUD_X - 4, y, controlsWidth, controlsHeight, 8, 8);
        
        // Titre sobre
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.setFill(Color.rgb(180, 180, 180));
        gc.fillText("CONTROLS", HUD_X + 5, y + 22);
        
        // Contrôles avec icônes colorés
        String[][] controls = {
            {"←→", "Move"},
            {"↓", "Drop"},
            {"SPC", "Fast Drop"},
            {"↑", "Rotate"},
            {"P", "Pause"}
        };
        
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        for (int i = 0; i < controls.length; i++) {
            double lineY = y + 50 + i * 18;
            
            // Touche minimaliste
            gc.setFill(Color.rgb(35, 35, 40));
            gc.fillRoundRect(HUD_X + 5, lineY - 12, 40, 16, 4, 4);
            
            gc.setStroke(Color.rgb(100, 100, 100));
            gc.setLineWidth(1);
            gc.strokeRoundRect(HUD_X + 5, lineY - 12, 40, 16, 4, 4);
            
            gc.setFill(Color.rgb(200, 200, 200));
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 11));
            gc.fillText(controls[i][0], HUD_X + 10, lineY);
            
            // Action
            gc.setFill(Color.rgb(160, 160, 160));
            gc.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            gc.fillText(controls[i][1], HUD_X + 55, lineY);
        }
    }
    
    /**
     * Rend un panel HUD professionnel minimaliste
     */
    private void renderModernHUDPanel(String label, String value, double x, double y, Color accentColor) {
        double panelWidth = 210;
        double panelHeight = 85;
        
        // Ombre portée subtile
        gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillRoundRect(x - 3, y + 2, panelWidth, panelHeight, 8, 8);
        
        // Fond sombre
        gc.setFill(Color.rgb(20, 20, 25));
        gc.fillRoundRect(x - 4, y, panelWidth, panelHeight, 8, 8);
        
        // Bordure élégante avec accent de couleur
        gc.setStroke(accentColor.deriveColor(0, 1, 1, 0.5));
        gc.setLineWidth(2);
        gc.strokeRoundRect(x - 4, y, panelWidth, panelHeight, 8, 8);
        
        // Ligne accent en haut
        gc.setStroke(accentColor.deriveColor(0, 1, 1, 0.7));
        gc.setLineWidth(2);
        gc.strokeLine(x + 5, y + 2, x + 60, y + 2);
        
        // Label sobre
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        gc.setFill(Color.rgb(160, 160, 160));
        gc.fillText(label, x + 5, y + 22);
        
        // Valeur élégante
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        gc.setFill(Color.WHITE);
        gc.fillText(value, x + 10, y + 62);
    }
    
    /**
     * Ancien panel HUD (garde pour compatibilité)
     */
    private void renderHUDPanel(String label, String value, double x, double y, Color accentColor) {
        renderModernHUDPanel(label, value, x, y, accentColor);
    }
    
    /**
     * Rend la pièce suivante avec design minimaliste
     */
    private void renderNextPiece(PuzzlePiece nextPiece) {
        if (nextPiece == null) return;
        
        double x = HUD_X;
        double y = 540; // Position ajustée
        double panelWidth = 210;
        double panelHeight = 140;
        
        // Ombre portée
        gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillRoundRect(x - 3, y + 2, panelWidth, panelHeight, 8, 8);
        
        // Fond sombre
        gc.setFill(Color.rgb(20, 20, 25));
        gc.fillRoundRect(x - 4, y, panelWidth, panelHeight, 8, 8);
        
        // Bordure élégante
        gc.setStroke(Color.rgb(100, 150, 200, 0.6));
        gc.setLineWidth(2);
        gc.strokeRoundRect(x - 4, y, panelWidth, panelHeight, 8, 8);
        
        // Ligne accent
        gc.setStroke(Color.rgb(100, 150, 200, 0.8));
        gc.setLineWidth(2);
        gc.strokeLine(x + 5, y + 2, x + 80, y + 2);
        
        // Titre sobre
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        gc.setFill(Color.rgb(160, 160, 160));
        gc.fillText("NEXT PIECE", x + 5, y + 22);
        
        // Zone de prévisualisation avec bordure
        gc.setFill(Color.rgb(10, 10, 10));
        gc.fillRect(x + 10, y + 35, panelWidth - 30, 85);
        
        gc.setStroke(Color.rgb(60, 60, 60));
        gc.setLineWidth(2);
        gc.strokeRect(x + 10, y + 35, panelWidth - 30, 85);
        
        // Pièce centrée
        nextPiece.render(gc, x + 50, y + 50, 20);
        
        // Indicateur power-up animé
        if (nextPiece.hasSpecialEffect()) {
            gc.setFill(Color.GOLD.deriveColor(0, 1, 1, 0.3));
            gc.fillRect(x + 20, y + 125, panelWidth - 50, 10);
            
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            gc.setFill(Color.GOLD);
            gc.fillText("⭐ POWER-UP ⭐", x + 30, y + 133);
        }
    }
    
    /**
     * Rend l'overlay de pause avec design moderne
     */
    private void renderPauseOverlay() {
        // Fond avec effet blur simulé
        gc.setFill(Color.rgb(0, 0, 0, 0.75));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Panel central
        gc.setFill(Color.rgb(40, 40, 60, 0.95));
        gc.fillRoundRect(120, 200, 380, 220, 20, 20);
        
        gc.setStroke(Color.rgb(100, 200, 255));
        gc.setLineWidth(3);
        gc.strokeRoundRect(120, 200, 380, 220, 20, 20);
        
        // Titre avec ombre
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 52));
        gc.fillText("PAUSED", 183, 283);
        
        gc.setFill(Color.rgb(100, 200, 255));
        gc.fillText("PAUSED", 180, 280);
        
        // Boutons
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        gc.setFill(Color.WHITE);
        gc.fillText("Press P to Resume", 200, 340);
        gc.fillText("Press M for Menu", 205, 380);
    }
    
    /**
     * Rend l'overlay game over avec design moderne
     */
    private void renderGameOverOverlay(GameContext context) {
        // Fond sombre
        gc.setFill(Color.rgb(0, 0, 0, 0.85));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Panel principal
        gc.setFill(Color.rgb(40, 40, 60, 0.95));
        gc.fillRoundRect(100, 150, 450, 380, 20, 20);
        
        gc.setStroke(Color.rgb(255, 100, 100));
        gc.setLineWidth(3);
        gc.strokeRoundRect(100, 150, 450, 380, 20, 20);
        
        // Titre avec ombre
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 52));
        gc.fillText("GAME OVER", 143, 223);
        
        gc.setFill(Color.rgb(255, 100, 100));
        gc.fillText("GAME OVER", 140, 220);
        
        // Panneau de stats
        double statsY = 280;
        gc.setFill(Color.rgb(30, 30, 45));
        gc.fillRoundRect(140, statsY, 360, 140, 10, 10);
        
        // Stats
        gc.setFill(Color.rgb(100, 200, 255));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        gc.fillText("Final Score:", 180, statsY + 40);
        gc.setFill(Color.WHITE);
        gc.fillText(String.valueOf(context.getScore()), 350, statsY + 40);
        
        gc.setFill(Color.rgb(200, 150, 100));
        gc.fillText("Level:", 180, statsY + 75);
        gc.setFill(Color.WHITE);
        gc.fillText(String.valueOf(context.getLevel()), 350, statsY + 75);
        
        gc.setFill(Color.rgb(150, 100, 200));
        gc.fillText("Lines:", 180, statsY + 110);
        gc.setFill(Color.WHITE);
        gc.fillText(String.valueOf(context.getLinesCleared()), 350, statsY + 110);
        
        // Boutons
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        gc.setFill(Color.rgb(200, 200, 200));
        gc.fillText("Press R to Restart", 220, 470);
        gc.fillText("Press M for Menu", 225, 500);
    }
}
