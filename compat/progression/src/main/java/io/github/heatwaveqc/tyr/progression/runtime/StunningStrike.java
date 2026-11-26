package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import io.github.heatwaveqc.tyr.progression.foundation.StunWindow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.bus.api.EventPriority;
import java.util.Comparator;

/** Direct, damaging, empty-hand strikes only. Never modifies saved NoAI flags. */
public final class StunningStrike {
    private StunningStrike(){}
    private static final String START="tyr_progression:stun_start",UNTIL="tyr_progression:stun_until",READY="tyr_progression:stun_ready";
    public static void register(){
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST,StunningStrike::hit);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST,StunningStrike::interrupt);
    }
    private static boolean immune(Mob mob){
        return !(mob instanceof Monster)||boss(mob)||mob instanceof TamableAnimal animal&&animal.isTame()
            ||Selectors.entity("#tyr_progression:stun_immune",mob);
    }
    static boolean boss(Mob mob){
        return mob instanceof WitherBoss||mob instanceof EnderDragon
            ||Selectors.entity("#tyr_progression:bosses",mob)
            ||Selectors.entity("#tyr_progression:minibosses",mob)
            ||Selectors.entity("#tyr_progression:boss_generic_death_excluded",mob)
            ||mob.getPersistentData().getBoolean("apoth.boss")||mob.getPersistentData().getBoolean("apoth.miniboss");
    }
    private static long now(Mob mob){return ((ServerLevel)mob.level()).getServer().overworld().getGameTime();}
    private static StunWindow.Window read(Mob mob){
        var data=mob.getPersistentData();
        if(!data.contains(START)||!data.contains(UNTIL)||!data.contains(READY))return null;
        try{return new StunWindow.Window(data.getLong(START),data.getLong(UNTIL),data.getLong(READY));}
        catch(IllegalArgumentException invalid){return null;}
    }
    public static boolean stunned(Mob mob){
        if(!(mob.level() instanceof ServerLevel)||!mob.isAlive())return false;
        if(!mob.getPersistentData().contains(UNTIL))return false;
        var w=read(mob);return w!=null&&!immune(mob)&&w.stunned(now(mob));
    }
    private static void hit(LivingDamageEvent.Post e){
        if(e.getNewDamage()<=0||!(e.getEntity() instanceof Mob mob)||!mob.isAlive()||immune(mob)
            ||!(e.getSource().getEntity() instanceof ServerPlayer p)||e.getSource().getDirectEntity()!=p
            ||!e.getSource().is(DamageTypes.PLAYER_ATTACK)||p.isCreative()||p.isSpectator()||!p.isAlive()
            ||NewbieMode.isNewbie(p)||p instanceof net.neoforged.neoforge.common.util.FakePlayer
            ||!p.getMainHandItem().isEmpty()||p.isAlliedTo(mob)||mob.isAlliedTo(p)
            ||!MechanicalEffects.areaAllowed(p,mob))return;
        long at=now(mob);var old=read(mob);
        if(old!=null&&!old.canApply(at)&&at>=old.start())return;
        // Deterministic best eligible duration; only one roll/lease per victim per hit.
        var selected=EffectRuntime.active(p).stream().filter(a->a.effect().type().equals("stunning_strike")
                &&Selectors.entity(a.effect().selector(),mob))
            .max(Comparator.<EffectRuntime.Active>comparingDouble(a->MechanicalSpec.scaled(a.effect().amountPerRank(),a.rank(),60))
                .thenComparing(EffectRuntime.Active::id)).orElse(null);
        if(selected==null)return;
        var params=selected.effect().params();
        if(p.getRandom().nextDouble()>=MechanicalSpec.number(params,"chance",1,0,1))return;
        int duration=Math.max(1,(int)Math.floor(MechanicalSpec.scaled(selected.effect().amountPerRank(),selected.rank(),60)));
        var w=StunWindow.begin(at,duration,MechanicalSpec.integer(params,"recovery_ticks",100,20,1200));
        var data=mob.getPersistentData();data.putLong(START,w.start());data.putLong(UNTIL,w.until());data.putLong(READY,w.ready());
        stopIntent(mob);
    }
    /** Preserve external knockback, gravity, damage, despawn and environmental physics. */
    public static void stopIntent(Mob mob){
        mob.getNavigation().stop();mob.setSpeed(0);mob.setXxa(0);mob.setYya(0);mob.setZza(0);mob.setJumping(false);
    }
    private static void interrupt(LivingIncomingDamageEvent e){
        // No retroactive cancellation of already-launched arrows, spell projectiles or environmental damage.
        if(e.getSource().getEntity() instanceof Mob mob&&e.getSource().getDirectEntity()==mob&&stunned(mob))
            e.setCanceled(true);
    }
}
