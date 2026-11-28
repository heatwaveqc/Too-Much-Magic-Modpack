package io.github.heatwaveqc.tmm.economy.audit;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.SpawnEggItem;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.emc.EMCMappingHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * Admin-only, on-demand Type-B data export for #47.
 *
 * No tick handler, auto-write, registry mutation, transmutation or EMC
 * overrides. The two audit inputs always come from the same synchronized
 * command invocation, *after* ProjectE mapped the current world.
 */
public final class EmcCoverageCommands {
    private EmcCoverageCommands() {}

    public static void registerIfProjectEInstalled() {
        NeoForge.EVENT_BUS.addListener(EmcCoverageCommands::onCommands);
    }

    private static void onCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("tmm_economy")
            .requires(source -> source.hasPermission(3))
            .then(Commands.literal("audit_emc")
                .then(Commands.argument("source_sha", StringArgumentType.word())
                    .executes(ctx -> export(ctx.getSource(),
                        StringArgumentType.getString(ctx, "source_sha"))))));
    }

    private static int export(CommandSourceStack source, String sha) {
        if (!sha.matches("[0-9a-fA-F]{40}")) {
            source.sendFailure(Component.literal(
                "Supply the exact 40-character integration commit SHA; this label is operator supplied."));
            return 0;
        }
        if (EMCMappingHandler.getLoadIndex() < 0 || EMCMappingHandler.getEmcMapSize() == 0) {
            source.sendFailure(Component.literal(
                "ProjectE's EMC mapping is not ready. Run this after the world and mapper load."));
            return 0;
        }

        // Read-only, bounded on-demand census. A malformed provider value
        // should report failure to the operator, never interrupt server ticks.
        EmcCoverageSnapshot.Payload payload;
        JsonObject shopItems = new JsonObject();
        try {
            SortedSet<String> registered = new TreeSet<>();
            Map<String, Long> mapped = new HashMap<>();
            for (ResourceLocation id : BuiltInRegistries.ITEM.keySet()) {
                String key = id.toString();
                registered.add(key);
                Item item = BuiltInRegistries.ITEM.get(id);
                // Default-stack facts and actual resolved tags from this exact invocation.
                var stack = item.getDefaultInstance();
                JsonObject facts = new JsonObject();
                JsonArray tags = new JsonArray();
                item.builtInRegistryHolder().tags().map(tag -> tag.location().toString())
                    .sorted().forEach(tags::add);
                facts.add("tags", tags);
                facts.addProperty("max_stack", stack.getMaxStackSize());
                facts.addProperty("food", stack.has(DataComponents.FOOD));
                facts.addProperty("spawn_egg", item instanceof SpawnEggItem);
                shopItems.add(key, facts);
                ItemInfo info = ItemInfo.fromItem(item);
                if (EMCMappingHandler.hasEmcValue(info)) {
                    mapped.put(key, EMCMappingHandler.getStoredEmcValue(info));
                }
            }
            payload = EmcCoverageSnapshot.encode(registered, mapped);
        } catch (RuntimeException ex) {
            source.sendFailure(Component.literal(
                "EMC census failed without modifying world data: " + ex.getMessage()));
            return 0;
        }
        MinecraftServer server = source.getServer();
        Path base = server.getWorldPath(LevelResource.ROOT)
            .resolve("tmm-economy").resolve("emc-audits");
        Path pending = null;
        try {
            Files.createDirectories(base);
            pending = Files.createTempDirectory(base, "incomplete-");
            write(pending.resolve("items.json"), payload.itemsJson());
            write(pending.resolve("emc.json"), payload.emcJson());
            write(pending.resolve("status.json"), payload.statusJson());
            JsonObject shopFacts = new JsonObject();
            shopFacts.addProperty("schema_version", 1);
            shopFacts.addProperty("source_sha_label", sha.toLowerCase(java.util.Locale.ROOT));
            shopFacts.addProperty("projecte_map_generation", EMCMappingHandler.getLoadIndex());
            shopFacts.add("items", shopItems);
            String factsJson = new GsonBuilder().setPrettyPrinting().create().toJson(shopFacts) + "\n";
            write(pending.resolve("shop-items.json"), factsJson);

            JsonObject meta = new JsonObject();
            meta.addProperty("schema_version", 1);
            meta.addProperty("source_sha_label", sha.toLowerCase(java.util.Locale.ROOT));
            meta.addProperty("source_sha_verified", false);
            meta.addProperty("source_sha_note",
                "SHA supplied by command operator; this snapshot does not attest the running binary identity");
            meta.addProperty("captured_utc", Instant.now().toString());
            meta.addProperty("projecte_map_generation", EMCMappingHandler.getLoadIndex());
            meta.addProperty("projecte_mapped_entries", EMCMappingHandler.getEmcMapSize());
            meta.addProperty("shop_items_sha256", java.util.HexFormat.of().formatHex(
                java.security.MessageDigest.getInstance("SHA-256")
                    .digest(factsJson.getBytes(StandardCharsets.UTF_8))));
            meta.addProperty("registry_items", payload.total());
            meta.addProperty("mapped_positive", payload.positive());
            meta.addProperty("mapped_zero", payload.mappedZero());
            meta.addProperty("unmapped", payload.unmapped());
            write(pending.resolve("metadata.json"),
                new GsonBuilder().setPrettyPrinting().create().toJson(meta) + "\n");

            String directory = "source-" + sha.substring(0, 12).toLowerCase(java.util.Locale.ROOT)
                + "-" + System.currentTimeMillis();
            Path finalPath = base.resolve(directory);
            try {
                Files.move(pending, finalPath, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(pending, finalPath);
            }
            source.sendSuccess(() -> Component.literal(
                "EMC audit exported to world/tmm-economy/emc-audits/" + directory
                    + ": " + payload.total() + " registry items; " + payload.positive()
                    + " mapped positive, " + payload.mappedZero() + " mapped zero, "
                    + payload.unmapped() + " unmapped. Run the offline #47 audit on items.json + emc.json."),
                false);
            return 1;
        } catch (IOException | java.security.NoSuchAlgorithmException | RuntimeException ex) {
            source.sendFailure(Component.literal(
                "EMC audit export failed safely; no EMC/items changed: " + ex.getMessage()));
            // A failed snapshot remains under incomplete-* for inspection;
            // it cannot be mistaken for a successfully completed export.
            return 0;
        }
    }

    private static void write(Path file, String content) throws IOException {
        Files.writeString(file, content, StandardCharsets.UTF_8);
    }
}
