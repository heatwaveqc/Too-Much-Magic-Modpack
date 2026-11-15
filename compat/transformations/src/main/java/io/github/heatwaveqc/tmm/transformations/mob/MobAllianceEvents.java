package io.github.heatwaveqc.tmm.transformations.mob;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.vampire.VampireDiagnostics;
import java.lang.reflect.Method;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

/**
 * Cross-mod mob neutrality, without altering damage, drops or native AI goals.
 * Applies to the vanilla undead entity tag and explicit cross-mod tags.
 * The two dragon relationships are tag-driven and become operative when
 * actual dragon entity types are assigned by a future mod integration.
 */
public final class MobAllianceEvents {
    private static final String PROVOKED_BY = "tmm.alliances.provoked_by";
    private static final String PROVOKED_UNTIL = "tmm.alliances.provoked_until";
    private static final long RETALIATION_TICKS = 600L;

    private static final TagKey<EntityType<?>> EXTRA_UNDEAD = tag("undead_extra");
    private static final TagKey<EntityType<?>> FAE = tag("fae");
    private static final TagKey<EntityType<?>> DEMONS = tag("demons");
    private static final TagKey<EntityType<?>> FOREST_DRAGONS = tag("forest_dragons");
    private static final TagKey<EntityType<?>> CAVE_DRAGONS = tag("cave_dragons");
    private static final TagKey<EntityType<?>> BOSS_EXEMPT = tag("boss_exempt");

    private MobAllianceEvents() {}

    private static TagKey<EntityType<?>> tag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(TransformationsMod.MOD_ID, name));
    }

    public static void register() {
        NeoForge.EVENT_BUS.addListener(MobAllianceEvents::onTarget);
        NeoForge.EVENT_BUS.addListener(MobAllianceEvents::onIncomingDamage);
        NeoForge.EVENT_BUS.addListener(MobAllianceEvents::onMobTick);
        NeoForge.EVENT_BUS.addListener(MobAllianceEvents::onServerStarted);
    }

    private static boolean undead(Mob mob) {
        return mob.getType().is(EntityTypeTags.UNDEAD)
                || mob.getType().is(EXTRA_UNDEAD);
    }

    private static boolean fae(LivingEntity entity) {
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        return entity.getType().is(FAE)
                || ("feywild".equals(id.getNamespace()) && !entity.getType().is(BOSS_EXEMPT));
    }

    private static boolean lich(ServerPlayer player) {
        if (!ModList.get().isLoaded("goety")) return false;
        try {
            Class<?> type = Class.forName("za.co.infernos.goety.utils.LichdomHelper");
            Method reader = type.getMethod("isLich", net.minecraft.world.entity.player.Player.class);
            return Boolean.TRUE.equals(reader.invoke(null, player));
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return false; // optional provider; never grant neutrality on an unknown status
        }
    }

    private static boolean vampireOrLich(ServerPlayer player) {
        return VampireDiagnostics.vampirism(player).map(v -> v.level() > 0).orElse(false)
                || VampireDiagnostics.jawr(player).map(v -> v.level() > 0).orElse(false)
                || lich(player);
    }

    private static boolean provoked(Mob mob, LivingEntity target) {
        CompoundTag data = mob.getPersistentData();
        return data.hasUUID(PROVOKED_BY)
                && data.getUUID(PROVOKED_BY).equals(target.getUUID())
                && mob.level().getGameTime() <= data.getLong(PROVOKED_UNTIL);
    }

    /**
     * Damage by the potential victim (including arrows) permits the mob
     * to defend itself for 30 seconds. Natural hostility remains blocked.
     */
    private static void onIncomingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Mob mob)) return;
        if (!(event.getSource().getEntity() instanceof LivingEntity source)) return;
        CompoundTag data = mob.getPersistentData();
        data.putUUID(PROVOKED_BY, source.getUUID());
        data.putLong(PROVOKED_UNTIL, mob.level().getGameTime() + RETALIATION_TICKS);
    }

    private static boolean commandedOrBoss(Mob mob) {
        if (mob.getType().is(BOSS_EXEMPT)) return true;
        // Preserve summoned servants' explicit owner/defense behavior.
        String id = BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType()).getPath();
        return id.endsWith("_servant") || id.endsWith("_minion");
    }

    private static boolean shouldDropTarget(Mob mob, LivingEntity target) {
        if (mob.level().isClientSide()) return false;

        boolean protectedPlayer = undead(mob) && target instanceof ServerPlayer player
                && vampireOrLich(player);
        boolean forestAffinity =
                (mob.getType().is(FOREST_DRAGONS) && fae(target))
                || (fae(mob) && target.getType().is(FOREST_DRAGONS));
        boolean caveAffinity =
                (mob.getType().is(CAVE_DRAGONS) && target.getType().is(DEMONS))
                || (mob.getType().is(DEMONS) && target.getType().is(CAVE_DRAGONS));

        return MobAlliancePolicy.ignoresTarget(undead(mob), protectedPlayer,
                forestAffinity, caveAffinity, provoked(mob, target), commandedOrBoss(mob));
    }

    private static void onTarget(LivingChangeTargetEvent event) {
        if (!(event.getEntity() instanceof Mob mob)) return;
        LivingEntity proposed = event.getNewAboutToBeSetTarget();
        if (proposed != null && shouldDropTarget(mob, proposed)) {
            event.setNewAboutToBeSetTarget(null);
        }
    }

    /** Clear old targets selected before a player became a vampire or lich. */
    private static void onMobTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof Mob mob) || mob.tickCount % 10 != 0) return;
        LivingEntity target = mob.getTarget();
        if (target != null && shouldDropTarget(mob, target)) mob.setTarget(null);
    }

    /** Complete non-vanilla living-mob census, emitted for each test server. */
    private static void onServerStarted(ServerStartedEvent event) {
        int total = 0, classified = 0;
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
            if ("minecraft".equals(id.getNamespace()) || type.getCategory() == MobCategory.MISC) continue;
            total++;
            String group = type.is(EntityTypeTags.UNDEAD) || type.is(EXTRA_UNDEAD) ? "undead"
                    : type.is(FAE) || "feywild".equals(id.getNamespace()) ? "fae"
                    : type.is(DEMONS) ? "demon"
                    : type.is(FOREST_DRAGONS) ? "forest_dragon"
                    : type.is(CAVE_DRAGONS) ? "cave_dragon"
                    : "unclassified";
            if (!"unclassified".equals(group)) classified++;
            TransformationsMod.LOGGER.info("TMM_MOB_CENSUS id={} category={} alliance={}",
                    id, type.getCategory(), group);
        }
        TransformationsMod.LOGGER.info(
                "TMM_MOB_CENSUS_SUMMARY modded_nonmisc={} classified={} unclassified={}",
                total, classified, total - classified);
    }
}
