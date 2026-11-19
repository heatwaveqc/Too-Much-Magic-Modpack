package io.github.heatwaveqc.tmm.progression.runtime.forms;

import io.github.heatwaveqc.tmm.progression.foundation.NativeAttackScope;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModList;
import java.util.function.Supplier;
import static io.github.heatwaveqc.tmm.progression.foundation.Condition.Result.PASS;

/** JAWR 0.5.39 uses vanilla player_attack for both bites: distinguish only the real native hurt call. */
public final class NativeBiteContext {
    private static final NativeAttackScope SCOPE=new NativeAttackScope();
    public static final String WOLF="jawr_wolf_bite",VAMPIRE="jawr_vampire_bite";
    private NativeBiteContext(){}
    private static boolean pinned(){
        return ModList.get().getModContainerById("witchery")
            .filter(m->{String v=m.getModInfo().getVersion().toString();
                return v.equals("0.5.39")||v.startsWith("0.5.39+");}).isPresent();
    }
    public static boolean wolf(ServerPlayer p){
        return NativeFormConditions.jawrShape(p,"WEREWOLF")==PASS||
               NativeFormConditions.jawrShape(p,"WOLF")==PASS;
    }
    public static boolean eligible(ServerPlayer p,LivingEntity target,DamageSource source){
        return p.isAlive()&&!p.isCreative()&&!p.isSpectator()&&p.getServer()!=null&&
            p.getServer().isSameThread()&&p.getServer().getPlayerList().getPlayer(p.getUUID())==p&&
            p!=target&&target.level()==p.level()&&source.getEntity()==p;
    }
    public static boolean hurt(String kind,LivingEntity target,DamageSource source,Supplier<Boolean> original){
        if(!(source.getEntity() instanceof ServerPlayer p)||!eligible(p,target,source)||!pinned()||
           source.getDirectEntity()!=p||!source.is(DamageTypes.PLAYER_ATTACK))return original.get();
        if(!(WOLF.equals(kind)&&wolf(p)||VAMPIRE.equals(kind)&&JawrFormState.present(p,"vampire")))
            return original.get();
        return SCOPE.around(kind,p,target,source,original);
    }
    public static String kind(ServerPlayer p,LivingEntity target,DamageSource source){
        return SCOPE.kind(p,target,source);
    }
}
