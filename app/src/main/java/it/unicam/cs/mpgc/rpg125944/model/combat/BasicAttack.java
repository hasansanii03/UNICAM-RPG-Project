package it.unicam.cs.mpgc.rpg125944.model.combat;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;

/**
 * Strategia concreta: Attacco fisico standard.
 * Il danno inflitto è pari alla potenza d'attacco dell'attaccante.
 */
public class BasicAttack implements CombatAction {

    @Override
    public ActionResult execute(Character attacker, Character target) {
        int rawDamage = attacker.getAttackPower();
        int damageDealt = target.takeDamage(rawDamage);
        return new ActionResult(rawDamage, damageDealt, false);
    }
}
