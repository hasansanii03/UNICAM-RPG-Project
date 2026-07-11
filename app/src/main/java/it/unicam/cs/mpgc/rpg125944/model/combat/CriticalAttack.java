package it.unicam.cs.mpgc.rpg125944.model.combat;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import java.util.Random;

/**
 * Strategia concreta: Attacco che ha una probabilità di infliggere danno doppio.
 */
public class CriticalAttack implements CombatAction {

    private final double criticalChance;
    private final Random random;

    public CriticalAttack(double criticalChance) {
        this.criticalChance = criticalChance;
        this.random = new Random();
    }

    @Override
    public void execute(Character attacker, Character target) {
        int damage = attacker.getAttackPower();
        boolean isCritical = random.nextDouble() <= criticalChance;

        if (isCritical) {
            damage *= 2;
            System.out.println("COLPO CRITICO! " + attacker.getName() + " colpisce duramente " + target.getName() + "!");
        } else {
            System.out.println(attacker.getName() + " attacca " + target.getName() + ".");
        }

        target.takeDamage(damage);
    }
}