package it.unicam.cs.mpgc.rpg125944.model.combat;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;

import java.util.Objects;

/**
 * Gestisce uno scontro un turno alla volta. Un turno contiene l'azione scelta
 * dal giocatore e, se il nemico sopravvive, la sua risposta automatica.
 */
public class Battle {
    public static final int POTION_HEALING = 20;

    private final Character hero;
    private final Character enemy;
    private BattleState state;
    private int turnNumber;
    private int remainingPotions;

    public Battle(Character hero, Character enemy, int availablePotions) {
        this(hero, enemy, availablePotions, 1);
    }

    public Battle(Character hero, Character enemy, int availablePotions, int turnNumber) {
        this.hero = Objects.requireNonNull(hero, "l'eroe non puo' essere null");
        this.enemy = Objects.requireNonNull(enemy, "il nemico non puo' essere null");
        if (availablePotions < 0) {
            throw new IllegalArgumentException("le pozioni disponibili non possono essere negative");
        }
        if (turnNumber <= 0) {
            throw new IllegalArgumentException("il numero del turno deve essere positivo");
        }
        this.remainingPotions = availablePotions;
        this.turnNumber = turnNumber;
        updateState();
    }

    public TurnResult executeTurn(PlayerAction playerAction) {
        Objects.requireNonNull(playerAction, "playerAction non puo' essere null");
        if (state != BattleState.IN_PROGRESS) {
            throw new IllegalStateException("lo scontro e' gia' terminato");
        }
        if (!playerAction.isAvailable(this)) {
            throw new IllegalStateException("azione non disponibile: " + playerAction.getName());
        }

        ActionOutcome outcome = playerAction.execute(this);

        updateState();
        ActionResult enemyActionResult = null;
        if (state == BattleState.IN_PROGRESS) {
            Character target = playerAction.protect(hero);
            enemyActionResult = enemy.getCombatAction().execute(enemy, target);
            updateState();
        }

        TurnResult result = new TurnResult(
                turnNumber,
                playerAction,
                outcome.damageResult(),
                enemyActionResult,
                outcome.hpRestored(),
                remainingPotions,
                state
        );
        turnNumber++;
        return result;
    }

    /**
     * Consuma una pozione e cura l'eroe. Pensato per essere chiamato dalle azioni del giocatore
     * (dopo aver verificato la disponibilita'). Restituisce i punti vita recuperati.
     */
    public int consumePotion() {
        if (remainingPotions == 0) {
            throw new IllegalStateException("non restano pozioni");
        }
        remainingPotions--;
        return hero.heal(POTION_HEALING);
    }

    public Character getHero() {
        return hero;
    }

    public Character getEnemy() {
        return enemy;
    }

    public BattleState getState() {
        return state;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public int getRemainingPotions() {
        return remainingPotions;
    }

    private void updateState() {
        if (!hero.isAlive()) {
            state = BattleState.HERO_LOST;
        } else if (!enemy.isAlive()) {
            state = BattleState.HERO_WON;
        } else {
            state = BattleState.IN_PROGRESS;
        }
    }
}
