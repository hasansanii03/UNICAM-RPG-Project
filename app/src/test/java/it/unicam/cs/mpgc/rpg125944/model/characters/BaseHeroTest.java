package it.unicam.cs.mpgc.rpg125944.model.characters;

import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BaseHeroTest {
    @Test
    void appliesDefenseAndReturnsActualDamage() {
        Character hero = new BaseHero("Hero", 20, 5, 3, new BasicAttack());

        int damageDealt = hero.takeDamage(8);

        assertEquals(5, damageDealt);
        assertEquals(15, hero.getHp());
    }

    @Test
    void rejectsInvalidCombatantData() {
        assertThrows(IllegalArgumentException.class,
                () -> new BaseHero("Hero", 0, 1, 0, new BasicAttack()));
        assertThrows(IllegalArgumentException.class,
                () -> new BaseHero("Hero", 10, -1, 0, new BasicAttack()));
        assertThrows(IllegalArgumentException.class,
                () -> new BaseHero("Hero", 10, 1, -1, new BasicAttack()));
    }

    @Test
    void rejectsNegativeIncomingDamage() {
        Character hero = new BaseHero("Hero", 10, 1, 0, new BasicAttack());

        assertThrows(IllegalArgumentException.class, () -> hero.takeDamage(-1));
    }
}
