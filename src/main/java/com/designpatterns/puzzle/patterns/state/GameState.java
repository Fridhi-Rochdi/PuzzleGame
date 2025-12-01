package com.designpatterns.puzzle.patterns.state;

import com.designpatterns.puzzle.core.GameContext;

/**
 * State Pattern - Interface principale pour les états du jeu
 * Définit le contrat que tous les états concrets doivent implémenter
 */
public interface GameState {
    
    /**
     * Entre dans cet état
     */
    void enter(GameContext context);
    
    /**
     * Met à jour l'état (appelé à chaque frame)
     */
    void update(GameContext context, double deltaTime);
    
    /**
     * Gère les entrées utilisateur
     */
    void handleInput(GameContext context, String input);
    
    /**
     * Sort de cet état
     */
    void exit(GameContext context);
    
    /**
     * Retourne le nom de l'état
     */
    String getStateName();
}
