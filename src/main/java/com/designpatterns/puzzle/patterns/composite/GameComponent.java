package com.designpatterns.puzzle.patterns.composite;

import javafx.scene.canvas.GraphicsContext;

/**
 * Composite Pattern - Interface Component
 * Composant de base pour tous les éléments du jeu
 */
public interface GameComponent {
    
    /**
     * Met à jour le composant
     */
    void update(double deltaTime);
    
    /**
     * Rend le composant graphiquement
     */
    void render(GraphicsContext gc);
    
    /**
     * Ajoute un composant enfant (pour les composites)
     */
    void add(GameComponent component);
    
    /**
     * Retire un composant enfant (pour les composites)
     */
    void remove(GameComponent component);
    
    /**
     * Obtient un composant enfant par index (pour les composites)
     */
    GameComponent getChild(int index);
    
    /**
     * Retourne le nombre d'enfants
     */
    int getChildCount();
}
