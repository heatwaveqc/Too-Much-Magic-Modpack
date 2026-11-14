package io.github.heatwaveqc.tmm.monk.faction;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import io.github.heatwaveqc.tmm.monk.MonkMod;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;

public final class MonkFaction {
    public static final int PROVISIONAL_MAX_LEVEL = 14;
    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(MonkMod.MOD_ID, "monk");
    public static IPlayableFaction<IMonkPlayer> FACTION;

    private MonkFaction() { }

    @SuppressWarnings("unchecked")
    public static synchronized void registerOnce() {
        if (FACTION != null) return;
        FACTION = VampirismAPI.factionRegistry()
                .createPlayableFaction(ID, IMonkPlayer.class, () -> (net.neoforged.neoforge.attachment.AttachmentType<IMonkPlayer>)(net.neoforged.neoforge.attachment.AttachmentType<?>) MonkPlayerData.MONK_PLAYER.get())
                .color(0xE1C477)
                .highestLevel(PROVISIONAL_MAX_LEVEL)
                .chatColor(ChatFormatting.YELLOW)
                .name("faction.tmm_monk.monk")
                .namePlural("faction.tmm_monk.monks")
                .register();
        MonkMod.LOGGER.info("Registered Monk faction (structural-only; ranks and eligibility unimplemented)");
    }
}
