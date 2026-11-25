package io.github.heatwaveqc.tyr.progression.mxp.hooks;

import io.github.heatwaveqc.tyr.progression.runtime.OwnedMagicDamage;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.ModList;
import java.util.UUID;
import java.util.function.Supplier;

/** Actual Well damage only; the stored native master-stone owner is authoritative. */
public final class NeoWellDamage {
    private static final String PREFIX="com.breakinblocks.neovitae.ritual.types.";
    private static final ResourceKey<DamageType> RITUAL=ResourceKey.create(Registries.DAMAGE_TYPE,
        ResourceLocation.fromNamespaceAndPath("neovitae","ritual"));
    private NeoWellDamage(){}

    public static boolean hurt(Object ritual,Object master,LivingEntity victim,DamageSource source,Supplier<Boolean> original){
        UUID owner=null;
        if(ModList.get().getModContainerById("neovitae")
                .filter(mod->mod.getModInfo().getVersion().toString().equals("1.1.53")).isPresent()
            &&isWell(ritual)&&master instanceof BlockEntity tile&&tile.getLevel()==victim.level()
            &&source.is(RITUAL)&&Boolean.TRUE.equals(MxpReflect.call(master,"isActive"))
            &&MxpReflect.call(master,"getCurrentRitual")==ritual){
            Object stored=MxpReflect.call(master,"getOwner");
            if(stored instanceof UUID uuid)owner=uuid;
        }
        return OwnedMagicDamage.hurt(OwnedMagicDamage.NEO_WELL,owner,victim,source,original);
    }
    private static boolean isWell(Object ritual){
        if(ritual==null)return false;
        String name=ritual.getClass().getName();
        return name.equals(PREFIX+"RitualWellOfSuffering")||name.equals(PREFIX+"RitualDeusWellOfSuffering");
    }
}
