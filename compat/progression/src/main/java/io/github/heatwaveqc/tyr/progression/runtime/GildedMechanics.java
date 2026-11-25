package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.runtime.forms.DietPermissions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import java.util.*;

/** First-party Gilded branch port, expressed as opt-in owned effects; no active path or Tyz bridge. */
public final class GildedMechanics {
    private GildedMechanics(){}
    private static final String LAVA_CAUGHT="tyr_progression:lava_caught";
    private static final String GOLD_TAG="#tyr_progression:gilded_gold_foods";
    private record GoldCache(RecipeManager recipes,Set<Item> gold){}
    private static final Map<MinecraftServer,GoldCache> GOLD=new WeakHashMap<>();
    private static final Map<MinecraftServer,Map<UUID,Long>> CAUGHT=new WeakHashMap<>();
    public static void clearFoodCache(){synchronized(GOLD){GOLD.clear();}}
    public static void register(){
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST,GildedMechanics::foodStart);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST,GildedMechanics::foodTick);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST,GildedMechanics::caught);
    }
    private static boolean usable(ServerPlayer p){
        return p.isAlive()&&!p.isCreative()&&!p.isSpectator()&&!NewbieMode.isNewbie(p);
    }
    private static boolean owns(ServerPlayer p,String name){
        return usable(p)&&EffectRuntime.active(p).stream().anyMatch(a->a.effect().type().equals(name));
    }
    public static boolean piglinKin(ServerPlayer p,boolean alarm){
        return usable(p)&&EffectRuntime.active(p).stream().anyMatch(a->a.effect().type().equals("piglin_kinship")
            &&(!alarm||MechanicalSpec.bool(a.effect().params(),"alarm_protection",true)));
    }
    public static void fireDamage(LivingIncomingDamageEvent e){
        if(e.getEntity() instanceof ServerPlayer p&&e.getSource().is(DamageTypeTags.IS_FIRE)&&owns(p,"fire_immunity"))e.setCanceled(true);
    }
    public static void tick(ServerPlayer p){
        if(p.getRemainingFireTicks()>0&&owns(p,"fire_immunity"))p.setRemainingFireTicks(0);
    }
    /** Current native tool harvest checks only; no toolless or above-diamond permission. */
    public static boolean heldHarvest(ServerPlayer p,BlockState state,int tier){
        if(!usable(p)||tier<1||tier>3)return false;
        ItemStack stack=p.getMainHandItem();
        if(!stack.is(Items.GOLDEN_PICKAXE)||!state.is(BlockTags.MINEABLE_WITH_PICKAXE))return false;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();
            if(fx.type().equals("held_harvest_tier")&&fx.tier()>=tier&&Selectors.item(fx.weapon(),stack)
                &&Selectors.block(fx.selector(),state))return true;
        }
        return false;
    }
    private static boolean baseFood(ItemStack stack){return !stack.isEmpty()&&stack.get(DataComponents.FOOD)!=null;}
    private static boolean goldSeed(ItemStack stack){
        return stack.is(Items.GOLDEN_APPLE)||stack.is(Items.ENCHANTED_GOLDEN_APPLE)||stack.is(Items.GOLDEN_CARROT)||Selectors.item(GOLD_TAG,stack);
    }
    /** Recipe-qualified item types, like the historical Gilded policy, not crafted-stack provenance. */
    private static boolean goldRecipe(ServerPlayer p,ItemStack stack){
        if(goldSeed(stack))return true;
        if(!baseFood(stack)||p.getServer()==null||!p.getServer().isSameThread())return false;
        var server=p.getServer();var recipes=server.getRecipeManager();
        synchronized(GOLD){
            var cached=GOLD.get(server);
            if(cached==null||cached.recipes()!=recipes){
                Set<Item> known=new HashSet<>(Set.of(Items.GOLDEN_APPLE,Items.ENCHANTED_GOLDEN_APPLE,Items.GOLDEN_CARROT));
                for(int depth=0;depth<8;depth++){
                    Set<Item> next=new HashSet<>(known);
                    for(var holder:recipes.getRecipes()){
                        if(!(holder.value() instanceof CraftingRecipe recipe))continue;
                        ItemStack output=recipe.getResultItem(p.registryAccess());
                        if(!baseFood(output)||known.contains(output.getItem()))continue;
                        if(goldSeed(output)){next.add(output.getItem());continue;}
                        boolean gold=false;
                        for(var ingredient:recipe.getIngredients()){
                            if(ingredient.test(new ItemStack(Items.GOLD_NUGGET))||ingredient.test(new ItemStack(Items.GOLD_INGOT))||ingredient.test(new ItemStack(Items.GOLD_BLOCK))){gold=true;break;}
                            for(var option:ingredient.getItems())if(baseFood(option)&&(goldSeed(option)||known.contains(option.getItem()))){gold=true;break;}
                            if(gold)break;
                        }
                        if(gold)next.add(output.getItem());
                    }
                    if(next.equals(known))break;
                    known=next;
                }
                cached=new GoldCache(recipes,Set.copyOf(known));GOLD.put(server,cached);
            }
            return cached.gold().contains(stack.getItem());
        }
    }
    public static boolean foodBonusMatches(ServerPlayer p,Definition.Effect fx,ItemStack stack){
        return Selectors.item(fx.selector(),stack)||MechanicalSpec.bool(fx.params(),"gold_recipes",false)&&goldRecipe(p,stack);
    }
    private static boolean lavaCaught(ItemStack stack){
        var data=stack.get(DataComponents.CUSTOM_DATA);
        return data!=null&&data.copyTag().getBoolean(LAVA_CAUGHT);
    }
    private static boolean foodAllowed(ServerPlayer p,ItemStack stack){
        if(!usable(p)||(!baseFood(stack)&&stack.getFoodProperties(p)==null))return true;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();if(!fx.type().equals("food_restriction"))continue;
            boolean beverage=stack.getUseAnimation()==UseAnim.DRINK&&MechanicalSpec.bool(fx.params(),"drink_permission",false)
                &&(DietPermissions.permits(p,stack,"alcohol")||DietPermissions.permits(p,stack,"recreational_drink"));
            if(Selectors.item(fx.selector(),stack)||beverage||MechanicalSpec.bool(fx.params(),"gold_recipes",false)&&goldRecipe(p,stack)
                ||MechanicalSpec.bool(fx.params(),"lava_caught",false)&&lavaCaught(stack))continue;
            return false;
        }
        return true;
    }
    public static void foodStart(LivingEntityUseItemEvent.Start e){
        if(e.getEntity() instanceof ServerPlayer p&&!foodAllowed(p,e.getItem()))e.setCanceled(true);
    }
    public static void foodTick(LivingEntityUseItemEvent.Tick e){
        if(e.getEntity() instanceof ServerPlayer p&&!foodAllowed(p,e.getItem()))e.setCanceled(true);
    }
    public static void ore(ServerPlayer p,BlockDropsEvent e){
        if(!usable(p)||e.isCanceled()||e.getDrops().isEmpty()||e.getTool().getEnchantmentLevel(p.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH))>0)return;
        var emitted=new HashSet<String>();
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();
            if(fx.type().equals("ore_practice")&&Selectors.block(fx.selector(),e.getState())&&emitted.add(fx.params().get("event")))
                ProgressionApi.recordExternal(p,fx.params().get("event"));
        }
    }
    public static void caught(ItemFishedEvent e){
        FishingHook hook=e.getHookEntity();
        if(e.isCanceled()||hook.level().isClientSide||!(e.getEntity() instanceof ServerPlayer p)||hook.getPlayerOwner()!=p||!usable(p)
            ||!(hook.level().getFluidState(hook.blockPosition()).is(FluidTags.LAVA)||hook.level().getFluidState(hook.blockPosition().below()).is(FluidTags.LAVA))
            ||e.getDrops().stream().allMatch(ItemStack::isEmpty))return;
        var effects=EffectRuntime.active(p).stream().filter(a->a.effect().type().equals("lava_fishing_practice")).toList();
        if(effects.isEmpty())return;
        long now=p.server.overworld().getGameTime();
        var history=CAUGHT.computeIfAbsent(p.server,server->new HashMap<>());
        history.entrySet().removeIf(x->now-x.getValue()>1200||now<x.getValue());
        if(!history.containsKey(hook.getUUID())&&history.size()>=2048)return;
        for(var stack:e.getDrops())if(baseFood(stack))
            CustomData.update(DataComponents.CUSTOM_DATA,stack,nbt->nbt.putBoolean(LAVA_CAUGHT,true));
        if(history.putIfAbsent(hook.getUUID(),now)!=null)return;
        var events=new HashSet<String>();
        for(var a:effects)if(events.add(a.effect().params().get("event")))ProgressionApi.recordExternal(p,a.effect().params().get("event"));
    }
}
