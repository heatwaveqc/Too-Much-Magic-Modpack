package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.runtime.enchanting.EnchantingFeatureGate;
import java.util.List;
import java.util.Set;
import net.neoforged.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

/** Decide before resolving optional target types, not after injection. */
public final class EconomyEnchantingMixinPlugin implements IMixinConfigPlugin {
    @Override public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // The shop toolbar is client-only and must not resolve absent FTB classes.
        if (mixinClassName.equals("io.github.heatwaveqc.tmm.economy.mixin.QuestShopToolbarMixin")) {
            LoadingModList mods = LoadingModList.get();
            return mods != null && List.of("ftbquests", "ftblibrary", "qshop").stream().allMatch(id ->
                mods.getMods().stream().anyMatch(mod -> mod.getModId().equals(id)));
        }
        boolean eidolon = mixinClassName.equals("io.github.heatwaveqc.tmm.economy.mixin.EidolonPersonalFilterMixin");
        boolean anvil = mixinClassName.equals("io.github.heatwaveqc.tmm.economy.mixin.PortableAnvilOpenMixin");
        boolean portable = mixinClassName.startsWith("io.github.heatwaveqc.tmm.economy.mixin.PortableEnchanting");
        if (!eidolon && !portable && !anvil && !mixinClassName.equals("io.github.heatwaveqc.tmm.economy.mixin.ApothicPersonalFilterMixin"))
            return true; // No changes to existing unrelated mixins.
        LoadingModList list = LoadingModList.get();
        if (list == null) return false;
        boolean enabled = EnchantingFeatureGate.enabled(id -> list.getMods().stream()
            .anyMatch(mod -> mod.getModId().equals(id)));
        if (anvil) return EnchantingFeatureGate.portableAnvilEnabled(id -> list.getMods().stream()
            .anyMatch(mod -> mod.getModId().equals(id)), list.getMods().stream().anyMatch(mod ->
            mod.getModId().equals("occultism") && mod.getVersion().toString().equals("1.227.0")));
        return enabled && (!eidolon || list.getMods().stream().anyMatch(mod ->
            mod.getModId().equals("eidolon_repraised") && mod.getVersion().toString().equals("0.5.0.8")));
    }
    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> mine, Set<String> others) {}
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String target, ClassNode node, String mixin, IMixinInfo info) {}
    @Override public void postApply(String target, ClassNode node, String mixin, IMixinInfo info) {}
}
