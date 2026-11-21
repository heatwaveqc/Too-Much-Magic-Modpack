package io.github.heatwaveqc.tyr.progression.mxp.hooks;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/** Narrow, fail-closed reflection over optional pinned magic-mod APIs. */
public final class MxpReflect {
    private MxpReflect() {}

    public static Object field(Object owner, String name) {
        if (owner == null) return null;
        Class<?> type = owner instanceof Class<?> c ? c : owner.getClass();
        while (type != null) {
            try {
                Field f = type.getDeclaredField(name);
                f.setAccessible(true);
                return f.get(owner instanceof Class<?> ? null : owner);
            } catch (NoSuchFieldException missing) {
                type = type.getSuperclass();
            } catch (ReflectiveOperationException | RuntimeException inaccessible) {
                return null;
            }
        }
        return null;
    }

    public static Object call(Object instance, String methodName, Object... args) {
        if (instance == null) return null;
        Class<?> type = instance instanceof Class<?> c ? c : instance.getClass();
        for (Method m : type.getMethods()) {
            if (!m.getName().equals(methodName) || m.getParameterCount() != args.length) continue;
            if (!argumentsMatch(m.getParameterTypes(), args)) continue;
            try {
                return m.invoke(instance instanceof Class<?> ? null : instance, args);
            } catch (ReflectiveOperationException | RuntimeException inaccessible) {
                return null;
            }
        }
        return null;
    }

    public static Object staticCall(String name, String method, Object... args) {
        try { return call(Class.forName(name), method, args); }
        catch (ClassNotFoundException | LinkageError absent) { return null; }
    }

    public static double number(Object obj) {
        return obj instanceof Number n ? n.doubleValue() : Double.NaN;
    }

    private static boolean argumentsMatch(Class<?>[] expected, Object[] args) {
        for (int i = 0; i < expected.length; i++) {
            if (args[i] == null) {
                if (expected[i].isPrimitive()) return false;
                continue;
            }
            Class<?> t = expected[i];
            if (t.isPrimitive()) {
                t = switch (t.getName()) {
                    case "int" -> Integer.class;
                    case "float" -> Float.class;
                    case "double" -> Double.class;
                    case "long" -> Long.class;
                    case "short" -> Short.class;
                    case "byte" -> Byte.class;
                    case "boolean" -> Boolean.class;
                    case "char" -> Character.class;
                    default -> t;
                };
            }
            if (!t.isAssignableFrom(args[i].getClass())) return false;
        }
        return true;
    }
}
