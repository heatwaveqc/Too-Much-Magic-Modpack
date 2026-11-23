import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.ContextFactory;
import dev.latvian.mods.rhino.Parser;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Parse but never evaluate KubeJS sources. Deliberately compile against the
 * pinned 2101.2.7-build.85 Rhino binary in an isolated Java 21 source launcher.
 */
class ParseKubeJSSyntax {
    public static void main(String[] paths) throws Exception {
        Context context = new ContextFactory().enter();
        int failures = 0;
        for (String path : paths) {
            try {
                new Parser(context).parse(Files.readString(Path.of(path)), path, 1);
                System.out.println("PASS Rhino parse: " + path);
            } catch (RuntimeException failure) {
                failures++;
                System.err.println("FAIL Rhino parse: " + path + " — " + failure);
            }
        }
        if (failures != 0) {
            System.err.println(failures + " KubeJS script(s) failed pinned Rhino syntax parse.");
            System.exit(1);
        }
        System.out.println("PASS pinned Rhino syntax: " + paths.length + " script(s)");
    }
}
