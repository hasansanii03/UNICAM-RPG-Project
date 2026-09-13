package it.unicam.cs.mpgc.rpg125944.model.combat;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;

import java.util.Objects;

/**
 * Controls one battle round at a time. A round contains the selected player
 * action and, if the enemy survives, its automatic response.
 */
public class Battle {
    public static final int POTION_HEALING = 20;

    private final Character hero;
    private final Character enemy;
    private BattleState state;
    private int turnNumber;
    private int remainingPotions;

    public Battle(Character hero, Character enemy, int availablePotions) {
        this.hero = Objects.requireNonNull(hero, "hero must not be null");
        this.enemy = Objects.requireNonNull(enemy, "enemy must not be null");
        if (availablePotions < 0) {
            throw new IllegalArgumentException("availablePotions must not be negative");
        }
        this.remainingPotions = availablePotions;
        this.turnNumber = 1;
        updateState();
    }

    public TurnResult executeTurn(PlayerAction playerAction) {
        Objects.requireNonNull(playerAction, "playerAction must not be null");
        if (state != BattleState.IN_PROGRESS) {
            throw new IllegalStateException("battle has already ended");
        }

        ActionResult playerActionResult = null;
        int hpRestored = 0;
        boolean defending = playerAction == PlayerAction.DEFEND;

        if (playerAction == PlayerAction.ATTACK) {
            playerActionResult = hero.getCombatAction().execute(hero, enemy);
        } else if (playerAction == PlayerAction.USE_POTION) {
            if (remainingPotions == 0) {
                throw new IllegalStateException("no potions remaining");
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

    private static class GuardedCharacter implements Character {
        private final Character delegate;

        private GuardedCharacter(Character delegate) {
            this.delegate = delegate;
        }

        @Override
        public String getName() {
            return delegate.getName();
        }

        @Override
        public int getHp() {
            return delegate.getHp();
        }

        @Override
        public int getMaxHp() {
            return delegate.getMaxHp();
        }

        @Override
        public int getAttackPower() {
            return delegate.getAttackPower();
        }

        @Override
        public int getDefense() {
            return delegate.getDefense();
        }

        @Override
        public int takeDamage(int damage) {
            return delegate.takeDamage((damage + 1) / 2);
        }

        @Override
        public int heal(int amount) {
            return delegate.heal(amount);
        }

        @Override
        public boolean isAlive() {
            return delegate.isAlive();
        }

        @Override
        public CombatAction getCombatAction() {
            return delegate.getCombatAction();
        }
    }
}
