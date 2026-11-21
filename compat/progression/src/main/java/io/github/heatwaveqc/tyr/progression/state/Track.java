package io.github.heatwaveqc.tyr.progression.state;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Server-owned, persistent per-path player state. */
public record Track(boolean joined, long xp, int rank, int points,
                    Map<String, Integer> skills, List<String> milestones,
                    Map<String, Integer> counters) {
    public Track {
        xp = Math.max(0, xp);
        rank = Math.max(0, rank);
        points = Math.max(0, points);
        skills = Map.copyOf(skills);
        milestones = List.copyOf(milestones);
        counters = Map.copyOf(counters);
    }
    public static Track empty() {
        return new Track(false, 0, 0, 0, Map.of(), List.of(), Map.of());
    }
    public int skillRank(String id) { return skills.getOrDefault(id, 0); }
    public int criterionCount(String key) { return counters.getOrDefault(key, 0); }
    public boolean completed(String id) { return milestones.contains(id); }
    public Track joinedAtRankOne(int startingPoints) {
        return new Track(true, xp, 1, startingPoints, skills, milestones, counters);
    }
    public Track progress(long nextXp, int nextRank, int nextPoints) {
        return new Track(joined, nextXp, nextRank, nextPoints, skills, milestones, counters);
    }
    public Track withSkill(String id, int newRank, int nextPoints) {
        Map<String,Integer> next = new HashMap<>(skills);
        if (newRank == 0) next.remove(id); else next.put(id, newRank);
        return new Track(joined, xp, rank, nextPoints, next, milestones, counters);
    }
    public Track withCounter(String key, int nextValue) {
        Map<String,Integer> next = new HashMap<>(counters);
        next.put(key, nextValue);
        return new Track(joined, xp, rank, points, skills, milestones, next);
    }
    public Track complete(String id) {
        if (completed(id)) return this;
        java.util.ArrayList<String> next = new java.util.ArrayList<>(milestones);
        next.add(id);
        return new Track(joined, xp, rank, points, skills, next, counters);
    }
    public Track grantPoints(int count) {
        return new Track(joined, xp, rank, (int) Math.min(Integer.MAX_VALUE, (long) points + count),
                         skills, milestones, counters);
    }
}
