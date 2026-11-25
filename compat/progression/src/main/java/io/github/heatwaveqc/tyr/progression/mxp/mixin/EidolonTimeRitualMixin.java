package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.E6bSpawnScope;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Pseudo
@Mixin(targets={"alexthw.eidolon_repraised.common.ritual.DaylightRitual","alexthw.eidolon_repraised.common.ritual.MoonlightRitual"},remap=false)
public abstract class EidolonTimeRitualMixin {
    @Unique private long tmm$beforeTime;
    @Inject(method="tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lalexthw/eidolon_repraised/api/ritual/Ritual$RitualResult;",at=@At("HEAD"),require=0,remap=false)
    private void tmm$before(Level world,BlockPos pos,CallbackInfoReturnable<Object> cir){tmm$beforeTime=world.getDayTime();}
    @Inject(method="tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lalexthw/eidolon_repraised/api/ritual/Ritual$RitualResult;",at=@At("RETURN"),require=0,remap=false)
    private void tmm$after(Level world,BlockPos pos,CallbackInfoReturnable<Object> cir){if(world instanceof ServerLevel level)E6bSpawnScope.timeChanged(this,level,tmm$beforeTime);}
}
