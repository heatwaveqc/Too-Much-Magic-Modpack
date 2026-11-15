package io.github.heatwaveqc.tmm.transformations.abomination;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.adapters.JAWRLycanthropeProbe;
import io.github.heatwaveqc.tmm.transformations.adapters.JAWRVampireProbe;
import io.github.heatwaveqc.tmm.transformations.adapters.VampirismFactionProbe;
import io.github.heatwaveqc.tmm.transformations.core.AbominationPilotPolicy;
import io.github.heatwaveqc.tmm.transformations.core.Observation;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import io.github.heatwaveqc.tmm.transformations.vampire.VampireChassisConfig;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/**
 * EXPERIMENTAL: delegates two specific upstream action IMPLEMENTATIONS, using
 * each action's native player attachment, under operator-only test commands.
 *
 * This does NOT register the planned Abomination faction, grant native skills,
 * activate the client HUD, or claim native cooldown/permission event parity.
 * It deliberately bypasses native skill entitlement to isolate interoperability
 * while retaining native config/canUse and a separate bounded test cooldown.
 */
public final class AbominationActionPilot {
    private static final JAWRVampireProbe VAMPIRE = new JAWRVampireProbe();
    private static final JAWRLycanthropeProbe WOLF = new JAWRLycanthropeProbe();
    private static final VampirismFactionProbe HUNTER =
            new VampirismFactionProbe(ProviderState.VAMPIRISM_HUNTER);
    private static final String COOLDOWN_PREFIX = "tmm_abomination_pilot_";

    public enum Ability {
        HISS("vampire", "de.teamlapen.vampirism.entity.player.vampire.VampirePlayer",
                "de.teamlapen.vampirism.entity.player.vampire.actions.VampireActions", "HISSING"),
        HOWL("werewolf", "de.teamlapen.werewolves.entities.player.werewolf.WerewolfPlayer",
                "de.teamlapen.werewolves.core.ModActions", "HOWLING");

        public final String command;
        final String playerClass;
        final String registryClass;
        final String registryField;

        Ability(String command, String playerClass, String registryClass, String registryField) {
            this.command = command;
            this.playerClass = playerClass;
            this.registryClass = registryClass;
            this.registryField = registryField;
        }
    }

    public record Readiness(AbominationPilotPolicy.Verdict verdict, Observation vampire,
                            Observation wolf, Observation hunter, String reason) {
        public boolean ready() { return verdict == AbominationPilotPolicy.Verdict.READY; }
    }

    public record Result(boolean success, String message) { }

    private AbominationActionPilot() { }

    public static Readiness readiness(ServerPlayer player) {
        if (!VampireChassisConfig.abominationPilot()) {
            return new Readiness(AbominationPilotPolicy.Verdict.DISABLED,
                    Observation.UNKNOWN, Observation.UNKNOWN, Observation.UNKNOWN,
                    "Enable experimentalAbominationTwoAbilityPilot in server config (disposable worlds only)");
        }
        if (!ModList.get().isLoaded("vampirism") || !ModList.get().isLoaded("werewolves")
                || !ModList.get().isLoaded("witchery")) {
            return new Readiness(AbominationPilotPolicy.Verdict.UNKNOWN_AFFLICTION,
                    Observation.UNKNOWN, Observation.UNKNOWN, Observation.UNKNOWN,
                    "Vampirism, Werewolves and JAWR must all be loaded");
        }
        Observation vamp = VAMPIRE.observe(player);
        Observation wolf = WOLF.observe(player);
        Observation hunter = HUNTER.observe(player);
        var verdict = AbominationPilotPolicy.assess(true, vamp, wolf, hunter);
        String reason = switch (verdict) {
            case READY -> "JAWR dual afflictions present; ability adapter test may run";
            case UNKNOWN_AFFLICTION -> "At least one JAWR affliction is unverified";
            case MISSING_DUAL_AFFLICTION -> "Acquire both JAWR vampire and lycanthropy first";
            case HUNTER_CONFLICT -> "Hunter conversion protection prohibits hybrid tests";
            case DISABLED -> "Experimental pilot is disabled";
        };
        if (hunter == Observation.UNKNOWN && verdict == AbominationPilotPolicy.Verdict.READY) {
            return new Readiness(AbominationPilotPolicy.Verdict.UNKNOWN_AFFLICTION,
                    vamp, wolf, hunter, "Cannot verify Hunter immunity: refusing pilot");
        }
        return new Readiness(verdict, vamp, wolf, hunter, reason);
    }

