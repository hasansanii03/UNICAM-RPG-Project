package it.unicam.cs.mpgc.rpg125944.model.characters; 

/**
 * Interfaccia base per il Decorator Pattern.
 * Rappresenta l'astrazione di un personaggio nell'Auto-Battler.
 */
public interface Character {
    String getName();
    int getHp();
    int getMaxHp();
    int getAttackPower();
    int getDefense();
    
    // Metodo per subire danni (sarà utile per la logica di combattimento)
    void takeDamage(int damage);
    
    // Metodo per sapere se il personaggio è ancora in vita
    boolean isAlive();
}