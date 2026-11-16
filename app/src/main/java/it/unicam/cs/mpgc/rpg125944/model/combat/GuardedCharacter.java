package it.unicam.cs.mpgc.rpg125944.model.combat;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.characters.CharacterDecorator;

/**
 * Decorator usato durante la difesa: dimezza il danno in ingresso
 * (arrotondando per eccesso) e delega tutto il resto al personaggio originale.
 */
class GuardedCharacter extends CharacterDecorator {
    private static final int GUARD_DAMAGE_DIVISOR = 2;

    GuardedCharacter(Character character) {
        super(character);
    }

    @Override
    public int takeDamage(int damage) {
        int reducedDamage = (damage + GUARD_DAMAGE_DIVISOR - 1) / GUARD_DAMAGE_DIVISOR;
        return character.takeDamage(reducedDamage);
    }
}
