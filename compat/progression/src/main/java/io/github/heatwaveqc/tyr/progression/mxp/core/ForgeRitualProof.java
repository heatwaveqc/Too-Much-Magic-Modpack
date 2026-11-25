package io.github.heatwaveqc.tyr.progression.mxp.core;

/** Native 2.6.1 forge results: output item identities or an actual one-tier upgrade. */
public final class ForgeRitualProof {
    private ForgeRitualProof(){}
    public static int requiredTier(RitualRewards.Rule rule){
        if(rule==null)return -1;
        return switch(rule.band()){
            case FORGE_1 -> 1;
            case FORGE_2 -> 2;
            case FORGE_3 -> 3;
            case FORGE_4 -> 4;
            case FORGE_5 -> 5;
            default -> -1;
        };
    }
    public static boolean matchesTier(String id,int required){
        return required>=1&&requiredTier(RitualRewards.lookup(id))==required;
    }
    public static int upgradeTarget(String id){
        if(id==null||!id.matches("forbidden_arcanus:upgrade_tier_[2-5]"))return -1;
        return id.charAt(id.length()-1)-'0';
    }
    public static boolean actualUpgrade(String id,int before,int after){
        int target=upgradeTarget(id);
        return target>=2&&before==target-1&&after==target;
    }
    public static boolean itemResult(String id,String result,int count){
        return upgradeTarget(id)<0&&requiredTier(RitualRewards.lookup(id))>0&&id.equals(result)&&count>0;
    }
}
