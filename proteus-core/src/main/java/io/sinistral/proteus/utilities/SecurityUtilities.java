/**
 *
 */
package io.sinistral.proteus.utilities;

import javax.net.ssl.*;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;

/**
 * @author jbauer
 */
public class SecurityUtilities
{
    public static SSLContext createSSLContext(final KeyStore keyStore, final KeyStore trustStore, final String password) throws Exception
    {
        KeyManager[] keyManagers;
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

        keyManagerFactory.init(keyStore, password.toCharArray());

        keyManagers = keyManagerFactory.getKeyManagers();

        TrustManager[] trustManagers;
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

        trustManagerFactory.init(trustStore);

        trustManagers = trustManagerFactory.getTrustManagers();

        SSLContext sslContext;

        sslContext = SSLContext.getInstance("TLS");

        sslContext.init(keyManagers, trustManagers, null);

        return sslContext;
    }

    @SuppressWarnings("resource")
    public static KeyStore loadKeyStore(String name, String password) throws Exception
    {
        File storeFile = new File(name);
        InputStream stream = null;

        if (!storeFile.exists()) {
            stream = SecurityUtilities.class.getResourceAsStream("/" + name);
        } else {
            stream = Files.newInputStream(Paths.get(name));
        }

        if (stream == null) {
            throw new RuntimeException("Could not load keystore");
        }

        try (InputStream is = stream) {

            KeyStore loadedKeystore = KeyStore.getInstance("JKS");

            loadedKeystore.load(is, password.toCharArray());

            return loadedKeystore;
        }
    }
}



