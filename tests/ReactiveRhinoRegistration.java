import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.ContextFactory;
import java.nio.file.Files;
import java.nio.file.Path;

/** Executes only the actual Reactive script's registration path against stubbed
 * globals on the hash-pinned Rhino binary. No Minecraft classes or loot rolls.
 */
class ReactiveRhinoRegistration {
    private static final String STUBS = """
        var registrations = 0, loads = 0, actions = 0;
        var ars = true, lootjs = true;
        var Platform = { isLoaded: function(id) {
            return id === 'ars_nouveau' ? ars : id === 'lootjs' && lootjs;
        }};
        var Java = { loadClass: function(id) {
            loads++;
            return {
                ENCHANTMENT: 'enchantment',
                parse: function(value) { return value; },
                create: function(registry, value) { return value; }
            };
        }};
        var LootType = { CHEST: 'chest' };
        var LootJS = { modifiers: function(callback) {
            registrations++;
            callback({ addTableModifier: function(type) {
                if (type !== LootType.CHEST) throw new Error('wrong loot type');
                return { name: function(name) {
                    if (name !== 'tmm:unconfigured_reactive_books') throw new Error('wrong modifier');
                    return { customAction: function(action) {
                        if (typeof action !== 'function') throw new Error('missing action');
                        actions++;
                    }};
                }};
            }});
        }};
        """;

    public static void main(String[] args) throws Exception {
        if (args.length != 1) throw new IllegalArgumentException("Expected Reactive script path");
        String source = Files.readString(Path.of(args[0]));
        Context context = new ContextFactory().enter();
        var scope = context.initStandardObjects();
        context.evaluateString(scope, STUBS, "registration-stubs", 1, null);
        context.evaluateString(scope, source, args[0], 1, null);
        context.evaluateString(scope, source, args[0], 1, null);
        context.evaluateString(scope, """
            if (registrations !== 2 || actions !== 2 || loads !== 16)
                throw new Error('incorrect enabled/repeated registration counts');
            if (typeof TMMReactiveComponents !== 'undefined')
                throw new Error('Reactive declarations leaked into global scope');
            ars = false;
            """, "enabled-assertions", 1, null);
        context.evaluateString(scope, source, args[0], 1, null);
        context.evaluateString(scope, "ars = true; lootjs = false;", "disable-lootjs", 1, null);
        context.evaluateString(scope, source, args[0], 1, null);
        context.evaluateString(scope, """
            if (registrations !== 2 || actions !== 2 || loads !== 16)
                throw new Error('optional mod guard registered or loaded classes');
            """, "disabled-assertions", 1, null);
        System.out.println("PASS pinned Rhino Reactive registration: enabled, repeat, both optional guards, isolated scope");
    }
}
