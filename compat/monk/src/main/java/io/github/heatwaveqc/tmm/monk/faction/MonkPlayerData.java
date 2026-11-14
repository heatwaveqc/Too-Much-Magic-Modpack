package io.github.heatwaveqc.tmm.monk.faction;

import io.github.heatwaveqc.tmm.monk.MonkMod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class MonkPlayerData {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MonkMod.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<MonkPlayer>> MONK_PLAYER =
            ATTACHMENTS.register("monk_player", () -> AttachmentType.builder(new MonkPlayer.Factory())
                    .serialize(new MonkPlayer.Serializer()).copyOnDeath().build());

    private MonkPlayerData() { }
}
