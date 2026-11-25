package dev.heatwaveqc.tmm.hexpilot;

import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv;
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import java.util.function.Consumer;

/** Stand-in contract test; NOT a Minecraft test. */
public final class PilotHarnessTest {
    private static int assertions;
    private static void eq(long expected, long actual, String what) {
        assertions++;
        if (expected != actual) throw new AssertionError(what + ": expected " + expected + ", got " + actual);
    }
    private static final class TestBus implements IEventBus {
        private Consumer<FMLCommonSetupEvent> listener;
        @Override @SuppressWarnings("unchecked")
        public <T> void addListener(Consumer<T> consumer) {
            this.listener = (Consumer<FMLCommonSetupEvent>) consumer;
        }
        void fire() { listener.accept(new FMLCommonSetupEvent()); }
    }
    private static PlayerBasedCastEnv created(ServerPlayer p) {
        var env = new PlayerBasedCastEnv(p);
        env.triggerCreateEvent();
        return env;
    }
    public static void main(String[] args) {
        var bus = new TestBus();
        new HexCastMediaPilot(bus);
        bus.fire();

        var user = new ServerPlayer();
        user.getTags().add(HexCastMediaPilot.TIER_2_TAG);
        var disabled = created(user);
        eq(10000, disabled.testExtractCost(10000), "tier 2 without explicit opt-in");
        eq(0, disabled.testExtensionCount(), "disabled extension count");

        user.getTags().add(HexCastMediaPilot.ENABLE_TAG);
        var tier2 = created(user);
        eq(8000, tier2.testExtractCost(10000), "tier 2 20 percent");
        eq(1, tier2.testExtensionCount(), "one modifier per environment");
        eq(1, tier2.testExtractCost(1), "never free a one-unit cast");
        eq(0, tier2.testExtractCost(0), "zero media debt");
        eq(80, tier2.testExtractCost(100), "small debt");
        eq(8001, tier2.testExtractCost(10001), "floor-rounded saving");
        eq(MediaDiscountMath.remainingCost(Long.MAX_VALUE, 20), tier2.testExtractCost(Long.MAX_VALUE), "overflow-safe long cost");

        var circle = new CircleCastEnv(user);
        circle.triggerCreateEvent();
        eq(10000, circle.testExtractCost(10000), "circles do not inherit character perks");
        eq(0, circle.testExtensionCount(), "circle has no extension");

        user.getTags().remove(HexCastMediaPilot.TIER_2_TAG);
        user.getTags().add(HexCastMediaPilot.TIER_1_TAG);
        var tier1 = created(user);
        eq(9000, tier1.testExtractCost(10000), "tier 1 ten percent");
        // Existing VM snapshots the tier; next casting environment sees updated tag.
        eq(8000, tier2.testExtractCost(10000), "existing environment retains its tier");
        user.getTags().add(HexCastMediaPilot.TIER_2_TAG);
        var both = created(user);
        eq(8000, both.testExtractCost(10000), "tier 2 takes precedence");
        var none = created(null);
        eq(10000, none.testExtractCost(10000), "missing caster");

        for (int pct : new int[] {0, 10, 20, 90}) {
            long previous = 0;
            for (long v : new long[] {0,1,2,3,9,10,99,100,101,999,10000,Long.MAX_VALUE}) {
                long x = MediaDiscountMath.remainingCost(v, pct);
                assertions++;
                if (x < 0 || x > v || x < previous) throw new AssertionError("out of bounds or non-monotone");
                previous = x;
            }
        }
        try { MediaDiscountMath.remainingCost(100, 100); throw new AssertionError("100% discount accepted"); }
        catch (IllegalArgumentException expected) { assertions++; }
        try { MediaDiscountMath.remainingCost(100, -1); throw new AssertionError("negative discount accepted"); }
        catch (IllegalArgumentException expected) { assertions++; }
        System.out.println("PASS: " + assertions + " assertions; bridge compiles against source-shaped stand-ins; NOT Minecraft runtime.");
    }
}
