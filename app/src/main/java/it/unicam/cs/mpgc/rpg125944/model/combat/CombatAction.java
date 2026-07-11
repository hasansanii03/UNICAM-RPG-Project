package it.unicam.cs.mpgc.rpg125944.model.combat;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;

/**
 * Interfaccia per lo Strategy Pattern.
 * Definisce un'azione che un personaggio può compiere in combattimento.
 */
public interface CombatAction {
    
    /**
     * Esegue l'azione di combattimento.
     * @param attacker Il personaggio che esegue l'attacco.
     * @param target Il bersaglio dell'attacco.
     */
    void execute(Character attacker, Character target);
}