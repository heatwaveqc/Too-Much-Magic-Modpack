package io.github.heatwaveqc.tmm.transformations.vampire;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import java.lang.reflect.Method;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

/**
 * Ordinary physical blood bottle compatibility with JAWR 0.5.39 (1.21.1).
 *
 * No blanket recipe tag: Hexerei's filling serializer checks only the item
 * identity and will otherwise accept even Lilith/chicken variants of JAWR's
 * single bottle item. Component-gated server transactions are mandatory.
 *
 * The bridge only handles player-facing bottles, leaving JAWR's own use,
 * drinking, crucible, wine glass, chicken and Lilith interactions untouched.
 * No per-tick handler; two click events and one native drink-finish event.
 */
public final class JAWRBottleInterop {
    private static final ResourceLocation JAWR_BOTTLE = id("witchery:bottle_of_blood");
    private static final ResourceLocation IRONS_VIAL = id("irons_spellbooks:blood_vial");
    private static final ResourceLocation HEX_BOTTLE = id("hexerei:blood_bottle");
    private static final ResourceLocation HEX_CAULDRON = id("hexerei:mixing_cauldron");
    private static final ResourceLocation IRONS_CAULDRON = id("irons_spellbooks:alchemist_cauldron");
    private static final ResourceLocation HEX_BLOOD = id("hexerei:blood_fluid");
    private static final ResourceLocation CANONICAL_BLOOD = id("evilcraft:blood");
    private static final int BOTTLE_MB = 250;
    private static final int JAWR_DRINK_CREDIT = 500; // Same as native WineGlassItem.consumeBlood.

    private static DataComponentType<?> chickenMarker;
    private static DataComponentType<?> vampireMarker;
    private static Method nativeBloodGain;
    private static boolean ready;

    private JAWRBottleInterop() { }

    private static ResourceLocation id(String key) {
        return ResourceLocation.parse(key);
    }

    private static DataComponentType<?> resolveMarker(String getter) throws ReflectiveOperationException {
        Class<?> registry = Class.forName("dev.sterner.witchery.core.registry.WitcheryDataComponents");
        Object singleton = registry.getField("INSTANCE").get(null);
        Object holder = registry.getMethod(getter).invoke(singleton);
        Object type = holder.getClass().getMethod("get").invoke(holder);
        if (!(type instanceof DataComponentType<?> component)) {
            throw new ReflectiveOperationException(getter + " does not return a DataComponentType");
        }
        return component;
    }

    public static void register() {
        if (!ModList.get().isLoaded("witchery")) return;
        if (!ModList.get().isLoaded("hexerei") && !ModList.get().isLoaded("irons_spellbooks")) return;
        // Do NOT resolve deferred JAWR components in the mod constructor:
        // registries are not populated until after mod construction.
        NeoForge.EVENT_BUS.addListener(JAWRBottleInterop::onServerStarting);
        NeoForge.EVENT_BUS.addListener(JAWRBottleInterop::onRightClickBlock);
        NeoForge.EVENT_BUS.addListener(JAWRBottleInterop::onRightClickItem);
        NeoForge.EVENT_BUS.addListener(JAWRBottleInterop::onDrinkFinish);
    }

    private static void onServerStarting(ServerStartingEvent event) {
        try {
            chickenMarker = resolveMarker("getCHICKEN_BLOOD");
            vampireMarker = resolveMarker("getVAMPIRE_BLOOD");
            nativeBloodGain = Class.forName("dev.sterner.witchery.feature.blood_pool.BloodPoolHandler")
                    .getMethod("increaseBlood", LivingEntity.class, int.class);
            ready = true;
            TransformationsMod.LOGGER.info("JAWR ordinary blood bottle bridge enabled (strict component gate)");
        } catch (ReflectiveOperationException | LinkageError | RuntimeException failure) {
            ready = false; // Fail closed: never treat unknown JAWR components as ordinary blood.
            TransformationsMod.LOGGER.error("Pinned JAWR blood markers/API unavailable; bottle bridge disabled", failure);
        }
    }

    /** A native JAWR blood bottle is ordinary ONLY when neither protected marker exists. */
    public static boolean isOrdinaryJAWRBottle(ItemStack stack) {
        return ready && JAWR_BOTTLE.equals(BuiltInRegistries.ITEM.getKey(stack.getItem()))
                && BloodBottlePolicy.isOrdinary(stack.has(chickenMarker), stack.has(vampireMarker));
    }

