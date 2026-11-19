package io.github.heatwaveqc.tmm.progression.runtime.forms;

import io.github.heatwaveqc.tmm.progression.model.Definition;
import io.github.heatwaveqc.tmm.progression.data.FormEffectSpec;
import io.github.heatwaveqc.tmm.progression.foundation.Condition.Result;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

/** Exact native sources where supplied; bare form melee remains a conservative direct hit context. */
public final class FormCombatContext {
    private FormCombatContext(){}
    public static boolean attack(ServerPlayer p,LivingEntity target,DamageSource src,Definition.Effect fx){
        String mode=fx.params().get("native_attack");
        if(mode==null)return true;
        if(!NativeBiteContext.eligible(p,target,src))return false;
        String bite=NativeBiteContext.kind(p,target,src);
        boolean direct=src.getDirectEntity()==p&&src.is(DamageTypes.PLAYER_ATTACK);
        return switch(mode){
            case "dragon_form_melee" -> direct&&bite==null&&DragonAttackContext.emptyClaw(p);
            case "dragon_claw_weapon_melee" -> direct&&bite==null&&DragonAttackContext.clawSword(p);
            case "jawr_wolf_melee" -> direct&&bite==null&&p.getMainHandItem().isEmpty()&&NativeBiteContext.wolf(p);
            case "dragon_breath" -> src.getDirectEntity()==p&&ProviderFlags.nativeDragon(p)==Result.PASS&&
                FormEffectSpec.breathDamage(fx.damageTag())&&exact(src,fx.damageTag());
            case "jawr_wolf_bite" -> direct&&NativeBiteContext.WOLF.equals(bite);
            case "jawr_vampire_bite" -> direct&&NativeBiteContext.VAMPIRE.equals(bite);
            case "jawr_bite" -> direct&&(NativeBiteContext.WOLF.equals(bite)||NativeBiteContext.VAMPIRE.equals(bite));
            default -> false;
        };
    }
    public static boolean special(Definition.Effect fx){
        String mode=fx.params().get("native_attack");
        return "dragon_breath".equals(mode)||"jawr_bite".equals(mode)||
            "jawr_wolf_bite".equals(mode)||"jawr_vampire_bite".equals(mode);
    }
    private static boolean exact(DamageSource src,String key){
        ResourceLocation id=ResourceLocation.tryParse(key);
        return id!=null&&src.is(ResourceKey.create(Registries.DAMAGE_TYPE,id));
    }
    public static boolean mining(ServerPlayer p,BlockState state,Definition.Effect fx){
        String mode=fx.params().get("native_mining");
        if(mode==null)return true;
        if(!p.isAlive()||p.isCreative()||p.isSpectator()||p.getServer()==null||
           !p.getServer().isSameThread()||p.getServer().getPlayerList().getPlayer(p.getUUID())!=p)return false;
        return switch(mode){
            case "dragon_claw" -> DragonAttackContext.legalClawMining(p,state);
            case "jawr_wolf_claw" -> p.getMainHandItem().isEmpty()&&p.hasCorrectToolForDrops(state)&&NativeBiteContext.wolf(p);
            default -> false;
        };
    }
}
