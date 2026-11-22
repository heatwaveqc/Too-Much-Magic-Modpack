package io.github.heatwaveqc.tmm.transformations.mixin;

import java.util.List;
import java.util.Set;
import net.neoforged.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

/** Reject optional mixins unless the exactly audited target binary is loaded. */
public final class DracolichMixinPlugin implements IMixinConfigPlugin {
    @Override public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        LoadingModList list = LoadingModList.get();
        if (list == null) return false;
        String hook = mixinClassName.substring(mixinClassName.lastIndexOf('.') + 1);
        if (hook.startsWith("Dracolich"))
            return pinned(list, "dragonsurvival", "2.0.74")
                && pinned(list, "geckolib", "4.9.3");
        return switch (hook) {
            case "GoetyDracolichAcquisitionMixin" ->
                pinned(list, "goety", "3.2.3");
            case "ForbiddenMagicDracolichAcquisitionMixin" ->
                pinned(list, "witchery_forbidden_magic", "0.5.37");
            case "DragonSurvivalDeathRepeatMixin", "NewbieDragonAcquisitionMixin" ->
                pinned(list, "dragonsurvival", "2.0.74");
            case "NewbieVampireAcquisitionMixin", "NewbieWerewolfAcquisitionMixin" ->
                pinned(list, "witchery", "0.5.39");
            case "BotaniaThermalilyNoCooldownMixin", "BotaniaThermalilyFiftyEndoflamesMixin" ->
                pinned(list, "botania", "2.0.2");
            case "MahouCatalystExchangeMixin" ->
                pinned(list, "mahoutsukai", "1.36.31");
            default -> false; // Explicitly review every future optional target
        };
    }

    private static boolean pinned(LoadingModList list, String modId, String version) {
        return list.getMods().stream().anyMatch(mod ->
            mod.getModId().equals(modId) && mod.getVersion().toString().equals(version));
    }

    @Override public void onLoad(String mixinPackage) { }
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> mine, Set<String> others) { }
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String target, ClassNode node, String mixin, IMixinInfo info) { }
    @Override public void postApply(String target, ClassNode node, String mixin, IMixinInfo info) { }
}
