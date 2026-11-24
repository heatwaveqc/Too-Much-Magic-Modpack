package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.core.BaselinePaths;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;
import io.github.heatwaveqc.tyr.progression.data.PotionEnrollmentTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Configurable manual enrollment or server-verified Monk sunset contemplation. */
public final class Enrollment {
    private Enrollment() {}
    private static final Map<UUID,Map<String,Integer>> sunsetTicks = new HashMap<>();

    public static boolean enrollManual(ServerPlayer player, String path) {
        if (!"manual".equals(PathRegistry.enrollment(path))
                || player.isCreative() || player.isSpectator()) return false;
        return ProgressionApi.join(player,path);
    }

    /**
     * Trusted server callback after a drink has fully finished. The supplied ItemStack is
     * the pre-consumption item from LivingEntityUseItemEvent.Finish, not the empty bottle.
     * Only the configured paths are eligible; custom/splash/lingering effects do not count.
     */
    public static void potionFinished(ServerPlayer player, ItemStack consumed) {
        if (player.isCreative() || player.isSpectator() || consumed.isEmpty()) return;
        var contents = consumed.get(DataComponents.POTION_CONTENTS);
        if (contents == null || !contents.customEffects().isEmpty()) return;
        String itemId = BuiltInRegistries.ITEM.getKey(consumed.getItem()).toString();
        String potionId = contents.potion()
            .flatMap(holder -> holder.unwrapKey())
            .map(key -> key.location().toString()).orElse("");
        for (Map.Entry<String,PotionEnrollmentTrigger> entry : PathRegistry.potionTriggers().entrySet()) {
            String path = entry.getKey();
            if (ProgressionApi.track(player,path).joined()) continue;
            PotionEnrollmentTrigger trigger = entry.getValue();
            if (!trigger.item().equals(itemId) || !trigger.potions().contains(potionId)) continue;
            var requiredEffect = BuiltInRegistries.MOB_EFFECT.getHolder(
                ResourceLocation.parse(trigger.requiredEffect())).orElse(null);
            if (requiredEffect == null) continue;
            var advancement = player.server.getAdvancements().get(
                ResourceLocation.parse(trigger.requiredAdvancement()));
            boolean earned = advancement != null
                && player.getAdvancements().getOrStartProgress(advancement).isDone();
            if (!trigger.matches(itemId,potionId,player.hasEffect(requiredEffect),earned)) continue;
            // This method is the single authority: mutual exclusion and external join vetoes
            // belong in ProgressionApi.join, never merely in this consumable event handler.
            if (!ProgressionApi.join(player,path)) continue;
            for (String effectId : trigger.clearEffects()) {
                var effect = BuiltInRegistries.MOB_EFFECT.getHolder(
                    ResourceLocation.parse(effectId)).orElse(null);
                if (effect != null) player.removeEffect(effect);
            }
            ProgressionApi.recordExternal(player,trigger.completionEvent());
            player.displayClientMessage(Component.literal("Trial complete: "
                + path + " unlocked"),false);
            break; // exactly one enrollment per completed drink
        }
    }

