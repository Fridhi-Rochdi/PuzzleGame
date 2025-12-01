package com.designpatterns.puzzle.patterns.composite;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Composite Pattern - Composite pour grouper plusieurs composants
 * Peut contenir d'autres composants (feuilles ou composites)
 */
public class GameComposite implements GameComponent {
    
    private List<GameComponent> children;
    private String name;
    
    public GameComposite(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }
    
    @Override
    public void update(double deltaTime) {
        // Met à jour tous les enfants
        for (GameComponent child : children) {
            child.update(deltaTime);
        }
    }
    
    @Override
    public void render(GraphicsContext gc) {
        // Rend tous les enfants
        for (GameComponent child : children) {
            child.render(gc);
        }
    }
    
    @Override
    public void add(GameComponent component) {
        children.add(component);
    }
    
    @Override
    public void remove(GameComponent component) {
        children.remove(component);
    }
    
    @Override
    public GameComponent getChild(int index) {
        if (index >= 0 && index < children.size()) {
            return children.get(index);
        }
        throw new IndexOutOfBoundsException("Invalid child index: " + index);
    }
    
    @Override
    public int getChildCount() {
        return children.size();
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * Retire tous les enfants
     */
    public void clear() {
        children.clear();
    }
}
