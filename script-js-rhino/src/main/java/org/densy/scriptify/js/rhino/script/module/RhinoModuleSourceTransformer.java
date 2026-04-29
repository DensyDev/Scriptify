package org.densy.scriptify.js.rhino.script.module;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RhinoModuleSourceTransformer {

    private static final String IDENTIFIER = "[A-Za-z_$][A-Za-z0-9_$]*";

    private static final Pattern IMPORT_STAR = Pattern.compile(
            "(?m)^\\s*import\\s+\\*\\s+as\\s+(" + IDENTIFIER + ")\\s+from\\s+['\"]([^'\"]+)['\"]\\s*;?\\s*$"
    );
    private static final Pattern IMPORT_NAMED = Pattern.compile(
            "(?m)^\\s*import\\s+\\{([^}]+)}\\s+from\\s+['\"]([^'\"]+)['\"]\\s*;?\\s*$"
    );
    private static final Pattern IMPORT_DEFAULT = Pattern.compile(
            "(?m)^\\s*import\\s+(" + IDENTIFIER + ")\\s+from\\s+['\"]([^'\"]+)['\"]\\s*;?\\s*$"
    );
    private static final Pattern IMPORT_SIDE_EFFECT = Pattern.compile(
            "(?m)^\\s*import\\s+['\"]([^'\"]+)['\"]\\s*;?\\s*$"
    );
    private static final Pattern EXPORT_NAMED = Pattern.compile(
            "(?m)^\\s*export\\s*\\{([^}]+)}\\s*;?\\s*$"
    );
    private static final Pattern EXPORT_DECLARATION = Pattern.compile(
            "(?m)^\\s*export\\s+(const|let|var|function|class)\\s+(" + IDENTIFIER + ")"
    );
    private static final Pattern EXPORT_DEFAULT = Pattern.compile("\\bexport\\s+default\\s+");

    private RhinoModuleSourceTransformer() {}

    public static String transformScript(String source) {
        return transformImports(source);
    }

    public static String transformModule(String source) {
        List<String> declaredExports = new ArrayList<>();
        String transformed = transformImports(source);

        var declarationMatcher = EXPORT_DECLARATION.matcher(transformed);
        var declarationBuffer = new StringBuilder();
        while (declarationMatcher.find()) {
            declaredExports.add(declarationMatcher.group(2));
            declarationMatcher.appendReplacement(
                    declarationBuffer,
                    Matcher.quoteReplacement(declarationMatcher.group(1) + " " + declarationMatcher.group(2))
            );
        }
        declarationMatcher.appendTail(declarationBuffer);
        transformed = declarationBuffer.toString();

        var namedMatcher = EXPORT_NAMED.matcher(transformed);
        var namedBuffer = new StringBuilder();
        while (namedMatcher.find()) {
            namedMatcher.appendReplacement(
                    namedBuffer,
                    Matcher.quoteReplacement(toExportAssignments(namedMatcher.group(1)))
            );
        }
        namedMatcher.appendTail(namedBuffer);
        transformed = namedBuffer.toString();

        transformed = EXPORT_DEFAULT.matcher(transformed).replaceAll("exports.default = ");

        if (!declaredExports.isEmpty()) {
            StringBuilder builder = new StringBuilder(transformed);
            builder.append("\n");
            for (String export : declaredExports) {
                builder.append("exports.").append(export).append(" = ").append(export).append(";\n");
            }
            transformed = builder.toString();
        }

        return transformed;
    }

    private static String transformImports(String source) {
        String transformed = replaceAll(IMPORT_STAR, source, matcher ->
                "const " + matcher.group(1) + " = __scriptify_require(\"" + escape(matcher.group(2)) + "\");"
        );
        transformed = replaceAll(IMPORT_NAMED, transformed, matcher ->
                "const { " + toDestructuringSpec(matcher.group(1)) + " } = __scriptify_require(\"" + escape(matcher.group(2)) + "\");"
        );
        transformed = replaceAll(IMPORT_DEFAULT, transformed, matcher ->
                "const " + matcher.group(1) + " = __scriptify_require(\"" + escape(matcher.group(2)) + "\").default;"
        );
        return replaceAll(IMPORT_SIDE_EFFECT, transformed, matcher ->
                "__scriptify_require(\"" + escape(matcher.group(1)) + "\");"
        );
    }

    private static String toDestructuringSpec(String spec) {
        List<String> parts = new ArrayList<>();
        for (String item : spec.split(",")) {
            String trimmed = item.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            String[] alias = trimmed.split("\\s+as\\s+");
            if (alias.length == 2) {
                parts.add(alias[0].trim() + ": " + alias[1].trim());
            } else {
                parts.add(trimmed);
            }
        }
        return String.join(", ", parts);
    }

    private static String toExportAssignments(String spec) {
        StringBuilder builder = new StringBuilder();
        for (String item : spec.split(",")) {
            String trimmed = item.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            String[] alias = trimmed.split("\\s+as\\s+");
            if (alias.length == 2) {
                builder.append("exports.")
                        .append(alias[1].trim())
                        .append(" = ")
                        .append(alias[0].trim())
                        .append(";\n");
            } else {
                builder.append("exports.").append(trimmed).append(" = ").append(trimmed).append(";\n");
            }
        }
        return builder.toString();
    }

    private static String replaceAll(Pattern pattern, String source, Function<Matcher, String> replacement) {
        Matcher matcher = pattern.matcher(source);
        StringBuilder buffer = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(replacement.apply(matcher)));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
