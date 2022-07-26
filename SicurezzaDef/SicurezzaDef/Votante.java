
import java.io.*;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.util.concurrent.TimeUnit;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.PrivateKey;
import java.security.KeyStore;

/**
 *
 * @author Kryptos
 */

/*
 * Questa classe rappresenta il singolo votante, e contiene al proprio interno
 * tutte le funzioni necessarie
 * a quest'ultimo per l'esecuzione delle sue funzionalità connesse
 * all'espressione della preferenza, alla conferma
 * della randomness utilizzata.
 * Per la simulazione del sistema implementato è stato previsto il
 * coinvolgimento di più votanti, motivo per cui il
 * codice all'interno presenta delle parametrizzazioni, che fanno riferimento
 * all'ID del votante in questione e
 * al voto espresso da quest'ultimo relativamente al referendum in questione,
 * inoltre per ciascuno dei votanti si
 * farà riferimento alla propria chiave pubblica.
 * 
 */

public class Votante {

    /**
     * Tale funzione è deputata all'invio dei messaggi, sfruttando la socket
     * instanziata, facendo sì
     * che il votante possa comunicare con il validatore.
     * All'invio del messaggio il votante deve attendere la conferma di corretta
     * ricezione dal validatore, o altrimenti
     * attendere sette secondi dopo di che ritentare l'invio del messaggio.
     * 
     * @param cSock     socket su cui inviare il messaggio
     * @param firstSent messaggio da inviare
     * @throws Exception
     */
    static void Protocol(Socket cSock, byte[] firstSent) throws Exception {
        // note that the SSLSocket object is converted in a standard Socket object and
        // henceforth we can work as for standard Java sockets
        char esito = 'c';
        OutputStream out = cSock.getOutputStream(); // output stream
        InputStream in = cSock.getInputStream(); // input stream

        while (!String.valueOf(esito).equals("0")) {
            out.write(firstSent); // il client manda il messaggio passato
            out.write(Utils.toByteArray("\n"));
            TimeUnit.MILLISECONDS.sleep(7000); // attende per la conferma
            out.flush();
            esito = (char) in.read(); // legge l'eventuale esito da parte del validatore
        }
        System.out.println("Comunicazione Votante-Validatore completata correttamente");
    }

    /**
     * Tale funzione sancisce l'inizio dell'esecuzione di ciascuno dei votanti,
     * prevede dei parametri passati a riga
     * di comando funzionali allo svolgimento delle attività di voto.
     * Inizialmente viene istaurata una connessione, mediante socket, attraverso la
     * quale votante e validatore possano
     * comunicare, l'inizializzazione di tale connessione è preceduta da una fase di
     * handshake.
     * 
     * @param args identificativo del votante e preferenza espressa
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SSLSocketFactory sockfact = (SSLSocketFactory) SSLSocketFactory.getDefault(); // similare a quella del server
        // eccetto per il fatto che usa SSLSocketFactory invece di
        // SSLSocketServerFactory
        SSLSocket cSock = (SSLSocket) sockfact.createSocket("localhost", 4000); // specifica l'host e la porta
        cSock.startHandshake(); // Handshake

        String voto = args[1]; // il secondo parametro passato a riga di comando corrisponde alla preferenza
                               // espressa dal votante
        PrivateKey ClientPrivatekey = null;
        PublicKey ClientPublickey = null;
        // ottenere chiave pubblica e privata associata al client a partire dal proprio
        // keystore
        try {
            File file = new File("Client" + args[0] + "keystore.jks"); // args[0] contiene l'ID del votante
            FileInputStream is = new FileInputStream(file);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            // Informazioni per il certificato
            String password = "mario99";
            String alias = "sslClient" + args[0];
            // ottenimento delle chiavi
            keystore.load(is, password.toCharArray());
            ClientPrivatekey = (PrivateKey) keystore.getKey(alias, password.toCharArray()); // chiave privata
            // Certificato della public key
            X509Certificate cert = (X509Certificate) keystore.getCertificate(alias);
            ClientPublickey = cert.getPublicKey(); // chiave pubblica

            // Here it prints the public key
            // System.out.println("Public Key Client" + args[0] + ": ");
            // System.out.println(ClientPublickey);
            // Here it prints the private key

        } catch (Exception e) {
            System.out.println(e);
        }

        KeyStore truststore = null;
        PublicKey SocietyPublicKey = null;
        // ottenere dal truststore del votante la chiave pubblica della società, utile
        // per le cifrature successive
        try {
            File file = new File("truststoreClient" + args[0] + ".jks");
            FileInputStream is = new FileInputStream(file);
            truststore = KeyStore.getInstance(KeyStore.getDefaultType());
            String password = "mario99";
            truststore.load(is, password.toCharArray());
            String alias = "sslSociety";
            // certificato della chiave pubblica
            X509Certificate cert = (X509Certificate) truststore.getCertificate(alias);
            SocietyPublicKey = cert.getPublicKey(); // chiave pubblica della società

        } catch (Exception e) {
            System.out.println(e);
        }
        // controllo che le chiavi ottenute abbiano validi valori associati
        if (ClientPrivatekey != null && ClientPublickey != null && SocietyPublicKey != null) {
            byte[] m = toVote.vote(ClientPrivatekey, SocietyPublicKey, voto, ClientPublickey);
            // m conterrà ciò che il votante invierà, ovvero il voto cifrato secondo il
            // processo previsto e firmato
            Protocol(cSock, m); // il valore di m verrà inviato al validatore
            TimeUnit.MILLISECONDS.sleep(25000); // simula la fine della fase T1-T2 quindi la fase deputata al voto
            // Ora ha inizio la fase T2-T3 quindi la fase deputata all'invio delle
            // randomness da parte dei votanti
            byte[] r = toVote.confirmVote(ClientPrivatekey, ClientPublickey); // r conterrà il valore della randomness
            // utilizzata nella cifratura del voto
            // si procede all'inizializzazione di una nuova socket in modo da poter
            // effettuare il secondo invio da parte del votante
            SSLSocket cSock2 = (SSLSocket) sockfact.createSocket("localhost", 4000); // specifica host e porta
            cSock2.startHandshake(); // Handshake
            // la randomness ottenuta viene firmata
            byte[] signature = Cryptare.signature(ClientPrivatekey, r);
            byte[] result = Utils.concatBytes(r, signature);
            Protocol(cSock2, result); // la randomness ottenuta e firmata viene inviata al validatore
            // simula la fine della finestra temporale T2-T3
            TimeUnit.MILLISECONDS.sleep(25000);
            // ora inizio la fase T3-T4 deputata alla computazione dell'esito finale del
            // referendum
        } else {
            System.out.println("Client" + args[0] + ": ci sono delle chiavi non ottenute correttamente");
        }

    }

}
