package io.github.heatwaveqc.tmm.economy;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.item.InkItem;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.proxy.IEMCProxy;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/** A scroll may be priced and copied from a physical template, but never learned.
 * Only explicitly identified consuming transactions substitute ink knowledge. */
public final class ScrollKnowledgePolicy {
    private static final ResourceLocation SCROLL = ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "scroll");
    private ScrollKnowledgePolicy() {}
    public static boolean isScroll(ItemInfo info) {
        return info != null && info.getItem().is(SCROLL);
    }
    public static boolean isScroll(ItemStack stack) {
        return !stack.isEmpty() && SCROLL.equals(BuiltInRegistries.ITEM.getKey(stack.getItem()));
    }

    /** Empty for an invalid or unpriced scroll; non-scroll items pass unchanged. */
    public static ItemStack burnKnowledge(ItemStack stack) {
        if (!isScroll(stack)) return stack;
        var container = ISpellContainer.get(stack);
        if (container == null || container.getActiveSpellCount() != 1) return ItemStack.EMPTY;
        var data = container.getSpellAtIndex(0);
        var spell = data.getSpell();
        int level = data.getLevel();
        if (spell == SpellRegistry.none() || !spell.isEnabled()
                || level < spell.getMinLevel() || level > spell.getMaxLevel()) return ItemStack.EMPTY;
        var canonical = IronScrollRecipeMapper.canonical(spell, level);
        if (!stack.getComponentsPatch().equals(canonical.getComponentsPatch())
                || IEMCProxy.INSTANCE.getSellValue(stack) <= 0) return ItemStack.EMPTY;
        var ink = new ItemStack(InkItem.getInkForRarity(spell.getRarity(level)));
        return IEMCProxy.INSTANCE.hasValue(ink) ? ink : ItemStack.EMPTY;
    }
    public static ItemInfo burnKnowledge(ItemInfo info) {
        if (!isScroll(info)) return info;
        ItemStack ink = burnKnowledge(info.createStack());
        return ink.isEmpty() ? null : ItemInfo.fromStack(ink);
    }
    public static boolean learnAfterBurn(IKnowledgeProvider provider, ItemStack stack) {
        ItemStack learned = burnKnowledge(stack);
        return !learned.isEmpty() && provider.addKnowledge(learned);
    }
    public static void syncAfterBurn(IKnowledgeProvider provider, ServerPlayer player, ItemInfo info, boolean learned) {
        ItemInfo target = burnKnowledge(info);
        if (target != null) provider.syncKnowledgeChange(player, target, learned);
    }
}
