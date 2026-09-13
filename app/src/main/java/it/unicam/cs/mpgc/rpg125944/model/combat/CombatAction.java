package it.unicam.cs.mpgc.rpg125944.model.combat;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;

/**
 * Interfaccia per lo Strategy Pattern.
 * Definisce un'azione che un personaggio può compiere in combattimento.
 */
public interface CombatAction {
    
    /**
     * Executes the action and returns its outcome.
     * @param attacker Il personaggio che esegue l'attacco.
     * @param target Il bersaglio dell'attacco.
     */
    ActionResult execute(Character attacker, Character target);
}
