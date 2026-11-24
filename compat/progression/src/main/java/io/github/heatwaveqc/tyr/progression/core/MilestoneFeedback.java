package io.github.heatwaveqc.tyr.progression.core;

import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.state.Track;

/**
 * Milestone discovery and progress are read from the authoritative server ledger.
 * Path JSON controls labels, ordering and opt-in; feedback never grants rewards.
 */
public final class MilestoneFeedback {
    private MilestoneFeedback() {}

    public record Notice(String milestoneId,String title,int current,int total,
                         boolean complete,boolean discovered) {
        public String message() {
            if(complete)return "Challenge complete: "+title;
            return (discovered?"Challenge discovered: ":"Challenge: ")+title+
                " ("+current+"/"+total+")";
        }
    }

    private static String label(Definition.Milestone m) {
        if(!m.title().isBlank())return m.title();
        String s=m.id().substring(m.id().indexOf(':')+1).replace('_',' ');
        return s.isEmpty()?m.id():Character.toUpperCase(s.charAt(0))+s.substring(1);
    }

    /**
     * One notice per completed event. Completion wins over simultaneous
     * partial progress; JSON order breaks ties. All counters still advance.
     */
    public static Notice changed(Definition.Path path,Track before,Track after) {
        Notice partial=null,finished=null;
        for(var m:path.milestones()) {
            if(!m.notifyProgress()||before.completed(m.id()))continue;
            boolean completed=after.completed(m.id());
            for(int i=0;i<m.criteria().size();i++){
                var criterion=m.criteria().get(i);
                String key=ProgressionMath.counterKey(m.id(),i);
                int oldCount=Math.min(criterion.count(),before.criterionCount(key));
                int newCount=Math.min(criterion.count(),after.criterionCount(key));
                if(newCount<=oldCount)continue;
                var n=new Notice(m.id(),label(m),newCount,criterion.count(),completed,oldCount==0);
                if(completed && finished==null)finished=n;
                if(!completed && partial==null)partial=n;
            }
            // A milestone with pre-completed counters may activate when its
            // 'after' prerequisite becomes true on a later server event.
            if(completed && finished==null){
                var criterion=m.criteria().getLast();
                finished=new Notice(m.id(),label(m),criterion.count(),criterion.count(),true,false);
            }
        }
        return finished!=null?finished:partial;
    }

    /** Recommendation only: 'after' affects completion, not progress collection. */
    public static Notice next(Definition.Path path,Track current){
        for(var m:path.milestones()){
            if(!m.notifyProgress()||current.completed(m.id())||
                    !current.milestones().containsAll(m.after()))continue;
            for(int i=0;i<m.criteria().size();i++){
                var criterion=m.criteria().get(i);
                int n=Math.min(criterion.count(),
                    current.criterionCount(ProgressionMath.counterKey(m.id(),i)));
                if(n<criterion.count())return new Notice(m.id(),label(m),
                    n,criterion.count(),false,n==0);
            }
        }
        return null;
    }
}
