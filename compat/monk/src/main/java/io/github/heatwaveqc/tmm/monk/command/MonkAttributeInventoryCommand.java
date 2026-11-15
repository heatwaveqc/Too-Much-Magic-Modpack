package io.github.heatwaveqc.tmm.monk.command;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

/**
 * Operator-only, read-only inventory export for the actual pinned modpack.
 * It does NOT grant skills, change attributes or assert source registry
 * availability in the current world.
 */
public final class MonkAttributeInventoryCommand {
    private MonkAttributeInventoryCommand() { }

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("tmmmonk")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("attributes")
                        .executes(context -> export(context.getSource()))));
    }

    private static int export(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        JsonObject snapshot = new JsonObject();
        snapshot.addProperty("format", "tmm_monk:player_attribute_inventory");
        snapshot.addProperty("schema_version", 1);
        snapshot.addProperty("world_specific", true);
        snapshot.addProperty("origin", "runtime_player_attribute_instances");
        snapshot.addProperty("note", "This file is an attribute snapshot, not a Monk skill unlock list.");
        JsonArray attributes = new JsonArray();
        var entries = BuiltInRegistries.ATTRIBUTE.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().location().toString()))
                .toList();
        int registered = entries.size();
        for (var entry : entries) {
            var holder = BuiltInRegistries.ATTRIBUTE.getHolderOrThrow(entry.getKey());
            AttributeInstance instance = player.getAttributes().getInstance(holder);
            if (instance == null) continue;
            Attribute definition = entry.getValue();
            JsonObject row = new JsonObject();
            row.addProperty("id", entry.getKey().location().toString());
            row.addProperty("description", definition.getDescriptionId());
            row.addProperty("base_value", instance.getBaseValue());
            row.addProperty("effective_value", instance.getValue());
            row.addProperty("default_value", definition.getDefaultValue());
            row.addProperty("value_type", definition.getClass().getName());
            if (definition instanceof RangedAttribute range) {
                row.addProperty("min", range.getMinValue());
                row.addProperty("max", range.getMaxValue());
            }
            attributes.add(row);
        }
        snapshot.addProperty("all_registered_attribute_definitions", registered);
        snapshot.addProperty("player_attached_attribute_count", attributes.size());
        snapshot.add("attributes", attributes);
        Path path = source.getServer().getWorldPath(LevelResource.ROOT)
                .resolve("tmm-monk-player-attributes.json");
        try {
            Files.writeString(path, new GsonBuilder().setPrettyPrinting().create().toJson(snapshot),
                    StandardCharsets.UTF_8);
        } catch (IOException exception) {
            source.sendFailure(Component.literal("Could not write Monk attribute inventory: "
                    + exception.getMessage()));
            return 0;
        }
        source.sendSuccess(() -> Component.literal("Exported " + attributes.size()
                + " player attributes out of " + registered + " registered definitions to "
                + path.getFileName()), true);
        return attributes.size();
    }
}
