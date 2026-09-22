package io.sinistral.proteus.server.compilation;

import io.undertow.server.RoutingHandler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * Compiles generated controller sources with the JDK compiler API.
 */
public class JdkControllerCompiler implements ControllerCompiler {

    /** Compiles a deterministic source batch and loads its route suppliers. */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Class<? extends Supplier<RoutingHandler>>> compile(
        Map<String, String> sources,
        ClassLoader parentClassLoader
    ) {
        if (sources.isEmpty()) {
            return Map.of();
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new ControllerCompilationException(
                "Generated controller compilation requires a JDK runtime compiler"
            );
        }

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (
            StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(
                diagnostics,
                null,
                null
            );
            MemoryJavaFileManager fileManager = new MemoryJavaFileManager(
                standardFileManager
            )
        ) {
            List<JavaFileObject> units = new ArrayList<>();
            sources.forEach((name, source) -> units.add(new SourceUnit(name, source)));
            List<String> options = List.of(
                "--release",
                Integer.toString(Runtime.version().feature()),
                "-classpath",
                System.getProperty("java.class.path")
            );
            Boolean compiled = compiler
                .getTask(null, fileManager, diagnostics, options, null, units)
                .call();
            if (!Boolean.TRUE.equals(compiled)) {
                throw new ControllerCompilationException(
                    formatDiagnostics(diagnostics)
                );
            }

            GeneratedClassLoader classLoader = new GeneratedClassLoader(
                parentClassLoader,
                fileManager.classBytes()
            );
            Map<String, Class<? extends Supplier<RoutingHandler>>> loaded = new LinkedHashMap<>();
            for (String className : sources.keySet()) {
                Class<?> generatedClass = classLoader.loadClass(className);
                if (!Supplier.class.isAssignableFrom(generatedClass)) {
                    throw new ControllerCompilationException(
                        "Generated class does not implement Supplier: " + className
                    );
                }
                loaded.put(
                    className,
                    (Class<? extends Supplier<RoutingHandler>>) generatedClass
                );
            }
            return Map.copyOf(loaded);
        } catch (IOException | ClassNotFoundException e) {
            throw new ControllerCompilationException(
                "Failed to load generated controller classes",
                e
            );
        }
    }

    /** Formats diagnostics from one failed batch compilation. */
    private String formatDiagnostics(
        DiagnosticCollector<JavaFileObject> diagnostics
    ) {
        StringBuilder message = new StringBuilder(
            "Generated controller compilation failed"
        );
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            message
                .append(System.lineSeparator())
                .append(diagnostic.getKind())
                .append(" ")
                .append(diagnostic.getSource() == null ? "<unknown>" : diagnostic.getSource().getName())
                .append(":")
                .append(diagnostic.getLineNumber())
                .append(":")
                .append(diagnostic.getColumnNumber())
                .append(" ")
                .append(diagnostic.getMessage(null));
        }
        return message.toString();
    }

    /** Holds generated Java source as a compiler input unit. */
    private static final class SourceUnit extends SimpleJavaFileObject {
        private final String source;

        private SourceUnit(String className, String source) {
            super(
                URI.create("string:///" + className.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension),
                JavaFileObject.Kind.SOURCE
            );
            this.source = source;
        }

        /** Returns the generated source text to the compiler. */
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return source;
        }
    }

    /** Captures generated bytecode without writing to disk. */
    private static final class ByteCodeUnit extends SimpleJavaFileObject {
        private final ByteArrayOutputStream output = new ByteArrayOutputStream();

        private ByteCodeUnit(String className) {
            super(
                URI.create("memory:///" + className.replace('.', '/') + JavaFileObject.Kind.CLASS.extension),
                JavaFileObject.Kind.CLASS
            );
        }

        /** Supplies an output stream for the compiler class file. */
        @Override
        public ByteArrayOutputStream openOutputStream() {
            return output;
        }

        /** Returns the compiled class bytes. */
        private byte[] bytes() {
            return output.toByteArray();
        }
    }

    /** Stores every bytecode output produced by one compiler batch. */
    private static final class MemoryJavaFileManager
        extends ForwardingJavaFileManager<StandardJavaFileManager> {
        private final Map<String, ByteCodeUnit> outputs = new LinkedHashMap<>();

        private MemoryJavaFileManager(StandardJavaFileManager delegate) {
            super(delegate);
        }

        /** Captures a generated class-file output unit. */
        @Override
        public JavaFileObject getJavaFileForOutput(
            JavaFileManager.Location location,
            String className,
            JavaFileObject.Kind kind,
            FileObject sibling
        ) {
            ByteCodeUnit output = new ByteCodeUnit(className);
            outputs.put(className, output);
            return output;
        }

        /** Returns compiled class bytes indexed by binary name. */
        private Map<String, byte[]> classBytes() {
            Map<String, byte[]> bytes = new LinkedHashMap<>();
            outputs.forEach((name, output) -> bytes.put(name, output.bytes()));
            return Map.copyOf(bytes);
        }
    }

    /** Defines generated classes only after a successful compiler batch. */
    private static final class GeneratedClassLoader extends ClassLoader {
        private final Map<String, byte[]> classes;

        private GeneratedClassLoader(ClassLoader parent, Map<String, byte[]> classes) {
            super(parent);
            this.classes = classes;
        }

        /** Defines a generated class from the completed compiler batch. */
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] bytes = classes.get(name);
            if (bytes == null) {
                throw new ClassNotFoundException(name);
            }
            return defineClass(name, bytes, 0, bytes.length);
        }
    }
}
