package it.unicam.cs.mpgc.rpg125944.model.combat;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;

/**
 * Azione che il giocatore puo' scegliere durante il proprio turno.
 * Per aggiungere una nuova azione basta scrivere una nuova implementazione:
 * {@link Battle} non va modificata (principio Open/Closed).
 */
public interface PlayerAction {

    /** Nome mostrato al giocatore (ad esempio sul pulsante). */
    String getName();

    /** Indica se l'azione si puo' eseguire nello stato attuale dello scontro. */
    boolean isAvailable(Battle battle);

    /** Esegue l'azione sullo scontro e ne restituisce l'effetto. */
    ActionOutcome execute(Battle battle);

    /**
     * Restituisce l'eroe cosi' come viene colpito dalla risposta del nemico in questo turno.
     * Di default e' l'eroe stesso; un'azione difensiva puo' restituire un decorator.
     */
    default Character protect(Character hero) {
        return hero;
    }
}
