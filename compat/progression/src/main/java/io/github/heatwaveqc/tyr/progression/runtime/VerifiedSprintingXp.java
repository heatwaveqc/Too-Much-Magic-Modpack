package io.github.heatwaveqc.tyr.progression.runtime;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Server-observed genuine on-foot sprint distance, not client sprint packets. */
public final class VerifiedSprintingXp {
    private VerifiedSprintingXp() {}
    private record Sample(Vec3 at,String dimension,long tick,double fraction) {}
    private static final Map<UUID,Sample> LAST=new HashMap<>();
    private static final double MAX_BLOCKS_PER_TICK=4.0;

    public static void tick(ServerPlayer player){
        String dimension=player.level().dimension().location().toString();
        long now=player.level().getGameTime();
        Vec3 at=player.position();
        Sample old=LAST.get(player.getUUID());
        double fraction=old==null?0:old.fraction();
        boolean valid=player.isSprinting()&&!player.isPassenger()
            &&!player.isFallFlying()&&!player.getAbilities().flying
            &&player.hurtTime==0&&player.isAlive();
        if(old!=null&&now==old.tick()+1&&old.dimension().equals(dimension)&&valid){
            double distance=Math.hypot(at.x-old.at().x,at.z-old.at().z);
            if(Double.isFinite(distance)&&distance>0&&distance<=MAX_BLOCKS_PER_TICK){
                double accumulated=fraction+distance;
                long whole=(long)Math.floor(accumulated+1e-9);
                fraction=accumulated-whole;
                if(whole>0)TrainingRewards.sprint(player,whole);
            }
        }
        LAST.put(player.getUUID(),new Sample(at,dimension,now,fraction));
    }
    public static void forget(ServerPlayer player){LAST.remove(player.getUUID());}
}
