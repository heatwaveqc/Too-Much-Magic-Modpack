package io.github.heatwaveqc.tmm.progression.mxp.hooks;
import io.github.heatwaveqc.tmm.progression.mxp.core.E6bSpawnCatalog;
import io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards;
import net.minecraft.server.level.ServerLevel;
import java.util.Collection;
import java.util.UUID;

public final class EnchantedCurseWitness{
    private EnchantedCurseWitness(){}
    public interface Rite{
        void tmm$bindCurseReceipt(RitualRewards.Receipt receipt);
        RitualRewards.Receipt tmm$curseReceipt();
        void tmm$markCurseMutation();
    }
    public record State(int count,long strength){}
    public static boolean eligible(Object rite,Object type,UUID target,ServerLevel level,boolean removing){
        if(!(rite instanceof Rite state)||state.tmm$curseReceipt()==null||target==null||level==null
            ||MxpReflect.call(rite,"getLevel")!=level||!level.getServer().isSameThread())return false;
        var receipt=state.tmm$curseReceipt();
        var proof=E6bSpawnCatalog.enchanted(receipt.recipeId());
        Object params=MxpReflect.field(rite,"params");
        return E6bSpawnCatalog.enchantedCurse(proof)&&proof.nativeClass().equals(rite.getClass().getName())
            &&proof.nativeClass().endsWith(removing?".RemoveCurseRite":".ApplyCurseRite")
            &&proof.output().equals(String.valueOf(MxpReflect.call(type,"id")))
            &&target.equals(MxpReflect.field(params,"target"))&&receipt.actor().equals(MxpReflect.field(params,"caster"))
            &&receipt.recipeId().equals(E6bSpawnScope.enchantedId(level,MxpReflect.call(rite,"getType")));
    }
    public static State snapshot(Object manager,Object type,UUID target,ServerLevel level){
        if(!(MxpReflect.call(manager,"getCurses",target,level) instanceof Collection<?> curses))return null;
        int count=0;long strength=0;
        for(Object entry:curses){
            Object curse=MxpReflect.call(entry,"getCurse");
            Object actual=MxpReflect.call(curse,"type");
            if(actual==null)return null;
            if(type.equals(actual)){
                if(!(MxpReflect.call(entry,"getStrength") instanceof Integer n)||n<0)return null;
                count++;strength+=n;
            }
        }
        return new State(count,strength);
    }
    public static void changed(Object rite,Object manager,Object type,UUID target,ServerLevel level,State before,boolean removing){
        if(before==null||!eligible(rite,type,target,level,removing))return;
        State after=snapshot(manager,type,target,level);
        if(after==null)return;
        int cmp=after.count()!=before.count()?Integer.compare(after.count(),before.count()):Long.compare(after.strength(),before.strength());
        if(removing?cmp<0:cmp>0)((Rite)rite).tmm$markCurseMutation();
    }
}
