package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.foundation.PhysicalMasteryMath;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/** Server-owned transient movement attributes, no synthetic equipment or global speed effect. */
public final class SoulStep {
    private SoulStep(){}
    private static final ResourceLocation ID=ResourceLocation.fromNamespaceAndPath("tyr_progression","soul_step");
    public static void register(){
        NeoForge.EVENT_BUS.addListener(SoulStep::tick);
        NeoForge.EVENT_BUS.addListener(SoulStep::logout);
        NeoForge.EVENT_BUS.addListener(SoulStep::respawn);
        NeoForge.EVENT_BUS.addListener(SoulStep::dimension);
    }
    private static boolean usable(ServerPlayer p){
        return p.isAlive()&&!p.isCreative()&&!p.isSpectator()&&!NewbieMode.isNewbie(p)
            &&!(p instanceof net.neoforged.neoforge.common.util.FakePlayer)
            &&!p.isPassenger()&&!p.getAbilities().flying&&!p.isFallFlying()&&!p.isInWaterOrBubble()&&!p.isInLava();
    }
    private static void set(ServerPlayer p,Holder<Attribute> attr,double amount){
        AttributeInstance inst=p.getAttribute(attr);if(inst==null)return;
        AttributeModifier old=inst.getModifier(ID);
        if(old!=null&&Double.compare(old.amount(),amount)!=0){inst.removeModifier(ID);old=null;}
        if(old==null&&amount>0)inst.addTransientModifier(new AttributeModifier(ID,amount,AttributeModifier.Operation.ADD_VALUE));
    }
    private static void tick(PlayerTickEvent.Post event){
        if(!(event.getEntity() instanceof ServerPlayer p))return;
        int rank=0;
        if(usable(p))for(var a:EffectRuntime.active(p))
            if(a.effect().type().equals("soul_step"))rank=Math.max(rank,PhysicalMasteryMath.soulLevel(a.effect().amountPerRank()*a.rank()));
        var speed=p.getAttribute(Attributes.MOVEMENT_SPEED);
        boolean prior=speed!=null&&speed.getModifier(ID)!=null;
        boolean terrain=rank>0&&p.level().getBlockState(p.getBlockPosBelowThatAffectsMyMovement()).is(BlockTags.SOUL_SPEED_BLOCKS);
        boolean keep=PhysicalMasteryMath.retainSpeed(rank>0,terrain,!p.onGround(),prior);
        set(p,Attributes.MOVEMENT_SPEED,keep?PhysicalMasteryMath.soulSpeed(rank):0);
        set(p,Attributes.MOVEMENT_EFFICIENCY,terrain?1:0);
    }
    private static void clear(ServerPlayer p){set(p,Attributes.MOVEMENT_SPEED,0);set(p,Attributes.MOVEMENT_EFFICIENCY,0);}
    private static void logout(PlayerEvent.PlayerLoggedOutEvent e){if(e.getEntity() instanceof ServerPlayer p)clear(p);}
    private static void respawn(PlayerEvent.PlayerRespawnEvent e){if(e.getEntity() instanceof ServerPlayer p)clear(p);}
    private static void dimension(PlayerEvent.PlayerChangedDimensionEvent e){if(e.getEntity() instanceof ServerPlayer p)clear(p);}
}