    /**
     * Exactly two whitelisted native action implementations. Does not touch
     * upstream faction/skill levels, nor catch-and-clear any player state.
     */
    public static Result activate(ServerPlayer player, Ability ability) {
        Readiness state = readiness(player);
        if (!state.ready()) return new Result(false, state.reason());
        final String key = COOLDOWN_PREFIX + ability.name().toLowerCase() + "_until";
        long now = player.serverLevel().getGameTime();
        CompoundTag tag = player.getPersistentData();
        long remaining = tag.getLong(key) - now;
        if (remaining > 0 && remaining < 20L * 60L * 60L) {
            return new Result(false, "Test cooldown: " + (remaining + 19) / 20 + "s");
        }
        try {
            Object original = Class.forName(ability.playerClass)
                    .getMethod("get", Player.class).invoke(null, player);
            if (original == null) return new Result(false, "Native player attachment absent");
            Object deferred = Class.forName(ability.registryClass)
                    .getField(ability.registryField).get(null);
            if (!(deferred instanceof Supplier<?> supplier)) {
                return new Result(false, "Native action registry type changed");
            }
            Object action = supplier.get();
            if (action == null) return new Result(false, "Native action unavailable");

            // Call via the original generic IAction interfaces; this avoids
            // assuming the custom faction player implements incompatible native
            // IVampirePlayer/IWerewolfPlayer generic contracts.
            Class<?> factionPlayerType = Class.forName(
                    "de.teamlapen.vampirism.api.entity.player.IFactionPlayer");
            Class<?> contextType = Class.forName(
                    "de.teamlapen.vampirism.api.entity.player.actions.IAction$ActivationContext");
            Object permitted = action.getClass().getMethod("canUse", factionPlayerType)
                    .invoke(action, original);
            if (!"ALLOWED".equals(String.valueOf(permitted))) {
                return new Result(false, "Native action veto: " + permitted);
            }
            Object context = Proxy.newProxyInstance(contextType.getClassLoader(),
                    new Class<?>[]{contextType}, (proxy, method, args) -> {
                        if (method.getName().equals("targetBlock") || method.getName().equals("targetEntity")) {
                            return Optional.empty();
                        }
                        if (method.getName().equals("toString")) return "TMM empty pilot target";
                        if (method.getName().equals("hashCode")) return System.identityHashCode(proxy);
                        if (method.getName().equals("equals")) return proxy == args[0];
                        throw new UnsupportedOperationException("Unexpected action context method " + method.getName());
                    });
            Object result = action.getClass().getMethod("onActivated", factionPlayerType, contextType)
                    .invoke(action, original, context);
            if (!(result instanceof Boolean applied) || !applied) {
                return new Result(false, "Native action did not apply");
            }
            int cooldown = ((Number) action.getClass()
                    .getMethod("getCooldown", factionPlayerType).invoke(action, original)).intValue();
            tag.putLong(key, AbominationPilotPolicy.nextReadyTick(now, cooldown));
            return new Result(true, "Native " + ability.command + " action ran using its own player attachment. "
                    + "Native skill entitlement/packet events are NOT tested.");
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            Throwable cause = e instanceof InvocationTargetException invocation
                    ? invocation.getTargetException() : e;
            TransformationsMod.LOGGER.error("Abomination pilot {} could not delegate native action", ability, cause);
            return new Result(false, "Native action API mismatch or error: " + cause.getClass().getSimpleName()
                    + ". Check server log; no progression was changed.");
        }
    }
}
