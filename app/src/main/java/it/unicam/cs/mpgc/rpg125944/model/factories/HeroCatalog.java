package it.unicam.cs.mpgc.rpg125944.model.factories;

import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import it.unicam.cs.mpgc.rpg125944.model.combat.CriticalAttack;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Elenco delle classi di eroe disponibili, ritrovabili per nome. */
public class HeroCatalog {
    private static final double THIEF_CRITICAL_CHANCE = 0.30;

    private final Map<String, HeroClass> classes = new LinkedHashMap<>();

    /** Registra una classe; non puo' esistere un'altra classe con lo stesso nome. */
    public HeroCatalog register(HeroClass heroClass) {
        Objects.requireNonNull(heroClass, "la classe non puo' essere null");
        if (classes.putIfAbsent(heroClass.getName(), heroClass) != null) {
            throw new IllegalArgumentException("classe gia' presente nel catalogo: " + heroClass.getName());
        }
        return this;
    }

    public HeroClass get(String name) {
        HeroClass heroClass = classes.get(name);
        if (heroClass == null) {
            throw new IllegalArgumentException("classe sconosciuta: " + name);
        }
        return heroClass;
    }

    /** Classi ordinate dalla piu' robusta (HP massimi piu' alti) alla meno robusta. */
    public List<HeroClass> getClassesByToughness() {
        return classes.values().stream()
                .sorted(Comparator.comparingInt(HeroClass::getMaxHp).reversed())
                .toList();
    }

    /** Catalogo di base del gioco: Cavaliere, Ladro e Guardiano. */
    public static HeroCatalog standard() {
        return new HeroCatalog()
                .register(new HeroClass("Cavaliere", 100, 18, 5, BasicAttack::new))
                .register(new HeroClass("Ladro", 70, 16, 2, () -> new CriticalAttack(THIEF_CRITICAL_CHANCE)))
                .register(new HeroClass("Guardiano", 130, 12, 8, BasicAttack::new));
    }
}
