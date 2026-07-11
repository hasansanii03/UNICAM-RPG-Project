package it.unicam.cs.mpgc.rpg125944.model.characters;

import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;

/**
 * Classe astratta base per il Decorator Pattern.
 * Implementa Character e contiene un riferimento al Character da decorare.
 */
public abstract class CharacterDecorator implements Character {
    
    // Protected per permettere alle sottoclassi (le armi/armature) di accedervi
    protected Character character;

    public CharacterDecorator(Character character) {
        this.character = character;
    }

    @Override
    public String getName() {
        return character.getName();
    }

    @Override
    public int getHp() {
        return character.getHp();
    }

    @Override
    public int getMaxHp() {
        return character.getMaxHp();
    }

    @Override
    public int getAttackPower() {
        return character.getAttackPower();
    }

    @Override
    public int getDefense() {
        return character.getDefense();
    }

    @Override
    public void takeDamage(int damage) {
        character.takeDamage(damage);
    }

    @Override
    public boolean isAlive() {
        return character.isAlive();
    }

    @Override
    public CombatAction getCombatAction() {
        return character.getCombatAction();
    }
}