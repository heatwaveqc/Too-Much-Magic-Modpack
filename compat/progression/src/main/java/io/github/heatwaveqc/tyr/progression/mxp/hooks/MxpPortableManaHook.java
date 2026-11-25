package io.github.heatwaveqc.tyr.progression.mxp.hooks;

import io.github.heatwaveqc.tyr.progression.mxp.MxpMod;
import io.github.heatwaveqc.tyr.progression.mxp.MxpService;
import io.github.heatwaveqc.tyr.progression.mxp.MxpSourceConfig;
import io.github.heatwaveqc.tyr.progression.mxp.core.PortableManaPolicy;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayDeque;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;
import io.github.heatwaveqc.tyr.progression.mxp.core.PaymentReceiptPolicy;

/**
 * Botania Revived 2.0.2. Observe only actual mana taken from whitelisted
 * portable storage stacks physically present in player inventory or Curios.
 * NEVER use the handler's overall return value as the credited resource
 * amount: PlayerManaSources can provide Ars or other outside energy.
 */
public final class MxpPortableManaHook {
    private record Snapshot(ServerPlayer player, ItemStack requester, int requested,
                            double nominalCost, Map<ItemStack, Integer> before) {}

    private static final ThreadLocal<ArrayDeque<Snapshot>> ACTIVE =
            ThreadLocal.withInitial(ArrayDeque::new);
    private record ToolQuote(ItemStack requester, Player player, double nominal) {}
    private static final ThreadLocal<ArrayDeque<ToolQuote>> TOOL_NOMINAL =
            ThreadLocal.withInitial(ArrayDeque::new);

    private MxpPortableManaHook() {}

    /** Preserve the pre-armor/pre-enchantment cost of *ForTool wrappers. */
    public static void beginTool(ItemStack requester, Player player, int nominal) {
        TOOL_NOMINAL.get().push(new ToolQuote(requester,player,Math.max(0,nominal)));
    }

    public static void endTool() {
        ArrayDeque<ToolQuote> values = TOOL_NOMINAL.get();
        if (!values.isEmpty()) values.pop();
        if (values.isEmpty()) TOOL_NOMINAL.remove();
    }

    public static void beforeRequest(ItemStack requester, Player player,
                                     int requested, boolean actuallyRemove) {
        // Keep the stack balanced even if this was a simulated/invalid call.
        Snapshot snapshot = null;
        ArrayDeque<Snapshot> scopes = ACTIVE.get();
        // A nested request may debit the same stacks or change a snapshot.
        // Reject the ambiguous outer operation and the nested operation.
        if (!scopes.isEmpty()) {
            int depth=scopes.size();
            scopes.clear();
            for(int i=0;i<=depth;i++) scopes.push(new Snapshot(null,ItemStack.EMPTY,0,0,Map.of()));
            return;
        }
        if (actuallyRemove && player instanceof ServerPlayer sp && MxpPlayerPolicy.eligible(sp)
                && requested > 0 && !requester.isEmpty()
                && !isStorage(requester) && !isOtherSpellPayment()) {
            Map<ItemStack, Integer> before = snapshotStorage(sp, requester);
            if (!before.isEmpty()) {
                ArrayDeque<ToolQuote> tool = TOOL_NOMINAL.get();
                ToolQuote q=tool.peek();
                double nominal = q!=null && q.player==player && q.requester==requester ? q.nominal : requested;
                snapshot = new Snapshot(sp, requester, requested, nominal, before);
            }
        }
        ACTIVE.get().push(snapshot == null
                ? new Snapshot(null, ItemStack.EMPTY, 0, 0, Map.of()) : snapshot);
    }

