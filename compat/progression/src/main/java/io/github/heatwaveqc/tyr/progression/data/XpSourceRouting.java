package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonObject;
import io.github.heatwaveqc.tyr.progression.core.XpModifierMath;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Independent mapping from an already verified source category to XP systems.
 * No activity authentication, Path-specific event rules or wallet state here.
 */
public record XpSourceRouting(Map<String, EarningPolicy> policies,
                              Map<String, List<Destination>> routes) {
    public static final int MAX_ROUTES = 4096;
    public static final int MAX_DESTINATIONS = 64;
    private static final EarningPolicy DEFAULT = new EarningPolicy("", 1);

    public XpSourceRouting {
        policies = Map.copyOf(policies);
        var copy = new HashMap<String,List<Destination>>();
        routes.forEach((source,destinations) -> copy.put(source,List.copyOf(destinations)));
        routes = Map.copyOf(copy);
    }

    public record EarningPolicy(String requiredPath, double defaultMultiplier) {
        public EarningPolicy {
            if (requiredPath == null || !requiredPath.isEmpty() && !validId(requiredPath))
                throw new IllegalArgumentException("Invalid XP earning Path");
            rate(defaultMultiplier);
        }
        public boolean eligible(Predicate<String> joinedAndEligible) {
            return requiredPath.isEmpty() || joinedAndEligible.test(requiredPath);
        }
    }

    public record Destination(String system, double multiplier) {
        public Destination {
            if (!validId(system)) throw new IllegalArgumentException("Invalid XP route system");
            rate(multiplier);
        }
    }

    /** A source candidate is a measurement from an eligible Path, not a second event. */
    public record Candidate(String fallback, String source, String completion,
                            java.math.BigDecimal nominal, boolean fixed) {}

    private record Group(String source,String fallback) {}

    /**
     * One configured source measurement per authenticated event and destination.
     * Explicit routing replaces fallback identity. Identical candidates collapse;
     * incompatible measurements or receipt semantics fail closed rather than
     * selecting by arbitrary Path iteration order. Independent categories remain additive.
     * Preserve source order so a fixed reward cannot jump ahead of the preceding
     * ordinary reward and unexpectedly change its growth calculation.
     */
    public List<Candidate> normalize(List<Candidate> candidates) {
        var groups=new java.util.LinkedHashMap<Group,Candidate>();
        var conflicts=new HashSet<Group>();
        for(var c:candidates){
            if(c.nominal()==null || c.nominal().signum()<=0)continue;
            String fallback=routes.containsKey(c.source())?"":c.fallback();
            var group=new Group(c.source(),fallback==null?"":fallback);
            var normalized=new Candidate(group.fallback(),c.source(),c.completion(),
                    c.nominal().stripTrailingZeros(),c.fixed());
            var existing=groups.putIfAbsent(group,normalized);
            if(existing!=null&&!existing.equals(normalized))conflicts.add(group);
        }
        conflicts.forEach(groups::remove);
        return List.copyOf(groups.values());
    }

    /** Observed settlement, including accepted zero rewards and first/repeat per currency. */
    public record Settlement(java.math.BigDecimal effectiveXp,boolean first) {}

    public static Map<String,Settlement> settled(List<Destination> destinations,String source,
            String transaction,String completion,
            io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy.Ledger before,
            io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy.Ledger after){
        var result=new java.util.LinkedHashMap<String,Settlement>();
        for(var destination:destinations){
            String id=destination.system(),prefix=id+"|"+source+"|";
            String tx=prefix+transaction;
            if(before.recentTransactions().contains(tx)||!after.recentTransactions().contains(tx))continue;
            boolean first=completion!=null&&!before.rewardReceipts().contains(prefix+completion)
                    &&after.rewardReceipts().contains(prefix+completion);
            var delta=after.currentXp().getOrDefault(id,java.math.BigDecimal.ZERO)
                    .subtract(before.currentXp().getOrDefault(id,java.math.BigDecimal.ZERO));
            result.put(id,new Settlement(delta,first));
        }
        return java.util.Collections.unmodifiableMap(result);
    }

    public static boolean alreadySettled(List<Destination> destinations,String source,
            String transaction,io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy.Ledger before){
        return !destinations.isEmpty()&&destinations.stream().allMatch(d->
                before.recentTransactions().contains(d.system()+"|"+source+"|"+transaction));
    }

    public static XpSourceRouting empty() { return new XpSourceRouting(Map.of(),Map.of()); }
    public EarningPolicy policy(String system) { return policies.getOrDefault(system,DEFAULT); }

    /**
     * Explicit route replaces the legacy caller's destination; never append it.
     * Empty explicit routes disable a source. Missing routes preserve existing
     * explicit grants; a source-only adapter without a fallback grants nothing.
     */
    public List<Destination> destinations(String source, String fallbackSystem) {
        if (!validId(source)) throw new IllegalArgumentException("Invalid XP source ID");
        if (routes.containsKey(source)) return routes.get(source);
        return fallbackSystem == null || fallbackSystem.isEmpty()
                ? List.of() : List.of(new Destination(fallbackSystem,1));
    }

    public static XpSourceRouting read(JsonObject root, Set<String> systems) {
        var policies = new HashMap<String,EarningPolicy>();
        for (var row : root.getAsJsonArray("systems")) {
            var j = row.getAsJsonObject();
            String system = id(j,"id");
            String path = j.has("earning_path") ? id(j,"earning_path") : "";
            double multiplier = number(j,"default_reward_multiplier",1);
            policies.put(system,new EarningPolicy(path,multiplier));
        }
        var routes = new HashMap<String,List<Destination>>();
        if (root.has("source_routes")) {
            var rows = root.getAsJsonArray("source_routes");
            if (rows.size() > MAX_ROUTES) throw new IllegalArgumentException("Too many XP source routes");
            for (var row : rows) {
                var j = row.getAsJsonObject();
                only(j,"source","destinations");
                String source = id(j,"source");
                var targets = j.getAsJsonArray("destinations");
                if (targets == null || targets.size() > MAX_DESTINATIONS)
                    throw new IllegalArgumentException("Invalid XP route destinations");
                var destinations = new ArrayList<Destination>();
                var seen = new HashSet<String>();
                for (var target : targets) {
                    var d = target.getAsJsonObject();
                    only(d,"system","multiplier");
                    String system = id(d,"system");
                    if (!systems.contains(system) || !seen.add(system))
                        throw new IllegalArgumentException("Unknown or duplicate XP route system " + system);
                    destinations.add(new Destination(system,number(d,"multiplier",1)));
                }
                if (routes.putIfAbsent(source,List.copyOf(destinations)) != null)
                    throw new IllegalArgumentException("Duplicate XP source route " + source);
            }
        }
        return new XpSourceRouting(policies,routes);
    }

    private static void only(JsonObject j,String... allowed) {
        if (!Set.of(allowed).containsAll(j.keySet()))
            throw new IllegalArgumentException("Unknown XP route field");
    }
    private static boolean validId(String id) {
        return id != null && id.length() <= 240 && id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+");
    }
    private static String id(JsonObject j,String key) {
        var value = j.get(key);
        if (value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString()
                || !validId(value.getAsString()))
            throw new IllegalArgumentException("Invalid XP route identifier " + key);
        return value.getAsString();
    }
    private static double number(JsonObject j,String key,double fallback) {
        if (!j.has(key)) return fallback;
        var value = j.get(key);
        if (!value.isJsonPrimitive() || !value.getAsJsonPrimitive().isNumber())
            throw new IllegalArgumentException("Invalid XP route number " + key);
        return rate(value.getAsDouble());
    }
    private static double rate(double value) {
        if (!Double.isFinite(value) || value < 0 || value > XpModifierMath.MAX_REWARD_MULTIPLIER)
            throw new IllegalArgumentException("Invalid XP route multiplier");
        return value;
    }
}
