package it.unicam.cs.mpgc.rpg125944.model.characters;

import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;

public interface Character {
    String getName();
    int getHp();
    int getMaxHp();
    int getAttackPower();
    int getDefense();
    
    /**
     * Applica il danno in ingresso e restituisce il danno subito dopo le difese.
     */
    int takeDamage(int damage);

    /**
     * Ripristina punti vita e restituisce la quantita effettivamente recuperata.
     */
    int heal(int amount);
    boolean isAlive();
    
    // Questo è il metodo che mancava per far compilare tutto!
    CombatAction getCombatAction();
}
