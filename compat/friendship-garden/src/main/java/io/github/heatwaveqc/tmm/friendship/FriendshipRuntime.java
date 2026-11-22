package io.github.heatwaveqc.tmm.friendship;

import com.mojang.brigadier.Command;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/**
 * Event-driven, loaded-chunk-only garden state. No global tick per flower,
 * no chunk loads and no queued remote potion delivery to offline players.
 */
public final class FriendshipRuntime {
    private static final String OPT_IN = "tmm_friendship_garden_harmful_opt_in";
    private static final Map<UUID, Set<FriendshipBlockEntity>> BY_CRAFTER = new HashMap<>();
    private static final Set<FriendshipBlockEntity> MOURNING = java.util.Collections.newSetFromMap(new IdentityHashMap<>());
    private static final Map<UUID, Long> IMPACTS_THIS_TICK = new HashMap<>();

    public static void track(FriendshipBlockEntity entity) {
        if (entity.flower() == null) return;
        BY_CRAFTER.computeIfAbsent(entity.flower().crafterId(), k -> new HashSet<>()).add(entity);
        if (entity.flower().mourning(currentTick(entity))) MOURNING.add(entity);
    }

    public static void untrack(FriendshipBlockEntity entity) {
        if (entity.flower() == null) return;
        Set<FriendshipBlockEntity> owned = BY_CRAFTER.get(entity.flower().crafterId());
        if (owned != null) {
            owned.remove(entity);
            if (owned.isEmpty()) BY_CRAFTER.remove(entity.flower().crafterId());
        }
        MOURNING.remove(entity);
    }

    private static long currentTick(FriendshipBlockEntity entity) {
        return entity.getLevel() instanceof ServerLevel level
                ? level.getServer().overworld().getGameTime() : 0;
    }

    private static void appearance(UUID id, boolean online) {
        Set<FriendshipBlockEntity> owned = BY_CRAFTER.get(id);
        if (owned == null) return;
        for (FriendshipBlockEntity entity : List.copyOf(owned)) {
            if (entity.getLevel() instanceof ServerLevel level && !entity.isRemoved()) {
                if (online) entity.updateFromServer(level, false);
                else entity.onCrafterLogout(level);
            }
        }
    }

