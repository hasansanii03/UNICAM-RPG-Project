package it.unicam.cs.mpgc.rpg125944.model.persistence;

/** Raw serializable representation of a combatant. */
public class CharacterSaveData {
    public String name;
    public int hp;
    public int maxHp;
    public int attack;
    public int defense;
    public String actionType;
    public double criticalChance;
}
