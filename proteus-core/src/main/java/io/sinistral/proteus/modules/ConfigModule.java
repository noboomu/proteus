/**
 *
 */
package io.sinistral.proteus.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.util.Types;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import java.io.File;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Guice module that loads Typesafe Config and binds every configuration value as a named
 * constant or nested {@link Config}, with reference defaults as fallback.
 *
 * <p>Much of this is taken with reverence from Jooby.
 *
 * @author jbauer
 */
@Singleton
public class ConfigModule extends AbstractModule {

    /** The log. */
    private static Logger log = LoggerFactory.getLogger(
        ConfigModule.class.getCanonicalName()
    );

    /** Config file path, or null when a URL or default resolution is used. */
    protected String configFile = null;
    /** Config URL, or null when a file path or default resolution is used. */
    protected URL configURL = null;
    /** The merged configuration bound by this module, or null before configure runs. */
    protected Config config = null;

    /** Creates a module that loads {@code config.file} or {@code application.conf}. */
    public ConfigModule() {
        this.configFile = System.getProperty("config.file");

        if (this.configFile == null) {
            this.configFile = "application.conf";
        }
    }

    /**
     * Creates a module that loads the config file at the given path.
     *
     * @param configFile the config file path relative to the working directory
     */
    public ConfigModule(String configFile) {
        this.configFile = configFile;
    }

    /**
     * Creates a module that loads the config at the given URL.
     *
     * @param configURL the config resource URL
     */
    public ConfigModule(URL configURL) {
        this.configURL = configURL;
    }

    /**
     * Returns the merged configuration, or null before {@link #configure()} ran.
     *
     * @return the bound configuration
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Replaces the exposed configuration.
     *
     * @param config the configuration to expose
     */
    public void setConfig(Config config) {
        this.config = config;
    }

    @SuppressWarnings("unchecked")
    private void bindConfig(final Config config) {
        traverse(this.binder(), "", config.root());

        for (Entry<String, ConfigValue> entry : config.entrySet()) {
            String name = entry.getKey();
            Named named = Names.named(name);
            Object value = entry.getValue().unwrapped();

            if (value instanceof List) {
                List<Object> values = (List<Object>) value;
                Type listType = (values.size() == 0)
                    ? String.class
                    : Types.listOf(values.iterator().next().getClass());
                Key<Object> key = (Key<Object>) Key.get(
                    listType,
                    Names.named(name)
                );

                this.binder().bind(key).toInstance(values);
            } else {
                this.binder()
                    .bindConstant()
                    .annotatedWith(named)
                    .to(value.toString());
            }
        }

        Config referenceConfig = ConfigFactory.load(
            ConfigFactory.defaultReference()
        );

        this.config = ConfigFactory.load(config).withFallback(referenceConfig);

        log.trace(this.config.toString());

        this.binder().bind(Config.class).toInstance(config);
    }

    /** Loads application config over reference defaults and binds every path as a named constant. */
    @Override
    protected void configure() {
        Config config = ConfigFactory.defaultApplication();
        Config referenceConfig = ConfigFactory.load(
            ConfigFactory.defaultReference()
        );

        config = ConfigFactory.load(config).withFallback(referenceConfig);

        if (configURL != null) {
            config = ConfigFactory.load(
                ConfigFactory.parseURL(configURL)
            ).withFallback(config);
        } else if (configFile != null) {
            config = fileConfig(configFile).withFallback(config);
        }

        this.bindConfig(config);

        install(new ApplicationModule(this.config));
    }

    private static Config fileConfig(final String fileName) {
        File userDirectory = new File(System.getProperty("user.dir"));
        File fileRoot = new File(userDirectory, fileName);

        if (fileRoot.exists()) {
            return ConfigFactory.load(ConfigFactory.parseFile(fileRoot));
        } else {
            File fileConfig = new File(
                new File(userDirectory, "conf"),
                fileName
            );

            if (fileConfig.exists()) {
                return ConfigFactory.load(ConfigFactory.parseFile(fileConfig));
            }
        }

        return ConfigFactory.empty();
    }

    private static void traverse(
        final Binder binder,
        final String nextPath,
        final ConfigObject rootConfig
    ) {
        rootConfig.forEach((key, value) -> {
            if (value instanceof ConfigObject) {
                try {
                    ConfigObject child = (ConfigObject) value;
                    String path = nextPath + key;

                    Named named = Names.named(path);

                    binder
                        .bind(Config.class)
                        .annotatedWith(named)
                        .toInstance(child.toConfig());

                    traverse(binder, path + ".", child);
                } catch (Exception e) {
                    log.error("Error binding " + value, e);
                }
            }
        });
    }
}