    @SubscribeEvent
    public void login(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) appearance(player.getUUID(), true);
    }

    @SubscribeEvent
    public void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) appearance(player.getUUID(), false);
    }

    @SubscribeEvent
    public void death(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        Set<FriendshipBlockEntity> owned = BY_CRAFTER.get(player.getUUID());
        if (owned == null) return;
        MinecraftServer server = player.getServer();
        if (server == null) return;
        long now = server.overworld().getGameTime();
        int petalBudget = 48; // at most 48 loaded flower particle bursts per death
        for (FriendshipBlockEntity entity : List.copyOf(owned)) {
            if (entity.getLevel() instanceof ServerLevel level && !entity.isRemoved()) {
                entity.onCrafterDeath(level, now, petalBudget-- > 0, false);
                MOURNING.add(entity);
            }
        }
    }

    @SubscribeEvent
    public void serverTick(ServerTickEvent.Post event) {
        // Only flowering events in the small mourning set need timed updates.
        long now = event.getServer().overworld().getGameTime();
        if (now % 20 != 0) return;
        IMPACTS_THIS_TICK.clear();
        for (FriendshipBlockEntity entity : List.copyOf(MOURNING)) {
            if (entity.isRemoved() || entity.flower() == null) { MOURNING.remove(entity); continue; }
            if (!entity.flower().mourning(now)) {
                MOURNING.remove(entity);
                if (entity.getLevel() instanceof ServerLevel level) entity.updateFromServer(level, false);
            }
        }
    }

    @SubscribeEvent
    public void serverStopped(ServerStoppedEvent event) {
        BY_CRAFTER.clear();
        MOURNING.clear();
        IMPACTS_THIS_TICK.clear();
    }

    @SubscribeEvent
    public void commands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("friendshipgarden")
                .then(Commands.literal("harmful")
                    .then(Commands.literal("enable").executes(context -> toggle(context.getSource().getPlayerOrException(), true)))
                    .then(Commands.literal("disable").executes(context -> toggle(context.getSource().getPlayerOrException(), false)))));
    }

    private static int toggle(ServerPlayer player, boolean enabled) {
        player.getPersistentData().putBoolean(OPT_IN, enabled);
        player.sendSystemMessage(Component.translatable(enabled
                ? "message.tmm_friendship_garden.harmful_on"
                : "message.tmm_friendship_garden.harmful_off"));
        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public void impact(ProjectileImpactEvent event) {
        if (!(event.getProjectile() instanceof ThrownPotion projectile)) return;
        if (!(projectile.level() instanceof ServerLevel level)) return;
        ItemStack item = projectile.getItem();
        if (!item.is(Items.SPLASH_POTION) || !FriendshipConfig.TRANSMIT.get()) return;
        long now = level.getServer().overworld().getGameTime();
        Long previous = IMPACTS_THIS_TICK.putIfAbsent(projectile.getUUID(), now);
        if (previous != null && previous == now) return;

        PotionContents contents = item.get(DataComponents.POTION_CONTENTS);
        if (contents == null) return;
        String potionId = contents.potion().map(holder ->
                BuiltInRegistries.POTION.getKey(holder.value()).toString()).orElse("minecraft:custom");
        List<SplashTransmission.Effect> candidates = new ArrayList<>();
        Map<String, MobEffectInstance> actual = new HashMap<>();
        Set<String> allowedModded = FriendshipConfig.ids(FriendshipConfig.ALLOWED_MODDED_EFFECTS.get());
        for (MobEffectInstance effect : contents.getAllEffects()) {
            ResourceLocation id = BuiltInRegistries.MOB_EFFECT.getKey(effect.getEffect().value());
            if (id == null) continue;
            String key = id.toString();
            var category = effect.getEffect().value().getCategory();
            SplashTransmission.Classification type =
                    category == MobEffectCategory.BENEFICIAL ? SplashTransmission.Classification.BENEFICIAL
                    : category == MobEffectCategory.HARMFUL ? SplashTransmission.Classification.HARMFUL
                    : SplashTransmission.Classification.UNKNOWN;
            boolean standard = id.getNamespace().equals("minecraft") || allowedModded.contains(key);
            candidates.add(new SplashTransmission.Effect(key, effect.getAmplifier(), effect.getDuration(),
                    effect.getEffect().value().isInstantenous(), type, standard));
            actual.putIfAbsent(key, effect);
        }
        if (actual.isEmpty()) return;
        // Vanilla splash radius 4. Scan only already-loaded flower block entities
        // (not projectile-solid collisions, not other dimensions' chunks).
        Vec3 hit = event.getRayTraceResult().getLocation();
        BlockPos min = BlockPos.containing(hit.x - 4, hit.y - 4, hit.z - 4);
        BlockPos max = BlockPos.containing(hit.x + 4, hit.y + 4, hit.z + 4);
        List<FriendshipFlower> touched = new ArrayList<>();
        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            if (!level.hasChunkAt(pos)) continue;
            if (!(level.getBlockEntity(pos) instanceof FriendshipBlockEntity be) || be.flower() == null) continue;
            double d = be.flower().form() == FriendshipFlower.Form.TALL
                ? Math.min(pos.distToCenterSqr(hit.x, hit.y, hit.z),
                      pos.above().distToCenterSqr(hit.x, hit.y, hit.z))
                : pos.distToCenterSqr(hit.x, hit.y, hit.z);
            if (d < 16.0) touched.add(be.flower());
        }
        if (touched.isEmpty()) return;
        Map<UUID, SplashTransmission.Recipient> online = new HashMap<>();
        MinecraftServer server = level.getServer();
        for (FriendshipFlower flower : touched) {
            ServerPlayer player = server.getPlayerList().getPlayer(flower.crafterId());
            if (player != null) {
                online.put(player.getUUID(), new SplashTransmission.Recipient(
                        player.getUUID(), player.level().dimension().location().toString(),
                        player.isAlive(), player.getPersistentData().getBoolean(OPT_IN)));
            }
        }
        var deliveries = SplashTransmission.resolve(potionId, level.dimension().location().toString(), now,
                candidates, touched, online, FriendshipConfig.policy(), server.isPvpAllowed());
        for (var delivery : deliveries) {
            ServerPlayer player = server.getPlayerList().getPlayer(delivery.playerId());
            MobEffectInstance effect = actual.get(delivery.effect().effectId());
            if (player == null || !player.isAlive() || effect == null) continue;
            if (effect.getEffect().value().isInstantenous()) {
                effect.getEffect().value().applyInstantenousEffect(projectile,
                        projectile.getOwner(), player, effect.getAmplifier(), 1.0D);
            } else {
                player.addEffect(new MobEffectInstance(effect), projectile);
            }
        }
        if (!deliveries.isEmpty()) level.sendParticles(ParticleTypes.ENCHANT, hit.x, hit.y, hit.z,
                8, .25, .25, .25, .02);
    }
}
