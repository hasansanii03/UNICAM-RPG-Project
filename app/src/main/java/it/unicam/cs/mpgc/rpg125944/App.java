package it.unicam.cs.mpgc.rpg125944;

import it.unicam.cs.mpgc.rpg125944.model.characters.BaseHero;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.characters.Weapon;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import it.unicam.cs.mpgc.rpg125944.model.combat.BattleEngine;
import it.unicam.cs.mpgc.rpg125944.model.factories.EnemyFactory;

public class App {
    public static void main(String[] args) {
        System.out.println("Benvenuto nell'Auto-Battler di UNICAM!");

        // 1. Creazione dell'Eroe e dell'Equipaggiamento
        Character hero = new BaseHero("Arthur", 150, 15, 5, new BasicAttack());
        hero = new Weapon(hero, "Spada Lunga", 20); // Danno totale: 35
        
        // 2. Inizializzazione del motore di gioco
        EnemyFactory factory = new EnemyFactory();
        BattleEngine engine = new BattleEngine();
        
        int currentWave = 1;
        boolean gameRunning = true;
        
        // 3. Ciclo principale di gioco (Le Ondate)
        while (gameRunning) {
            System.out.println("\n==================================");
            System.out.println("🌊 PREPARAZIONE ONDATA " + currentWave + " 🌊");
            System.out.println("==================================");
            
            // Generiamo un nemico per questa ondata
            Character enemy = factory.createRandomEnemy(currentWave);
            
            // Eseguiamo la battaglia
            boolean heroWon = engine.fight(hero, enemy);
            
            if (heroWon) {
                System.out.println("🎉 Hai superato l'ondata " + currentWave + "!");
                System.out.println("HP rimanenti di " + hero.getName() + ": " + hero.getHp());
                currentWave++;
                
                // Simoliamo una piccola pozione di cura tra un'ondata e l'altra (temporaneo)
                // In futuro creeremo un sistema di inventario vero e proprio
                System.out.println("*(Arthur riposa e si prepara per il prossimo scontro...)*");
            } else {
                System.out.println("GAME OVER! Sei sopravvissuto a " + (currentWave - 1) + " ondate.");
                gameRunning = false;
            }
            
            // Sicurezza per evitare cicli infiniti durante i test
            if (currentWave > 5) {
                System.out.println("👑 HAI VINTO IL GIOCO! Hai sconfitto 5 ondate!");
                gameRunning = false;
            }
        }
    }
}