package it.unicam.cs.mpgc.rpg125944.model.combat;

/**
 * Effetto immutabile di un'azione del giocatore: un eventuale attacco al nemico
 * e/o i punti vita recuperati dall'eroe.
 *
 * @param damageResult esito dell'attacco, oppure null se l'azione non infligge danni
 * @param hpRestored   punti vita effettivamente recuperati (zero se l'azione non cura)
 */
public record ActionOutcome(ActionResult damageResult, int hpRestored) {

    public static ActionOutcome damage(ActionResult damageResult) {
        return new ActionOutcome(damageResult, 0);
    }

    public static ActionOutcome healing(int hpRestored) {
        return new ActionOutcome(null, hpRestored);
    }
}
