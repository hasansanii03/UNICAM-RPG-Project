package it.unicam.cs.mpgc.rpg125944.model.characters;

import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;

public interface Character {
    String getName();
    int getHp();
    int getMaxHp();
    int getAttackPower();
    int getDefense();
    
    /**
     * Applies incoming damage and returns the damage received after defenses.
     */
    int takeDamage(int damage);

    /**
     * Restores hit points and returns the amount effectively restored.
     */
    int heal(int amount);
    boolean isAlive();
    
    // Questo è il metodo che mancava per far compilare tutto!
    CombatAction getCombatAction();
}
