
cd /Users/nando/Library/Mobile\ Documents/com\~apple\~CloudDocs/Unisa/Quarto\ anno/Secondo\ Semstre/Sicurezza/ProjectWork/Def/SicurezzaDef/SicurezzaDef/SicurezzaDef

##Client


javac -cp .:bcprov-jdk15on-159.jar Votante.java;java -Djavax.net.ssl.keyStore=Client3keystore.jks -Djavax.net.ssl.keyStorePassword=mario99 -Djavax.net.ssl.trustStore=truststoreClient3.jks -Djavax.net.ssl.trustStorePassword=mario99 -cp .:bcprov-jdk15on-159.jar Votante 3 00