package io.github.heatwaveqc.tyr.progression.runtime.forms;
import io.github.heatwaveqc.tyr.progression.foundation.LichSkillMath;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerPlayer;

/** Known native zombie/skeleton families only. Keep actual provider counts and spawn paths. */
public final class LichServantLimits{
    private static final String PREFIX="com.Polarice3.Goety.common.magic.spells.necromancy.";
    private static final TagKey<EntityType<?>> ZOMBIES=TagKey.create(Registries.ENTITY_TYPE,ResourceLocation.parse("goety:zombie_servants"));
    private static final TagKey<EntityType<?>> SKELETONS=TagKey.create(Registries.ENTITY_TYPE,ResourceLocation.parse("goety:skeleton_servants"));
    private LichServantLimits(){}
    private static int boosted(int limit,LivingEntity owner){
        if(!(owner instanceof ServerPlayer p)||!p.isAlive()||!LichEffects.pinned("goety","3.2.2")||!LichEffects.eligible(p))return limit;
        return LichSkillMath.servantLimit(limit,LichEffects.rate(p,"lich_servant_capacity"));
    }
    public static int spell(int limit,Object spell,LivingEntity owner){
        if(spell==null)return limit;
        String name=spell.getClass().getName();
        return name.equals(PREFIX+"ZombieSpell")||name.equals(PREFIX+"SkeletonSpell")?boosted(limit,owner):limit;
    }
    public static int ritual(int limit,LivingEntity owner,EntityType<?> type){
        return type!=null&&(type.is(ZOMBIES)||type.is(SKELETONS))?boosted(limit,owner):limit;
    }
}
