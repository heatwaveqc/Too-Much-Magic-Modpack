package io.github.heatwaveqc.tyr.progression.runtime;

import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.foundation.RegularHomePolicy;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.InteractionResult;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;

/**
 * Trial Regular-stage one-time starter-home placement.
 *
 * Deliberately uses the vanilla structure-template loader for the approved NBT files
 * produced by the housing author. It does not patch Prefab or depend on addon internals.
 * The Native Generic alternative delegates to Prefab's original one-use House item.
 * Everything is authoritative on the server; no player-supplied file paths or NBT.
 */
public final class RegularHomePower {
    private static final String KEY = "tyr_progression:regular_starter_home";
    private static final long MAX_COMPRESSED_BYTES = 8L * 1024L * 1024L;
    private static final long MAX_TEMPLATE_VOLUME = 250_000;
    private static final ResourceLocation NATIVE_ITEM =
        ResourceLocation.fromNamespaceAndPath("prefab", "item_house");

    private RegularHomePower() {}

    public static void register() {
        NeoForge.EVENT_BUS.addListener(RegularHomePower::onCommands);
        PrefabHomeGrant.register();
        NeoForge.EVENT_BUS.addListener(RegularHomePower::onBlueprintUse);
    }

    private static String state(ServerPlayer player) {
        return ProgressionApi.mode(player, KEY, null);
    }

    private static void state(ServerPlayer player, String value) {
        if (!ProgressionApi.setMode(player, KEY, value))
            throw new IllegalStateException("Starter-home receipt rejected: " + value);
    }

    private static boolean regular(ServerPlayer player) {
        return !NewbieMode.isNewbie(player);
    }

    /** Called only by trusted personal graduation, never login migration or Path join. */
    public static void onGraduated(ServerPlayer player) {
        state(player, RegularHomePolicy.graduate(state(player)));
        reconcileLogin(player);
        if (RegularHomePolicy.AVAILABLE.equals(state(player)))
            message(player, "Choose one free home in the Introduction graduation quest.");
    }

    public static void reconcileLogin(ServerPlayer player) {
        String before = state(player);
        String after = RegularHomePolicy.reconcile(before, regular(player), PrefabHomeGrant.nativeGrant(player));
        if (after != null && !after.equals(ProgressionApi.mode(player, KEY, null))) state(player, after);
    }

    private static boolean eligible(ServerPlayer player) {
        reconcileLogin(player);
        return regular(player) && !player.isCreative() && !player.isSpectator()
            && player.getServer() != null && player.getServer().isSameThread()
            && player.getServer().getPlayerList().getPlayer(player.getUUID()) == player
            && PrefabHomeGrant.nativeGrant(player) == RegularHomePolicy.NativeGrant.NOT_GIVEN;
    }

    private static boolean canClaim(ServerPlayer player) {
        return eligible(player) && RegularHomePolicy.canUse(true, state(player));
    }

    private static void message(ServerPlayer player, String text) {
        player.sendSystemMessage(Component.literal("[Starter Home] " + text));
    }

