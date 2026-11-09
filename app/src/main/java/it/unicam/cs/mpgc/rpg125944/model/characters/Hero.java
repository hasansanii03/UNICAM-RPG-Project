package it.unicam.cs.mpgc.rpg125944.model.characters;

import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;

/** Personaggio controllato dal giocatore. */
public class Hero extends AbstractCharacter {

    public Hero(String name, int maxHp, int baseAttack, int baseDefense, CombatAction combatAction) {
        super(name, maxHp, baseAttack, baseDefense, combatAction);
    }

    public Hero(String name, int maxHp, int hp, int baseAttack, int baseDefense, CombatAction combatAction) {
        super(name, maxHp, hp, baseAttack, baseDefense, combatAction);
    }

    @Override
    public CharacterRole getRole() {
        return CharacterRole.HERO;
    }
}