    /** Called after requestMana or requestManaExact has committed or failed. */
    public static void afterRequest(int amountActuallyGranted, boolean successful) {
        ArrayDeque<Snapshot> stack = ACTIVE.get();
        if (stack.isEmpty()) return;
        Snapshot snapshot = stack.pop();
        if (stack.isEmpty()) ACTIVE.remove();
        if (!successful || !PaymentReceiptPolicy.bounded(amountActuallyGranted,snapshot.requested) || !MxpPlayerPolicy.eligible(snapshot.player)) return;

        long portableDebit = 0;
        for (var entry : snapshot.before.entrySet()) {
            ItemStack item = entry.getKey();
            // Identity not name: another same-name stack elsewhere cannot
            // disguise whether the original tablet/ring was really debited.
            int current = manaIn(item);
            if (current >= 0 && current < entry.getValue()) {
                portableDebit += entry.getValue() - current;
            }
        }
        if (portableDebit <= 0) return;
        // If Ars or another PlayerManaSources adapter supplied the remainder,
        // credit ONLY the proportional cost paid by tablets/rings.
        double eligibleNominal = PortableManaPolicy.eligibleNominal(
                snapshot.nominalCost, portableDebit, amountActuallyGranted, snapshot.requested);
        double baseline = MxpSourceConfig.BOTANIA_PORTABLE_POOL.get();
        if (!Double.isFinite(baseline) || baseline <= 0 || eligibleNominal <= 0) return;
        try {
            MxpService.resource(snapshot.player, "botania:portable_mana",
                    "botania:portable:" + UUID.randomUUID(),
                    eligibleNominal, baseline, portableDebit);
        } catch (RuntimeException e) {
            MxpMod.LOGGER.warn("Botania portable MXP observation rejected: {}", e.toString(), e);
        }
    }

    public static void clear() { ACTIVE.remove(); TOOL_NOMINAL.remove(); }

    private static boolean isStorage(ItemStack stack) {
        return PortableManaPolicy.eligibleStorage(
                BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
    }

    private static Map<ItemStack, Integer> snapshotStorage(ServerPlayer player,
                                                           ItemStack requester) {
        Map<ItemStack, Integer> found = new IdentityHashMap<>();
        for (ItemStack stack : player.getInventory().items) addIfEligible(found, stack, requester);
        for (ItemStack stack : player.getInventory().offhand) addIfEligible(found, stack, requester);
        // This is Botania's *own* source for its getManaAccesories() path;
        // when Curios is installed, its implementation maps equipped Curios
        // into this Container. No backpack, storage network or world pool.
        Object api = MxpReflect.staticCall("velrondevs.botania.api.BotaniaAPI", "instance");
        Object acc = MxpReflect.call(api, "getAccessoriesInventory", player);
        if (acc instanceof Container contents) {
            for (int i = 0; i < contents.getContainerSize(); i++) {
                addIfEligible(found, contents.getItem(i), requester);
            }
        }
        return found;
    }

    private static void addIfEligible(Map<ItemStack, Integer> found,
                                      ItemStack item, ItemStack requester) {
        if (item == null || item.isEmpty() || item == requester || !isStorage(item)) return;
        int amount = manaIn(item);
        if (amount > 0) found.put(item, amount);
    }

    private static int manaIn(ItemStack stack) {
        try {
            Class<?> xplat = Class.forName("velrondevs.botania.xplat.XplatAbstractions");
            Object impl = MxpReflect.field(xplat, "INSTANCE");
            Object item = MxpReflect.call(impl, "findManaItem", stack);
            double mana = MxpReflect.number(MxpReflect.call(item, "getMana"));
            return Double.isFinite(mana) && mana >= 0 ? (int) Math.min(mana, Integer.MAX_VALUE) : -1;
        } catch (ClassNotFoundException | LinkageError absent) {
            return -1;
        }
    }

    private static boolean isOtherSpellPayment() {
        if (MxpCastHooks.isProcessingSpell()) return true;
        // Botania -> Ars bridge transfers mana to finance a DIFFERENT school's
        // cast. That cast's primary transaction must own the MXP credit.
        for (StackTraceElement frame : Thread.currentThread().getStackTrace()) {
            String caller = frame.getClassName();
            if (caller.contains(".integration.arsnouveau.ArsManaBridge")
                    || caller.startsWith("com.otectus.arsnspells.")
                    || caller.contains("com.hollingsworth.arsnouveau.api.spell.SpellResolver")
                    || caller.contains("io.redspace.ironsspellbooks.api.spells.AbstractSpell")) {
                return true;
            }
        }
        return false;
    }
}
