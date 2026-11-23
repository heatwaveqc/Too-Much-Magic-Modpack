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
    print("PASS #532: nonpotion server-owned permission, client lease, legacy cleanup and native flight extension")
    print("TYPE A ONLY. Elytra physics, HUD invisibility in installed client, respawn and mod conflicts require B/C.")


if __name__ == "__main__":
    main()