    private static boolean isJAWRVampire(ServerPlayer player) {
        if (!ready) return false;
        try {
            Object data = VampireNativeAccess.jawrVampireData(player);
            return data != null && Boolean.TRUE.equals(data.getClass().getMethod("isVampire").invoke(data));
        } catch (ReflectiveOperationException | RuntimeException failure) {
            TransformationsMod.LOGGER.error("Unable to verify JAWR vampire state for blood drinking", failure);
            return false;
        }
    }

    private static boolean creditNativeBlood(ServerPlayer player) {
        try {
            nativeBloodGain.invoke(null, player, JAWR_DRINK_CREDIT);
            return true;
        } catch (ReflectiveOperationException | RuntimeException failure) {
            TransformationsMod.LOGGER.error("JAWR native blood credit failed", failure);
            return false;
        }
    }

    private static void giveEmptyBottle(ServerPlayer player, net.minecraft.world.InteractionHand hand) {
        if (player.getAbilities().instabuild) return;
        ItemStack held = player.getItemInHand(hand);
        held.shrink(1);
        ItemStack glass = new ItemStack(Items.GLASS_BOTTLE);
        if (held.isEmpty()) player.setItemInHand(hand, glass);
        else if (!player.getInventory().add(glass)) player.drop(glass, false);
    }

    private static void sound(ServerPlayer player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.GENERIC_DRINK,
                SoundSource.PLAYERS, 0.6F, 1.0F);
    }

    /** Only ordinary JAWR bottles can supply 250 mB to these native mod cauldrons. */
    private static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!ready || !(event.getEntity() instanceof ServerPlayer player)) return;
        if (!isOrdinaryJAWRBottle(player.getItemInHand(event.getHand()))) return;
        ResourceLocation block = BuiltInRegistries.BLOCK.getKey(
                player.level().getBlockState(event.getPos()).getBlock());
        ResourceLocation fluidId;
        if (block.equals(HEX_CAULDRON) && ModList.get().isLoaded("hexerei")) fluidId = HEX_BLOOD;
        else if (block.equals(IRONS_CAULDRON) && ModList.get().isLoaded("irons_spellbooks"))
            fluidId = CANONICAL_BLOOD;
        else return;
        IFluidHandler handler = player.level().getCapability(
                Capabilities.FluidHandler.BLOCK, event.getPos(), null);
        Fluid fluid = BuiltInRegistries.FLUID.getOptional(fluidId).orElse(null);
        if (handler == null || fluid == null) return;
        FluidStack transfer = new FluidStack(fluid, BOTTLE_MB);
        if (handler.fill(transfer, IFluidHandler.FluidAction.SIMULATE) != BOTTLE_MB) return;
        int added = handler.fill(transfer, IFluidHandler.FluidAction.EXECUTE);
        if (added != BOTTLE_MB) {
            TransformationsMod.LOGGER.error("Non-atomic cauldron fluid fill: requested {}, added {}",
                    BOTTLE_MB, added);
            return; // Do not consume bottle for an incomplete transfer.
        }
        giveEmptyBottle(player, event.getHand());
        sound(player);
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    /** Iron's blood vial has no native use action; consume it only for JAWR vampires. */
    private static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!ready || !ModList.get().isLoaded("irons_spellbooks")
                || !(event.getEntity() instanceof ServerPlayer player)) return;
        if (!IRONS_VIAL.equals(BuiltInRegistries.ITEM.getKey(event.getItemStack().getItem()))) return;
        if (!isJAWRVampire(player) || !creditNativeBlood(player)) return;
        giveEmptyBottle(player, event.getHand());
        sound(player);
        player.awardStat(Stats.ITEM_USED.get(event.getItemStack().getItem()));
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    /** Hexerei bottles remain native food/drink items, with native effects intact. */
    private static void onDrinkFinish(LivingEntityUseItemEvent.Finish event) {
        if (!ready || !ModList.get().isLoaded("hexerei")
                || !(event.getEntity() instanceof ServerPlayer player)) return;
        if (!HEX_BOTTLE.equals(BuiltInRegistries.ITEM.getKey(event.getItem().getItem()))) return;
        if (isJAWRVampire(player)) creditNativeBlood(player);
    }
}
