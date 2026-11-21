package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import io.github.heatwaveqc.tyr.progression.foundation.UtilityMath;
import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.CropBlock;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import java.util.*;

/** Native utility events, with narrow positive-only awards and no recipe ingredient refunds. */
public final class UtilityEffects {
    private UtilityEffects(){}
    private record FoodBefore(ItemStack item,int food,float saturation,int tick){}
    private static final Map<UUID,FoodBefore> FOOD=new HashMap<>();
    public static void forget(ServerPlayer p){FOOD.remove(p.getUUID());}
    private static boolean usable(ServerPlayer p){return p.isAlive()&&!p.isCreative()&&!p.isSpectator()&&!NewbieMode.isNewbie(p);}
    private static double sum(ServerPlayer p,String type,java.util.function.Predicate<EffectRuntime.Active> when){
        double total=0;
        for(var a:EffectRuntime.active(p))if(a.effect().type().equals(type)&&when.test(a))total+=a.effect().amountPerRank()*a.rank();
        return Math.min(2,Math.max(0,total));
    }
    public static void foodTick(LivingEntityUseItemEvent.Tick event){
        if(event.isCanceled()||!(event.getEntity() instanceof ServerPlayer p)||!usable(p)||event.getItem().getFoodProperties(p)==null)return;
        if(event.getDuration()>2)return;
        if(sum(p,"food_efficiency",a->GildedMechanics.foodBonusMatches(p,a.effect(),event.getItem()))<=0)return;
        var data=p.getFoodData();
        FOOD.put(p.getUUID(),new FoodBefore(event.getItem().copy(),data.getFoodLevel(),data.getSaturationLevel(),p.tickCount));
    }
    public static void foodFinished(ServerPlayer p,ItemStack consumed){
        FoodBefore old=FOOD.remove(p.getUUID());
        if(old==null||!usable(p)||p.tickCount-old.tick()<0||p.tickCount-old.tick()>2||
            !ItemStack.isSameItemSameComponents(old.item(),consumed)||consumed.getFoodProperties(p)==null)return;
        var data=p.getFoodData();
        int actual=Math.max(0,data.getFoodLevel()-old.food());
        float saturation=Math.max(0,data.getSaturationLevel()-old.saturation());
        double rate=sum(p,"food_efficiency",a->GildedMechanics.foodBonusMatches(p,a.effect(),consumed));
        if(rate<=0)return;
        int extra=UtilityMath.bonus(actual,rate,p.getRandom().nextDouble(),20);
        data.setFoodLevel(Math.min(20,data.getFoodLevel()+extra));
        data.setSaturation(Math.min(data.getFoodLevel(),data.getSaturationLevel()+(float)(saturation*rate)));
    }
    public static void foodStopped(LivingEntityUseItemEvent.Stop event){if(event.getEntity() instanceof ServerPlayer p)forget(p);}
    public static void vanillaXp(PlayerXpEvent.XpChange event){
        if(event.isCanceled()||event.getAmount()<=0||!(event.getEntity() instanceof ServerPlayer p)||!usable(p))return;
        double rate=sum(p,"vanilla_xp_bonus",a->true);
        int extra=UtilityMath.bonus(event.getAmount(),rate,p.getRandom().nextDouble(),1000000);
        if(extra>0)event.setAmount((int)Math.min(Integer.MAX_VALUE,(long)event.getAmount()+extra));
    }
    public static void immunity(MobEffectEvent.Applicable event){
        if(!(event.getEntity() instanceof ServerPlayer p)||!usable(p))return;
        var instance=event.getEffectInstance();if(instance==null)return;
        var id=instance.getEffect().unwrapKey().map(key->key.location().toString()).orElse("");
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();if(!fx.type().equals("status_immunity")||!id.equals(fx.params().get("effect")))continue;
            if(MechanicalSpec.bool(fx.params(),"harmful_only",true)&&instance.getEffect().value().getCategory()!=MobEffectCategory.HARMFUL)continue;
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);return;
        }
    }
    public static void crafted(PlayerEvent.ItemCraftedEvent event){
        if(event.getEntity() instanceof ServerPlayer p&&usable(p)&&!event.getCrafting().isEmpty())
            ProgressionApi.record(p,Selectors.itemAction("item_crafted",event.getCrafting()));
    }
    private static boolean roll(ServerPlayer p,EffectRuntime.Active a){
        return p.getRandom().nextDouble()<MechanicalSpec.scaled(a.effect().amountPerRank(),a.rank(),1)*
            MechanicalSpec.number(a.effect().params(),"chance",1,0,1);
    }
    private static boolean plain(ItemStack stack,net.minecraft.world.item.Item... items){
        for(var item:items)if(stack.is(item)&&ItemStack.isSameItemSameComponents(stack,new ItemStack(item)))return true;
        return false;
    }
    public static void crops(ServerPlayer p,BlockDropsEvent event){
        if(!usable(p)||event.isCanceled()||event.getBlockEntity()!=null||!(event.getState().getBlock() instanceof CropBlock crop)||!crop.isMaxAge(event.getState()))return;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();if(!fx.type().equals("crop_bonus")||!Selectors.block(fx.selector(),event.getState())||
                !Selectors.item(fx.weapon(),event.getTool())||!roll(p,a))continue;
            for(var drop:event.getDrops()){
                var stack=drop.getItem();
                if(!plain(stack,Items.WHEAT,Items.CARROT,Items.POTATO,Items.BEETROOT))continue;
                event.getDrops().add(new ItemEntity(event.getLevel(),drop.getX(),drop.getY(),drop.getZ(),stack.copyWithCount(1)));return;
            }
        }
    }
    public static void mobDrops(LivingDropsEvent event){
        if(event.isCanceled()||!(event.getSource().getEntity() instanceof ServerPlayer p)||!usable(p)||
            event.getSource().getDirectEntity()!=p||!(event.getEntity() instanceof Monster)||event.getEntity() instanceof WitherBoss||
            event.getEntity() instanceof EnderDragon||p.isAlliedTo(event.getEntity()))return;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();if(!fx.type().equals("ordinary_drop_bonus")||!Selectors.entity(fx.selector(),event.getEntity())||
                !Selectors.item(fx.weapon(),p.getMainHandItem())||!EffectRuntime.sourceMatches(fx.damageTag(),event.getSource())||!roll(p,a))continue;
            for(var drop:event.getDrops()){
                var stack=drop.getItem();
                if(!plain(stack,Items.ROTTEN_FLESH,Items.BONE,Items.STRING,Items.SPIDER_EYE,Items.GUNPOWDER,Items.SLIME_BALL))continue;
                event.getDrops().add(new ItemEntity(event.getEntity().level(),drop.getX(),drop.getY(),drop.getZ(),stack.copyWithCount(1)));return;
            }
        }
    }
}
