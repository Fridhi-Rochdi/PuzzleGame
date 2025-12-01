package com.designpatterns.puzzle.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Singleton Pattern - GameLogger
 * Système de logging centralisé pour tracer tous les événements du jeu.
 * Cette classe implémente le pattern Singleton pour garantir une seule instance
 * du logger dans toute l'application.
 */
public class GameLogger {
    
    private static GameLogger instance;
    private static final Logger logger = LogManager.getLogger(GameLogger.class);
    
    /**
     * Constructeur privé pour empêcher l'instanciation directe (Singleton)
     */
    private GameLogger() {
        logInfo("Logger initialized");
    }
    
    /**
     * Retourne l'instance unique du GameLogger (Singleton)
     * Thread-safe avec synchronisation
     */
    public static synchronized GameLogger getInstance() {
        if (instance == null) {
            instance = new GameLogger();
        }
        return instance;
    }
    
    /**
     * Log un message d'information générale
     */
    public void logInfo(String message) {
        logger.info(message);
    }
    
    /**
     * Log un changement d'état (State Pattern)
     * Format: [TIMESTAMP] [STATE] Context: FROM_STATE -> TO_STATE
     */
    public void logStateChange(String context, String fromState, String toState) {
        String message = String.format("[STATE] %s: %s -> %s", context, fromState, toState);
        logger.info(message);
    }
    
    /**
     * Log l'application d'un décorateur (Decorator Pattern)
     * Format: [TIMESTAMP] [DECORATOR] DecoratorType applied to Target
     */
    public void logDecoratorApplied(String decoratorType, String target) {
        String message = String.format("[DECORATOR] %s applied to %s", decoratorType, target);
        logger.info(message);
    }
    
    /**
     * Log le retrait d'un décorateur (Decorator Pattern)
     * Format: [TIMESTAMP] [DECORATOR] DecoratorType removed from Target
     */
    public void logDecoratorRemoved(String decoratorType, String target) {
        String message = String.format("[DECORATOR] %s removed from %s", decoratorType, target);
        logger.info(message);
    }
    
    /**
     * Log un événement de jeu important
     * Format: [TIMESTAMP] [EVENT] Event description
     */
    public void logGameEvent(String event) {
        String message = String.format("[EVENT] %s", event);
        logger.info(message);
    }
    
    /**
     * Log la création d'un objet (Factory Pattern)
     */
    public void logObjectCreation(String objectType, String details) {
        String message = String.format("[FACTORY] Created %s: %s", objectType, details);
        logger.info(message);
    }
    
    /**
     * Log une erreur
     */
    public void logError(String message, Exception e) {
        logger.error(message, e);
    }
    
    /**
     * Log un avertissement
     */
    public void logWarning(String message) {
        logger.warn(message);
    }
    
    /**
     * Log le score final
     */
    public void logFinalScore(int score, int level, int linesCleared) {
        String message = String.format("[SCORE] Final score: %d | Level: %d | Lines cleared: %d", 
                                      score, level, linesCleared);
        logger.info(message);
    }
}
