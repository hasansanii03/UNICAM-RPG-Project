
# ⚔️ UNICAM RPG Auto-Battler

Progetto per l'esame di Metodologie di Programmazione (Informatica per la Comunicazione Digitale - UNICAM).
Sviluppato da **Ahmed Muhammad Hasan** (Matricola: 125944).

## 🎮 Descrizione
Un videogioco gestionale a turni in cui un eroe affronta ondate di nemici procedurali. Il progetto implementa una rigorosa architettura MVC, vari Design Pattern (Observer, Decorator, Strategy, Factory), un'interfaccia grafica in JavaFX e il salvataggio dei dati in formato JSON tramite pattern DAO.

## 📚 Documentazione
Tutta la documentazione tecnica, le scelte architetturali, i diagrammi (se presenti) e la dichiarazione di utilizzo dell'AI sono disponibili nella Wiki del repository.

👉 **[Clicca qui per leggere la Wiki completa](https://github.com/hasansanii03/UNICAM-RPG-Project/wiki)**


## 🚀 Come avviare il progetto

Il progetto utilizza **Gradle** per la gestione delle dipendenze e la build. Non è necessario avere Gradle installato sul computer, basta usare il wrapper incluso (`gradlew`).

### Prerequisiti
* Java Development Kit (JDK) 21 o superiore.

### Comandi da terminale
Posizionati nella cartella principale del progetto ed esegui:

# **Per compilare il progetto:**
```bash
./gradlew build
(Su Windows: gradlew build)

 Per avviare l'interfaccia grafica (JavaFX):

# Bash
./gradlew run
(Su Windows: gradlew run)

# 🛠 Tecnologie Utilizzate
 Linguaggio: Java 21

 Build System: Gradle

 GUI: JavaFX

 Persistenza: Google Gson (JSON)