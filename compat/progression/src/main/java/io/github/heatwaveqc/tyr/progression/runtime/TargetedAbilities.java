package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.runtime.charm.CharmRuntime;
import io.github.heatwaveqc.tyr.progression.runtime.taming.TamingRuntime;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** Input delivery only. The server chooses the aimed target; neither a target ID
 * nor a cost, skill rank or ownership claim comes from the client. */
public final class TargetedAbilities {
    private TargetedAbilities(){}
    private static final Set<UUID> NATIVE_TAMING=new HashSet<>();
    private static final ThreadLocal<Boolean> CHECKING=ThreadLocal.withInitial(()->false);
    public static void setNativeTaming(ServerPlayer p,boolean enabled){
        if(enabled)NATIVE_TAMING.add(p.getUUID());else NATIVE_TAMING.remove(p.getUUID());
    }
    public static boolean nativeTaming(ServerPlayer p){return NATIVE_TAMING.contains(p.getUUID());}
    public static void forget(ServerPlayer p){NATIVE_TAMING.remove(p.getUUID());}
    public static boolean checkingPermission(){return CHECKING.get();}
    public static boolean request(ServerPlayer p,String action){
        if(!p.isAlive()||p.isSpectator()||p.isCreative()||!p.getMainHandItem().isEmpty()
                ||checkingPermission())return false;
        if(!action.equals("charm")&&(!action.equals("tame")||nativeTaming(p)))return false;
        double reach=p.entityInteractionRange();
        if(!Double.isFinite(reach)||reach<=0)return false;
        var start=p.getEyePosition();
        var delta=p.getLookAngle().scale(reach);
        var end=start.add(delta);
        var block=p.pick(reach,1.0F,false);
        if(block.getType()==HitResult.Type.BLOCK)end=block.getLocation();
        var hit=ProjectileUtil.getEntityHitResult(p,start,end,p.getBoundingBox().expandTowards(delta).inflate(1),
                e->!e.isSpectator()&&e.isPickable(),start.distanceToSqr(end));
        if(hit==null||!(hit.getEntity() instanceof Mob mob)||!p.canInteractWithEntity(mob,0)
                ||!p.hasLineOfSight(mob))return false;
        // Memoize the permission event so alternative affordable effects cannot
        // post it twice. Native right-click uses its actual event instead.
        final Boolean[] allowed={null};
        java.util.function.BooleanSupplier permission=()->{
            if(allowed[0]==null)allowed[0]=permit(p,mob);
            return allowed[0];
        };
        return action.equals("charm")?CharmRuntime.attempt(p,mob,permission):TamingRuntime.attempt(p,mob,permission);
    }
    private static boolean permit(ServerPlayer p,Mob mob){
        // Let other interaction/claim handlers veto a deliberate key action.
        // Do not invoke native mob.interact or recurse into native Taming.
        CHECKING.set(true);
        try{return !NeoForge.EVENT_BUS.post(new PlayerInteractEvent.EntityInteract(p,InteractionHand.MAIN_HAND,mob)).isCanceled();}
        finally{CHECKING.remove();}
    }
}
