package it.unicam.cs.mpgc.rpg125944.model.factories;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import it.unicam.cs.mpgc.rpg125944.model.combat.CriticalAttack;
import org.junit.jupiter.api.Test;

import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnemyFactoryTest {
    /** Generatore finto che sceglie sempre l'indice richiesto. */
    private static RandomGenerator alwaysPicking(int index) {
        return new RandomGenerator() {
            @Override
            public long nextLong() {
                return index;
            }

            @Override
            public int nextInt(int bound) {
                return index;
            }
        };
    }

    @Test
    void scalesStatisticsWithLevel() {
        Character goblin = new EnemyFactory(alwaysPicking(0)).createEnemy(2);

        assertAll(
                () -> assertEquals("Goblin Liv. 2", goblin.getName()),
                () -> assertEquals(50, goblin.getMaxHp()),
                () -> assertEquals(9, goblin.getAttackPower()),
                () -> assertEquals(2, goblin.getDefense()),
                () -> assertInstanceOf(BasicAttack.class, goblin.getCombatAction())
        );
    }

    @Test
    void orcUsesCriticalAttack() {
        Character orc = new EnemyFactory(alwaysPicking(1)).createEnemy(1);

        assertAll(
                () -> assertEquals("Orco Liv. 1", orc.getName()),
                () -> assertInstanceOf(CriticalAttack.class, orc.getCombatAction())
        );
    }

    @Test
    void newEnemyTypeIsJustANewCatalogEntry() {
        EnemyCatalog catalog = new EnemyCatalog()
                .register(new EnemyTemplate("Drago", 200, 20, 30, 5, 10, BasicAttack::new));

        Character dragon = new EnemyFactory(catalog, alwaysPicking(0)).createEnemy(1);

        assertAll(
                () -> assertEquals("Drago Liv. 1", dragon.getName()),
                () -> assertEquals(220, dragon.getMaxHp())
        );
    }

    @Test
    void catalogRejectsDuplicatesAndUnknownNames() {
        EnemyCatalog catalog = EnemyCatalog.standard();

        assertThrows(IllegalArgumentException.class,
                () -> catalog.register(new EnemyTemplate("Goblin", 1, 1, 1, 1, 0, BasicAttack::new)));
        assertThrows(IllegalArgumentException.class, () -> catalog.get("Drago"));
        assertEquals(3, catalog.getTemplates().size());
    }

    @Test
    void rejectsInvalidLevelAndEmptyCatalog() {
        assertThrows(IllegalArgumentException.class, () -> new EnemyFactory().createEnemy(0));
        assertThrows(IllegalStateException.class,
                () -> new EnemyFactory(new EnemyCatalog(), alwaysPicking(0)).createEnemy(1));
    }
}
