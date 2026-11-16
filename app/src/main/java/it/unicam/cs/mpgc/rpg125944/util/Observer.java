package it.unicam.cs.mpgc.rpg125944.util;

/**
 * Interfaccia base per il pattern Observer.
 * Chi vuole essere avvisato dei cambiamenti del model (es. la vista) la implementa
 * e si registra presso il soggetto osservato.
 */
@FunctionalInterface
public interface Observer {
    /** Chiamato dal soggetto osservato dopo ogni cambiamento del suo stato. */
    void update();
}