package io.github.heatwaveqc.tyr.progression.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/** Bounded read-only client prediction projection; never accepted from clients. */
public final class MiningPredictionRules {
    private MiningPredictionRules(){}
    public static final int MAX_ENCODED=32768, MAX_RULES=128;
    public record Rule(String kind,String selector,String tool,double amount,boolean emptyHand,boolean unarmored){
        public Rule {
            if(selector==null||selector.length()>256||!selector.matches("(?:\\*|#?[a-z0-9_.-]+:[a-z0-9_./-]+)?"))
                throw new IllegalArgumentException("Invalid mining selector");
            if(!Double.isFinite(amount)||Math.abs(amount)>1000000||tool==null)
                throw new IllegalArgumentException("Invalid mining value");
            switch(kind){
                case "speed" -> {if(!tool.isEmpty())throw new IllegalArgumentException("Unexpected tool");}
                case "native" -> {if(!tool.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")||tool.length()>256||amount!=0)
                    throw new IllegalArgumentException("Invalid native harvest tool");}
                case "tier" -> {if(!tool.isEmpty()||amount<0||amount>1000000||amount!=Math.floor(amount))
                    throw new IllegalArgumentException("Invalid harvest tier");}
                default -> throw new IllegalArgumentException("Unknown mining rule");
            }
        }
        public boolean eligible(boolean empty,boolean bare){return (!emptyHand||empty)&&(!unarmored||bare);}
    }
    public static String encode(List<Rule> rules){
        if(rules.size()>MAX_RULES)throw new IllegalArgumentException("Too many mining rules");
        String result=String.join("\n",rules.stream().map(r->String.join("\t",r.kind(),r.selector(),r.tool(),
            Double.toString(r.amount()),r.emptyHand()?"1":"0",r.unarmored()?"1":"0")).sorted().toList());
        if(result.length()>MAX_ENCODED)throw new IllegalArgumentException("Mining projection too large");
        return result;
    }
    public static List<Rule> decode(String text){
        if(text==null||text.length()>MAX_ENCODED)throw new IllegalArgumentException("Invalid mining projection");
        if(text.isEmpty())return List.of();
        String[] rows=text.split("\n",-1);
        if(rows.length>MAX_RULES)throw new IllegalArgumentException("Too many mining rules");
        var rules=new ArrayList<Rule>();
        for(String row:rows){
            String[] fields=row.split("\t",-1);
            if(fields.length!=6||!fields[4].matches("[01]")||!fields[5].matches("[01]"))
                throw new IllegalArgumentException("Malformed mining rule");
            rules.add(new Rule(fields[0],fields[1],fields[2],Double.parseDouble(fields[3]),
                fields[4].equals("1"),fields[5].equals("1")));
        }
        return List.copyOf(rules);
    }
    public static double scale(List<Rule> rules,Predicate<String> matches,boolean empty,boolean bare){
        double result=1;
        for(var r:rules)if(r.kind().equals("speed")&&r.eligible(empty,bare)&&matches.test(r.selector()))result+=r.amount();
        return result;
    }
}
