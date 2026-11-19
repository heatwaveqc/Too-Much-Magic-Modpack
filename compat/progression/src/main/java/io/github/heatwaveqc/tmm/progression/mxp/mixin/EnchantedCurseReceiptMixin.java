package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.heatwaveqc.tmm.progression.mxp.MxpMod;
import io.github.heatwaveqc.tmm.progression.mxp.core.E6bSpawnCatalog;
import io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets="net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite",remap=false)
public abstract class EnchantedCurseReceiptMixin implements EnchantedCurseWitness.Rite {
    @Unique private RitualRewards.Receipt tmm$curse;
    @Unique private boolean tmm$dispatching,tmm$mutation,tmm$stopped;
    @Unique private static final String TMM_CURSE="tmm_progression_rite_receipt";
    @Unique private boolean tmm$supported(){
        return ModList.get().getModContainerById("enchanted").map(c->"4.2.7".equals(c.getModInfo().getVersion().toString())).orElse(false);
    }
    @Unique private ServerLevel tmm$level(){return MxpReflect.call(this,"getLevel") instanceof ServerLevel l?l:null;}
    @Unique private boolean tmm$matches(RitualRewards.Receipt r){
        if(r==null||!tmm$supported()||!E6bSpawnCatalog.enchantedEffect(E6bSpawnCatalog.enchanted(r.recipeId()))||r.actor()==null||r.operation()==null)return false;
        ServerLevel level=tmm$level();
        return level!=null&&r.actor().equals(MxpReflect.field(MxpReflect.field(this,"params"),"caster"))
            &&r.recipeId().equals(E6bSpawnScope.enchantedId(level,MxpReflect.call(this,"getType")));
    }
    @Unique private void tmm$dirty(){
        ServerLevel level=tmm$level();
        if(level!=null&&MxpReflect.staticCall("net.favouriteless.enchanted.common.enchanted.circle_magic.RiteManager","get",level) instanceof SavedData data)data.setDirty();
    }
    @Override public void tmm$bindCurseReceipt(RitualRewards.Receipt r){
        if(tmm$matches(r)&&MxpMod.active()){tmm$curse=r;tmm$dirty();}
    }
    @Override public RitualRewards.Receipt tmm$curseReceipt(){return tmm$dispatching&&MxpMod.active()&&tmm$matches(tmm$curse)?tmm$curse:null;}
    @Override public void tmm$markCurseMutation(){if(tmm$curseReceipt()!=null)tmm$mutation=true;}
    @Unique private void tmm$enter(){tmm$dispatching=true;tmm$mutation=false;tmm$stopped=false;}
    @Unique private void tmm$leave(boolean normal){
        var receipt=tmm$curse;boolean award=normal&&tmm$mutation&&tmm$matches(receipt)&&MxpMod.active();
        boolean clear=!normal||tmm$stopped||award;
        tmm$dispatching=false;tmm$mutation=false;tmm$stopped=false;
        if(clear&&tmm$curse!=null){tmm$curse=null;tmm$dirty();}
        ServerLevel level=tmm$level();
        if(award&&level!=null)RitualSettlement.settle(level.getServer().getPlayerList().getPlayer(receipt.actor()),receipt);
    }
    @WrapMethod(method="start()V",require=0,remap=false)
    private void tmm$start(Operation<Void> original){
        if(tmm$curse==null||tmm$dispatching){original.call();return;}
        tmm$enter();boolean normal=false;
        try{original.call();normal=true;}finally{tmm$leave(normal);}
    }
    @WrapMethod(method="tick()Z",require=0,remap=false)
    private boolean tmm$tick(Operation<Boolean> original){
        if(tmm$curse==null||tmm$dispatching)return original.call();
        tmm$enter();boolean normal=false;
        try{boolean result=original.call();normal=true;return result;}finally{tmm$leave(normal);}
    }
    @Inject(method="stop()Z",at=@At("HEAD"),require=0,remap=false)
    private void tmm$stop(CallbackInfoReturnable<Boolean> cir){
        if(tmm$curse==null)return;
        if(tmm$dispatching)tmm$stopped=true;else{tmm$curse=null;tmm$dirty();}
    }
    @Inject(method="cancel()Z",at=@At("HEAD"),require=0,remap=false)
    private void tmm$cancel(CallbackInfoReturnable<Boolean> cir){if(tmm$curse!=null){tmm$curse=null;tmm$mutation=false;tmm$dirty();}}
    @Inject(method="save()Lnet/minecraft/nbt/CompoundTag;",at=@At("RETURN"),require=0,remap=false)
    private void tmm$save(CallbackInfoReturnable<CompoundTag> cir){
        var tag=cir.getReturnValue();var r=tmm$curse;
        if(tag==null||!tmm$matches(r))return;
        CompoundTag data=new CompoundTag();data.putString("recipe",r.recipeId());data.putUUID("actor",r.actor());data.putUUID("operation",r.operation());tag.put(TMM_CURSE,data);
    }
    @Inject(method="load(Lnet/minecraft/nbt/CompoundTag;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$load(CompoundTag tag,CallbackInfo ci){
        tmm$curse=null;tmm$dispatching=false;tmm$mutation=false;tmm$stopped=false;
        CompoundTag data=tag.getCompound(TMM_CURSE);
        if(data.hasUUID("actor")&&data.hasUUID("operation")){
            var r=new RitualRewards.Receipt(data.getString("recipe"),data.getUUID("operation"),data.getUUID("actor"),true,false);
            if(tmm$matches(r))tmm$curse=r;
        }
    }
}
