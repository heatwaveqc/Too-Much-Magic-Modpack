package io.github.heatwaveqc.tmm.monk.faction;

import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import org.jetbrains.annotations.NotNull;

public interface IMonkPlayer extends IFactionPlayer<IMonkPlayer> {
    @Override
    default @NotNull IPlayableFaction<IMonkPlayer> getFaction() {
        return MonkFaction.FACTION;
    }
}
