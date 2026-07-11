package it.unicam.cs.mpgc.rpg125944;

import it.unicam.cs.mpgc.rpg125944.model.characters.BaseHero;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.characters.Weapon;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;

public class App {
    public static void main(String[] args) {
        System.out.println("--- Test Iniziale Auto-Battler ---");

        // Abbiamo aggiunto "new BasicAttack()" al costruttore!
        Character hero = new BaseHero("Arthur", 100, 10, 5, new BasicAttack());
        
        System.out.println("Personaggio creato: " + hero.getName());
        
        // Equipaggiamo l'eroe
        hero = new Weapon(hero, "Spada Lunga", 15);
        System.out.println("Arma equipaggiata! Nuovo Nome: " + hero.getName());
        System.out.println("Nuovo Attacco: " + hero.getAttackPower());
        
        // Proviamo a usare lo Strategy Pattern per attaccare un finto nemico
        Character goblin = new BaseHero("Goblin", 30, 5, 2, new BasicAttack());
        
        System.out.println("\n--- Inizio Combattimento ---");
        // L'eroe usa la sua azione di combattimento contro il goblin
        hero.getCombatAction().execute(hero, goblin);
    }
}