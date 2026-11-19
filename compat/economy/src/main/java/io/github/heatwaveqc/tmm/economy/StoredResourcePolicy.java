package io.github.heatwaveqc.tmm.economy;

import com.mojang.logging.LogUtils;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import moze_intel.projecte.api.ItemInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import org.slf4j.Logger;

/** A dynamic filled/charged variant is not the ordinary empty item.
 * Only opt-in native storage types/capability evidence, not arbitrary
 * integer or NBT names, are interpreted. Canonical buckets and native
 * ProjectE EMC storage retain their normal pricing and processors. */
public final class StoredResourcePolicy {
    private static final Logger LOG = LogUtils.getLogger();
    private static final Set<String> FAILED = ConcurrentHashMap.newKeySet();
    private StoredResourcePolicy() {}

    public static boolean held(ItemInfo info) {
        if (info == null || !info.hasModifiedComponents()
                || TomeTemplatePolicy.applies(info) || RelicTemplatePolicy.applies(info)) return false;
        String id = BuiltInRegistries.ITEM.getKey(info.getItem().value()).toString();
        try {
            // These stacks are detached from the player's actual objects.
            ItemStack actual = info.createStack();
            ItemStack plain = info.itemOnly().createStack();
            // A changed native fluid payload may have no public item capability.
            for (var entry : info.getComponentsPatch().entrySet()) {
                Object before = plain.get(entry.getKey());
                Object after = entry.getValue().orElse(null);
                if (isFluid(before) || isFluid(after)) {
                    FluidStack b = fluid(before), a = fluid(after);
                    if ((!a.isEmpty() || !b.isEmpty()) && !FluidStack.matches(a,b)) return true;
                }
            }
            if (id.startsWith("projecte_energy:emc_to_energy_") || id.startsWith("projecte_energy:energy_to_emc_")) {
                var data = actual.get(DataComponents.BLOCK_ENTITY_DATA);
                if (data != null) {
                    var tag = data.copyTag();
                    if (tag.getLong("emc") != 0 || tag.getLong("energy") != 0
                            || !tag.getList("Items", Tag.TAG_COMPOUND).isEmpty()) return true;
                }
            }
            if (ModList.get().isLoaded("pastel") && PastelInkState.held(actual, plain)) return true;
            // Do not reinterpret native Klein-star EMC as conventional OP.
            if (!id.startsWith("projecte:")) {
                var a = actual.getCapability(Capabilities.EnergyStorage.ITEM);
                var b = plain.getCapability(Capabilities.EnergyStorage.ITEM);
                int av = a == null ? 0 : a.getEnergyStored();
                int bv = b == null ? 0 : b.getEnergyStored();
                // Saturation is not proof that two large OP values are equal.
                if (av < 0 || bv < 0 || av != bv || av == Integer.MAX_VALUE) return true;
            }
            var af = actual.getCapability(Capabilities.FluidHandler.ITEM);
            var bf = plain.getCapability(Capabilities.FluidHandler.ITEM);
            int an = af == null ? 0 : af.getTanks(), bn = bf == null ? 0 : bf.getTanks();
            if (an < 0 || bn < 0 || an > 64 || bn > 64) return true;
            for (int i = 0; i < Math.max(an,bn); i++) {
                FluidStack a = i < an ? af.getFluidInTank(i) : FluidStack.EMPTY;
                FluidStack b = i < bn ? bf.getFluidInTank(i) : FluidStack.EMPTY;
                if (a == null || b == null || ((!a.isEmpty() || !b.isEmpty()) && !FluidStack.matches(a,b))) return true;
            }
            return false;
        } catch (RuntimeException | LinkageError e) {
            // A broken storage query for a modified stack is not permission
            // to replace it with a priced default. Log once per registry ID.
            if (FAILED.add(id)) LOG.warn("TMM could not verify stored resource state for {}; modified stack held", id, e);
            return true;
        }
    }
    private static boolean isFluid(Object value) {
        return value instanceof SimpleFluidContent || value instanceof FluidStack;
    }
    private static FluidStack fluid(Object value) {
        if (value instanceof SimpleFluidContent f) return f.copy();
        if (value instanceof FluidStack f) return f.copy();
        return FluidStack.EMPTY;
    }
}
