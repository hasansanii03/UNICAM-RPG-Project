package it.unicam.cs.mpgc.rpg125944.model.characters;

import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;
import it.unicam.cs.mpgc.rpg125944.util.Observer;
import java.util.ArrayList;
import java.util.List;

public class BaseHero implements Character {
    
    private String name;
    private int hp;
    private int maxHp;
    private int baseAttack;
    private int baseDefense;
    private CombatAction combatAction; 
    
    // CORRETTO: Aggiunto <Observer>
    private List<Observer> observers = new ArrayList<>();

    public BaseHero(String name, int maxHp, int baseAttack, int baseDefense, CombatAction combatAction) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp; // Inizia con gli HP al massimo
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.combatAction = combatAction; 
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
    public void takeDamage(int damage) {
        // Il danno effettivo è calcolato sottraendo la difesa
        int actualDamage = Math.max(0, damage - this.getDefense());
        this.hp -= actualDamage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(this.name + " subisce " + actualDamage + " danni. HP rimanenti: " + this.hp);
        
        // CORRETTO: Avvisa l'interfaccia grafica che gli HP sono cambiati!
        notifyObservers();
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
    if(loadedHp >= 0 && loadedHp <= this.maxHp) {
        this.hp = loadedHp;
    }
}
}