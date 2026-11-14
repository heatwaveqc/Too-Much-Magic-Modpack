package io.github.heatwaveqc.tmm.monk.skills;

import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.player.skills.DefaultSkill;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import io.github.heatwaveqc.tmm.monk.MonkMod;
import io.github.heatwaveqc.tmm.monk.faction.IMonkPlayer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * The root skill is intentionally inert. More powers are to be registered
 * in Java; the layout and prerequisites live in datapack JSON.
 */
public final class MonkSkills {
    public static final DeferredRegister<ISkill<?>> SKILLS =
            DeferredRegister.create(VampirismRegistries.Keys.SKILL, MonkMod.MOD_ID);
    public static final DeferredHolder<ISkill<?>, ISkill<IMonkPlayer>> ROOT =
            SKILLS.register("monk_root", () -> new DefaultSkill<IMonkPlayer>(0) { });

    private MonkSkills() { }
}
