package io.github.heatwaveqc.tyr.progression.runtime.forms;

import io.github.heatwaveqc.tyr.progression.foundation.LichSkillMath;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpReflect;
import io.github.heatwaveqc.tyr.progression.runtime.EffectRuntime;
import io.github.heatwaveqc.tyr.progression.runtime.Selectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

/** Live native ownership checks at combat boundaries; no saved buffs or world scan. */
public final class LichServantEffects {
    private static final String OWNED="com.Polarice3.Goety.api.entities.IOwned";
    private static final TagKey<EntityType<?>> UNDEAD=TagKey.create(Registries.ENTITY_TYPE,ResourceLocation.parse("minecraft:undead"));
    private LichServantEffects(){}
    private static final String ETHEREAL="dev.sterner.witchery_forbidden_magic.feature.necromancy.EtherealEntityAttachment";
    private static ServerPlayer owner(Entity entity){
        if(!(entity instanceof Mob mob)||!mob.isAlive()||!(mob.level() instanceof net.minecraft.server.level.ServerLevel level)||
            level.getServer()==null||!level.getServer().isSameThread())return null;
        ServerPlayer ethereal=etherealOwner(mob,level);
        ServerPlayer goety=goetyOwner(mob);
        if(ethereal!=null&&goety!=null&&ethereal!=goety)return null;
        ServerPlayer p=ethereal!=null?ethereal:goety;
        return p!=null&&p.isAlive()&&p.level()==level&&LichEffects.eligible(p)?p:null;
    }
    private static ServerPlayer goetyOwner(Mob mob){
        if(!LichEffects.pinned("goety","3.2.2")||!mob.getType().is(UNDEAD))return null;
        try{
            if(!Class.forName(OWNED).isInstance(mob))return null;
            Object player=MxpReflect.call(mob,"getTrueOwner");
            Object id=MxpReflect.call(mob,"getOwnerId");
            return player instanceof ServerPlayer p&&p.getUUID().equals(id)&&
                Boolean.FALSE.equals(MxpReflect.call(mob,"isHostile"))?p:null;
        }catch(ClassNotFoundException|RuntimeException|LinkageError e){return null;}
    }
    /** A native raised corpse is undead by its Ethereal state, not necessarily its original mob tag. */
    private static ServerPlayer etherealOwner(Mob mob,net.minecraft.server.level.ServerLevel level){
        if(!LichEffects.pinned("witchery_forbidden_magic","0.5.37"))return null;
        Object data=MxpReflect.staticCall(ETHEREAL,"getData",mob);
        if(!Boolean.TRUE.equals(MxpReflect.call(data,"isEthereal"))||
            !(MxpReflect.call(data,"getOwnerUUID") instanceof java.util.UUID id)||
            !(MxpReflect.call(data,"getSummonTime") instanceof Long born)||
            !(MxpReflect.call(data,"getMaxLifeTime") instanceof Long lifetime))return null;
        // Match NecroHandler's native expiry; never extend or recreate a servant.
        long now=level.getGameTime();
        if(born>now||born<0||lifetime>0&&now-born>=lifetime)return null;
        return level.getPlayerByUUID(id) instanceof ServerPlayer p?p:null;
    }
    private static double rate(ServerPlayer p,String type,Mob mob){
        double result=0;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();
            if(fx.type().equals(type)&&Selectors.entity(fx.selector(),mob))result+=fx.amountPerRank()*a.rank();
        }
        return Double.isFinite(result)?Math.max(0,result):0;
    }
    public static void incoming(LivingIncomingDamageEvent e){
        if(e.isCanceled()||e.getAmount()<=0||e.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY))return;
        Entity attacker=e.getSource().getEntity();LivingEntity target=e.getEntity();
        double attack=0,defense=0;
        ServerPlayer master=owner(attacker);
        if(master!=null&&attacker instanceof Mob servant&&target!=master&&target!=attacker&&
            !master.isAlliedTo(target)&&owner(target)!=master)
            attack=rate(master,"lich_servant_damage",servant);
        ServerPlayer protector=owner(target);
        if(protector!=null&&target instanceof Mob servant&&attacker!=null&&attacker!=protector&&attacker!=target&&owner(attacker)!=protector)
            defense=rate(protector,"lich_servant_protection",servant);
        if(attack>0||defense>0)e.setAmount(LichSkillMath.damage(e.getAmount(),attack,defense));
    }
}
