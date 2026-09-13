package it.unicam.cs.mpgc.rpg125944.model.combat;

/** Immutable outcome of a combat action. */
public record ActionResult(int rawDamage, int damageDealt, boolean critical) {
}
