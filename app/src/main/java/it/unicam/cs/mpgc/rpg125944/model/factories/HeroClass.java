package it.unicam.cs.mpgc.rpg125944.model.factories;

import it.unicam.cs.mpgc.rpg125944.model.characters.Hero;
import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Classe di eroe scegliibile all'inizio della partita (es. Cavaliere, Ladro).
 * Due classi sono uguali se hanno lo stesso nome: il nome e' la chiave del catalogo.
 */
public final class HeroClass {
    private final String name;
    private final int maxHp;
    private final int attack;
    private final int defense;
    private final Supplier<CombatAction> combatActionSupplier;

    public HeroClass(String name, int maxHp, int attack, int defense, Supplier<CombatAction> combatActionSupplier) {
        this.name = Objects.requireNonNull(name, "il nome non puo' essere null");
        this.maxHp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.combatActionSupplier =
                Objects.requireNonNull(combatActionSupplier, "combatActionSupplier non puo' essere null");
    }

    /** Crea un nuovo eroe di questa classe; la validazione delle statistiche e' quella di Hero. */
    public Hero createHero() {
        return new Hero(name, maxHp, attack, defense, combatActionSupplier.get());
    }

    public String getName() {
        return name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HeroClass that)) {
            return false;
        }
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /** Testo mostrato nella scelta della classe. */
    @Override
    public String toString() {
        return name + " (HP " + maxHp + ", ATT " + attack + ", DIF " + defense + ")";
    }
}
