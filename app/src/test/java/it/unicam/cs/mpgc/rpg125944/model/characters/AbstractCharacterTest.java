package it.unicam.cs.mpgc.rpg125944.model.characters;

import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractCharacterTest {
    @Test
    void appliesDefenseAndReturnsActualDamage() {
        Character hero = new Hero("Hero", 20, 5, 3, new BasicAttack());

        int damageDealt = hero.takeDamage(8);

        assertEquals(5, damageDealt);
        assertEquals(15, hero.getHp());
    }

    @Test
    void rejectsInvalidCombatantData() {
        assertThrows(IllegalArgumentException.class,
                () -> new Hero("Hero", 0, 1, 0, new BasicAttack()));
        assertThrows(IllegalArgumentException.class,
                () -> new Hero("Hero", 10, -1, 0, new BasicAttack()));
        assertThrows(IllegalArgumentException.class,
                () -> new Hero("Hero", 10, 1, -1, new BasicAttack()));
    }

    @Test
    void heroAndEnemyDeclareTheirRole() {
        assertEquals(CharacterRole.HERO, new Hero("Hero", 10, 1, 0, new BasicAttack()).getRole());
        assertEquals(CharacterRole.ENEMY, new Enemy("Enemy", 10, 1, 0, new BasicAttack()).getRole());
    }

    @Test
    void restoresCharacterWithGivenHp() {
        Character enemy = new Enemy("Enemy", 30, 12, 5, 2, new BasicAttack());

        assertAll(
                () -> assertEquals(12, enemy.getHp()),
                () -> assertEquals(30, enemy.getMaxHp()),
                () -> assertTrue(enemy.isAlive())
        );
        assertThrows(IllegalArgumentException.class,
                () -> new Enemy("Enemy", 30, 31, 5, 2, new BasicAttack()));
        assertThrows(IllegalArgumentException.class,
                () -> new Hero("Hero", 30, -1, 5, 2, new BasicAttack()));
    }

    @Test
    void notifiesObserversWhenHpChanges() {
        AbstractCharacter hero = new Hero("Hero", 20, 5, 0, new BasicAttack());
        int[] notifications = {0};
        hero.addObserver(() -> notifications[0]++);

        hero.takeDamage(4);
        hero.heal(2);

        assertEquals(2, notifications[0]);
    }

    @Test
    void rejectsNegativeIncomingDamage() {
        Character hero = new Hero("Hero", 10, 1, 0, new BasicAttack());

        assertThrows(IllegalArgumentException.class, () -> hero.takeDamage(-1));
    }
}
