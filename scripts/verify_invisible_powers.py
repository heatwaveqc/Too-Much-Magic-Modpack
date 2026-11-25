#!/usr/bin/env python3
"""#532 Type-A source invariant: real Elytra permissions without status effects.

Read-only. No Minecraft build/boot, runtime claim or dependency download.
"""
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MAIN = ROOT / "compat/progression/src/main/java/io/github/heatwaveqc/tyr/progression"
RESOURCES = ROOT / "compat/progression/src/main/resources"


def read(path: str) -> str:
    return (MAIN / path).read_text(encoding="utf-8")


def require(path: str, *tokens: str) -> None:
    content = read(path)
    for token in tokens:
        if token not in content:
            raise AssertionError(f"{path}: missing {token!r}")


def main() -> None:
    ve = read("runtime/VirtualElytra.java")
    for bad in ("new MobEffectInstance(", "player.addEffect(", "player.hasEffect(ACCESS)",
                "return wearer.hasEffect", "getAbilities().mayfly"):
        if bad in ve:
            raise AssertionError("Virtual Elytra must not grant/require a potion or creative flight: " + bad)
    for necessary in ("PassivePowers.register(POWER", "PassivePowers.active(server, POWER)",
                      "ClientPowerSnapshot.active(wearer, POWER)",
                      "player.removeEffect(OLD_ACCESS)", "VirtualElytraPolicy.sources"):
        if necessary not in ve:
            raise AssertionError("Virtual Elytra missing nonpotion or legacy cleanup: " + necessary)

    require("runtime/PassivePowers.java",
            "Function<ServerPlayer, Set<String>>", "resolver.apply(player)",
            "PassivePowerNetwork.send", "player.tickCount % 20", "public static void forget")
    require("runtime/ClientPowerSnapshot.java",
            "owner.get() == player", "player.level().isClientSide()",
            "dimension().location().toString().equals(dimension)",
            "PassivePowerLease.valid")
    require("client/ClientPassivePowers.java",
            "ClientPlayerNetworkEvent.LoggingIn", "ClientPlayerNetworkEvent.LoggingOut",
            "ClientPlayerNetworkEvent.Clone", "ClientPowerSnapshot.clear()")
    require("network/PassivePowerNetwork.java",
            "playToClient", "ClientPassivePowers.accept", "ByteBufCodecs.stringUtf8(8192)")
    if "playToServer" in read("network/PassivePowerNetwork.java"):
        raise AssertionError("Clients must not grant or revoke passive powers")
    require("ProgressionMod.java", "VirtualElytra.register(modBus)",
            "PassivePowerNetwork::register")
    require("runtime/NewbieMode.java", "VirtualElytra.reconcile(player,true)")
    require("runtime/ClassAbilities.java", "if (changed) VirtualElytra.reconcile(p,true)")
    require("runtime/ProgressionEvents.java", "PassivePowers.forget(player)")
    require("network/ProgressionMenuNetwork.java", "VirtualElytra.reconcile(player,true)")
    require("mixin/VirtualElytraItemExtensionMixin.java",
            "method = \"canElytraFly\"", "method = \"elytraFlightTick\"",
            "if (VirtualElytra.canGlide(wearer)) cir.setReturnValue(true)")
    mixins = (RESOURCES / "tyr_progression.mixins.json").read_text(encoding="utf-8")
    if '"VirtualElytraItemExtensionMixin"' not in mixins:
        raise AssertionError("Preserve additive, existing NeoForge flight mixin")
    hunter = read("runtime/HunterMutations.java")
    if "player.addEffect(" in hunter:
        raise AssertionError("Permanent Hunter mutations must not refresh status effects")
    require("runtime/MutationPowers.java", "createModifiers(", "addTransientModifier(",
            "getActiveEffectsMap()", "ClientPowerSnapshot.mutation")
    require("mixin/MutationEffectQueryMixin.java", "getEffect", "hasEffect")
    require("network/PassivePowerNetwork.java", "Snapshot::mutations")
    require("runtime/MutagenicBrewItem.java", "MobEffects.WITHER")
    # Permanent entitlements and genuine temporary effects are distinct.
    # The detached mutation descriptor is a query view, not an active potion.
    mutation = read("runtime/MutationPowers.java")
    if ".addEffect(" in mutation:
        raise AssertionError("Permanent mutations must not grant native status instances")
    require("runtime/MutationPowers.java",
            "return new MobEffectInstance(effect,240,amp,true,false,false)",
            "holder.value().createModifiers", "effect.applyEffectTick")
    require("runtime/EffectRuntime.java", "addTransientModifier(")
    # Finite buffs/debuffs remain ordinary potion effects with visible icons.
    require("runtime/MechanicalEffects.java",
            "target.addEffect(new MobEffectInstance(holder,duration,amp,true,false,true))",
            "status_on_hit", "status_on_hurt", "health_threshold_status", "status_pulse",
            "MobEffects.GLOWING")
    require("runtime/forms/LichEffects.java",
            "p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE")
    require("runtime/ElixirRuntime.java",
            "p.addEffect(new MobEffectInstance(holder,buff.ticks(),buff.amplifier()))")
    require("runtime/ClassAbilities.java",
            "new MobEffectInstance(MobEffects.BLINDNESS", "new MobEffectInstance(MobEffects.WITHER")
    require("data/HunterMutationPolicy.java",
            "moddedEffectWhitelist.contains(effectId)", "!effectBlacklist.contains(effectId)")
    print("PASS #532: permanent powers stay out of the effect map; finite status effects remain native")
    print("PASS #532: nonpotion server-owned permission, client lease, legacy cleanup and native flight extension")
    print("TYPE A ONLY. Elytra physics, HUD invisibility in installed client, respawn and mod conflicts require B/C.")


if __name__ == "__main__":
    main()
