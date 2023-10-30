package org.example;

import io.undertow.Undertow;
import io.undertow.util.Headers;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

            Undertow server = Undertow.builder()
                // Set up the listener - you can change the port/host here
                // 0.0.0.0 means "listen on ALL available addresses"
                .addHttpListener(80, "0.0.0.0")
                    .addHttpsListener(
                            443,
                            "0.0.0.0",
                            serverSslContext("password1", "password2", "password3")
                    )
                .setHandler(exchange -> {
                    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
                    exchange.setRequestURI("/welcome");
                    exchange.getResponseSender().send("<html>" +
                                    "<body>" +
                                    "<h1>Hello, world!, Hello buddy How are you ?</h1>" +
                                    "</body>" +
                                    "</html>");
                }).build();
        server.start();

    }

    public static SSLContext serverSslContext(String password1, String password2, String password3) {
        try {
            KeyStore keyStore = loadKeyStore("C:\\intellij-projects\\server-undertow\\cert-resources\\my-keystore.jks", password1);
            KeyStore trustStore = loadKeyStore("C:\\intellij-projects\\server-undertow\\cert-resources\\my-truststore.ts", password3);
            return createSSLContext(keyStore, trustStore, password2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static SSLContext createSSLContext(final KeyStore keyStore, final KeyStore trustStore, String keyStorePassword) throws IOException {
        try {
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            return sslContext;
        } catch (Exception e) {
            throw new IOException("Unable to create and initialise the SSLContext", e);
        }
    }

    private static KeyStore loadKeyStore(final String name, String password) throws IOException {
        try(InputStream stream = new FileInputStream(name)) {
            KeyStore loadedKeystore = KeyStore.getInstance("JKS");
            loadedKeystore.load(stream, password.toCharArray());
            return loadedKeystore;
        } catch (Exception e) {
            throw new IOException(String.format("Unable to load KeyStore %s", name), e);
        }
    }


}
