package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.ScrollKnowledgePolicy;
import java.util.ArrayList;
import java.util.List;
import moze_intel.projecte.api.ItemInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** The collector receives only knowledge for drops selected for conversion. */
@Pseudo
@Mixin(targets = "cool.furry.mc.neoforge.projectexpansion.util.AlchemicalCollectionCollector", remap = false)
public abstract class ExpansionScrollCollectionMixin {
    @ModifyVariable(method = "add", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static List<ItemInfo> tmm$convertQueuedKnowledge(List<ItemInfo> items) {
        List<ItemInfo> result = new ArrayList<>();
        for (var item : items) {
            ItemInfo mapped = ScrollKnowledgePolicy.burnKnowledge(item);
            if (mapped != null) result.add(mapped);
        }
        return result;
    }
}
