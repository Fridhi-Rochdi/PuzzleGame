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
    
    private static final double BLOCK_SIZE = 30;
    private static final double GRID_X = 50;
    private static final double GRID_Y = 50;
    private static final double HUD_X = 400;
    
    public GameView(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.gameManager = GameManager.getInstance();
    }
    
    /**
     * Rend la vue complète
     */
    public void render() {
        // Efface le canvas avec dégradé
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
     * Rend le menu principal style Tetris moderne avec effets néon
     */
    private void renderMenu() {
        // Grille décorative en arrière-plan
        gc.setStroke(Color.rgb(30, 30, 30));
        gc.setLineWidth(1);
        for (int i = 0; i < canvas.getWidth(); i += 30) {
            gc.strokeLine(i, 0, i, canvas.getHeight());
        }
        for (int i = 0; i < canvas.getHeight(); i += 30) {
            gc.strokeLine(0, i, canvas.getWidth(), i);
        }
        
        // Logo "TETRIS" style avec blocs colorés
        double logoX = 80;
        double logoY = 80;
        Color[] tetrisColors = {
            Color.CYAN, Color.YELLOW, Color.MAGENTA, 
            Color.LIME, Color.RED, Color.BLUE, Color.ORANGE
        };
        
        // Titre avec effet néon multicouleur
        String title = "PUZZLE GAME";
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 56));
        for (int i = 0; i < title.length(); i++) {
            // Effet glow
            gc.setFill(tetrisColors[i % tetrisColors.length].deriveColor(0, 1, 1, 0.3));
            gc.fillText(String.valueOf(title.charAt(i)), logoX + i * 32 - 2, logoY - 2);
            gc.fillText(String.valueOf(title.charAt(i)), logoX + i * 32 + 2, logoY + 2);
            
            // Texte principal
            gc.setFill(tetrisColors[i % tetrisColors.length]);
            gc.fillText(String.valueOf(title.charAt(i)), logoX + i * 32, logoY);
        }
        
        // Sous-titre avec effet néon cyan
        gc.setFill(Color.CYAN.deriveColor(0, 1, 1, 0.4));
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        gc.fillText("DESIGN PATTERNS PROJECT", 150, logoY + 45);
        
        gc.setFill(Color.CYAN);
        gc.fillText("DESIGN PATTERNS PROJECT", 150, logoY + 43);
        
        // Blocs décoratifs style Tetris
        drawDecorativeBlocks(50, 200, Color.CYAN);
        drawDecorativeBlocks(500, 200, Color.MAGENTA);
        drawDecorativeBlocks(50, 500, Color.YELLOW);
        drawDecorativeBlocks(500, 500, Color.LIME);
        
        // Zone des boutons centrée
        double buttonCenterX = canvas.getWidth() / 2;
        double buttonWidth = 340;
        double buttonHeight = 55;
        
        // Bouton START avec effet néon
        double startY = 200;
        double startX = buttonCenterX - buttonWidth / 2;
        
        // Ombre/Glow externe
        gc.setFill(Color.LIME.deriveColor(0, 1, 1, 0.4));
        gc.fillRect(startX - 5, startY - 5, buttonWidth + 10, buttonHeight + 10);
        
        // Fond du bouton
        gc.setFill(Color.rgb(0, 80, 0));
        gc.fillRect(startX, startY, buttonWidth, buttonHeight);
        
        // Double bordure néon
        gc.setStroke(Color.LIME);
        gc.setLineWidth(5);
        gc.strokeRect(startX, startY, buttonWidth, buttonHeight);
        
        gc.setStroke(Color.LIME.brighter());
        gc.setLineWidth(2);
        gc.strokeRect(startX + 3, startY + 3, buttonWidth - 6, buttonHeight - 6);
        
        // Barre décorative intérieure
        gc.setFill(Color.LIME.deriveColor(0, 1, 1, 0.5));
        gc.fillRect(startX + 10, startY + 10, buttonWidth - 20, 3);
        
        // Texte parfaitement centré avec effet glow
        String startText = "PRESS ENTER";
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        double startTextWidth = gc.getFont().getSize() * startText.length() * 0.58; // Approximation
        double startTextX = startX + (buttonWidth - startTextWidth) / 2;
        
        gc.setFill(Color.LIME.deriveColor(0, 1, 1, 0.6));
        gc.fillText(startText, startTextX + 2, startY + 38);
        
        gc.setFill(Color.WHITE);
        gc.fillText(startText, startTextX, startY + 36);
        
        // Bouton QUIT avec effet néon
        double quitY = 280;
        double quitX = buttonCenterX - buttonWidth / 2;
        
        // Ombre/Glow externe
        gc.setFill(Color.RED.deriveColor(0, 1, 1, 0.4));
        gc.fillRect(quitX - 5, quitY - 5, buttonWidth + 10, buttonHeight + 10);
        
        // Fond du bouton
        gc.setFill(Color.rgb(80, 0, 0));
        gc.fillRect(quitX, quitY, buttonWidth, buttonHeight);
        
        // Double bordure néon
        gc.setStroke(Color.RED);
        gc.setLineWidth(5);
        gc.strokeRect(quitX, quitY, buttonWidth, buttonHeight);
        
        gc.setStroke(Color.RED.brighter());
        gc.setLineWidth(2);
        gc.strokeRect(quitX + 3, quitY + 3, buttonWidth - 6, buttonHeight - 6);
        
        // Barre décorative intérieure
        gc.setFill(Color.RED.deriveColor(0, 1, 1, 0.5));
        gc.fillRect(quitX + 10, quitY + 10, buttonWidth - 20, 3);
        
        // Texte parfaitement centré avec effet glow
        String quitText = "PRESS ESC";
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        double quitTextWidth = gc.getFont().getSize() * quitText.length() * 0.58; // Approximation
        double quitTextX = quitX + (buttonWidth - quitTextWidth) / 2;
        
        gc.setFill(Color.RED.deriveColor(0, 1, 1, 0.6));
        gc.fillText(quitText, quitTextX + 2, quitY + 38);
        
        gc.setFill(Color.WHITE);
        gc.fillText(quitText, quitTextX, quitY + 36);
    }
    
    /**
     * Dessine des blocs décoratifs style Tetris
     */
    private void drawDecorativeBlocks(double x, double y, Color color) {
        double blockSize = 15;
        // Forme en L
        for (int i = 0; i < 3; i++) {
            renderBlock(x + i * blockSize, y, blockSize, color);
        }
        renderBlock(x, y + blockSize, blockSize, color);
    }
    
    /**
     * Rend un bouton de menu avec style
     */
    private void renderMenuButton(String text, double x, double y, Color color) {
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        double width = 260;
        double height = 45;
        double buttonX = x - 10;
        double buttonY = y - 30;
        
        // Ombre
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRoundRect(buttonX + 3, buttonY + 3, width, height, 10, 10);
        
        // Fond du bouton
        gc.setFill(color);
        gc.fillRoundRect(buttonX, buttonY, width, height, 10, 10);
        
        // Bordure
        gc.setStroke(color.brighter());
        gc.setLineWidth(2);
        gc.strokeRoundRect(buttonX, buttonY, width, height, 10, 10);
        
        // Texte
        gc.setFill(Color.WHITE);
        gc.fillText(text, x, y);
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
     * Rend la grille de jeu style Tetris classique
     */
    private void renderGrid(GameGrid grid) {
        double gridWidth = grid.getWidth() * BLOCK_SIZE;
        double gridHeight = grid.getHeight() * BLOCK_SIZE;
        
        // Fond noir pur de la grille
        gc.setFill(Color.BLACK);
        gc.fillRect(GRID_X, GRID_Y, gridWidth, gridHeight);
        
        // Grille subtile en gris très foncé
        gc.setStroke(Color.rgb(30, 30, 30));
        gc.setLineWidth(1);
        for (int i = 0; i <= grid.getHeight(); i++) {
            double y = GRID_Y + i * BLOCK_SIZE;
            gc.strokeLine(GRID_X, y, GRID_X + gridWidth, y);
        }
        for (int i = 0; i <= grid.getWidth(); i++) {
            double x = GRID_X + i * BLOCK_SIZE;
            gc.strokeLine(x, GRID_Y, x, GRID_Y + gridHeight);
        }
        
        // Bordure blanche épaisse comme le vrai Tetris
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(4);
        gc.strokeRect(GRID_X - 2, GRID_Y - 2, gridWidth + 4, gridHeight + 4);
        
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
     * Rend un bloc individuel style Tetris classique
     */
    private void renderBlock(double x, double y, double size, Color color) {
        // Bloc coloré simple
        gc.setFill(color);
        gc.fillRect(x + 1, y + 1, size - 2, size - 2);
        
        // Effet 3D simple mais efficace
        // Highlight en haut et à gauche
        gc.setStroke(color.brighter().brighter());
        gc.setLineWidth(3);
        gc.strokeLine(x + 2, y + 2, x + size - 3, y + 2);
        gc.strokeLine(x + 2, y + 2, x + 2, y + size - 3);
        
        // Ombre en bas et à droite
        gc.setStroke(color.darker().darker());
        gc.setLineWidth(3);
        gc.strokeLine(x + size - 2, y + 3, x + size - 2, y + size - 2);
        gc.strokeLine(x + 3, y + size - 2, x + size - 2, y + size - 2);
        
        // Bordure noire fine
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(x + 0.5, y + 0.5, size - 1, size - 1);
    }
    
    /**
     * Rend la pièce active
     */
    private void renderCurrentPiece(ActivePiece piece) {
        if (piece == null) return;
        
        double x = GRID_X + piece.getX() * BLOCK_SIZE;
        double y = GRID_Y + piece.getY() * BLOCK_SIZE;
        
        piece.getPiece().render(gc, x, y, BLOCK_SIZE);
    }
    
    /**
     * Rend le HUD (score, niveau, etc.) avec design moderne néon
     */
    private void renderHUD(GameContext context) {
        double y = GRID_Y;
        
        // Panel Score avec effet néon cyan
        renderModernHUDPanel("SCORE", String.valueOf(context.getScore()), HUD_X, y, Color.CYAN);
        
        y += 95;
        // Panel Level avec effet néon orange
        renderModernHUDPanel("LEVEL", String.valueOf(context.getLevel()), HUD_X, y, Color.ORANGE);
        
        y += 95;
        // Panel Lines avec effet néon magenta
        renderModernHUDPanel("LINES", String.valueOf(context.getLinesCleared()), HUD_X, y, Color.MAGENTA);
        
        // Panel Contrôles style moderne
        y += 105;
        double controlsWidth = 210;
        double controlsHeight = 145;
        
        // Effet glow
        gc.setFill(Color.LIME.deriveColor(0, 1, 1, 0.2));
        gc.fillRect(HUD_X - 8, y - 3, controlsWidth + 6, controlsHeight + 6);
        
        // Fond noir
        gc.setFill(Color.BLACK);
        gc.fillRect(HUD_X - 5, y, controlsWidth, controlsHeight);
        
        // Double bordure néon
        gc.setStroke(Color.LIME);
        gc.setLineWidth(4);
        gc.strokeRect(HUD_X - 5, y, controlsWidth, controlsHeight);
        
        gc.setStroke(Color.LIME.brighter());
        gc.setLineWidth(1.5);
        gc.strokeRect(HUD_X - 3, y + 2, controlsWidth - 4, controlsHeight - 4);
        
        // Barre décorative
        gc.setFill(Color.LIME.deriveColor(0, 1, 1, 0.5));
        gc.fillRect(HUD_X, y + 5, controlsWidth - 10, 3);
        
        // Titre avec glow
        gc.setFill(Color.LIME.deriveColor(0, 1, 1, 0.4));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gc.fillText("⌨ CONTROLS", HUD_X + 6, y + 27);
        
        gc.setFill(Color.WHITE);
        gc.fillText("⌨ CONTROLS", HUD_X + 5, y + 25);
        
        // Contrôles avec icônes colorés
        String[][] controls = {
            {"←→", "Move"},
            {"↓", "Drop"},
            {"SPC", "Fast Drop"},
            {"↑", "Rotate"},
            {"P", "Pause"}
        };
        
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        for (int i = 0; i < controls.length; i++) {
            double lineY = y + 50 + i * 18;
            
            // Touche avec fond
            gc.setFill(Color.rgb(40, 40, 40));
            gc.fillRect(HUD_X + 5, lineY - 12, 40, 16);
            
            gc.setStroke(Color.LIME.deriveColor(0, 0.8, 0.8, 1));
            gc.setLineWidth(1);
            gc.strokeRect(HUD_X + 5, lineY - 12, 40, 16);
            
            gc.setFill(Color.LIME);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            gc.fillText(controls[i][0], HUD_X + 10, lineY);
            
            // Action
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            gc.fillText(controls[i][1], HUD_X + 55, lineY);
        }
    }
    
    /**
     * Rend un panel HUD moderne avec effet néon
     */
    private void renderModernHUDPanel(String label, String value, double x, double y, Color accentColor) {
        double panelWidth = 210;
        double panelHeight = 80;
        
        // Effet glow extérieur
        gc.setFill(accentColor.deriveColor(0, 1, 1, 0.2));
        gc.fillRect(x - 8, y - 8, panelWidth + 6, panelHeight + 6);
        
        // Fond noir avec bordure intérieure
        gc.setFill(Color.BLACK);
        gc.fillRect(x - 5, y - 5, panelWidth, panelHeight);
        
        // Double bordure pour effet néon
        gc.setStroke(accentColor);
        gc.setLineWidth(4);
        gc.strokeRect(x - 5, y - 5, panelWidth, panelHeight);
        
        gc.setStroke(accentColor.brighter());
        gc.setLineWidth(1.5);
        gc.strokeRect(x - 3, y - 3, panelWidth - 4, panelHeight - 4);
        
        // Barre décorative en haut
        gc.setFill(accentColor.deriveColor(0, 1, 1, 0.5));
        gc.fillRect(x, y, panelWidth - 10, 3);
        
        // Label avec effet glow
        gc.setFill(accentColor.deriveColor(0, 1, 1, 0.4));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        gc.fillText(label, x + 6, y + 22);
        
        gc.setFill(Color.WHITE);
        gc.fillText(label, x + 5, y + 20);
        
        // Valeur avec effet néon prononcé
        gc.setFill(accentColor.deriveColor(0, 1, 1, 0.5));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 38));
        gc.fillText(value, x + 12, y + 62);
        
        gc.setFill(accentColor);
        gc.fillText(value, x + 10, y + 60);
    }
    
    /**
     * Ancien panel HUD (garde pour compatibilité)
     */
    private void renderHUDPanel(String label, String value, double x, double y, Color accentColor) {
        renderModernHUDPanel(label, value, x, y, accentColor);
    }
    
    /**
     * Rend la pièce suivante avec effet néon
     */
    private void renderNextPiece(PuzzlePiece nextPiece) {
        if (nextPiece == null) return;
        
        double x = HUD_X;
        double y = 540; // Position ajustée
        double panelWidth = 210;
        double panelHeight = 140;
        
        // Effet glow extérieur
        gc.setFill(Color.YELLOW.deriveColor(0, 1, 1, 0.2));
        gc.fillRect(x - 8, y - 3, panelWidth + 6, panelHeight + 6);
        
        // Fond noir
        gc.setFill(Color.BLACK);
        gc.fillRect(x - 5, y, panelWidth, panelHeight);
        
        // Double bordure néon jaune
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(4);
        gc.strokeRect(x - 5, y, panelWidth, panelHeight);
        
        gc.setStroke(Color.YELLOW.brighter());
        gc.setLineWidth(1.5);
        gc.strokeRect(x - 3, y + 2, panelWidth - 4, panelHeight - 4);
        
        // Barre décorative
        gc.setFill(Color.YELLOW.deriveColor(0, 1, 1, 0.5));
        gc.fillRect(x, y + 5, panelWidth - 10, 3);
        
        // Titre avec glow
        gc.setFill(Color.YELLOW.deriveColor(0, 1, 1, 0.4));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gc.fillText("▶ NEXT PIECE", x + 6, y + 27);
        
        gc.setFill(Color.WHITE);
        gc.fillText("▶ NEXT PIECE", x + 5, y + 25);
        
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
