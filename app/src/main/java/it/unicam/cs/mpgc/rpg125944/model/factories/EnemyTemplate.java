package it.unicam.cs.mpgc.rpg125944.model.factories;

import it.unicam.cs.mpgc.rpg125944.model.characters.Enemy;
import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Descrive un tipo di nemico: statistiche di partenza, crescita per livello e strategia di attacco.
 * E' un semplice insieme di dati: aggiungere un nemico significa descriverlo, non scrivere nuovo codice.
 *
 * @param name                 nome del tipo di nemico (ad esempio "Goblin")
 * @param baseHp               punti vita al livello zero
 * @param hpPerLevel           punti vita aggiunti per ogni livello
 * @param baseAttack           attacco al livello zero
 * @param attackPerLevel       attacco aggiunto per ogni livello
 * @param defense              difesa, uguale a ogni livello
 * @param combatActionSupplier produce la strategia di attacco di ogni nuovo nemico
 */
public record EnemyTemplate(
        String name,
        int baseHp,
        int hpPerLevel,
        int baseAttack,
        int attackPerLevel,
        int defense,
        Supplier<CombatAction> combatActionSupplier
) {
    public EnemyTemplate {
        Objects.requireNonNull(name, "il nome non puo' essere null");
        Objects.requireNonNull(combatActionSupplier, "combatActionSupplier non puo' essere null");
    }

    /** Crea un nemico di questo tipo con le statistiche scalate sul livello indicato. */
    public Enemy create(int level) {
        if (level <= 0) {
            throw new IllegalArgumentException("il livello deve essere positivo");
        }
        return new Enemy(
                name + " Liv. " + level,
                baseHp + hpPerLevel * level,
                baseAttack + attackPerLevel * level,
                defense,
                combatActionSupplier.get()
        );
    }
}
