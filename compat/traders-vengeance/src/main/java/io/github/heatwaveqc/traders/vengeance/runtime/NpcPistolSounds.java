package io.github.heatwaveqc.traders.vengeance.runtime;

import io.github.heatwaveqc.traders.vengeance.TradersVengeance;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Only local NPC audio, using the imported Caliber Arms CC0 recordings. */
public final class NpcPistolSounds {
    private static final DeferredRegister<SoundEvent> SOUNDS =
        DeferredRegister.create(Registries.SOUND_EVENT, TradersVengeance.MOD_ID);
    public static final Supplier<SoundEvent> FIRE = sound("gun.m1911.fire");
    public static final Supplier<SoundEvent> MAG_OUT = sound("reload.pistol.mag_out");
    public static final Supplier<SoundEvent> MAG_IN = sound("reload.pistol.mag_in");
    public static final Supplier<SoundEvent> SLIDE = sound("reload.pistol.slide");
    public static final Supplier<SoundEvent> MINIGUN_LOOP = sound("minigun.loop");
    public static final Supplier<SoundEvent> MINIGUN_STOP = sound("minigun.stop");
    public static final Supplier<SoundEvent> MINIGUN_SPINUP = sound("minigun.spinup");
    private NpcPistolSounds() {}
    private static Supplier<SoundEvent> sound(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(
            ResourceLocation.fromNamespaceAndPath(TradersVengeance.MOD_ID, name)));
    }
    public static void register(IEventBus bus) { SOUNDS.register(bus); }
}
