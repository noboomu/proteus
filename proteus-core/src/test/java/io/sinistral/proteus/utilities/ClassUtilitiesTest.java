package io.sinistral.proteus.utilities;

import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassUtilitiesTest {



    @Test
    void parseLonger() throws Exception
    {
        TypeToken<Set<String>> simpleToken = new TypeToken<>() {};


        var result = ClassUtilities.generateVariableName(simpleToken);

        assertEquals("javaLangStringJavaUtilSet",result);

        TypeToken<Map<String, Map<UUID, Set<Long>>>> complexToken = new TypeToken<>() {};
        result = ClassUtilities.generateVariableName(complexToken);

        assertEquals("javaLangStringjavaUtilUUIDjavaLangLongJavaUtilSetJavaUtilMapJavaUtilMap",result);


    }

}