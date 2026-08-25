package io.sinistral.proteus.security.jwt;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.typesafe.config.ConfigFactory;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class DefaultJwtConfigurationTest {

    @Test
    public void hmacSecretsAreReadAsPlainUtf8WithoutBase64Guessing() {
        String secret = "0123456789abcdef0123456789abcdef";
        DefaultJwtConfiguration configuration = new DefaultJwtConfiguration(
            ConfigFactory.parseString(
                "proteus.security.jwt.hmac.secrets=[\"" + secret + "\"]"
            )
        );

        assertArrayEquals(
            secret.getBytes(StandardCharsets.UTF_8),
            configuration.getHmacSecrets().getFirst()
        );
    }

    @Test
    public void globalVerificationKeysCannotSpanMultipleIssuers() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new DefaultJwtConfiguration(
                ConfigFactory.parseString("""
                    proteus.security.jwt {
                      hmac.secrets = ["issuer-a-hmac-secret-32-bytes-0001"]
                      allowedIssuers = ["issuer-a", "issuer-b"]
                    }
                    """)
            )
        );
    }
}
