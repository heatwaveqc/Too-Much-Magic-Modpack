package io.github.heatwaveqc.tyr.progression.runtime.resources;

import java.math.BigDecimal;
import java.util.*;

/** Integer resource arithmetic; no XP, floating balances or implicit shared pools. */
public final class ResourcePolicy {
    private ResourcePolicy() {}
    public static final int MAX=1_000_000, MAX_STORED=1024, MAX_RECEIPTS=256;
    public enum Operation { ADD, SET, SPEND }
    public record Change(String id, int initial, int capacity, Operation operation, long amount) {
        public Change {
            if (!validId(id) || capacity<1 || capacity>MAX || initial<0 || initial>capacity
                    || operation==null || amount < -(long)MAX*MAX-MAX || amount>(long)MAX*MAX+MAX
                    || (operation!=Operation.ADD && amount<0))
                throw new IllegalArgumentException("Invalid resource mutation");
        }
    }
    public static boolean validId(String id) {
        return id!=null && id.length()<=160 && id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+");
    }
    public static int clamp(long amount,int capacity) {
        if(capacity<1||capacity>MAX)throw new IllegalArgumentException("Invalid capacity");
        return (int)Math.max(0,Math.min(capacity,amount));
    }
    /** All-or-nothing across resources. Insufficient spend never mutates input. */
    public static Optional<Map<String,Integer>> apply(Map<String,Integer> values,List<Change> changes) {
        var next=new HashMap<>(values);
        for(var change:changes) {
            int before=clamp(next.getOrDefault(change.id(),change.initial()),change.capacity());
            if(change.operation()==Operation.SPEND && change.amount()>before)return Optional.empty();
            long after=switch(change.operation()){
                case ADD -> before+change.amount();
                case SET -> change.amount();
                case SPEND -> before-change.amount();
            };
            next.put(change.id(),clamp(after,change.capacity()));
        }
        if(next.size()>MAX_STORED)return Optional.empty();
        return Optional.of(Map.copyOf(next));
    }
    public static boolean validDivisor(int divisor) {
        return Set.of(1,10,100,1000,10000,100000,1000000).contains(divisor);
    }
    public static String display(int value,int divisor) {
        if(!validDivisor(divisor)||value<0||value>MAX)throw new IllegalArgumentException("Invalid display");
        return BigDecimal.valueOf(value).divide(BigDecimal.valueOf(divisor)).stripTrailingZeros().toPlainString();
    }
}
