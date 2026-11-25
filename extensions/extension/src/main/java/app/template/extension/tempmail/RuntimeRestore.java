package app.template.extension.tempmail;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Resolves stored method signatures to real methods in the patched app. */
public final class RuntimeRestore {
    private static final Map<String, Class<?>> PRIMITIVES = new HashMap<>();

    static {
        PRIMITIVES.put("boolean", boolean.class);
        PRIMITIVES.put("byte", byte.class);
        PRIMITIVES.put("char", char.class);
        PRIMITIVES.put("short", short.class);
        PRIMITIVES.put("int", int.class);
        PRIMITIVES.put("long", long.class);
        PRIMITIVES.put("float", float.class);
        PRIMITIVES.put("double", double.class);
        PRIMITIVES.put("void", void.class);
    }

    private RuntimeRestore() {
    }

    public static Method get(String signature) {
        try {
            int open = signature.indexOf('(');
            int close = signature.indexOf(')', open);
            String left = signature.substring(0, open);
            String parametersText = signature.substring(open + 1, close);
            int separator = left.lastIndexOf('.');
            String className = left.substring(0, separator);
            String methodName = left.substring(separator + 1);

            List<Class<?>> parameters = new ArrayList<>();
            if (!parametersText.isEmpty()) {
                for (String parameter : parametersText.split(",")) {
                    parameters.add(classFor(parameter.trim()));
                }
            }

            Class<?> owner = Class.forName(className, false, RuntimeRestore.class.getClassLoader());
            Method method = owner.getDeclaredMethod(methodName, parameters.toArray(new Class<?>[0]));
            method.setAccessible(true);
            return method;
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Class<?> classFor(String name) throws ClassNotFoundException {
        String type = name;
        int dimensions = 0;
        while (type.endsWith("[]")) {
            dimensions++;
            type = type.substring(0, type.length() - 2);
        }

        Class<?> result = PRIMITIVES.get(type);
        if (result == null) {
            result = Class.forName(type, false, RuntimeRestore.class.getClassLoader());
        }
        for (int i = 0; i < dimensions; i++) {
            result = Array.newInstance(result, 0).getClass();
        }
        return result;
    }
}
