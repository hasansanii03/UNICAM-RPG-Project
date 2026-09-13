package it.unicam.cs.mpgc.rpg125944.model.characters;

import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;
import it.unicam.cs.mpgc.rpg125944.util.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BaseHero implements Character {
    
    private final String name;
    private int hp;
    private final int maxHp;
    private final int baseAttack;
    private final int baseDefense;
    private final CombatAction combatAction;
    
    // CORRETTO: Aggiunto <Observer>
    private final List<Observer> observers = new ArrayList<>();

    public BaseHero(String name, int maxHp, int baseAttack, int baseDefense, CombatAction combatAction) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (maxHp <= 0) {
            throw new IllegalArgumentException("maxHp must be positive");
        }
        if (baseAttack < 0 || baseDefense < 0) {
            throw new IllegalArgumentException("combat statistics must not be negative");
        }
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.combatAction = Objects.requireNonNull(combatAction, "combatAction must not be null");
    }

    // CORRETTO: Rimosso il blocco duplicato di inizializzazione che c'era qui

    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
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
    public int takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("damage must not be negative");
        }
        int actualDamage = Math.min(hp, Math.max(0, damage - this.getDefense()));
        this.hp -= actualDamage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        notifyObservers();
        return actualDamage;
    }

    @Override
    public int heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("healing must not be negative");
        }
        int restoredHp = Math.min(amount, maxHp - hp);
        hp += restoredHp;
        notifyObservers();
        return restoredHp;
    }

    @Override
    public boolean isAlive() {
        return this.hp > 0;
    }

    @Override
    public CombatAction getCombatAction() {
        return combatAction;
    }
    // Usato SOLO dal sistema di persistenza per ripristinare gli HP senza passare da takeDamage
    public void setHpForLoading(int loadedHp) {
        if (loadedHp < 0 || loadedHp > this.maxHp) {
            throw new IllegalArgumentException("loadedHp must be between zero and maxHp");
        }
        this.hp = loadedHp;
        notifyObservers();
    }
}
