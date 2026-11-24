import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.ContextFactory;
import dev.latvian.mods.rhino.Parser;
import java.nio.file.Files;
import java.nio.file.Path;

/** Syntax-only check with an externally supplied pinned Rhino JAR; never starts Minecraft. */
class ParseQaSurveys {
    public static void main(String[] paths) throws Exception {
        Context context = new ContextFactory().enter();
        for (String path : paths) {
            new Parser(context).parse(Files.readString(Path.of(path)), path, 1);
            System.out.println("PASS Rhino parse: " + path);
        }
    }
}
