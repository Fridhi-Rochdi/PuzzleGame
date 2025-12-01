package com.designpatterns.puzzle.patterns.composite;

import javafx.scene.canvas.GraphicsContext;


public interface GameComponent {
    
   
    void update(double deltaTime);
    
    
    void render(GraphicsContext gc);
    
    
    void add(GameComponent component);
    
    
    void remove(GameComponent component);
    
   
    GameComponent getChild(int index);
    
    
    int getChildCount();
}
