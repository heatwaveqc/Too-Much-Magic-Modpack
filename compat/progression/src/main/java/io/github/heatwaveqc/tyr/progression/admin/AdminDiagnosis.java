package io.github.heatwaveqc.tyr.progression.admin;

import io.github.heatwaveqc.tyr.progression.core.*;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.runtime.NewbieMode;
import io.github.heatwaveqc.tyr.progression.state.*;
import net.minecraft.server.level.ServerPlayer;
import java.util.*;

/** Ledger invariants independent of the optional mechanics' registered validation. */
public final class AdminDiagnosis {
    private AdminDiagnosis(){}
    public static List<String> ledgerFindings(Ledger ledger,boolean newbie){
        var issues=new ArrayList<String>();
        var catalog=PathRegistry.economyCatalog();
        var expected=BaselinePaths.reconcile(ledger,newbie);
        if(!expected.equals(ledger))issues.add("Baseline membership disagrees with saved stage; use repair");
        if(!newbie&&!ledger.get(BaselinePaths.CORE).joined())issues.add("Graduate lacks Core; use repair");
        if(newbie&&ledger.get(BaselinePaths.CORE).joined())issues.add("Newbie retains Core; manual stage recovery required");
        if(newbie&&BaselinePaths.hasSpecialist(ledger))issues.add("Newbie retains specialist membership; manual stage recovery required");
        for(var row:ledger.economy().currentXp().entrySet()){
            var spec=catalog.systems().get(row.getKey());
            if(spec==null){issues.add("Unknown saved currency: "+row.getKey());continue;}
            if(row.getValue().signum()<0||row.getValue().compareTo(spec.maxXp())>0)issues.add("XP outside configured cap: "+row.getKey());
            try{ExperienceEconomy.available(ledger.economy(),spec);}
            catch(RuntimeException ex){issues.add("Invalid ordinary point backing: "+row.getKey());}
        }
        var receipts=new TreeMap<String,List<String>>();
        for(var rank:ledger.economy().paidRanks()){
            String key=rank.path()+"|"+rank.skill();
            receipts.computeIfAbsent(key,k->new ArrayList<>()).add(rank.system()+"|"+rank.pointCost());
            if(!catalog.systems().containsKey(rank.system()))issues.add("Paid rank has unknown currency: "+key);
            if(!ledger.get(rank.path()).joined())issues.add("Paid rank on departed path: "+key);
            var tree=PathRegistry.get(rank.path());
            if(tree==null||tree.skill(rank.skill())==null)issues.add("Paid rank has missing skill definition: "+key);
        }
        for(var row:receipts.entrySet()){
            var actual=ledger.payments().getOrDefault(row.getKey(),List.of()).stream()
                .map(v->v.startsWith("!")?v.substring(1):v)
                .filter(v->{int i=v.lastIndexOf('|');return i>0&&catalog.systems().containsKey(v.substring(0,i));}).toList();
            if(!row.getValue().equals(actual))issues.add("Canonical and payment receipt order/count disagree: "+row.getKey());
        }
        for(var row:ledger.paths().entrySet())if(row.getValue().joined()&&PathRegistry.get(row.getKey())==null)
            issues.add("Joined path definition missing: "+row.getKey());
        return issues.stream().distinct().sorted().limit(100).toList();
    }
    /** No destructive edits, XP changes, reward receipt clearing or provider writes. */
    public static void repairDerived(ServerPlayer p){
        NewbieMode.reconcileBaselines(p);
        io.github.heatwaveqc.tyr.progression.runtime.forms.ProviderFlags.forget(p.getUUID());
        AdminStateRefresh.reconcile(p);
    }
}
