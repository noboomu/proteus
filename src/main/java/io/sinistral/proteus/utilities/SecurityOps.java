/**
 * 
 */
package io.sinistral.proteus.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.KeyStore;
import java.util.jar.JarFile;
import java.util.zip.ZipInputStream;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jbauer
 */
public class SecurityOps
{

	@SuppressWarnings("resource")
	public static KeyStore loadKeyStore(String name, String password) throws Exception
	{

		File storeFile = new File(name);

		InputStream stream = null;

		if (!storeFile.exists())
		{
			stream = SecurityOps.class.getResourceAsStream("/" + name);

		}
		else
		{

			stream = Files.newInputStream(Paths.get(name));
		}

		if (stream == null)
		{
			throw new RuntimeException("Could not load keystore");
		}

		try (InputStream is = stream)
		{
			KeyStore loadedKeystore = KeyStore.getInstance("JKS");
			loadedKeystore.load(is, password.toCharArray());
			return loadedKeystore;
		}
	}

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

}
