package it.unicam.cs.mpgc.rpg125944.model.factories;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;

import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Factory Pattern per la generazione dei nemici: sceglie a caso un tipo dal catalogo
 * e lo crea con le statistiche scalate sul livello dell'ondata.
 */
public class EnemyFactory implements EnemyProvider {

    private final EnemyCatalog catalog;
    private final RandomGenerator random;

    public EnemyFactory() {
        this(EnemyCatalog.standard(), RandomGenerator.getDefault());
    }

    public EnemyFactory(RandomGenerator random) {
        this(EnemyCatalog.standard(), random);
    }

    public EnemyFactory(EnemyCatalog catalog, RandomGenerator random) {
        this.catalog = Objects.requireNonNull(catalog, "catalog non puo' essere null");
        this.random = Objects.requireNonNull(random, "random non puo' essere null");
    }

    /**
     * Genera un nemico casuale basato sul livello di difficolta'.
     * @param difficultyLevel Il livello dell'ondata attuale.
     * @return Un'istanza di Character nemico.
     */
    @Override
    public Character createEnemy(int difficultyLevel) {
        if (difficultyLevel <= 0) {
            throw new IllegalArgumentException("il livello di difficolta deve essere positivo");
        }
        List<EnemyTemplate> templates = catalog.getTemplates();
        if (templates.isEmpty()) {
            throw new IllegalStateException("il catalogo dei nemici e' vuoto");
        }
        return templates.get(random.nextInt(templates.size())).create(difficultyLevel);
    }
}
