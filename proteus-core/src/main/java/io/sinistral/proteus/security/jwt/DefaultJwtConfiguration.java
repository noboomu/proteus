package io.sinistral.proteus.security.jwt;

import com.google.inject.Singleton;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Default implementation of JwtConfiguration that reads {@code proteus.security.jwt} from
 * Typesafe Config. It supports RSA and EC public keys from files, classpath resources, or
 * inline PEM, plus plain-text, Base64, or file-backed HMAC secrets.
 *
 * <p>Defaults require signatures and expiration, allow {@code RS256}, {@code ES256}, and
 * {@code HS256}, and permit 30 seconds of clock skew. Invalid Base64 HMAC configuration and
 * multiple configured issuers with one global verification-key set fail construction. Individual
 * unreadable RSA, EC, or HMAC key files are logged and skipped.
 *
 * @since 1.0
 */
@Singleton
public class DefaultJwtConfiguration implements JwtConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DefaultJwtConfiguration.class);

    private final List<RSAPublicKey> rsaPublicKeys = new ArrayList<>();
    private final List<ECPublicKey> ecPublicKeys = new ArrayList<>();
    private final List<byte[]> hmacSecrets = new ArrayList<>();
    private final List<String> allowedAlgorithms;
    private final List<String> allowedIssuers;
    private final List<String> allowedAudiences;
    private final long clockSkewToleranceSeconds;
    private final boolean expirationRequired;
    private final boolean issuerRequired;
    private final boolean audienceRequired;
    private final long maxTokenAgeSeconds;
    private final boolean signatureVerificationRequired;

    /**
     * Reads and validates the configured JWT policy and verification material.
     *
     * @param config application configuration containing the optional
     *               {@code proteus.security.jwt} object
     * @throws IllegalArgumentException for invalid Base64 HMAC material or an unsupported
     *                                  multi-issuer/global-key configuration
     */
    public DefaultJwtConfiguration(Config config) {
        // Load validation settings
        Config jwtConfig = config.hasPath("proteus.security.jwt") ?
                          config.getConfig("proteus.security.jwt") :
                          config.withFallback(com.typesafe.config.ConfigFactory.parseString(getDefaultConfig()));

        this.allowedIssuers = getStringList(jwtConfig, "allowedIssuers");
        this.allowedAudiences = getStringList(jwtConfig, "allowedAudiences");
        this.allowedAlgorithms = jwtConfig.hasPath("allowedAlgorithms")
            ? List.copyOf(jwtConfig.getStringList("allowedAlgorithms"))
            : List.of("RS256", "ES256", "HS256");
        this.clockSkewToleranceSeconds = jwtConfig.hasPath("clockSkewToleranceSeconds") ?
                                        jwtConfig.getLong("clockSkewToleranceSeconds") : 30L;
        this.expirationRequired = jwtConfig.hasPath("expirationRequired") ?
                                 jwtConfig.getBoolean("expirationRequired") : true;
        this.issuerRequired = jwtConfig.hasPath("issuerRequired") ?
                             jwtConfig.getBoolean("issuerRequired") : false;
        this.audienceRequired = jwtConfig.hasPath("audienceRequired") ?
                               jwtConfig.getBoolean("audienceRequired") : false;
        this.maxTokenAgeSeconds = jwtConfig.hasPath("maxTokenAgeSeconds") ?
                                 jwtConfig.getLong("maxTokenAgeSeconds") : 0L;
        this.signatureVerificationRequired = jwtConfig.hasPath("signatureVerificationRequired") ?
                                           jwtConfig.getBoolean("signatureVerificationRequired") : true;

        // Load signing keys
        loadRsaKeys(jwtConfig);
        loadEcKeys(jwtConfig);
        loadHmacSecrets(jwtConfig);
        validateIssuerKeyIsolation();

        logger.info("JWT Configuration loaded: RSA keys: {}, EC keys: {}, HMAC secrets: {}, " +
                   "allowed issuers: {}, allowed audiences: {}",
                   rsaPublicKeys.size(), ecPublicKeys.size(), hmacSecrets.size(),
                   allowedIssuers.size(), allowedAudiences.size());
    }

    private void validateIssuerKeyIsolation() {
        boolean hasVerificationKeys = !rsaPublicKeys.isEmpty() ||
            !ecPublicKeys.isEmpty() ||
            !hmacSecrets.isEmpty();
        if (hasVerificationKeys && allowedIssuers.size() > 1) {
            throw new IllegalArgumentException(
                "Multiple allowedIssuers cannot share the global JWT verification " +
                "key set; configure at most one issuer"
            );
        }
    }

    private List<String> getStringList(Config config, String path) {
        if (config.hasPath(path)) {
            return config.getStringList(path);
        }
        return List.of();
    }

    private void loadRsaKeys(Config config) {
        if (config.hasPath("rsa.publicKeys")) {
            List<String> keyPaths = config.getStringList("rsa.publicKeys");
            for (String keyPath : keyPaths) {
                try {
                    RSAPublicKey key = loadRsaPublicKey(keyPath);
                    if (key != null) {
                        rsaPublicKeys.add(key);
                        logger.debug("Loaded RSA public key from: {}", keyPath);
                    }
                } catch (Exception e) {
                    logger.warn("Failed to load RSA public key from: {}", keyPath, e);
                }
            }
        }

        if (config.hasPath("rsa.publicKeysPem")) {
            List<String> pemKeys = config.getStringList("rsa.publicKeysPem");
            for (String pemKey : pemKeys) {
                try {
                    RSAPublicKey key = parseRsaPublicKeyFromPem(pemKey);
                    if (key != null) {
                        rsaPublicKeys.add(key);
                        logger.debug("Loaded RSA public key from inline PEM");
                    }
                } catch (Exception e) {
                    logger.warn("Failed to parse inline RSA public key", e);
                }
            }
        }
    }

    private void loadEcKeys(Config config) {
        if (config.hasPath("ec.publicKeys")) {
            List<String> keyPaths = config.getStringList("ec.publicKeys");
            for (String keyPath : keyPaths) {
                try {
                    ECPublicKey key = loadEcPublicKey(keyPath);
                    if (key != null) {
                        ecPublicKeys.add(key);
                        logger.debug("Loaded EC public key from: {}", keyPath);
                    }
                } catch (Exception e) {
                    logger.warn("Failed to load EC public key from: {}", keyPath, e);
                }
            }
        }
    }

    private void loadHmacSecrets(Config config) {
        if (config.hasPath("hmac.secrets")) {
            for (String secret : config.getStringList("hmac.secrets")) {
                hmacSecrets.add(secret.getBytes(StandardCharsets.UTF_8));
                logger.debug("Loaded plain-text HMAC secret");
            }
        }

        if (config.hasPath("hmac.secretsBase64")) {
            for (String secret : config.getStringList("hmac.secretsBase64")) {
                try {
                    hmacSecrets.add(Base64.getDecoder().decode(secret));
                    logger.debug("Loaded base64-encoded HMAC secret");
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(
                        "Invalid base64 HMAC secret in proteus.security.jwt.hmac.secretsBase64",
                        e
                    );
                }
            }
        }

        if (config.hasPath("hmac.secretFiles")) {
            List<String> secretFiles = config.getStringList("hmac.secretFiles");
            for (String secretFile : secretFiles) {
                try {
                    byte[] secretBytes = Files.readAllBytes(Paths.get(secretFile));
                    hmacSecrets.add(secretBytes);
                    logger.debug("Loaded HMAC secret from file: {}", secretFile);
                } catch (Exception e) {
                    logger.warn("Failed to load HMAC secret from file: {}", secretFile, e);
                }
            }
        }
    }

    private RSAPublicKey loadRsaPublicKey(String keyPath) throws Exception {
        if (keyPath.startsWith("classpath:")) {
            String resourcePath = keyPath.substring("classpath:".length());
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
                if (is == null) {
                    throw new IOException("Resource not found: " + resourcePath);
                }
                return parseRsaPublicKey(is);
            }
        } else {
            try (FileInputStream fis = new FileInputStream(keyPath)) {
                return parseRsaPublicKey(fis);
            }
        }
    }

    private ECPublicKey loadEcPublicKey(String keyPath) throws Exception {
        if (keyPath.startsWith("classpath:")) {
            String resourcePath = keyPath.substring("classpath:".length());
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
                if (is == null) {
                    throw new IOException("Resource not found: " + resourcePath);
                }
                return parseEcPublicKey(is);
            }
        } else {
            try (FileInputStream fis = new FileInputStream(keyPath)) {
                return parseEcPublicKey(fis);
            }
        }
    }

    private RSAPublicKey parseRsaPublicKey(InputStream keyStream) throws Exception {
        String keyContent = new String(keyStream.readAllBytes(), "UTF-8");
        return parseRsaPublicKeyFromPem(keyContent);
    }

    private RSAPublicKey parseRsaPublicKeyFromPem(String pemContent) throws Exception {
        // Handle both certificate and public key formats
        if (pemContent.contains("BEGIN CERTIFICATE")) {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            String cert = pemContent.replaceAll("-----\\w+ CERTIFICATE-----", "")
                                   .replaceAll("\\s", "");
            byte[] certBytes = Base64.getDecoder().decode(cert);
            X509Certificate certificate = (X509Certificate) factory.generateCertificate(
                    new ByteArrayInputStream(certBytes));
            return (RSAPublicKey) certificate.getPublicKey();
        } else if (pemContent.contains("BEGIN PUBLIC KEY")) {
            String key = pemContent.replaceAll("-----\\w+ PUBLIC KEY-----", "")
                                  .replaceAll("\\s", "");
            byte[] keyBytes = Base64.getDecoder().decode(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) factory.generatePublic(spec);
        } else {
            throw new IllegalArgumentException("Unsupported key format");
        }
    }

    private ECPublicKey parseEcPublicKey(InputStream keyStream) throws Exception {
        String keyContent = new String(keyStream.readAllBytes(), "UTF-8");
        String key = keyContent.replaceAll("-----\\w+ PUBLIC KEY-----", "")
                              .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance("EC");
        return (ECPublicKey) factory.generatePublic(spec);
    }

    private String getDefaultConfig() {
        return """
            proteus.security.jwt {
                clockSkewToleranceSeconds = 30
                expirationRequired = true
                issuerRequired = false
                audienceRequired = false
                maxTokenAgeSeconds = 0
                signatureVerificationRequired = true
                allowedAlgorithms = ["RS256", "ES256", "HS256"]
                allowedIssuers = []
                allowedAudiences = []
            }
            """;
    }

    @Override
    public List<RSAPublicKey> getRsaPublicKeys() {
        return List.copyOf(rsaPublicKeys);
    }

    @Override
    public List<ECPublicKey> getEcPublicKeys() {
        return List.copyOf(ecPublicKeys);
    }

    @Override
    public List<byte[]> getHmacSecrets() {
        return hmacSecrets.stream().map(byte[]::clone).toList();
    }

    @Override
    public List<String> getAllowedAlgorithms() {
        return allowedAlgorithms;
    }

    @Override
    public List<String> getAllowedIssuers() {
        return allowedIssuers;
    }

    @Override
    public List<String> getAllowedAudiences() {
        return allowedAudiences;
    }

    @Override
    public long getClockSkewToleranceSeconds() {
        return clockSkewToleranceSeconds;
    }

    @Override
    public boolean isExpirationRequired() {
        return expirationRequired;
    }

    @Override
    public boolean isIssuerRequired() {
        return issuerRequired;
    }

    @Override
    public boolean isAudienceRequired() {
        return audienceRequired;
    }

    @Override
    public long getMaxTokenAgeSeconds() {
        return maxTokenAgeSeconds;
    }

    @Override
    public boolean isSignatureVerificationRequired() {
        return signatureVerificationRequired;
    }
}
