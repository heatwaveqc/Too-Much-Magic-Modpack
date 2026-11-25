package io.github.heatwaveqc.tyr.progression.core;

import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import io.github.heatwaveqc.tyr.progression.state.Track;
import java.util.*;

/** Operator progress repair without invoking gameplay award producers. */
public final class AdminMilestones {
    private AdminMilestones(){}
    public static Definition.Milestone milestone(Definition.Path path,String id){
        if(path==null)throw new IllegalArgumentException("Unknown path");
        return path.milestones().stream().filter(m->m.id().equals(id)).findFirst()
            .orElseThrow(()->new IllegalArgumentException("Unknown milestone "+id));
    }
    public static Ledger counter(Ledger old,Definition.Path path,String id,int index,long desired){
        var m=milestone(path,id);
        var track=old.get(path.id());
        if(!track.joined()||track.completed(id))throw new IllegalArgumentException("Requires an unfinished milestone on a joined path");
        if(index<0||index>=m.criteria().size()||desired<0||desired>m.criteria().get(index).count())
            throw new IllegalArgumentException("Counter outside configured bounds");
        var key=ProgressionMath.counterKey(id,index);
        var counters=new HashMap<>(track.counters());
        if(desired==0)counters.remove(key);else counters.put(key,(int)desired);
        return old.with(path.id(),new Track(track.joined(),track.xp(),track.rank(),track.points(),
            track.skills(),track.milestones(),counters));
    }
    /** Mark the trial and its achievement complete, deliberately without issuing its reward. */
    public static Ledger complete(Ledger old,Definition.Path path,String id){
        var m=milestone(path,id);
        var track=old.get(path.id());
        if(!track.joined()||track.completed(id)||!track.milestones().containsAll(m.after()))
            throw new IllegalArgumentException("Requires joined, unfinished milestone with completed prerequisites");
        var next=old;
        for(int i=0;i<m.criteria().size();i++)next=counter(next,path,id,i,m.criteria().get(i).count());
        return next.with(path.id(),next.get(path.id()).complete(id))
            .withEconomy(next.economy().withAchievement(id));
    }
    /** Repair a missing global achievement only when local completion proves it.
     * Do not grant XP or alter any receipt, counter or ownership. */
    public static Ledger reconcileCompletion(Ledger old,Definition.Path path,String id){
        milestone(path,id);
        if(!old.get(path.id()).completed(id))throw new IllegalArgumentException("No completed local milestone");
        return old.withEconomy(old.economy().withAchievement(id));
    }
    /** Reset unfinished counters only. Completed achievements are durable; do not re-arm paid trials. */
    public static Ledger resetProgress(Ledger old,Definition.Path path,String id){
        var m=milestone(path,id);
        if(!old.get(path.id()).joined()||old.get(path.id()).completed(id))
            throw new IllegalArgumentException("Completed milestones are durable; reset unfinished progress only");
        var next=old;
        for(int i=0;i<m.criteria().size();i++)next=counter(next,path,id,i,0);
        return next;
    }
}
