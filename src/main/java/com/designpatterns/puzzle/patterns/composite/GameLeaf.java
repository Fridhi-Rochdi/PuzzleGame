package com.designpatterns.puzzle.patterns.composite;

import javafx.scene.canvas.GraphicsContext;

/**
 * Composite Pattern - Classe abstraite pour les composants simples (feuilles)
 * Les feuilles ne peuvent pas avoir d'enfants
 */
public abstract class GameLeaf implements GameComponent {
    
    @Override
    public void add(GameComponent component) {
        throw new UnsupportedOperationException("Cannot add to a leaf component");
    }
    
    @Override
    public void remove(GameComponent component) {
        throw new UnsupportedOperationException("Cannot remove from a leaf component");
    }
    
    @Override
    public GameComponent getChild(int index) {
        throw new UnsupportedOperationException("Leaf has no children");
    }
    
    @Override
    public int getChildCount() {
        return 0;
    }
    
    @Override
    public abstract void update(double deltaTime);
    
    @Override
    public abstract void render(GraphicsContext gc);
}
