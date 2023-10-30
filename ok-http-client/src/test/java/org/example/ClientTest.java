package org.example;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit test for simple App.
 */
public class ClientTest {
    private final String HTTPS_WELCOME_URL = "https://localhost/welcome";

    private static OkHttpClient newClient;

    @BeforeClass
    public static void init() throws NoSuchAlgorithmException, KeyManagementException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        final TrustManager TRUST_ALL_CERTS = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }
        };
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[] { TRUST_ALL_CERTS }, new java.security.SecureRandom());
        builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) TRUST_ALL_CERTS);
        builder.hostnameVerifier((hostname, session) -> true);
        newClient = builder.build();
    }

    @Test
    public void trustSelfSignedSSLCert() throws IOException {
        Response response = newClient.newCall(new Request.Builder().url(HTTPS_WELCOME_URL).build()).execute();
        assertEquals(200, response.code());
        assertNotNull(response.body());
        assertEquals("<html><body><h1>Hello, world!, Hello buddy How are you ?</h1></body></html>", response.body().string());
    }

    @Test
    public void trustSelfSignedSSLCert1() throws IOException {
        Response response = newClient.newCall(new Request.Builder().url(HTTPS_WELCOME_URL).build()).execute();
        assertEquals(200, response.code());
        assertNotNull(response.body());
        assertEquals("<html><body><h1>Hello, world!, Hello buddy How are you ?</h1></body></html>", response.body().string());
    }
}
