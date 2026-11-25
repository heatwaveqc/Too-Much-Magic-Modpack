import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.ContextFactory;

/** Optional offline reproduction against the externally supplied installed Rhino JAR.
 * Reports the known engine limitation; never loads Minecraft or executes pack scripts.
 */
class RhinoBlockScopeProbe {
    public static void main(String[] args) {
        Context context = new ContextFactory().enter();
        String[] sources = {
            "if (true) { const TMMReactiveComponents = 1; }",
            "const TMMReactiveComponents = 1;",
            "if (true) { let TMMReactiveComponents = 1; }",
            "(function () { if (true) { const TMMReactiveComponents = 1; } })()"
        };
        for (int i = 0; i < sources.length; i++) {
            boolean failed = false;
            String message = "";
            try {
                context.evaluateString(context.initStandardObjects(), sources[i], "scope-probe", 1, null);
            } catch (RuntimeException failure) {
                failed = true;
                message = failure.getMessage();
            }
            if (i == 0) {
                if (!failed || !message.contains("redeclaration of var TMMReactiveComponents")) {
                    throw new AssertionError("Installed-Rhino failure did not reproduce: " + message);
                }
                System.out.println("REPRODUCED conditional-const failure: " + message);
            } else if (failed) {
                throw new AssertionError("Control " + i + " failed: " + message);
            } else {
                System.out.println("PASS control " + i);
            }
        }
    }
}
