package it.unicam.cs.mpgc.rpg125944.model.persistence;

import com.google.gson.JsonObject;
import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;

/**
 * Sa tradurre un tipo di {@link CombatAction} da e verso JSON.
 * Per salvare una nuova azione di combattimento basta scrivere un nuovo codec e registrarlo:
 * ne' il repository ne' l'adapter vanno modificati.
 */
public interface CombatActionCodec {

    /** Nome con cui il tipo di azione compare nel file di salvataggio. */
    String getType();

    /** Indica se questo codec sa salvare l'azione indicata. */
    boolean supports(CombatAction action);

    /** Scrive i dati propri dell'azione (il tipo viene aggiunto dall'adapter). */
    JsonObject encode(CombatAction action);

    /** Ricostruisce l'azione dai dati salvati. */
    CombatAction decode(JsonObject json);
}
