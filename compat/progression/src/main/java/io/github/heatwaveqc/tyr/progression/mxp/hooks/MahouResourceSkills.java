package io.github.heatwaveqc.tyr.progression.mxp.hooks;

import io.github.heatwaveqc.tyr.progression.runtime.MagicSkillRuntime;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/** Effective personal reservoir, not Mahou's persisted learned maximum.
 * No intervention in native growth, charge rate, spell-power scaling, or practice receipts. */
public final class MahouResourceSkills {
    public static final String SOURCE="mahoutsukai:personal_mana";
    private MahouResourceSkills(){}
    // Two existing +100% tracks may contribute additively. This is a safety ceiling
    // for this adapter, not a new global skill-balance policy.
    private static final double LIMIT=2;
    public static int capacity(Player player,int earned){
        if(!(player instanceof ServerPlayer p)||earned<=0)return earned;
        return integer(earned,MagicSkillRuntime.capacity(p,SOURCE,earned,LIMIT));
    }
    public static int recovery(Player player,int nativeRecovery){
        if(!(player instanceof ServerPlayer p)||nativeRecovery<=0)return nativeRecovery;
        return integer(nativeRecovery,MagicSkillRuntime.recovery(p,SOURCE,nativeRecovery,LIMIT));
    }
    private static int integer(int original,double adjusted){
        if(!Double.isFinite(adjusted)||adjusted<=original)return original;
        // Preserve integral resource units and reserve headroom for native additions.
        return (int)Math.min(Math.max(original,Integer.MAX_VALUE/4),Math.floor(adjusted+1e-9));
    }
    public static int ownerCapacity(Object tile,int earned){
        Object caster=MxpReflect.call(tile,"getCaster");
        return caster instanceof Player p?capacity(p,earned):earned;
    }
    /** Captured on the sending server thread, never query progression during async packet encoding. */
    public static void snapshot(ServerPlayer player,Object payload){
        if(!(payload instanceof CapacityPacket packet))return;
        Object holder=MxpReflect.field(payload,"mahou");
        Object max=MxpReflect.call(holder,"getMaxMana"),stored=MxpReflect.call(holder,"getStoredMana");
        if(max instanceof Integer earned&&stored instanceof Integer current){
            int effective=capacity(player,earned);
            if(effective!=earned)packet.tyr$capacitySnapshot(current,effective);
        }
    }
    public interface CapacityPacket{
        void tyr$capacitySnapshot(int current,int maximum);
    }
}
