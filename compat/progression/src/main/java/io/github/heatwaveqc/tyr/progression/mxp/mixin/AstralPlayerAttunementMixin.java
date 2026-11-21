package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.MxpMod;
import io.github.heatwaveqc.tyr.progression.mxp.core.AstralAttunementProof;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpPlayerPolicy;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpReflect;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.RitualSettlement;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import java.util.Optional;

@Pseudo
@Mixin(targets="hellfirepvp.astralsorcery.common.recipe.attunement.PlayerAttunementRecipe$Active",remap=false)
public abstract class AstralPlayerAttunementMixin {
    @WrapOperation(method="finishRecipe(Lhellfirepvp/astralsorcery/common/tile/TileAttunementAltar;)V",
        at=@At(value="INVOKE",target="Lhellfirepvp/astralsorcery/common/research/ResearchHelper;attuneConstellation(Lnet/minecraft/server/level/ServerPlayer;Lhellfirepvp/astralsorcery/common/constellation/BaseConstellation;)Z"),
        require=0,allow=1,remap=false)
    private boolean tmm$attune(ServerPlayer actor,@Coerce Object constellation,Operation<Boolean> original,@Coerce Object altar){
        boolean watch=false;
        try{
            if(MxpMod.active()&&ModList.get().getModContainerById("astralsorcery").map(c->"2.0.3".equals(c.getModInfo().getVersion().toString())).orElse(false)&&
                MxpPlayerPolicy.eligible(actor)&&actor.isAlive()&&altar instanceof BlockEntity be&&be.getLevel() instanceof ServerLevel world&&actor.level()==world&&
                world.getBlockEntity(be.getBlockPos())==be&&actor.getUUID().equals(MxpReflect.call(this,"getPlayerUUID"))&&
                constellation!=null&&MxpReflect.call(this,"getConstellation")==constellation&&Boolean.TRUE.equals(MxpReflect.call(this,"isFinished",altar))){
                Object data=MxpReflect.call(altar,"getTileData"),active=MxpReflect.call(data,"getActiveRecipe");
                Object progress=MxpReflect.staticCall("hellfirepvp.astralsorcery.common.research.ResearchManager","getProgress",actor,LogicalSide.SERVER);
                watch=active instanceof Optional<?> opt&&opt.orElse(null)==(Object)this&&Boolean.TRUE.equals(MxpReflect.call(progress,"isValid"))&&
                    Boolean.FALSE.equals(MxpReflect.call(progress,"wasOnceAttuned"))&&Boolean.FALSE.equals(MxpReflect.call(progress,"isAttuned"));
            }
        }catch(RuntimeException|LinkageError ignored){watch=false;}
        boolean success=original.call(actor,constellation);
        if(watch&&success){
            Object progress=MxpReflect.staticCall("hellfirepvp.astralsorcery.common.research.ResearchManager","getProgress",actor,LogicalSide.SERVER);
            Object attuned=MxpReflect.call(progress,"getAttunedConstellation");
            boolean actual=Boolean.TRUE.equals(MxpReflect.call(progress,"wasOnceAttuned"))&&attuned instanceof Optional<?> opt&&opt.orElse(null)==constellation;
            RitualSettlement.settle(actor,AstralAttunementProof.receipt(actor.getUUID(),true,actual));
        }
        return success;
    }
}
