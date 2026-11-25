package io.github.heatwaveqc.tyr.progression.admin;

import java.util.*;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;

/** Short-lived opt-in observations of trusted XP settlement, not an award source.
 * Server-thread only; no disk logging, client packet, or unbounded history. */
public final class XpTrace {
    private XpTrace(){}
    public static final int MAX_SECONDS=120, MAX_LINES=24;
    private record Session(long until,ArrayDeque<String> lines){}
    private static final Map<UUID,Session> SESSIONS=new HashMap<>();
    private static long now(ServerPlayer p){return p.getServer().overworld().getGameTime();}
    public static void start(ServerPlayer p,int seconds){
        if(seconds<1||seconds>MAX_SECONDS)throw new IllegalArgumentException("Trace duration must be 1..120 seconds");
        SESSIONS.put(p.getUUID(),new Session(now(p)+seconds*20L,new ArrayDeque<>()));
    }
    public static void forget(UUID id){SESSIONS.remove(id);}
    public static void note(ServerPlayer p,Supplier<String> message){
        if(p==null||p.getServer()==null||!p.getServer().isSameThread()||SESSIONS.isEmpty())return;
        var s=SESSIONS.get(p.getUUID());
        if(s==null||now(p)>=s.until())return;
        String line=message.get();
        if(line.length()>320)line=line.substring(0,320);
        if(s.lines().size()==MAX_LINES)s.lines().removeFirst();
        s.lines().addLast(now(p)+": "+line);
    }
    public static String inspect(ServerPlayer p){
        var s=SESSIONS.get(p.getUUID());
        if(s==null)return "Trace off; set 1..120 seconds to start";
        return "remaining ticks="+Math.max(0,s.until()-now(p))+"; last "+s.lines().size()+" entries:\n"+String.join("\n",s.lines());
    }
}
