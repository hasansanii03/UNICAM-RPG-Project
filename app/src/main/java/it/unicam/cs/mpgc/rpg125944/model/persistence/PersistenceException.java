package it.unicam.cs.mpgc.rpg125944.model.persistence;

/** Signals an error while reading or writing persistent game data. */
public class PersistenceException extends RuntimeException {
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(String message) {
        super(message);
    }
}
