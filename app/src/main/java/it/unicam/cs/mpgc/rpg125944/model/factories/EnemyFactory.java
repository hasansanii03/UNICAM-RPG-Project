package it.unicam.cs.mpgc.rpg125944.model.factories;

import it.unicam.cs.mpgc.rpg125944.model.characters.BaseHero;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import it.unicam.cs.mpgc.rpg125944.model.combat.CriticalAttack;
import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Factory Pattern per la generazione dei nemici.
 * Incapsula la logica di creazione e il bilanciamento delle statistiche.
 */
public class EnemyFactory implements EnemyProvider {

    private final RandomGenerator random;

    public EnemyFactory() {
        this(RandomGenerator.getDefault());
    }

    public EnemyFactory(RandomGenerator random) {
        this.random = Objects.requireNonNull(random, "random non puo' essere null");
    }

    /**
     * Genera un nemico casuale basato sul livello di difficoltà.
     * @param difficultyLevel Il livello dell'ondata attuale.
     * @return Un'istanza di Character nemico.
     */
    public Character createRandomEnemy(int difficultyLevel) {
        return createEnemy(difficultyLevel);
    }

    @Override
    public Character createEnemy(int difficultyLevel) {
        if (difficultyLevel <= 0) {
            throw new IllegalArgumentException("il livello di difficolta deve essere positivo");
        }
        int enemyType = random.nextInt(3); // Genera un numero da 0 a 2

        // Le statistiche scalano in base al livello di difficoltà
        int hpMultiplier = 10 * difficultyLevel;
        int attackMultiplier = 2 * difficultyLevel;

        switch (enemyType) {
            case 0:
                return new BaseHero(
                    "Goblin Liv. " + difficultyLevel,
                    30 + hpMultiplier,
                    5 + attackMultiplier,
                    2,
                    new BasicAttack()
                );
            case 1:
                return new BaseHero(
                    "Orco Liv. " + difficultyLevel,
                    50 + hpMultiplier,
                    10 + attackMultiplier,
                    5,
                    new CriticalAttack(0.20) // 20% di probabilità di attacco critico
                );
            case 2:
                // Un nemico debole ma molto resistente
                return new BaseHero(
                    "Golem di Pietra Liv. " + difficultyLevel,
                    80 + hpMultiplier,
                    4 + attackMultiplier,
                    15, // Difesa altissima
                    new BasicAttack()
                );
            default:
                throw new IllegalStateException("Tipo di nemico non valido");
        }
    }
}
