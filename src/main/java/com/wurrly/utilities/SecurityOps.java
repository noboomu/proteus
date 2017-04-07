/**
 * 
 */
package com.wurrly.utilities;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.function.Function;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * @author jbauer
 *
 */
public class SecurityOps
{
    public static SSLContext createSSLContext(final KeyStore keyStore, final KeyStore trustStore, final String password) throws Exception {
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
    
    public static KeyStore loadKeyStore(String storePath, String password) throws Exception 
    {
         
    	try( final InputStream stream = Files.newInputStream(Paths.get(storePath)) )
    	{
    		if(stream == null) {
                throw new RuntimeException("Could not load keystore");
            }
        	 
            try(InputStream is = stream) 
            {
                
            	KeyStore loadedKeystore = KeyStore.getInstance("JKS");
                loadedKeystore.load(is, password.toCharArray());
                return loadedKeystore;
                
            } catch(Exception e)
            {
            	throw new RuntimeException("Could not load keystore: " + e.getMessage());
            } 
    	}
    	
    
         
    }
}
