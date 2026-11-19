package io.github.heatwaveqc.tmm.progression.runtime.forms;

import io.github.heatwaveqc.tmm.progression.model.Definition;
import io.github.heatwaveqc.tmm.progression.foundation.Condition.Result;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.level.block.state.BlockState;

/** Never infer bite or breath from empty hands alone. Scope exact provider damage and actual owner. */
public final class FormCombatContext {
    private FormCombatContext(){}
    private static boolean wolf(ServerPlayer p){
        return NativeFormConditions.jawrShape(p,"WEREWOLF")==Result.PASS||
               NativeFormConditions.jawrShape(p,"WOLF")==Result.PASS;
    }
    public static boolean attack(ServerPlayer p,DamageSource src,Definition.Effect fx){
        String mode=fx.params().get("native_attack");
        if(mode==null)return true;
        if(src.getEntity()!=p||!p.isAlive()||p.isCreative()||p.isSpectator())return false;
        boolean direct=src.getDirectEntity()==p&&p.getMainHandItem().isEmpty()&&src.is(DamageTypes.PLAYER_ATTACK);
        return switch(mode){
            case "dragon_form_melee" -> direct&&ProviderFlags.nativeDragon(p)==Result.PASS;
            case "jawr_wolf_melee" -> direct&&wolf(p);
            case "dragon_breath" -> ProviderFlags.nativeDragon(p)==Result.PASS&&exact(src,fx.damageTag(),"dragonsurvival:");
            case "jawr_bite" -> (JawrFormState.present(p,"vampire")||wolf(p))&&exact(src,fx.damageTag(),"witchery:");
            default -> false;
        };
    }
    public static boolean special(Definition.Effect fx){
        String mode=fx.params().get("native_attack");
        return "dragon_breath".equals(mode)||"jawr_bite".equals(mode);
    }
    private static boolean exact(DamageSource src,String key,String prefix){
        if(!key.startsWith(prefix))return false;
        ResourceLocation id=ResourceLocation.tryParse(key);
        return id!=null&&src.is(ResourceKey.create(Registries.DAMAGE_TYPE,id));
    }
    public static boolean mining(ServerPlayer p,BlockState state,Definition.Effect fx){
        String mode=fx.params().get("native_mining");
        if(mode==null)return true;
        if(!p.getMainHandItem().isEmpty()||!p.hasCorrectToolForDrops(state)||p.isCreative()||p.isSpectator())return false;
        return switch(mode){
            case "dragon_claw" -> ProviderFlags.nativeDragon(p)==Result.PASS;
            case "jawr_wolf_claw" -> wolf(p);
            default -> false;
        };
    }
}
