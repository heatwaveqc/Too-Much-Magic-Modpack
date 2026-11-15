package io.github.heatwaveqc.tmm.monk.gameplay;

import de.teamlapen.vampirism.api.event.PlayerFactionEvent;
import io.github.heatwaveqc.tmm.monk.faction.MonkFaction;
import io.github.heatwaveqc.tmm.monk.rules.MonkEligibility;
import io.github.heatwaveqc.tmm.monk.rules.DisciplineRules;
import io.github.heatwaveqc.tmm.monk.skills.MonkSkills;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * One later-tier Monk mobility power, applied only while the faction/root skill and
 * equipment discipline are valid. A transient, stable-id modifier means
 * relogs and faction changes cannot permanently inflate the player's base
 * STEP_HEIGHT. It never modifies a universal Tyz rank.
 */
public final class StepAssistEffects {
    public static final ResourceLocation MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath("tmm_monk", "step_assist");
    static final AttributeModifier MODIFIER =
            new AttributeModifier(MODIFIER_ID, 0.5D, AttributeModifier.Operation.ADD_VALUE);

    private StepAssistEffects() { }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) reconcile(player);
    }

    public static void onFactionChanged(PlayerFactionEvent.FactionLevelChanged event) {
        if (event.getPlayer().asEntity() instanceof ServerPlayer player) reconcile(player);
    }

    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) remove(player);
    }

    public static void reconcile(ServerPlayer player) {
        AttributeInstance step = player.getAttribute(Attributes.STEP_HEIGHT);
        if (step == null) return;
        boolean monk = MonkFaction.FACTION != null
                && de.teamlapen.vampirism.api.VampirismAPI.factionPlayerHandler(player).isInFaction(MonkFaction.FACTION);
        boolean root = monk && MonkFaction.FACTION.getPlayerCapability(player)
                .map(p -> p.getLevel() > 0 && p.getSkillHandler().isSkillEnabled(MonkSkills.STEP_ASSIST.get()))
                .orElse(false);
        boolean active = DisciplineRules.permitsStepAssist(monk, root, MonkEligibility.equipmentAllowsDiscipline(player));
        boolean applied = step.getModifier(MODIFIER_ID) != null;
        if (active && !applied) step.addTransientModifier(MODIFIER);
        else if (!active && applied) step.removeModifier(MODIFIER_ID);
    }

    private static void remove(ServerPlayer player) {
        AttributeInstance step = player.getAttribute(Attributes.STEP_HEIGHT);
        if (step != null && step.getModifier(MODIFIER_ID) != null) {
            step.removeModifier(MODIFIER_ID);
        }
    }
}
