package it.unicam.cs.mpgc.rpg125944.model.combat;

/** Esito immutabile di un'azione di combattimento. */
public record ActionResult(int rawDamage, int damageDealt, boolean critical) {
}
