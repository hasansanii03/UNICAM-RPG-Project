package it.unicam.cs.mpgc.rpg125944.model.characters;

import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;

public class BaseHero implements Character {
    
    private String name;
    private int hp;
    private int maxHp;
    private int baseAttack;
    private int baseDefense;
    private CombatAction combatAction; // NUOVO CAMPO

    public BaseHero(String name, int maxHp, int baseAttack, int baseDefense, CombatAction combatAction) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp; // Inizia con gli HP al massimo
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.combatAction = combatAction; // Inizializza l'azione di combattimento
    }
     {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp; // Inizia con gli HP al massimo
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public int getMaxHp() {
        return maxHp;
    }

    @Override
    public int getAttackPower() {
        return baseAttack;
    }

    @Override
    public int getDefense() {
        return baseDefense;
    }

    @Override
    public void takeDamage(int damage) {
        // Il danno effettivo è calcolato sottraendo la difesa
        int actualDamage = Math.max(0, damage - this.getDefense());
        this.hp -= actualDamage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(this.name + " subisce " + actualDamage + " danni. HP rimanenti: " + this.hp);
    }

    @Override
    public boolean isAlive() {
        return this.hp > 0;
    }

    @Override
    public CombatAction getCombatAction() {
        return combatAction;
    }
}