package io.github.heatwaveqc.tmm.monk.gameplay;

import de.teamlapen.vampirism.api.VampirismAPI;
import io.github.heatwaveqc.tmm.monk.faction.MonkFaction;
import io.github.heatwaveqc.tmm.monk.rules.MonkEligibility;
import io.github.heatwaveqc.tmm.monk.skills.MonkSkills;
import io.github.heatwaveqc.tmm.monk.skills.MonkAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/** Reversible faction-native abilities; not duplicate Tyz universal purchases. */
public final class MonkTechniqueEffects {
    private static final ResourceLocation PALM_ID = ResourceLocation.fromNamespaceAndPath("tmm_monk", "open_palm");
    private static final ResourceLocation POISE_ID = ResourceLocation.fromNamespaceAndPath("tmm_monk", "unarmored_poise");
    private static final ResourceLocation FLEET_ID = ResourceLocation.fromNamespaceAndPath("tmm_monk", "fleet_footwork");
    private static final ResourceLocation TYZ_PALM_ID = ResourceLocation.fromNamespaceAndPath("tmm_monk", "tyz_iron_palm");
    private static final ResourceLocation TYZ_HARVEST_ID = ResourceLocation.fromNamespaceAndPath("tmm_monk", "tyz_barehand_harvest");
    private static final AttributeModifier PALM = new AttributeModifier(PALM_ID, 1.0, AttributeModifier.Operation.ADD_VALUE);
    private static final AttributeModifier POISE = new AttributeModifier(POISE_ID, 2.0, AttributeModifier.Operation.ADD_VALUE);
    private static final AttributeModifier FLEET = new AttributeModifier(FLEET_ID, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    private MonkTechniqueEffects() { }

    public static void onTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) reconcile(player);
    }

    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            update(player, Attributes.ATTACK_DAMAGE, PALM, false);
            update(player, Attributes.ARMOR, POISE, false);
            update(player, Attributes.MOVEMENT_SPEED, FLEET, false);
            updateAmount(player, Attributes.ATTACK_DAMAGE, TYZ_PALM_ID, 0);
            updateAmount(player, Attributes.MINING_EFFICIENCY, TYZ_HARVEST_ID, 0);
        }
    }

    private static void reconcile(ServerPlayer player) {
        boolean eligible = MonkEligibility.eligibleForMonkPower(player);
        var state = MonkFaction.FACTION == null ? java.util.Optional.<io.github.heatwaveqc.tmm.monk.faction.IMonkPlayer>empty()
                : MonkFaction.FACTION.getPlayerCapability(player);
        // Explicit state on each tick prevents lingering bonuses after
        // wearing armor, leaving the faction, respec or dying.
        boolean hasFaction = eligible && VampirismAPI.factionPlayerHandler(player).isInFaction(MonkFaction.FACTION);
        boolean palm = hasFaction && player.getMainHandItem().isEmpty()
                && state.map(s -> s.getSkillHandler().isSkillEnabled(MonkSkills.OPEN_PALM.get())).orElse(false);
        boolean poise = hasFaction
                && state.map(s -> s.getSkillHandler().isSkillEnabled(MonkSkills.POISE.get())).orElse(false);
        boolean fleet = hasFaction
                && state.map(s -> s.getSkillHandler().isSkillEnabled(MonkSkills.FLEET.get())).orElse(false);

        update(player, Attributes.ATTACK_DAMAGE, PALM, palm);
        update(player, Attributes.ARMOR, POISE, poise);
        update(player, Attributes.MOVEMENT_SPEED, FLEET, fleet);

        // Tyz owns all rank values. This adapter only translates Monk-specific
        // inert attributes into conditional benefits; no duplicate Tyz wallet.
        double handPower = hasFaction && player.getMainHandItem().isEmpty()
                ? attributeValue(player, MonkAttributes.UNARMED_TRAINING) : 0;
        double handMining = hasFaction && player.getMainHandItem().isEmpty()
                ? attributeValue(player, MonkAttributes.HAND_HARVEST_TRAINING) : 0;
        updateAmount(player, Attributes.ATTACK_DAMAGE, TYZ_PALM_ID, handPower);
        updateAmount(player, Attributes.MINING_EFFICIENCY, TYZ_HARVEST_ID, handMining);
    }

    private static double attributeValue(ServerPlayer player, net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute) {
        AttributeInstance a = player.getAttribute(attribute);
        return a == null ? 0 : Math.max(0, Math.min(64, a.getValue()));
    }

    private static void updateAmount(ServerPlayer player,
                                     net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute,
                                     ResourceLocation modifierId, double amount) {
        AttributeInstance a = player.getAttribute(attribute);
        if (a == null) return;
        AttributeModifier old = a.getModifier(modifierId);
        if (old != null && Double.compare(old.amount(), amount) != 0) {
            a.removeModifier(modifierId);
            old = null;
        }
        if (amount > 0 && old == null) {
            a.addTransientModifier(new AttributeModifier(modifierId, amount, AttributeModifier.Operation.ADD_VALUE));
        }
    }

    private static void update(ServerPlayer player, net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute,
                               AttributeModifier modifier, boolean enabled) {
        AttributeInstance a = player.getAttribute(attribute);
        if (a == null) return;
        boolean existing = a.getModifier(modifier.id()) != null;
        if (enabled && !existing) a.addTransientModifier(modifier);
        else if (!enabled && existing) a.removeModifier(modifier.id());
    }
}
