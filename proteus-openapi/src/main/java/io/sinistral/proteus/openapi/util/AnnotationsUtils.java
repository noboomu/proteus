package io.sinistral.proteus.openapi.util;

import io.sinistral.proteus.openapi.models.ExternalDocumentation;
import io.sinistral.proteus.openapi.models.info.Contact;
import io.sinistral.proteus.openapi.models.info.Info;
import io.sinistral.proteus.openapi.models.info.License;
import io.sinistral.proteus.openapi.models.media.Content;
import io.sinistral.proteus.openapi.models.media.MediaType;
import io.sinistral.proteus.openapi.models.servers.Server;
import io.sinistral.proteus.openapi.models.servers.ServerVariable;
import io.sinistral.proteus.openapi.models.tags.Tag;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Utilities for processing OpenAPI annotations
 */
public class AnnotationsUtils {

    /**
     * Get annotation of specific type from array
     */
    public static <T extends Annotation> T getAnnotation(Class<T> annotationClass, Annotation[] annotations) {
        if (annotations == null || annotationClass == null) {
            return null;
        }
        for (Annotation annotation : annotations) {
            if (annotationClass.isInstance(annotation)) {
                return annotationClass.cast(annotation);
            }
        }
        return null;
    }

    /**
     * Process Info annotation
     */
    public static Optional<Info> getInfo(io.swagger.v3.oas.annotations.info.Info infoAnnotation) {
        if (infoAnnotation == null) {
            return Optional.empty();
        }

        Info info = new Info();
        info.setTitle(infoAnnotation.title());
        info.setDescription(infoAnnotation.description());
        info.setVersion(infoAnnotation.version());
        info.setTermsOfService(infoAnnotation.termsOfService());

        if (infoAnnotation.contact() != null) {
            Contact contact = new Contact();
            contact.setName(infoAnnotation.contact().name());
            contact.setEmail(infoAnnotation.contact().email());
            contact.setUrl(infoAnnotation.contact().url());
            getExtensions(infoAnnotation.contact().extensions()).forEach(contact::addExtension);
            info.setContact(contact);
        }

        if (infoAnnotation.license() != null) {
            License license = new License();
            license.setName(infoAnnotation.license().name());
            license.setUrl(infoAnnotation.license().url());
            getExtensions(infoAnnotation.license().extensions()).forEach(license::addExtension);
            info.setLicense(license);
        }

        getExtensions(infoAnnotation.extensions()).forEach(info::addExtension);
        return Optional.of(info);
    }

    /**
     * Process ExternalDocumentation annotation
     */
    public static Optional<ExternalDocumentation> getExternalDocumentation(
        io.swagger.v3.oas.annotations.ExternalDocumentation annotation
    ) {
        if (annotation == null) {
            return Optional.empty();
        }

        ExternalDocumentation externalDocs = new ExternalDocumentation();
        externalDocs.setDescription(annotation.description());
        externalDocs.setUrl(annotation.url());
        getExtensions(annotation.extensions()).forEach(externalDocs::addExtension);

        return Optional.of(externalDocs);
    }

    /**
     * Process Tag annotations
     */
    public static Optional<List<Tag>> getTags(io.swagger.v3.oas.annotations.tags.Tag[] annotations, boolean skipEmpty) {
        if (annotations == null || annotations.length == 0) {
            return Optional.empty();
        }

        List<Tag> tags = new ArrayList<>();
        for (io.swagger.v3.oas.annotations.tags.Tag tagAnnotation : annotations) {
            if (skipEmpty && (tagAnnotation.name() == null || tagAnnotation.name().isEmpty())) {
                continue;
            }

            Tag tag = new Tag();
            tag.setName(tagAnnotation.name());
            tag.setDescription(tagAnnotation.description());

            if (tagAnnotation.externalDocs() != null) {
                getExternalDocumentation(tagAnnotation.externalDocs()).ifPresent(tag::setExternalDocs);
            }
            getExtensions(tagAnnotation.extensions()).forEach(tag::addExtension);

            tags.add(tag);
        }

        return tags.isEmpty() ? Optional.empty() : Optional.of(tags);
    }

