package it.unicam.cs.mpgc.rpg125944.model.combat;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;

import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Strategia concreta: Attacco che ha una probabilità di infliggere danno doppio.
 */
public class CriticalAttack implements CombatAction {

    private final double criticalChance;
    private final RandomGenerator random;

    public CriticalAttack(double criticalChance) {
        this(criticalChance, RandomGenerator.getDefault());
    }

    public CriticalAttack(double criticalChance, RandomGenerator random) {
        if (criticalChance < 0 || criticalChance > 1) {
            throw new IllegalArgumentException("criticalChance must be between zero and one");
        }
        this.criticalChance = criticalChance;
        this.random = Objects.requireNonNull(random, "random must not be null");
    }

    @Override
    public ActionResult execute(Character attacker, Character target) {
        int rawDamage = attacker.getAttackPower();
        boolean isCritical = random.nextDouble() < criticalChance;

        if (isCritical) {
            rawDamage *= 2;
        }
        int damageDealt = target.takeDamage(rawDamage);
        return new ActionResult(rawDamage, damageDealt, isCritical);
    }
}
