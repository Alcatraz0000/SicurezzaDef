
cd /Users/nando/Library/Mobile\ Documents/com\~apple\~CloudDocs/Unisa/Quarto\ anno/Secondo\ Semstre/Sicurezza/ProjectWork/Def/SicurezzaDef/SicurezzaDef/SicurezzaDef

##Client


javac -cp .:bcprov-jdk15on-159.jar Votante.java;java -Djavax.net.ssl.keyStore=Client4keystore.jks -Djavax.net.ssl.keyStorePassword=mario99 -Djavax.net.ssl.trustStore=truststoreClient4.jks -Djavax.net.ssl.trustStorePassword=mario99 -cp .:bcprov-jdk15on-159.jar Votante 4 11