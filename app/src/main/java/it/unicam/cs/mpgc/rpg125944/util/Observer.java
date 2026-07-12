package it.unicam.cs.mpgc.rpg125944.util;

/**
 * Interfaccia base per il pattern Observer.
 * Chiunque voglia "ascoltare" i cambiamenti (es. la GUI) deve implementare questa interfaccia.
 */
public interface Observer {
    // Metodo chiamato quando i dati cambiano
    void update();
}