    private static void onCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("tmmhome")
            .requires(source -> source.hasPermission(2))
            .then(Commands.literal("reward")
                .then(Commands.argument("house", StringArgumentType.word())
                    .executes(ctx -> reward(ctx.getSource().getPlayerOrException(),
                        StringArgumentType.getString(ctx, "house")))))
            .then(Commands.literal("status")
                .executes(ctx -> status(ctx.getSource().getPlayerOrException())))
            .then(Commands.literal("list")
                .executes(ctx -> list(ctx.getSource().getPlayerOrException())))
            .then(Commands.literal("inspect")
                .then(Commands.argument("house", StringArgumentType.word())
                    .suggests((ctx, builder) -> {
                        for (String name : RegularHomePolicy.CUSTOM)
                            if (name.startsWith(builder.getRemaining())) builder.suggest(name);
                        return builder.buildFuture();
                    })
                    .executes(ctx -> inspect(ctx.getSource().getPlayerOrException(),
                        StringArgumentType.getString(ctx, "house")))))
            .then(Commands.literal("place")
                .then(Commands.argument("house", StringArgumentType.word())
                    .suggests((ctx, builder) -> {
                        for (String name : RegularHomePolicy.CUSTOM)
                            if (name.startsWith(builder.getRemaining())) builder.suggest(name);
                        return builder.buildFuture();
                    })
                    .executes(ctx -> place(ctx.getSource().getPlayerOrException(),
                        StringArgumentType.getString(ctx, "house")))))
            .then(Commands.literal("generic")
                .executes(ctx -> generic(ctx.getSource().getPlayerOrException())))
            .executes(ctx -> status(ctx.getSource().getPlayerOrException())));
    }

    private static int status(ServerPlayer player) {
        reconcileLogin(player);
        String status = state(player);
        if (!regular(player)) message(player, "Locked until personal Normie graduation.");
        else if (RegularHomePolicy.PENDING.equals(status))
            message(player, "Graduation recorded; waiting for verified native Prefab grant state.");
        else if (RegularHomePolicy.LEGACY_REVIEW.equals(status) || RegularHomePolicy.LOCKED.equals(status))
            message(player, "No verified personal graduation receipt; contact an operator if you never received a free home.");
        else if (RegularHomePolicy.AVAILABLE.equals(status))
            message(player, PrefabHomeGrant.nativeGrant(player)==RegularHomePolicy.NativeGrant.NOT_GIVEN
                ? "One free home available. Choose a home in the graduation quest." : "Native house grant check unavailable; no house issued.");
        else if (status.startsWith(RegularHomePolicy.SELECTED))
            message(player, "Your selected home is " + status.substring(RegularHomePolicy.SELECTED.length()) + ". Use its blueprint.");
        else if (status.startsWith(RegularHomePolicy.RESERVED))
            message(player, "A prior build is reserved for safety (" + status
                + "). Contact an operator before any reset.");
        else message(player, "Your one-time starter-home benefit is already used (" + status + ").");
        return 1;
    }

    private static int list(ServerPlayer player) {
        if (!canClaim(player)) return status(player);
        message(player, "One choice: Generic (/tmmhome generic) or an approved custom home.");
        message(player, "Custom: " + String.join(", ", RegularHomePolicy.CUSTOM));
        message(player, "Look at a safe empty site; /tmmhome inspect <id>, then /tmmhome place <id>.");
        return 1;
    }

    private static Path file(String name) {
        if (!RegularHomePolicy.custom(name)) return null;
        Path parent = FMLPaths.GAMEDIR.get().resolve("prefab-extension")
            .resolve("server-cache").toAbsolutePath().normalize();
        Path candidate = parent.resolve(name + ".nbt").normalize();
        if (!parent.equals(candidate.getParent())) return null;
        return candidate;
    }

    private static StructureTemplate template(ServerPlayer player, String name) throws Exception {
        Path source = file(name);
        if (source == null || !Files.isRegularFile(source, LinkOption.NOFOLLOW_LINKS)
            || Files.size(source) < 1 || Files.size(source) > MAX_COMPRESSED_BYTES)
            throw new IllegalStateException("Approved NBT missing from prefab-extension/server-cache: " + name);
        // The addon's upload/cache directory is not inherently trusted.
        // Read once, then verify immutable approved bytes before parsing.
        byte[] bytes = Files.readAllBytes(source);
        if (!ApprovedHomeAssets.matches(name, bytes))
            throw new IllegalStateException("Starter home does not match approved source digest: " + name);
        CompoundTag nbt = NbtIo.readCompressed(new java.io.ByteArrayInputStream(bytes),
            NbtAccounter.create(64L * 1024L * 1024L));
        StructureTemplate template = new StructureTemplate();
        template.load(player.serverLevel().registryAccess().registryOrThrow(Registries.BLOCK).asLookup(), nbt);
        Vec3i size = template.getSize();
        long volume = (long) size.getX() * size.getY() * size.getZ();
        if (size.getX() < 1 || size.getY() < 1 || size.getZ() < 1
            || size.getX() > 96 || size.getY() > 128 || size.getZ() > 96
            || volume > MAX_TEMPLATE_VOLUME)
            throw new IllegalStateException("Invalid or oversized house structure");
        return template;
    }

    private static BlockPos target(ServerPlayer player) {
        HitResult hit = player.pick(8.0, 0.0f, false);
        if (!(hit instanceof BlockHitResult block) || hit.getType() != HitResult.Type.BLOCK)
            throw new IllegalStateException("Look at the top of a solid block within 8 blocks");
        if (block.getDirection() != Direction.UP)
            throw new IllegalStateException("Aim at the TOP face of the intended base block");
        return block.getBlockPos().above();
    }

    private static boolean claimAllows(ServerPlayer player, BlockPos position) {
        if (!ModList.get().isLoaded("ftbchunks")) return true;
        try {
            Class<?> apiClass = Class.forName("dev.ftb.mods.ftbchunks.api.FTBChunksAPI");
            Object api = apiClass.getMethod("api").invoke(null);
            // Invoke public API interfaces, not implementation classes (whose
            // access may be package-private in an otherwise compatible release).
            Class<?> apiInterface = Class.forName(
                "dev.ftb.mods.ftbchunks.api.FTBChunksAPI$API");
            Object manager = apiInterface.getMethod("getManager").invoke(api);
            Class<?> managerInterface = Class.forName(
                "dev.ftb.mods.ftbchunks.api.ClaimedChunkManager");
            Class<?> protectionClass = Class.forName("dev.ftb.mods.ftbchunks.api.Protection");
            Field edit = protectionClass.getField("EDIT_BLOCK");
            Method check = managerInterface.getMethod("shouldPreventInteraction",
                net.minecraft.world.entity.Entity.class, InteractionHand.class,
                BlockPos.class, protectionClass, net.minecraft.world.entity.Entity.class);
            return !Boolean.TRUE.equals(check.invoke(manager, player,
                InteractionHand.MAIN_HAND, position, edit.get(null), null));
        } catch (ReflectiveOperationException | RuntimeException ex) {
            ProgressionMod.LOGGER.warn("Starter Home: FTB Chunks permission check unavailable (fail closed)", ex);
            return false;
        }
    }

    private static void validateSite(ServerPlayer player, BlockPos origin, Vec3i size) {
        ServerLevel level = player.serverLevel();
        if (!Level.OVERWORLD.equals(level.dimension()))
            throw new IllegalStateException("Trial starter houses can only be placed in the Overworld");
        BlockPos max = origin.offset(size.getX() - 1, size.getY() - 1, size.getZ() - 1);
        if (origin.getY() < level.getMinBuildHeight() || max.getY() >= level.getMaxBuildHeight()
            || !level.getWorldBorder().isWithinBounds(origin) || !level.getWorldBorder().isWithinBounds(max))
            throw new IllegalStateException("House exceeds the build height or world border");
        if (!level.hasChunksAt(origin, max))
            throw new IllegalStateException("Load the entire destination area before placing the house");

        // Never bypass claim protection: validate every touched chunk, including
        // empty/replaceable land; stop if the optional protection API is unavailable.
        for (int cx = Math.floorDiv(origin.getX(),16); cx <= Math.floorDiv(max.getX(),16); cx++)
            for (int cz = Math.floorDiv(origin.getZ(),16); cz <= Math.floorDiv(max.getZ(),16); cz++) {
                BlockPos sample = new BlockPos(Math.max(origin.getX(),cx*16), origin.getY(), Math.max(origin.getZ(),cz*16));
                if (!claimAllows(player, sample))
                    throw new IllegalStateException("The site crosses a protected claim");
            }
        if (!claimAllows(player, max)) throw new IllegalStateException("Protected claim at far edge");

        // Strict test-build policy: no overwriting existing bases, terrain or
        // containers. Foundations must be placed on an unobstructed site.
        for (BlockPos p : BlockPos.betweenClosed(origin, max)) {
            if (!level.mayInteract(player, p) || !player.mayUseItemAt(p, Direction.UP, ItemStack.EMPTY))
                throw new IllegalStateException("Placement permission denied at " + p.toShortString());
            var old = level.getBlockState(p);
            if (level.getBlockEntity(p) != null || (!old.isAir() && !old.canBeReplaced()))
                throw new IllegalStateException("Site is obstructed at " + p.toShortString());
        }
        int x2 = max.getX(), z2 = max.getZ(), y = origin.getY() - 1;
        for (BlockPos p : List.of(
            new BlockPos(origin.getX(), y, origin.getZ()),
            new BlockPos(x2, y, origin.getZ()),
            new BlockPos(origin.getX(), y, z2),
            new BlockPos(x2, y, z2))) {
            if (!level.getBlockState(p).isFaceSturdy(level, p, Direction.UP))
                throw new IllegalStateException("All four corners need a solid supporting platform");
        }
        var occupants = level.getEntitiesOfClass(LivingEntity.class,
            new AABB(origin.getX(), origin.getY(), origin.getZ(),
                max.getX() + 1, max.getY() + 1, max.getZ() + 1), LivingEntity::isAlive);
        if (!occupants.isEmpty()) throw new IllegalStateException("Clear players and mobs from the site");
    }

    private static int inspect(ServerPlayer player, String name) {
        if (!canClaim(player))
            return status(player);
        try {
            StructureTemplate structure = template(player, name);
            BlockPos origin = target(player);
            validateSite(player, origin, structure.getSize());
            message(player, name + " will fit at " + origin.toShortString()
                + " (size " + structure.getSize().toShortString() + ").");
            message(player, "To commit one irreversible placement: /tmmhome place " + name);
            return 1;
        } catch (Exception ex) {
            message(player, "Not ready: " + ex.getMessage());
            return 0;
        }
    }

    private static int place(ServerPlayer player, String name) {
        return place(player, name, false);
    }

    private static int place(ServerPlayer player, String name, boolean blueprint) {
        if (!eligible(player)) return status(player);
        String reserved = blueprint
            ? RegularHomePolicy.reserveSelected(true, state(player), name)
            : RegularHomePolicy.reserve(true, state(player), name);
        if (reserved == null || !RegularHomePolicy.custom(name)) return status(player);
        try {
            StructureTemplate structure = template(player, name);
            BlockPos origin = target(player);
            validateSite(player, origin, structure.getSize());
            state(player, reserved); // reserve before any world mutation
            boolean built;
            try {
                built = structure.placeInWorld(player.serverLevel(), origin, origin,
                    new StructurePlaceSettings().setIgnoreEntities(true),
                    player.serverLevel().random, 3);
            } catch (Throwable ex) {
                // A provider may have placed part of the structure. Fail closed
                // without refund; requires operator inspection before recovery.
                ProgressionMod.LOGGER.error("Starter Home placement interrupted: {} for {}",
                    name, player.getGameProfile().getName(), ex);
                message(player, "Build interrupted; entitlement reserved for safe manual recovery.");
                return 0;
            }
            if (built) state(player, RegularHomePolicy.finish(reserved, name, true));
            message(player, built
                ? "Built " + name + ". Your Normie-stage home placement is now spent."
                : "Placement returned failure after reservation. Contact an operator for safe review.");
            return built ? 1 : 0;
        } catch (Exception ex) {
            message(player, "Cannot build: " + ex.getMessage());
            return 0;
        }
    }

    /** Trusted FTB choice command. Selecting before the separate graduation reward is
     * equivalent to personally claiming graduation; both are on the same quest. */
    private static int reward(ServerPlayer player, String name) {
        if (!RegularHomePolicy.choice(name) || player.isCreative() || player.isSpectator()) return 0;
        if (NewbieMode.isNewbie(player)) NewbieMode.graduate(player);
        if (!canClaim(player)) return status(player);
        if (RegularHomePolicy.GENERIC.equals(name)) return generic(player);
        // Check the approved immutable asset now, but defer terrain checks to placement.
        try {
            template(player, name);
        } catch (Exception ex) {
            message(player, "Selected home is unavailable: " + ex.getMessage());
            return 0;
        }
        String selected = RegularHomePolicy.select(true, state(player), name);
        if (selected == null) return 0;
        ItemStack blueprint = new ItemStack(Items.PAPER);
        CompoundTag tag = new CompoundTag();
        tag.putString("tmm_starter_home", name);
        tag.putUUID("tmm_starter_home_owner", player.getUUID());
        blueprint.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        blueprint.set(DataComponents.CUSTOM_NAME, Component.literal("Starter Home Blueprint: " + name.replace('_', ' ')));
        state(player, selected);
        deliver(player, blueprint);
        message(player, "Right-click the top of a block at your intended building site with your blueprint.");
        return 1;
    }

    private static void deliver(ServerPlayer player, ItemStack item) {
        player.getInventory().add(item);
        if (!item.isEmpty()) player.drop(item, false);
    }

    private static void onBlueprintUse(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ItemStack item = event.getItemStack();
        if (!item.is(Items.PAPER)) return;
        CustomData data = item.get(DataComponents.CUSTOM_DATA);
        if (data == null) return;
        CompoundTag tag = data.copyTag();
        if (!tag.contains("tmm_starter_home")) return;
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
        String name = tag.getString("tmm_starter_home");
        if (!tag.hasUUID("tmm_starter_home_owner")
            || !player.getUUID().equals(tag.getUUID("tmm_starter_home_owner"))
            || !RegularHomePolicy.custom(name)) {
            message(player, "This blueprint is not yours or is invalid.");
            return;
        }
        if (event.getFace() != Direction.UP) {
            message(player, "Right-click the TOP face of the intended base block.");
            return;
        }
        if (place(player, name, true) == 1) item.shrink(1);
    }

    private static int generic(ServerPlayer player) {
        if (!canClaim(player)) return status(player);
        String reserved = RegularHomePolicy.reserve(true, state(player),
            RegularHomePolicy.GENERIC);
        if (reserved == null) return status(player);
        if (!ModList.get().isLoaded("prefab") || !BuiltInRegistries.ITEM.containsKey(NATIVE_ITEM)) {
            message(player, "Prefab's Starter House item isn't installed.");
            return 0;
        }
        ItemStack item = new ItemStack(BuiltInRegistries.ITEM.get(NATIVE_ITEM));
        // No dropped/tradable bonus if the inventory is full. Reserve first to
        // prevent repeated command issues in one tick.
        state(player, reserved);
        state(player, RegularHomePolicy.SPENT + RegularHomePolicy.GENERIC);
        deliver(player, item);
        message(player, "You chose Generic. Prefab's native one-use Starter House blueprint"
            + " is in your inventory. The graduation entitlement is now spent.");
        return 1;
    }
}
