package it.unicam.cs.mpgc.rpg125944; 
import it.unicam.cs.mpgc.rpg125944.model.characters.BaseHero;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.characters.Weapon;

public class App {
    public static void main(String[] args) {
        System.out.println("--- Test Iniziale Auto-Battler ---");

        // 1. Creiamo un eroe "nudo"
        Character hero = new BaseHero("Arthur", 100, 10, 5);
        
        System.out.println("Personaggio creato:");
        System.out.println("Nome: " + hero.getName());
        System.out.println("Attacco: " + hero.getAttackPower());
        System.out.println("Difesa: " + hero.getDefense());
        
        System.out.println("\n-----------------------------------\n");

        // 2. Equipaggiamo l'eroe usando il Decorator!
        // Notare come riassegniamo la variabile hero. Il Decorator "avvolge" l'oggetto originale.
        hero = new Weapon(hero, "Spada Lunga", 15);
        
        System.out.println("Equipaggiamento aggiunto!");
        System.out.println("Nuovo Nome: " + hero.getName());
        System.out.println("Nuovo Attacco: " + hero.getAttackPower()); // Dovrebbe essere 10 (base) + 15 (spada) = 25
        System.out.println("Difesa invariata: " + hero.getDefense()); // Rimane 5
        
        System.out.println("\n-----------------------------------\n");

        // 3. Testiamo il sistema di danni
        System.out.println("Arthur subisce un attacco da 20 danni!");
        hero.takeDamage(20);
        // Il danno dovrebbe essere 20 - 5 (difesa) = 15. HP finali = 100 - 15 = 85.
        
        System.out.println("\nStato finale: Vivo? " + hero.isAlive());
    }
}