package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.ScrollKnowledgePolicy;
import java.util.HashSet;
import java.util.Collections;
import java.util.Set;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.impl.capability.KnowledgeImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Covers ordinary, full-knowledge, shared and offline provider instances.
 * Never change EMC or persistent item identity: templates still work. */
@Mixin(value = KnowledgeImpl.class, remap = false)
public abstract class ScrollKnowledgeMixin {
    @Inject(method = {"addKnowledge", "hasKnowledge"}, at = @At("HEAD"), cancellable = true)
    private void tmm$excludeScroll(ItemInfo info, CallbackInfoReturnable<Boolean> cir) {
        if (ScrollKnowledgePolicy.isScroll(info)) cir.setReturnValue(false);
    }
    @Inject(method = "getKnowledge", at = @At("RETURN"), cancellable = true)
    private void tmm$filterScrolls(CallbackInfoReturnable<Set<ItemInfo>> cir) {
        var original = cir.getReturnValue();
        if (original.stream().noneMatch(ScrollKnowledgePolicy::isScroll)) return;
        var filtered = new HashSet<>(original);
        filtered.removeIf(ScrollKnowledgePolicy::isScroll);
        cir.setReturnValue(Collections.unmodifiableSet(filtered));
    }
}
