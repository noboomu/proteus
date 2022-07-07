package io.sinistral.proteus.utilities;

import com.google.common.base.Joiner;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import io.sinistral.proteus.server.handlers.HandlerGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassUtilities {

    private static final Logger logger = LoggerFactory.getLogger(ClassUtilities.class.getName());
    private static final Pattern TYPE_NAME_PATTERN = Pattern.compile("(java\\.util\\.[A-Za-z]+)<([^>]+)+", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static final Pattern CONCURRENT_TYPE_NAME_PATTERN = Pattern.compile("(java\\.util\\.concurrent\\.[A-Za-z]+)<([^>]+)", Pattern.DOTALL | Pattern.UNIX_LINES);
    private static final Pattern CLASS_NAME_PATTERN = Pattern.compile("([^<>,\\s]+)+", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    static Map<TypeToken<?>, List<TypeToken<?>>> typeTokenMap = new LinkedHashMap<>();

    public static String generateVariableName(TypeToken<?> typeToken) throws Exception {

        Collection<TypeToken<?>> typeTokenList = getGenericParameterTypeTokens(typeToken);

        visitTypes(typeTokenMap, typeToken);

        Deque<TypeToken<?>> tokenStack = new ArrayDeque<>();

        Map<TypeToken<?>, String> typeTokenNameMap = new LinkedHashMap<>();

        tokenStack.push(typeToken);

        for (TypeToken<?> tt : typeTokenList) {
            tokenStack.push(tt);
        }

        while (!tokenStack.isEmpty()) {
            TypeToken<?> next = tokenStack.pop();

            List<TypeToken<?>> subTypes = getGenericParameterTypeTokens(next);

            if (!subTypes.isEmpty()) {
                typeTokenMap.put(next, subTypes);
            } else {
                typeTokenMap.put(next, Collections.emptyList());
            }

            for (TypeToken<?> subType : subTypes) {
                getGenericParameterTypeTokens(subType);
            }
        }

        for (var entry : typeTokenMap.entrySet()) {

            TypeToken<?> k = entry.getKey();
            List<TypeToken<?>> v = entry.getValue();

            for (TypeToken<?> subType : v) {

                List<TypeToken<?>> parameters = typeTokenMap.getOrDefault(subType, new ArrayList<>());

                if (!parameters.isEmpty()) {

                    String subName = ClassUtilities.generateName(typeTokenNameMap, subType, parameters);

                    typeTokenNameMap.put(subType, subName);
                } else {

                    String subName = ClassUtilities.generateName(typeTokenNameMap, subType, Collections.emptyList());

                    typeTokenNameMap.put(subType, subName);

                }

            }

            if (v.isEmpty()) {
                String subName = ClassUtilities.generateName(typeTokenNameMap, k, Collections.emptyList());

                typeTokenNameMap.put(k, subName);

            } else {

                String existing = ClassUtilities.generateName(typeTokenNameMap, k, v);

                typeTokenNameMap.put(k, existing);
            }
        }

        return typeTokenNameMap.get(typeToken);

    }

    static String typeToString(Type type) {
        return (type instanceof Class) ? ((Class<?>) type).getName() : type.toString();
    }

 private   static List<TypeToken<?>> getGenericParameterTypeTokens(TypeToken<?> t) {

        Class<?> rawType = t.getRawType();

        TypeVariable<?>[] pT = rawType.getTypeParameters();

        List<TypeToken<?>> pTT = new ArrayList<>();

        for (TypeVariable<?> typeVariable : pT) {
            TypeToken<?> token = t.resolveType(typeVariable);
            pTT.add(token);
        }

        return pTT;
    }


    public static String typeReferenceNameForParameterizedType(Type type) {

      //  logger.info("creating name for reference: {}", type);
        String typeName = type.getTypeName();

        if (typeName.contains("Optional")) {
            logger.debug("Type is for an optional named {}", typeName);
        }

        Matcher matcher = TYPE_NAME_PATTERN.matcher(typeName);

        if (matcher.find()) {

            int matches = matcher.groupCount();

            if (matches == 2) {
                String genericInterface = matcher.group(1);
                String erasedType = matcher.group(2).replaceAll("\\$", ".");

                String[] genericParts = genericInterface.split("\\.");
                String[] erasedParts = erasedType.split("\\.");

                String genericTypeName = genericParts[genericParts.length - 1];
                String erasedTypeName;

                if (erasedParts.length > 1) {
                    erasedTypeName = erasedParts[erasedParts.length - 2] + erasedParts[erasedParts.length - 1];
                } else {
                    erasedTypeName = erasedParts[0];
                }

                typeName = String.format("%s%s%s", Character.toLowerCase(erasedTypeName.charAt(0)), erasedTypeName.substring(1), genericTypeName);

                return typeName;
            }

        }

        matcher = CONCURRENT_TYPE_NAME_PATTERN.matcher(typeName);

        if (matcher.find()) {

            int matches = matcher.groupCount();

            if (matches == 2) {
                String genericInterface = matcher.group(1);
                String erasedType = matcher.group(2).replaceAll("\\$", ".");

                String[] genericParts = genericInterface.split("\\.");
                String[] erasedParts = erasedType.split("\\.");

                String genericTypeName = genericParts[genericParts.length - 1];
                String erasedTypeName;

                if (erasedParts.length > 1) {
                    erasedTypeName = erasedParts[erasedParts.length - 2] + erasedParts[erasedParts.length - 1];
                } else {
                    erasedTypeName = erasedParts[0];
                }

                typeName = String.format("%s%s%s", Character.toLowerCase(erasedTypeName.charAt(0)), erasedTypeName.substring(1), genericTypeName);
                return typeName;
            }

        }

        if (type.getTypeName().startsWith("sun")) {
            return typeName;
        }

        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
          //  logger.info("pType: {}", pType);

            Type actualTypeArgument0 = pType.getActualTypeArguments()[0];

            if (actualTypeArgument0 instanceof Class) {
                Class<?> genericType = (Class<?>) pType.getActualTypeArguments()[0];
                Class<?> rawType = (Class<?>) pType.getRawType();
                Class<?> erasedType = (Class<?>) HandlerGenerator.extractErasedType(genericType);

                if (!(pType.getRawType() instanceof ParameterizedType)) {
//                    logger.info("not a raw type that is parameterized {} {}", rawType, genericType);
                    return Character.toLowerCase(rawType.getSimpleName().charAt(0)) + rawType.getSimpleName().substring(1) + genericType.getSimpleName();
                }
            } else {
                logger.error(
                        "failed to process {} ptype: {}", type, pType
                );
            }

        }

        return typeName;
    }

    private static String generateName(Map<TypeToken<?>, String> nameMap, TypeToken<?> k, List<TypeToken<?>> v) {
        Class<?> rawType = k.getRawType();

        String parentName = rawType.getCanonicalName().replaceAll("[$]+", ".");

        List<String> parts = new ArrayList<>(Arrays.asList(parentName.split("[.]+")));

        List<String> partNames = new ArrayList<>();

        if (!v.isEmpty()) {

            for (TypeToken<?> sub : v) {
                String name = nameMap.get(sub);

                if (name != null) {
                    partNames.add(name);
                }
            }

        }

        for (String p : parts) {

            partNames.add(StringUtils.capitalize(p));
        }

        parentName = String.join("", partNames);

        return StringUtils.uncapitalize(parentName);

    }

    private static void visitTypes(Map<TypeToken<?>, List<TypeToken<?>>> map, TypeToken<?> token) {
        List<TypeToken<?>> subTypes = getGenericParameterTypeTokens(token);

        for (TypeToken<?> subType : subTypes) {
            visitTypes(map, subType);
        }

        if (!subTypes.isEmpty()) {
            map.put(token, subTypes);
        } else {
            map.put(token, Collections.emptyList());
        }


    }

}



