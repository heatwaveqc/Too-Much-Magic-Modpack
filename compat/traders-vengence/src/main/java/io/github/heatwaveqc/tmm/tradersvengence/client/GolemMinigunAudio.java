package io.github.heatwaveqc.tmm.tradersvengence.client;

import io.github.heatwaveqc.tmm.tradersvengence.network.GolemVisualState;
import io.github.heatwaveqc.tmm.tradersvengence.runtime.NpcPistolSounds;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.animal.IronGolem;

/** Follow the vanilla entity, driven only by server-authoritative firing snapshots.
 * The original donor loop/stop audio is reproduced without its external mod or HUD clip.
 */
public final class GolemMinigunAudio {
    private static final float VOLUME = 1F; // upstream default for drone-mounted weapons
    private static final Map<UUID, Loop> LOOPS = new HashMap<>();
    private GolemMinigunAudio() {}

    public static void tick(IronGolem golem) {
        var sample = GolemVisualState.get(golem.getUUID());
        boolean firing = golem.isAlive() && sample != null && sample.freshFiring();
        Loop current = LOOPS.get(golem.getUUID());
        if (firing) {
            if (current == null || current.isStopped()) {
                var loop = new Loop(golem);
                LOOPS.put(golem.getUUID(), loop);
                Minecraft.getInstance().getSoundManager().play(loop);
            }
        } else if (current != null) {
            remove(golem.getUUID());
            if (golem.isAlive() && !golem.isRemoved()) {
                golem.level().playLocalSound(golem.getX(), golem.getY(), golem.getZ(),
                    NpcPistolSounds.MINIGUN_STOP.get(), SoundSource.NEUTRAL, VOLUME, 1F, false);
            }
        }
    }

    public static void remove(UUID id) {
        Loop loop = LOOPS.remove(id);
        if (loop != null) loop.end();
    }

    public static void clear() {
        for (Loop loop : LOOPS.values()) loop.end();
        LOOPS.clear();
    }

    private static final class Loop extends AbstractTickableSoundInstance {
        private final IronGolem golem;
        Loop(IronGolem golem) {
            super(NpcPistolSounds.MINIGUN_LOOP.get(), SoundSource.NEUTRAL,
                SoundInstance.createUnseededRandom());
            this.golem = golem;
            this.looping = true;
            this.delay = 0;
            this.volume = VOLUME;
            this.pitch = 1F;
            this.attenuation = SoundInstance.Attenuation.LINEAR;
            updatePosition();
        }
        private void updatePosition() {
            this.x = golem.getX();
            this.y = golem.getY();
            this.z = golem.getZ();
        }
        @Override public void tick() {
            if (golem.isRemoved() || !golem.isAlive()) { stop(); return; }
            updatePosition();
        }
        void end() { stop(); }
    }
}
