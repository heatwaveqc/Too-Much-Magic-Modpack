package io.github.heatwaveqc.tyr.progression.model;
import io.github.heatwaveqc.tyr.progression.core.MxpMath;
import java.util.List;
/** Independent XP definition. The curve is a skill-free parsed specification, not a tree. */
public record XpChannel(String id, Definition.Path source, double growth,String earningPath,String name,String pointName) {
    public static final String MXP="tyr_progression:mxp";
    public XpChannel {
        if(id==null || !id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))throw new IllegalArgumentException("Invalid XP ID");
        if((source==null)!=MXP.equals(id))throw new IllegalArgumentException("Reserved MXP identity");
        MxpMath.validateGrowth(growth);
    }
    public boolean magic(){return source==null;}
    public int maxRank(){return magic()?Integer.MAX_VALUE:source.maxRank();}
    public boolean earnsFrom(String path){return !magic()&&earningPath.equals(path);}
    public List<Definition.Activity> sources(){return magic()?List.of():source.activities();}
    public static XpChannel mxp(double growth){return new XpChannel(MXP,null,growth,"","MXP","Magic skill points");}
}
