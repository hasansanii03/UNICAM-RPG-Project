package it.unicam.cs.mpgc.rpg125944.model.combat;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.characters.CharacterDecorator;

import java.util.Objects;

/**
 * Gestisce uno scontro un turno alla volta. Un turno contiene l'azione scelta
 * dal giocatore e, se il nemico sopravvive, la sua risposta automatica.
 */
public class Battle {
    public static final int POTION_HEALING = 20;
    private static final int COUNTERATTACK_DIVISOR = 2;

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

        ActionResult playerActionResult = null;
        int hpRestored = 0;
        boolean defending = playerAction == PlayerAction.DEFEND;

        if (playerAction == PlayerAction.ATTACK) {
            playerActionResult = hero.getCombatAction().execute(hero, enemy);
        } else if (playerAction == PlayerAction.DEFEND) {
            int rawDamage = (hero.getAttackPower() + 1) / COUNTERATTACK_DIVISOR;
            int damageDealt = enemy.takeDamage(rawDamage);
            playerActionResult = new ActionResult(rawDamage, damageDealt, false);
        } else if (playerAction == PlayerAction.USE_POTION) {
            if (remainingPotions == 0) {
                throw new IllegalStateException("non restano pozioni");
            }
            if (hero.getHp() == hero.getMaxHp()) {
                throw new IllegalStateException("l'eroe ha gia' tutti gli HP");
            }
            remainingPotions--;
            hpRestored = hero.heal(POTION_HEALING);
        }

        updateState();
        ActionResult enemyActionResult = null;
        if (state == BattleState.IN_PROGRESS) {
            Character target = defending ? new GuardedCharacter(hero) : hero;
            enemyActionResult = enemy.getCombatAction().execute(enemy, target);
            updateState();
        }

        TurnResult result = new TurnResult(
                turnNumber,
                playerAction,
                playerActionResult,
                enemyActionResult,
                hpRestored,
                remainingPotions,
                state
        );
        turnNumber++;
        return result;
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

    /**
     * Decorator usato solo durante la difesa: dimezza il danno in ingresso
     * (arrotondando per eccesso) e delega tutto il resto al personaggio originale.
     */
    private static class GuardedCharacter extends CharacterDecorator {
        private static final int GUARD_DAMAGE_DIVISOR = 2;

        private GuardedCharacter(Character character) {
            super(character);
        }

        @Override
        public int takeDamage(int damage) {
            int reducedDamage = (damage + GUARD_DAMAGE_DIVISOR - 1) / GUARD_DAMAGE_DIVISOR;
            return character.takeDamage(reducedDamage);
        }
    }
}
