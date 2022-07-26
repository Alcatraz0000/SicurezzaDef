
import java.io.*;

import java.security.PublicKey;
import java.security.KeyFactory;
import java.security.cert.X509Certificate;
import java.security.PrivateKey;
import java.security.KeyStore;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Societa {
    public static void main(String[] args) throws Exception {
        PrivateKey SocietaPrivateKey = null;
        PublicKey SocietaPublicKey = null;
        try {
            /* PK e PrK da client */
            File file = new File("Societykeystore.jks");
            FileInputStream is = new FileInputStream(file);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            // Information for certificate to be generated
            String password = "mario99";
            String alias = "sslSociety";
            // getting the key
            keystore.load(is, password.toCharArray());
            SocietaPrivateKey = (PrivateKey) keystore.getKey(alias, password.toCharArray());
            // Get certificate of public key
            X509Certificate cert = (X509Certificate) keystore.getCertificate(alias);
            SocietaPublicKey = cert.getPublicKey();

            // Here it prints the public key
            System.out.println("Public Key Society: ");
            System.out.println(SocietaPublicKey);
            // Here it prints the private key

        } catch (Exception e) {
            System.out.println(e);
        }
        TimeUnit.MILLISECONDS.sleep(60000);
        // seeAll();
        // SSLSocketFactory sockfact = (SSLSocketFactory) SSLSocketFactory.getDefault();
        // // similar to the server except
        // use SSLSocketFactory instead of SSLSocketServerFactory
        // SSLSocket SocSock = (SSLSocket) sockfact.createSocket("localhost", 4000); //
        // specify host and port
        // SocSock.startHandshake();
        SmartContract.computeFinalResult(SocietaPrivateKey, 4);

    }
}