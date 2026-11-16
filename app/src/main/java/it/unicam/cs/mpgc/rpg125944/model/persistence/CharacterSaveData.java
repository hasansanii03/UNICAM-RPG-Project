package it.unicam.cs.mpgc.rpg125944.model.persistence;

import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;

/** Rappresentazione grezza serializzabile di un combattente. */
public class CharacterSaveData {
    public String name;
    public int hp;
    public int maxHp;
    public int attack;
    public int defense;
    /** Scritta e letta da {@link CombatActionAdapter}. */
    public CombatAction combatAction;
}
