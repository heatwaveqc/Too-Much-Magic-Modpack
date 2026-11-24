package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy;
import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.core.XpModifierMath;
import io.github.heatwaveqc.tyr.progression.core.XpModifierMath.Kind;
import io.github.heatwaveqc.tyr.progression.core.XpModifierMath.Modifier;
import io.github.heatwaveqc.tyr.progression.core.XpModifierMath.Operation;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.data.XpModifierConfig.Rule;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.server.level.ServerPlayer;

/** Read-only server eligibility and a single settlement adapter for trusted sources.
 * Callers retain event authentication, Newbie/creative veto, transaction selection,
 * and attachment publication. Never accept client-provided awards. */
public final class ExperienceModifiers {
    private ExperienceModifiers() {}

    public static double growth(ServerPlayer player, double configured, String system, String source) {
        return resolve(player, Kind.GROWTH, configured, system, source, false).value();
    }

    public static XpModifierMath.Resolution resolve(ServerPlayer player, Kind kind, double base,
                                                     String system, String source, boolean fixed) {
        var rules = PathRegistry.economyCatalog().modifiers();
        var active = EffectRuntime.active(player);
        var candidates = new ArrayList<Modifier>();
        // Historic growth effects remain compatible; aggregate only within the
        // same owning Path, not implicitly across compatible Paths.
        if (kind == Kind.GROWTH && !fixed) legacyGrowth(active, system, candidates);
        var ledger = player.getData(ProgressionAttachments.LEDGER.get());
        var activeIds = new HashSet<String>();
        for (var effect : active) activeIds.add(effect.id());
        for (Rule rule : rules) {
            if (rule.kind() != kind || !rule.targets(system, source, fixed)) continue;
            if (!rule.achievement().isEmpty()
                    && !ledger.economy().achievements().contains(rule.achievement())) continue;
            int rank = 1;
            if (!rule.path().isEmpty()) {
                var path = PathRegistry.get(rule.path());
                if (path == null || !PathRegistry.eligible(player, rule.path())) continue;
                var track = ProgressionApi.track(player, rule.path());
                if (!track.joined()) continue;
                if (!rule.skill().isEmpty()) {
                    if (path.skill(rule.skill()) == null
                            || !PathRegistry.skillEligible(player, rule.path(), rule.skill())) continue;
                    rank = track.skillRank(rule.skill());
                    if (rank < rule.minRank()) continue;
                    if (rule.effectIndex() != null && !activeIds.contains(
                            effectId(rule.path(), rule.skill(), rule.effectIndex()))) continue;
                }
            }
            candidates.add(rule.contribution(rank));
        }
        return XpModifierMath.resolve(kind, base, candidates);
    }

    private static String effectId(String path, String skill, int index) {
        return path.replace(':','/') + "/" + skill.replace(':','/') + "/" + index;
    }

    private static void legacyGrowth(List<EffectRuntime.Active> active, String system,
                                     List<Modifier> candidates) {
        // Match exact IDs from the definitions rather than guessing a Path
        // from a slash-delimited string. Ambiguous ownership fails closed.
        Map<String,String> owners = new HashMap<>();
        var ambiguous = new HashSet<String>();
        for (var path : PathRegistry.all().values())
            for (var skill : path.skills())
                for (int i = 0; i < skill.effects().size(); i++) {
                    if (!skill.effects().get(i).type().equals("xp_growth_reduction")) continue;
                    String id = effectId(path.id(), skill.id(), i);
                    String previous = owners.putIfAbsent(id, path.id());
                    if (previous != null && !previous.equals(path.id())) ambiguous.add(id);
                }
        Map<String,Double> reductions = new HashMap<>();
        for (var a : active) {
            if (!a.effect().type().equals("xp_growth_reduction") || ambiguous.contains(a.id())) continue;
            String target = a.effect().params().getOrDefault("xp_system", "");
            if (!target.isEmpty() && !target.equals(system) && !target.equals("*")) continue;
            String owner = owners.get(a.id());
            if (owner == null) continue;
            double amount = a.effect().amountPerRank() * a.rank();
            if (Double.isFinite(amount) && amount >= 0)
                reductions.merge(owner, amount, Double::sum);
        }
        for (var row : reductions.entrySet())
            candidates.add(new Modifier("legacy:" + row.getKey(), 0, false, Operation.ADD,
                    -Math.min(XpModifierMath.MAX_REWARD_MULTIPLIER, row.getValue()), null, null));
    }

    /**
     * Apply reward multiplier then growth, then the ledger's cap/minimum policy.
     * The caller must choose first vs repeat and verify source before calling.
     * This is deliberately not a second award path: it delegates to the one
     * immutable ledger and returns its ordinary Award result.
     */
    public static ExperienceEconomy.Award earn(ServerPlayer player, Ledger ledger,
            ExperienceEconomy.SystemSpec system, String source, String transaction,
            String firstCompletion, BigDecimal nominal, boolean fixed, double configuredGrowth) {
        double multiplier = resolve(player, Kind.REWARD, 1, system.id(), source, fixed).value();
        double growth = fixed ? configuredGrowth : growth(player, configuredGrowth, system.id(), source);
        return ExperienceEconomy.earnModified(ledger.economy(), system, source, transaction,
                firstCompletion, nominal, fixed, growth, multiplier);
    }
}
