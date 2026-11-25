package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.core.Signal;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import net.minecraft.server.level.ServerPlayer;

/** Achievement receipt generated from declarative, server-owned mastery requirements. */
public final class MasteryCompletionRuntime {
    private MasteryCompletionRuntime(){}
    public static void tick(ServerPlayer p){
        if(p.tickCount%20!=0||!p.isAlive()||p.isCreative()||p.isSpectator()
            ||NewbieMode.isNewbie(p)||p instanceof net.neoforged.neoforge.common.util.FakePlayer)return;
        for(var entry:PathRegistry.masteryCompletions().entrySet()){
            String path=entry.getKey();var spec=entry.getValue();
            var track=ProgressionApi.track(p,path);
            if(!track.joined()||track.completed(spec.milestone())||!PathRegistry.eligible(p,path))continue;
            if(spec.condition().test(p))
                ProgressionApi.recordFiltered(p,Signal.external(spec.event()),candidate->candidate.id().equals(path));
        }
    }
}
