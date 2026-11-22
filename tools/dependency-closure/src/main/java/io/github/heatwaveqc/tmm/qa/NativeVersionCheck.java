package io.github.heatwaveqc.tmm.qa;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.VersionRange;

/** Fail-closed NeoForge/Maven version-range evaluator. No game or mod classes loaded. */
public final class NativeVersionCheck {
    private NativeVersionCheck() {}

    static boolean matches(String range, String actual) throws Exception {
        return VersionRange.createFromVersionSpec(range)
                .containsVersion(new DefaultArtifactVersion(actual));
    }

    static void selfTest() throws Exception {
        assertCase("[15.10.6]", "15.10.6+neoforge", false);
        assertCase("[15.10.6+neoforge]", "15.10.6+neoforge", true);
        assertCase("[1.2.0,)", "1.2.1", true);
        assertCase("[1.2.0,)", "1.1.9", false);
        assertCase("[1.21.1-3.16.3]", "1.21.1-3.16.3", true);
        assertCase("[1.21.1-3.16.3]", "3.16.3", false);
        System.out.println("PASS: Maven version matcher regressions (including #501)");
    }

    static void assertCase(String range, String actual, boolean expected) throws Exception {
        boolean value = matches(range, actual);
        if (value != expected) {
            throw new IllegalStateException("Maven semantics changed: " + range + " vs "
                    + actual + " expected=" + expected + " actual=" + value);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 1 && "--self-test".equals(args[0])) {
            selfTest();
            return;
        }
        if (args.length != 1) {
            throw new IllegalArgumentException("Expected exact checked dependency TSV path");
        }
        selfTest();
        List<String> lines = Files.readAllLines(Path.of(args[0]), StandardCharsets.UTF_8);
        int matched = 0;
        int failed = 0;
        for (String line : lines) {
            if (line.isEmpty()) {
                throw new IllegalArgumentException("Unexpected blank dependency record");
            }
            String[] parts = line.split("\t", -1);
            if (parts.length != 6 || !parts[1].matches("client|server")
                    || !parts[3].matches("required|optional")) {
                throw new IllegalArgumentException("Malformed dependency record");
            }
            boolean ok = matches(parts[4], parts[5]);
            if (!ok) {
                System.err.printf("FAIL: %s [%s] -> %s (%s): required %s, actual %s%n",
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                failed++;
            } else {
                matched++;
            }
        }
        if (matched + failed == 0 || failed != 0) {
            throw new IllegalStateException(
                    "Native dependency closure: matched=" + matched + " failed=" + failed);
        }
        System.out.println("PASS: " + matched + " Maven-native first-party dependency checks");
    }
}
