package dev.heatwaveqc.tmm.hexpilot;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironmentComponent;
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * EXPERIMENTAL and NOT INSTALLED by packwiz.
 * A separate NeoForge bridge, not a KubeJS integration or production Pufferfish skill.
 */
@Mod(HexCastMediaPilot.MOD_ID)
public final class HexCastMediaPilot {
    public static final String MOD_ID = "tmm_hexcast_media_pilot";
    public static final String ENABLE_TAG = "tmm_hex_media_test";
    public static final String TIER_1_TAG = "tmm_hex_media_t1";
    public static final String TIER_2_TAG = "tmm_hex_media_t2";

    public HexCastMediaPilot(IEventBus modBus) {
        modBus.addListener((FMLCommonSetupEvent event) ->
            event.enqueueWork(() -> CastingEnvironment.addCreateEventListener(HexCastMediaPilot::onEnvironmentCreated)));
    }

    private static void onEnvironmentCreated(CastingEnvironment environment) {
        // A circle can report a caster; it still must NOT inherit a player's perks.
        if (!(environment instanceof PlayerBasedCastEnv)) {
            return;
        }
        ServerPlayer caster = environment.getCaster();
        if (caster == null || !caster.getTags().contains(ENABLE_TAG)) {
            return;
        }
        int discount = caster.getTags().contains(TIER_2_TAG) ? 20 :
            caster.getTags().contains(TIER_1_TAG) ? 10 : 0;
        if (discount > 0) {
            environment.addExtension(new MediaDiscount(discount));
        }
    }

    private record MediaDiscount(int percent) implements CastingEnvironmentComponent.ExtractMedia {
        private static final CastingEnvironmentComponent.Key<MediaDiscount> KEY = new CastingEnvironmentComponent.Key<>() {};

        @Override
        public CastingEnvironmentComponent.Key<MediaDiscount> getKey() {
            return KEY;
        }

        @Override
        public long onExtractMedia(long cost) {
            return MediaDiscountMath.remainingCost(cost, percent);
        }
    }
}