    /**
     * Process Server annotations
     */
    public static Optional<List<Server>> getServers(io.swagger.v3.oas.annotations.servers.Server[] annotations) {
        if (annotations == null || annotations.length == 0) {
            return Optional.empty();
        }

        List<Server> servers = new ArrayList<>();
        for (io.swagger.v3.oas.annotations.servers.Server serverAnnotation : annotations) {
            getServer(serverAnnotation).ifPresent(servers::add);
        }

        return servers.isEmpty() ? Optional.empty() : Optional.of(servers);
    }

    public static Optional<Server> getServer(
        io.swagger.v3.oas.annotations.servers.Server serverAnnotation
    ) {
        if (serverAnnotation == null) return Optional.empty();

        boolean specified = !serverAnnotation.url().isBlank()
            || !serverAnnotation.description().isBlank()
            || serverAnnotation.variables().length > 0
            || serverAnnotation.extensions().length > 0;
        if (!specified) return Optional.empty();

        Server server = new Server();
        if (!serverAnnotation.url().isBlank()) server.setUrl(serverAnnotation.url());
        if (!serverAnnotation.description().isBlank()) {
            server.setDescription(serverAnnotation.description());
        }
        getExtensions(serverAnnotation.extensions()).forEach(server::addExtension);

        for (io.swagger.v3.oas.annotations.servers.ServerVariable varAnnotation : serverAnnotation.variables()) {
            ServerVariable variable = new ServerVariable();
            if (!varAnnotation.defaultValue().isBlank()) {
                variable.setDefault(varAnnotation.defaultValue());
            }
            if (!varAnnotation.description().isBlank()) {
                variable.setDescription(varAnnotation.description());
            }
            if (varAnnotation.allowableValues().length > 0
                && !varAnnotation.allowableValues()[0].isBlank()) {
                variable.setEnum(Arrays.asList(varAnnotation.allowableValues()));
            }
            getExtensions(varAnnotation.extensions()).forEach(variable::addExtension);
            server.addVariablesItem(varAnnotation.name(), variable);
        }
        return Optional.of(server);
    }

    /**
     * Get extensions from Extension annotations
     */
    public static Map<String, Object> getExtensions(io.swagger.v3.oas.annotations.extensions.Extension[] annotations) {
        Map<String, Object> extensions = new LinkedHashMap<>();
        if (annotations == null) return extensions;

        for (io.swagger.v3.oas.annotations.extensions.Extension extension : annotations) {
            String extensionName = extension.name();
            String key = extensionName.isBlank()
                ? ""
                : extensionName.startsWith("x-") ? extensionName : "x-" + extensionName;

            for (io.swagger.v3.oas.annotations.extensions.ExtensionProperty property : extension.properties()) {
                if (property.name().isBlank() || property.value().isBlank()) continue;
                Object value = property.value();
                if (property.parseValue()) {
                    try {
                        value = Json.mapper().readTree(property.value());
                    } catch (Exception ignored) {
                        // Preserve the source value when it is not valid JSON.
                    }
                }

                if (key.isBlank()) {
                    String propertyKey = property.name().startsWith("x-")
                        ? property.name()
                        : "x-" + property.name();
                    extensions.put(propertyKey, value);
                } else {
                    Object current = extensions.computeIfAbsent(key, ignored -> new LinkedHashMap<String, Object>());
                    if (current instanceof Map<?, ?> map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> properties = (Map<String, Object>) map;
                        properties.put(property.name(), value);
                    }
                }
            }
        }
        return extensions;
    }

    /**
     * Apply media types to content
     */
    public static void applyTypes(
        String[] classTypes,
        String[] methodTypes,
        Content content,
        MediaType mediaType
    ) {
        String[] types = methodTypes != null && methodTypes.length > 0 ? methodTypes : classTypes;
        if (types != null && types.length > 0) {
            for (String type : types) {
                content.addMediaType(type, mediaType);
            }
        } else {
            content.addMediaType("*/*", mediaType);
        }
    }
}