    /** Invoke only on completed real food consumption, before subsequent food event rewards. */
    public static void foodFinished(ServerPlayer player,ItemStack consumed){
        if(player.isCreative()||player.isSpectator()||consumed.isEmpty()||consumed.getFoodProperties(player)==null)return;
        for(var entry:PathRegistry.foodRites().entrySet().stream().sorted(Map.Entry.comparingByKey()).toList()){
            String path=entry.getKey();var rite=entry.getValue();
            if(ProgressionApi.track(player,path).joined()||!Selectors.item(rite.spec().target(),consumed)||!rite.conditions().test(player))continue;
            if(!ProgressionApi.join(player,path))continue;
            if(!rite.spec().completionEvent().isEmpty())ProgressionApi.recordExternal(player,rite.spec().completionEvent());
            player.displayClientMessage(Component.literal("Rite complete: "+path+" unlocked"),false);
            break;
        }
    }
    /** Before native mobInteract: a recognized offering is reserved for enrollment,
     * not passed through to an NPC's unrelated enchantment/trade handler on rejection. */
    public static boolean entityOffering(ServerPlayer player,
            net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.EntityInteract event) {
        if (event.isCanceled() || event.getHand() != net.minecraft.world.InteractionHand.MAIN_HAND
                || PathRegistry.entityOfferings().isEmpty()
                || !event.getTarget().isAlive()) return false;
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) return false;
        String entityId = BuiltInRegistries.ENTITY_TYPE.getKey(event.getTarget().getType()).toString();
        String itemId = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        var candidates = PathRegistry.entityOfferings().entrySet().stream()
            .filter(e -> e.getValue().entity().equals(entityId) && e.getValue().item().equals(itemId))
            .toList();
        if (candidates.isEmpty()) return false;
        var materials = candidates.stream().anyMatch(e -> !e.getValue().jewelryMaterial().isEmpty())
            ? JewelryOfferingMaterials.read(stack) : java.util.Set.<String>of();
        var matches = candidates.stream().filter(e -> e.getValue().matches(entityId,itemId,materials)).toList();
        if (matches.isEmpty()) return false;
        event.setCanceled(true);
        event.setCancellationResult(net.minecraft.world.InteractionResult.FAIL);
        if (matches.size() != 1 || player.isCreative() || player.isSpectator()) return true;
        var match = matches.getFirst();
        String path = match.getKey();
        if (ProgressionApi.track(player,path).joined() || !ProgressionApi.join(player,path)) {
            player.displayClientMessage(Component.literal("You cannot undertake this initiation."),true);
            return true;
        }
        stack.consume(1,player); // only after authoritative admission, exactly once
        event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
        if (!match.getValue().completionEvent().isEmpty())
            ProgressionApi.recordExternal(player,match.getValue().completionEvent());
        player.displayClientMessage(Component.literal("Offering accepted: " + path + " unlocked"),false);
        return true;
    }

    public static void tick(ServerPlayer player) {
        if (player.tickCount % 20 == 0 && !player.isCreative() && !player.isSpectator())
            for (String candidate:PathRegistry.all().keySet())
                if (!BaselinePaths.isBaseline(candidate)
                    && "automatic".equals(PathRegistry.enrollment(candidate))
                    && !ProgressionApi.track(player,candidate).joined())
                    ProgressionApi.join(player,candidate);
        var timers=sunsetTicks.computeIfAbsent(player.getUUID(),ignored->new HashMap<>());
        timers.keySet().retainAll(PathRegistry.timedEnrollments().keySet());
        for(var entry:PathRegistry.timedEnrollments().entrySet()){
            String path=entry.getKey();var ritual=entry.getValue();
            if(ProgressionApi.track(player,path).joined()||!PathRegistry.eligible(player,path)){
                timers.remove(path);continue;
            }
            long time=Math.floorMod(player.level().getDayTime(),24000L);
            var pos=player.blockPosition();
            boolean valid=!player.isCreative()&&!player.isSpectator()
                &&player.level().dimension().location().toString().equals(ritual.dimension())
                &&ritual.acceptsTime(time)
                &&(!ritual.dry()||!player.level().isRainingAt(pos))
                &&(!ritual.clearSky()||player.level().canSeeSky(pos.above()))
                &&player.getLookAngle().x<ritual.maxLookX()
                &&Math.abs(player.getLookAngle().y)<ritual.maxAbsLookY()
                &&(!ritual.emptyHand()||player.getMainHandItem().isEmpty());
            if(valid&&ritual.unarmored())for(var stack:player.getArmorSlots())if(!stack.isEmpty())valid=false;
            int ticks=valid?timers.getOrDefault(path,0)+1:0;
            if(ticks==0)timers.remove(path);else timers.put(path,ticks);
            if(ticks>0&&ticks%20==0)player.displayClientMessage(Component.literal(ritual.label()+": "+ticks+"/"+ritual.ticks()),true);
            if(ticks>=ritual.ticks()){
                timers.remove(path);
                if(ProgressionApi.join(player,path)){
                    ProgressionApi.recordExternal(player,ritual.completionEvent());
                    player.displayClientMessage(Component.literal(ritual.label()+" complete: "+path+" unlocked"),false);
                }
            }
        }
        if(timers.isEmpty())sunsetTicks.remove(player.getUUID());
    }

    public static void clear(ServerPlayer player) { sunsetTicks.remove(player.getUUID()); }
}
