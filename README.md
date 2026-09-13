# UNICAM RPG Arena

Progetto Java di gioco di ruolo a turni per l'esame di Metodologie di
Programmazione dell'Universita di Camerino (UNICAM).

Autore: Ahmed Muhammad Hasan, matricola 125944.

## Funzionalita

- Interfaccia grafica JavaFX.
- Combattimenti a turni contro nemici generati proceduralmente.
- Azioni del giocatore: attacco, difesa e uso di pozioni limitate.
- Avanzamento a ondate con stati di vittoria e sconfitta.
- Registro dello scontro e barre della vita di eroe e nemico.
- Persistenza JSON dell'intera sessione di gioco attiva.
- Test automatici per combattimento, flusso di gioco, controller e persistenza.
- Verifica della build su Linux tramite GitHub Actions.

## Requisiti

- JDK 21.
- Connessione Internet alla prima build, per scaricare le dipendenze Gradle.

## Compilazione Ed Esecuzione

Dalla directory radice del repository eseguire i comandi seguenti.

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

Il task `run` apre l'interfaccia JavaFX. Il task `build` compila l'applicazione
ed esegue tutti i test automatici.

## Persistenza

L'applicazione salva la sessione corrente in:

```text
~/.unicam-rpg/savegame.json
```

Il salvataggio include statistiche di eroe e nemico, tipo di azione di
combattimento, ondata corrente, pozioni residue, turno corrente e stato della
partita. I dati di esecuzione non sono versionati in Git.

## Documentazione

La Wiki del progetto documenta architettura, responsabilita delle classi,
persistenza, estendibilita, test e scelte di sviluppo:

https://github.com/hasansanii03/UNICAM-RPG-Project/wiki

## Dichiarazione AI

Durante lo sviluppo sono stati usati strumenti di AI generativa come supporto.
L'utilizzo ha incluso revisione dell'architettura iniziale, proposte di
refactoring per combattimento incrementale e persistenza della sessione,
stesura di casi di test, diagnosi di configurazioni Gradle e JavaFX e revisione
di README e Wiki.

L'autore ha revisionato i suggerimenti, integrato le modifiche, eseguito
l'applicazione e i test automatici e rimane responsabile delle scelte
progettuali e del codice consegnato.
