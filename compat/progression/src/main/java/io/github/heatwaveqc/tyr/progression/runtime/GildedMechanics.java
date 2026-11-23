package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.runtime.forms.DietPermissions;
import io.github.heatwaveqc.tyr.progression.data.FoodPolicyRegistry;
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
    // Canonical tags are externally extensible, not a recipe-ingredient
    // heuristic or a second item-ID whitelist.
    private static final String GOLD_TAG="#c:foods/golden";
    private static final String NETHER_TAG="#c:foods/nether_only";
    private static final Map<MinecraftServer,Map<UUID,Long>> CAUGHT=new WeakHashMap<>();
    public static void clearFoodCache() { /* live item tags need no stale recipe cache */ }
    public static void register(){
        // Dietary event gating is now registered once by NativeDietAdapters.
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
    public static boolean canonicalGolden(ItemStack stack) {
        return !stack.isEmpty() && Selectors.item(GOLD_TAG, stack);
    }
    public static boolean canonicalNether(ItemStack stack) {
        return !stack.isEmpty() && Selectors.item(NETHER_TAG, stack);
    }
    private static boolean gildedEffect(EffectRuntime.Active active) {
        return active.id().startsWith("tyr_progression/gilded/");
    }
    /** Only the canonical Golden tag grants the 2x Golden bonus.
     * A legacy gold_recipes=true selector must not turn arbitrary crafted
     * foods with a trace of gold into Golden cuisine.
     */
    public static boolean foodBonusMatches(ServerPlayer p,Definition.Effect fx,ItemStack stack){
        return MechanicalSpec.bool(fx.params(),"gold_recipes",false)
            ? canonicalGolden(stack) : Selectors.item(fx.selector(),stack);
    }
    public static boolean lavaCaught(ItemStack stack){
        var data=stack.get(DataComponents.CUSTOM_DATA);
        return data!=null&&data.copyTag().getBoolean(LAVA_CAUGHT);
    }
    private static boolean nativeGildedAllowed(ItemStack stack, boolean lava) {
        return FoodPolicyRegistry.universallyPermitted(stack)
            || canonicalGolden(stack) || canonicalNether(stack) || lava;
    }

    /** Exact stack restriction: canonical tag eligibility, optional
     * earned drink permission, and per-stack lava-catch evidence.
     */
    public static boolean foodAllowed(ServerPlayer p,ItemStack stack){
        if (!usable(p) || (!baseFood(stack) && stack.getFoodProperties(p)==null
                && !FoodPolicyRegistry.customUse(stack))) return true;
        for (var a:EffectRuntime.active(p)) {
            var fx=a.effect();
            if (!fx.type().equals("food_restriction")) continue;
            boolean lava=MechanicalSpec.bool(fx.params(),"lava_caught",false)
                && lavaCaught(stack);
            boolean permittedDrink = beverageAllowed(p, stack, fx);
            if (gildedEffect(a)) {
                if (nativeGildedAllowed(stack,lava) || permittedDrink) continue;
            } else {
                // Other Paths may define other food_restriction effects;
                // canonical Gilded rules do not implicitly authorize them.
                if (FoodPolicyRegistry.universallyPermitted(stack)
                    || Selectors.item(fx.selector(),stack) || lava
                    || MechanicalSpec.bool(fx.params(),"gold_recipes",false)
                       && canonicalGolden(stack) || permittedDrink) continue;
            }
            return false;
        }
        return true;
    }

    private static boolean beverageAllowed(ServerPlayer p,ItemStack stack,Definition.Effect fx){
        if(stack.getUseAnimation()!=UseAnim.DRINK)return false;
        return MechanicalSpec.bool(fx.params(),"drink_permission",false)
                  &&(DietPermissions.permits(p,stack,"alcohol")
                     ||DietPermissions.permits(p,stack,"recreational_drink"))
            ||MechanicalSpec.bool(fx.params(),"baseline_alcohol",false)
                  &&Selectors.item("#c:foods/alcohol",stack);
    }

    /** Allowing a drink must not grant Gilded extra nutrition. */
    public static boolean permissionOnlyDrink(ServerPlayer p,ItemStack stack) {
        if(!usable(p)||stack.isEmpty()||stack.getUseAnimation()!=UseAnim.DRINK
                ||FoodPolicyRegistry.universallyPermitted(stack)) return false;
        boolean any=false;
        for(var a:EffectRuntime.active(p)){
            if(!a.effect().type().equals("food_restriction")
                    ||!gildedEffect(a))continue;
            any=true;
            if(nativeGildedAllowed(stack,
                 MechanicalSpec.bool(a.effect().params(),"lava_caught",false)
                     &&lavaCaught(stack)))return false;
            if(!beverageAllowed(p,stack,a.effect()))return false;
        }
        return any;
    }

    /** Dynamic stack provenance cannot be represented as a per-item client veto. */
    public static boolean hasPotentialStackException(ServerPlayer p) {
        if (!usable(p)) return false;
        return EffectRuntime.active(p).stream().anyMatch(a ->
            a.effect().type().equals("food_restriction")
            && MechanicalSpec.bool(a.effect().params(),"lava_caught",false));
    }

    /** Exact active effects are part of the client projection invalidation key. */
    public static String foodPolicyFingerprint(ServerPlayer p) {
        if (!usable(p)) return "";
        return EffectRuntime.active(p).stream()
            .filter(a -> Set.of("food_restriction","diet_permission").contains(a.effect().type()))
            .map(a -> a.id() + "=" + a.rank() + ":" + a.effect())
            .sorted().collect(java.util.stream.Collectors.joining("\\n"));
    }

    public static boolean hasFoodRestrictions(ServerPlayer p) {
        return usable(p) && EffectRuntime.active(p).stream()
            .anyMatch(a -> a.effect().type().equals("food_restriction"));
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
