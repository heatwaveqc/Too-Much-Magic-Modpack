package io.github.heatwaveqc.tmm.monk.skills;

import com.mojang.datafixers.util.Either;
import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.entity.player.skills.VampirismSkill;
import io.github.heatwaveqc.tmm.monk.MonkMod;
import io.github.heatwaveqc.tmm.monk.faction.IMonkPlayer;
import io.github.heatwaveqc.tmm.monk.faction.MonkFaction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

/** Tree membership is defined by Vampirism; skills and powers are Java. */
public final class MonkSkills {
    public static final ResourceKey<ISkillTree> LEVEL_TREE = ResourceKey.create(
            VampirismRegistries.Keys.SKILL_TREE,
            ResourceLocation.fromNamespaceAndPath(MonkMod.MOD_ID, "monk/level"));
    public static final DeferredRegister<ISkill<?>> SKILLS =
            DeferredRegister.create(VampirismRegistries.Keys.SKILL, MonkMod.MOD_ID);
    public static final DeferredHolder<ISkill<?>, ISkill<IMonkPlayer>> ROOT =
            SKILLS.register("monk_root", () -> new SimpleMonkSkill(0));

    public static final DeferredHolder<ISkill<?>, ISkill<IMonkPlayer>> OPEN_PALM =
            SKILLS.register("open_palm", () -> new SimpleMonkSkill(1));
    public static final DeferredHolder<ISkill<?>, ISkill<IMonkPlayer>> POISE =
            SKILLS.register("unarmored_poise", () -> new SimpleMonkSkill(1));
    public static final DeferredHolder<ISkill<?>, ISkill<IMonkPlayer>> FLEET =
            SKILLS.register("fleet_footwork", () -> new SimpleMonkSkill(1));

    private MonkSkills() { }

    private static final class SimpleMonkSkill extends VampirismSkill<IMonkPlayer> {
        private SimpleMonkSkill(int skillCost) {
            super(Either.left(LEVEL_TREE), skillCost, false);
        }

        @Override public @NotNull Optional<IPlayableFaction<?>> getFaction() {
            return Optional.of(MonkFaction.FACTION);
        }
    }
}
