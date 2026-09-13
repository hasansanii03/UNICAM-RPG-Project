package it.unicam.cs.mpgc.rpg125944.model.combat;

/** Esito immutabile di un singolo turno di battaglia. */
public record TurnResult(
        int turnNumber,
        PlayerAction playerAction,
        int damageToEnemy,
        int damageToHero,
        BattleState battleState
) {
}
