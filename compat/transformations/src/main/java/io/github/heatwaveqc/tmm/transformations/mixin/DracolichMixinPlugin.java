package io.github.heatwaveqc.tmm.transformations.mixin;

import java.util.List;
import java.util.Set;
import net.neoforged.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

/** Gate the new client hooks before injection, not merely after a render starts. */
public final class DracolichMixinPlugin implements IMixinConfigPlugin {
    @Override public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!mixinClassName.startsWith("io.github.heatwaveqc.tmm.transformations.mixin.Dracolich")) return true;
        LoadingModList list = LoadingModList.get();
        if (list == null) return false;
        boolean dragon = list.getMods().stream().anyMatch(mod -> mod.getModId().equals("dragonsurvival")
                && mod.getVersion().toString().equals("2.0.74"));
        boolean gecko = list.getMods().stream().anyMatch(mod -> mod.getModId().equals("geckolib")
                && mod.getVersion().toString().equals("4.9.3"));
        return dragon && gecko;
    }
    @Override public void onLoad(String mixinPackage) { }
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> mine, Set<String> others) { }
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String target, ClassNode node, String mixin, IMixinInfo info) { }
    @Override public void postApply(String target, ClassNode node, String mixin, IMixinInfo info) { }
}
