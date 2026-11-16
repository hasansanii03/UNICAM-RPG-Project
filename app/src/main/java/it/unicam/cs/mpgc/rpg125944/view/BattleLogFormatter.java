package it.unicam.cs.mpgc.rpg125944.view;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.combat.ActionResult;
import it.unicam.cs.mpgc.rpg125944.model.combat.TurnResult;
import it.unicam.cs.mpgc.rpg125944.model.game.GameSession;
import it.unicam.cs.mpgc.rpg125944.model.game.GameState;

/**
 * Trasforma gli esiti del model in testi italiani. Non dipende da JavaFX,
 * quindi puo' essere riusato da qualsiasi interfaccia e testato senza GUI.
 */
public class BattleLogFormatter {

    /** Racconta un turno: l'azione del giocatore, la risposta del nemico e l'eventuale fine dello scontro. */
    public String formatTurn(TurnResult result, String heroName, String enemyName) {
        StringBuilder text = new StringBuilder();
        text.append("Turno ").append(result.turnNumber()).append(": ");
        text.append(heroName).append(" usa ").append(result.playerAction().getName());
        if (result.playerActionResult() != null) {
            text.append(": ").append(describeDamage(result.playerActionResult()));
        }
        if (result.hpRestored() > 0) {
            text.append(": recupera ").append(result.hpRestored()).append(" HP");
        }
        text.append(".");
        if (result.enemyActionResult() != null) {
            text.append(" ").append(enemyName).append(" ").append(describeDamage(result.enemyActionResult()));
            text.append(".");
        }
        text.append(describeBattleEnd(result));
        return text.toString();
    }

    public String formatState(GameState state) {
        return switch (state) {
            case IN_PROGRESS -> "Scontro in corso";
            case WAVE_WON -> "Ondata superata - continua quando vuoi";
            case VICTORY -> "Vittoria";
            case DEFEAT -> "Sconfitta";
        };
    }

    public String formatWave(GameSession session) {
        return "Ondata " + session.getCurrentWave() + " di " + session.getTotalWaves()
                + " | Pozioni: " + session.getRemainingPotions();
    }

    public String formatCombatant(Character character) {
        return character.getName() + " - HP " + character.getHp() + "/" + character.getMaxHp();
    }

    private String describeDamage(ActionResult result) {
        String text = "infligge " + result.damageDealt() + " danni";
        return result.critical() ? text + " con un colpo critico" : text;
    }

    private String describeBattleEnd(TurnResult result) {
        return switch (result.battleState()) {
            case HERO_WON -> " Il nemico e' stato sconfitto.";
            case HERO_LOST -> " L'eroe e' stato sconfitto.";
            case IN_PROGRESS -> "";
        };
    }
}
