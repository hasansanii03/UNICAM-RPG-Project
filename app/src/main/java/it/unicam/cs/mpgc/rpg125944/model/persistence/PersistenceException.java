package it.unicam.cs.mpgc.rpg125944.model.persistence;

/** Segnala un errore durante la lettura o la scrittura dei dati di gioco persistenti. */
public class PersistenceException extends RuntimeException {
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(String message) {
        super(message);
    }
}
