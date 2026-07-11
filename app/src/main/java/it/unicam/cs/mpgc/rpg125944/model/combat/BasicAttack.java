package it.unicam.cs.mpgc.rpg125944.model.combat;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;

/**
 * Strategia concreta: Attacco fisico standard.
 * Il danno inflitto è pari alla potenza d'attacco dell'attaccante.
 */
public class BasicAttack implements CombatAction {

    @Override
    public void execute(Character attacker, Character target) {
        System.out.println(attacker.getName() + " sferra un attacco contro " + target.getName() + "!");
        
        // Il calcolo della difesa viene gestito direttamente dal metodo takeDamage del bersaglio
        target.takeDamage(attacker.getAttackPower());
    }
}