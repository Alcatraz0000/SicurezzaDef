# SicurezzaDef

Tale progetto ha lo scopo di simulare la realizzazione di un sistema di e-vote basato su BlockChain. La documentazione e la descrizione completa del progetto è reperibile al seguente link:

-- link del PDF, magari caricato su github direttamente

I file riportati nella cartella "SicurezzaDef" comprono lo svolgimento del WP4. Nello specifico gli attori previsti sono: un Validatore, quattro Votanti e la Societa, ciascuno rappresentato da uno specifico programma. Questi diversi programmi comunicano tra di loro mediante l'impiego di Socket simulando in questo modo l'invio del voto correttamente cifrato e firmato e della annessa randomness, da parte dei votanti al validatore e l'invio della chiave privata della Societa al validatore. Come simulazione della blockchian il validatore farà riferimento a metodi contenuto all'interno della classe SmartContract, simulando l'aggiunta di blocchi attraverso la scrittura su un file di testo "ItalyChain.txt" (da eliminare prima di ogni esecuzione).
Le altre classi ovvero, Utils.java, Cryptare.java, SmartContract.java AppendingObjectOutputStream.java e toVote.java contengono metodi funzionali allo svolgimento della simulazione di votazione.

### How To Execute

Nella cartella execute si trovano:

- exec.sh, da modificare opportunamente per permettere l'esecuzione di terminali relativamente al proprio sistema operativo, è necessario anche modificare il path presente nella prima riga in modo da poter cancellare automaticamente il file ItalyChain.txt
- execClient#.sh, da modificare opportunamente per accedere alla directory corretta (dove è contenuto il file Votante.java)
- execServer.sh, da modificare opportunamente per accedere alla directory corretta (dove è contenuto il file Validatore.java)
- execSocieta.sh, da modificare opportunamente per accedere alla directory corretta (dove è contenuto il file Societa.java)

Dopo aver effettuato le dovute modifiche il progetto può essere lanciato eseguendo solo il file "./exec.sh"

- "Generazione Certificati.txt", che contiene le istruzioni eseguite per generare i certificati utilizzati nel progetto, keyStores e trustStores
- "Run Manuale.txt", che contiene le istruzioni necessarie per la compilazione ed esecuzione manuale dell'intero WP4. Ogni istruzione necessita di un proprio terminale.
