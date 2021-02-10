Per eseguire la build del progetto organizzato tramite la struttura di Maven:

- posizionarsi tramite terminale nella cartella contenente il file pom.xml e le cartelle "client", "server" e "shared".
- eseguire "mvn clean package" per la build del progetto


NOTARE CON ATTENZIONE:

I file .jar generati con questo comando, saranno contenuti all'interno delle rispettive cartelle in una sotto-cartella di nome "target" (Esempio: client/target).
Verranno generati due file .jar per ogni cartella "target", tuttavia solo il file .jar con nel nome la parola "dependencies" è il file corretto da eseguire (poiché contiene le dependencies necessarie per la sua corretta esecuzione). Nel caso si tentasse di eseguire il file sbagliato, tale file non funzionerà.


NOTARE CON ATTENZIONE:

Gli applicativi (client e server), sono stati testati sul medesimo e differenti dispositivi collegati alla stessa rete. Pertanto il funzionamento è garantito fino a quando questo vincolo è rispettato. Nel caso in cui si tentasse di eseguire gli applicativi su dispositivi differenti appartenenti a reti differenti, non è garantita la corretta esecuzione del programma.



Eseguire il file .jar del server:

- posizionarsi tramite terminale nella cartella server.
- digitare: "java -jar target/nome_file_da_eseguire.jar"
(Esempio: "java -jar target/server-1.0-SNAPSHOT-jar-with-dependencies.jar")

Quando l'applicativo server verrà eseguito, bisognerà inserire l'indirizzo IP e la porta del database per la connessione (Es. localhost:5432). Inoltre verrà chiesto di inserire lo username del database e la password.
Solitamente le credenziali di default dovrebbero essere come username "postgres" e come password basta premere semplicemente INVIO.


Eseguire il file .jar del client:

- posizionarsi tramite terminale nella cartella client.
- digitare: "java -jar target/nome_file_da_eseguire.jar IP_server"
(Esempio: "java -jar target/client-1.0-SNAPSHOT-jar-with-dependencies.jar 192.168.1.4")






