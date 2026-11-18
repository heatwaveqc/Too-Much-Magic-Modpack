package io.github.heatwaveqc.tmm.tradersvengence.network;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Common-safe client visual cache. Never used to authorize gameplay. */
public final class GolemVisualState {
    private static final Map<UUID, Sample> SAMPLES = new HashMap<>();
    private GolemVisualState() {}
    public record Sample(float angle, float speed, long received) {
        public float predictedAngle() {
            // At most ten ticks of extrapolation between heartbeat packets.
            float ticks = Math.max(0F, Math.min(10F, (System.nanoTime() - received) / 50_000_000F));
            return angle + ticks * speed;
        }
    }
    public static void apply(GolemVisualPayload payload) {
        if (!Float.isFinite(payload.angle()) || !Float.isFinite(payload.speed())) return;
        long now = System.nanoTime();
        SAMPLES.entrySet().removeIf(e -> now - e.getValue().received() > 60_000_000_000L);
        SAMPLES.put(payload.id(), new Sample(payload.angle(), Math.max(0F, Math.min(0.4F, payload.speed())), now));
    }
    public static Sample get(UUID id) { return SAMPLES.get(id); }
    public static void remove(UUID id) { SAMPLES.remove(id); }
    public static void clear() { SAMPLES.clear(); }
}
