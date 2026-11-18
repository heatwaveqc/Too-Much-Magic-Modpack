package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.data.MechanicalSpec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.entity.EntityTypeTest;
import java.util.ArrayList;

/** Haggler is a bounded emerald lure, not a trade-price modifier. */
public final class VillagerAttraction {
    private VillagerAttraction(){}
    public static void tick(ServerPlayer p){
        if(p.tickCount%20!=0||!p.isAlive()||p.isCreative()||p.isSpectator()||NewbieMode.isNewbie(p)
            ||!p.getMainHandItem().is(Items.EMERALD)&&!p.getOffhandItem().is(Items.EMERALD))return;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();if(!fx.type().equals("villager_attraction"))continue;
            double r=MechanicalSpec.scaled(fx.amountPerRank(),a.rank(),12);
            if(r<3)continue;
            int cap=MechanicalSpec.integer(fx.params(),"max_targets",4,1,8);
            double speed=MechanicalSpec.number(fx.params(),"speed",1,.1,1.5);
            var villagers=new ArrayList<Villager>();
            p.serverLevel().getEntities(EntityTypeTest.forClass(Villager.class),p.getBoundingBox().inflate(r),
                v->v.isAlive()&&!v.isBaby()&&!v.isSleeping()&&!v.isPassenger()&&!v.isOnFire()&&v.getTradingPlayer()==null
                    &&v.getTarget()==null&&v.getLastHurtByMob()==null&&v.getNavigation().isDone()&&v.distanceToSqr(p)>9&&v.distanceToSqr(p)<=r*r
                    &&Selectors.entity(fx.selector(),v)&&p.hasLineOfSight(v)&&p.serverLevel().mayInteract(p,v.blockPosition()),villagers,cap);
            for(var v:villagers)v.getNavigation().moveTo(p.getX(),p.getY(),p.getZ(),speed);
            break;
        }
    }
}
