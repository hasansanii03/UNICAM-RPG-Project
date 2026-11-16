package it.unicam.cs.mpgc.rpg125944.model.factories;

import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import it.unicam.cs.mpgc.rpg125944.model.combat.CriticalAttack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Elenco dei tipi di nemico disponibili, ritrovabili per nome. */
public class EnemyCatalog {
    private static final double ORC_CRITICAL_CHANCE = 0.20;

    private final Map<String, EnemyTemplate> templates = new LinkedHashMap<>();

    /** Registra un tipo di nemico; non puo' esistere un altro tipo con lo stesso nome. */
    public EnemyCatalog register(EnemyTemplate template) {
        Objects.requireNonNull(template, "il template non puo' essere null");
        if (templates.putIfAbsent(template.name(), template) != null) {
            throw new IllegalArgumentException("nemico gia' presente nel catalogo: " + template.name());
        }
        return this;
    }

    public EnemyTemplate get(String name) {
        EnemyTemplate template = templates.get(name);
        if (template == null) {
            throw new IllegalArgumentException("nemico sconosciuto: " + name);
        }
        return template;
    }

    /** Tipi di nemico nell'ordine di registrazione. */
    public List<EnemyTemplate> getTemplates() {
        return new ArrayList<>(templates.values());
    }

    /** Catalogo di base del gioco: Goblin, Orco e Golem di Pietra. */
    public static EnemyCatalog standard() {
        return new EnemyCatalog()
                .register(new EnemyTemplate("Goblin", 30, 10, 5, 2, 2, BasicAttack::new))
                .register(new EnemyTemplate("Orco", 50, 10, 10, 2, 5,
                        () -> new CriticalAttack(ORC_CRITICAL_CHANCE)))
                // Debole ma molto resistente: difesa altissima
                .register(new EnemyTemplate("Golem di Pietra", 80, 10, 4, 2, 15, BasicAttack::new));
    }
}
