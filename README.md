# UNICAM RPG Arena

Progetto Java di gioco di ruolo a turni per l'esame di Metodologie di
Programmazione dell'Universita di Camerino (UNICAM).

Autore: Ahmed Muhammad Hasan, matricola 125944.

Il gioco e' il contesto, non il fine: il progetto serve a mostrare un sistema
modulare, estendibile e testato, in cui aggiungere nemici, classi di eroe,
azioni del giocatore o una nuova interfaccia non richiede di modificare il
codice esistente.

## Funzionalita

- Interfaccia grafica JavaFX descritta in FXML.
- Scelta della classe dell'eroe (Cavaliere, Ladro, Guardiano) a ogni nuova partita.
- Combattimenti a turni a ondate contro nemici di un catalogo (Goblin, Orco, Golem di Pietra)
  che diventano piu' forti a ogni ondata.
- Azioni del giocatore: attacco, difesa e uso di pozioni limitate.
- Stati di vittoria e sconfitta, registro dello scontro e barre della vita.
- Salvataggio e caricamento della partita in JSON.
- Test automatici (JUnit 5) e verifica della build con GitHub Actions.

## Requisiti

- JDK 21.
- Connessione Internet alla prima build, per scaricare le dipendenze Gradle.

## Compilazione ed esecuzione

Dalla directory radice del repository.

Linux o macOS:

```bash
./gradlew build
./gradlew run
```

Windows PowerShell:

```powershell
.\gradlew.bat build
.\gradlew.bat run
```

Il task `run` apre l'interfaccia JavaFX. Il task `build` compila
l'applicazione ed esegue tutti i test automatici.

## Struttura del codice

Tutto il codice sta nel package `it.unicam.cs.mpgc.rpg125944`.

| Package | Contenuto |
|---|---|
| `model.characters` | `Character`, `AbstractCharacter`, `Hero`, `Enemy`, `CharacterDecorator` |
| `model.combat` | `Battle`, `CombatAction` (`BasicAttack`, `CriticalAttack`), `PlayerAction` (`AttackAction`, `DefendAction`, `UsePotionAction`) |
| `model.factories` | `EnemyCatalog`, `EnemyTemplate`, `EnemyFactory`, `HeroCatalog`, `HeroClass` |
| `model.game` | `GameSession`, `GameState` |
| `model.persistence` | `GameRepository`, `JsonGameRepository`, `CombatActionAdapter` e i codec delle azioni |
| `controller` | `GameController`, punto di accesso usato da qualsiasi interfaccia |
| `view` | `GameViewController`, `BattleLogFormatter` e `game.fxml` (tra le risorse) |
| `util` | `Observer` |

Il model non conosce la view: la vista si registra come `Observer` e viene
avvisata quando la partita cambia. `MainGUI` e' solo il punto di ingresso, dove
si scelgono le implementazioni concrete.

## Estendere il gioco

| Voglio aggiungere... | Cosa faccio |
|---|---|
| un nemico | registro un `EnemyTemplate` in `EnemyCatalog` |
| una classe di eroe | registro una `HeroClass` in `HeroCatalog` |
| un'azione del giocatore | scrivo una classe che implementa `PlayerAction` e la passo a `GameController` |
| un nuovo tipo di attacco salvabile | scrivo la `CombatAction` e il suo `CombatActionCodec` |
| un'altra interfaccia (web, mobile) | uso `GameController`, senza toccare il model |
| un altro formato di salvataggio | implemento `GameRepository` |

## Persistenza

La partita viene salvata in:

```text
~/.unicam-rpg/savegame.json
```

Il file contiene statistiche di eroe e nemico, azione di combattimento
(con il campo `type` scritto da `CombatActionAdapter`), ondata corrente,
pozioni residue, turno e stato della partita. Il file ha un numero di versione
del formato: salvataggi di versioni diverse vengono rifiutati con un messaggio
nel registro. I dati di esecuzione non sono versionati in Git.

## Test e Git

- `./gradlew build` esegue i test JUnit 5 di combattimento, flusso di gioco,
  controller, cataloghi, persistenza e formattazione del registro.
- Il lavoro di miglioramento e' sul branch `improvement/oop-solid-refactoring`,
  con commit piccoli (`feat:`, `fix:`, `refactor:`, `test:`, `docs:`).
- GitHub Actions esegue `./gradlew build` a ogni push e pull request.

## Documentazione

La Wiki del progetto documenta funzionalita, architettura e responsabilita
delle classi, classi e interfacce, persistenza, estendibilita e principi SOLID,
test e Git, e la dichiarazione dettagliata di uso dell'AI:

https://github.com/hasansanii03/UNICAM-RPG-Project/wiki

## Dichiarazione di uso dell'AI

Nel progetto sono stati usati strumenti di AI generativa, in particolare
**Claude Code** (Anthropic), sia per la prima versione sia per la fase di
miglioramento dopo la valutazione del docente.

Nella fase di miglioramento Claude Code e' stato usato per:

- analizzare i commenti del docente e il codice esistente e proporre un piano di refactoring;
- scrivere e modificare il codice dei passi di refactoring (gerarchia dei personaggi,
  azioni del giocatore polimorfiche, cataloghi di nemici ed eroi, adapter Gson,
  GUI con FXML, Observer);
- scrivere i test automatici corrispondenti;
- preparare bozze di README e Wiki.

L'autore ha deciso quali passi eseguire e in che ordine, ha letto e provato ogni
modifica, ha eseguito build, test e applicazione, ha fatto personalmente tutti i
commit e le push e rimane responsabile delle scelte progettuali e del codice
consegnato. Il dettaglio per fase e' nella pagina "Dichiarazione di uso
dell'AI" della Wiki.
