package it.unicam.cs.mpgc.rpg125944.model.factories;

import it.unicam.cs.mpgc.rpg125944.model.characters.Hero;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HeroCatalogTest {
    @Test
    void standardCatalogCreatesHeroesWithTheirOwnStats() {
        HeroCatalog catalog = HeroCatalog.standard();

        Hero thief = catalog.get("Ladro").createHero();

        assertAll(
                () -> assertEquals("Ladro", thief.getName()),
                () -> assertEquals(70, thief.getMaxHp()),
                () -> assertEquals(70, thief.getHp())
        );
    }

    @Test
    void listsClassesFromTheToughestToTheLeast() {
        List<String> names = HeroCatalog.standard().getClassesByToughness().stream()
                .map(HeroClass::getName)
                .toList();

        assertEquals(List.of("Guardiano", "Cavaliere", "Ladro"), names);
    }

    @Test
    void rejectsDuplicatedAndUnknownClasses() {
        HeroCatalog catalog = HeroCatalog.standard();

        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> catalog.register(new HeroClass("Ladro", 1, 1, 0, BasicAttack::new))),
                () -> assertThrows(IllegalArgumentException.class, () -> catalog.get("Mago"))
        );
    }

    @Test
    void classesWithTheSameNameAreEqual() {
        HeroClass first = new HeroClass("Mago", 50, 20, 1, BasicAttack::new);
        HeroClass second = new HeroClass("Mago", 90, 5, 4, BasicAttack::new);
        Set<HeroClass> set = new HashSet<>(List.of(first));

        assertAll(
                () -> assertEquals(first, second),
                () -> assertEquals(first.hashCode(), second.hashCode()),
                () -> assertEquals(true, set.contains(second))
        );
    }
}
