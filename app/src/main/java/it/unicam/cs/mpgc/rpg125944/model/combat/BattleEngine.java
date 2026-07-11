package it.unicam.cs.mpgc.rpg125944.model.combat;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;

/**
 * Gestisce l'esecuzione di un singolo combattimento a turni tra due personaggi.
 */
public class BattleEngine {

    /**
     * Esegue la battaglia finché uno dei due partecipanti non muore.
     * @param hero L'eroe del giocatore.
     * @param enemy Il nemico generato.
     * @return true se l'eroe vince (sopravvive), false altrimenti.
     */
    public boolean fight(Character hero, Character enemy) {
        System.out.println("\n⚔️ BATTAGLIA INIZIATA: " + hero.getName() + " VS " + enemy.getName() + " ⚔️");
        
        int turn = 1;
        
        // Il ciclo continua finché entrambi sono vivi
        while (hero.isAlive() && enemy.isAlive()) {
            System.out.println("\n--- Turno " + turn + " ---");
            
            // Turno dell'Eroe
            hero.getCombatAction().execute(hero, enemy);
            
            // Se il nemico è morto, interrompiamo il ciclo
            if (!enemy.isAlive()) {
                System.out.println("🏆 " + enemy.getName() + " è stato sconfitto!");
                break;
            }
            
            // Turno del Nemico
            enemy.getCombatAction().execute(enemy, hero);
            
            // Se l'eroe è morto
            if (!hero.isAlive()) {
                System.out.println("💀 " + hero.getName() + " è caduto in battaglia...");
                break;
            }
            
            turn++;
        }
        
        return hero.isAlive();
    }
}