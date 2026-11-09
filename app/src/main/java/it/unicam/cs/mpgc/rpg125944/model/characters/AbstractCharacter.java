package it.unicam.cs.mpgc.rpg125944.model.characters;

import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;
import it.unicam.cs.mpgc.rpg125944.util.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Parte comune a tutti i personaggi "veri" (non decorati): nome, punti vita,
 * statistiche base, strategia di attacco e notifica agli osservatori.
 * Le sottoclassi si distinguono per il ruolo che hanno nello scontro.
 */
public abstract class AbstractCharacter implements Character {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int baseAttack;
    private final int baseDefense;
    private final CombatAction combatAction;
    private final List<Observer> observers = new ArrayList<>();

    /** Crea un personaggio con tutti i punti vita. */
    protected AbstractCharacter(String name, int maxHp, int baseAttack, int baseDefense, CombatAction combatAction) {
        this(name, maxHp, maxHp, baseAttack, baseDefense, combatAction);
    }

    /** Crea un personaggio con i punti vita indicati (usato per ripristinare un salvataggio). */
    protected AbstractCharacter(
            String name, int maxHp, int hp, int baseAttack, int baseDefense, CombatAction combatAction
    ) {
        this.name = Objects.requireNonNull(name, "il nome non puo' essere null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("il nome non puo' essere vuoto");
        }
        if (maxHp <= 0) {
            throw new IllegalArgumentException("gli HP massimi devono essere positivi");
        }
        if (hp < 0 || hp > maxHp) {
            throw new IllegalArgumentException("gli HP devono essere compresi tra zero e gli HP massimi");
        }
        if (baseAttack < 0 || baseDefense < 0) {
            throw new IllegalArgumentException("le statistiche di combattimento non possono essere negative");
        }
        this.maxHp = maxHp;
        this.hp = hp;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.combatAction = Objects.requireNonNull(combatAction, "combatAction non puo' essere null");
    }

    /** Ruolo del personaggio nello scontro: ogni sottoclasse concreta lo dichiara. */
    public abstract CharacterRole getRole();

    public void addObserver(Observer observer) {
        this.observers.add(Objects.requireNonNull(observer, "l'osservatore non puo' essere null"));
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
            throw new IllegalArgumentException("il danno non puo' essere negativo");
        }
        int actualDamage = Math.min(hp, Math.max(0, damage - getDefense()));
        hp -= actualDamage;
        notifyObservers();
        return actualDamage;
    }

    @Override
    public int heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("la cura non puo' essere negativa");
        }
        int restoredHp = Math.min(amount, maxHp - hp);
        hp += restoredHp;
        notifyObservers();
        return restoredHp;
    }

    @Override
    public boolean isAlive() {
        return hp > 0;
    }

    @Override
    public CombatAction getCombatAction() {
        return combatAction;
    }
}
