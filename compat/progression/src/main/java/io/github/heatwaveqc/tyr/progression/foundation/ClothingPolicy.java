package io.github.heatwaveqc.tyr.progression.foundation;

/** Pure, deliberately fail-closed clothing policy. Allowlisting is necessary but never
 * sufficient when a stack carries protection, enchantments or augmentation. */
public final class ClothingPolicy {
    private ClothingPolicy(){}
    public static boolean allows(boolean listed,boolean denied,boolean durability,
                                  boolean enchanted,boolean modifiedAttributes,boolean augmented){
        return listed&&!denied&&!durability&&!enchanted&&!modifiedAttributes&&!augmented;
    }
    public static boolean augmentationComponent(String id){
        return switch(id){
            case "apotheosis:affixes","apotheosis:rarity","apotheosis:affix_name",
                 "apotheosis:sockets","apotheosis:socketed_gems","apotheosis:durability_bonus",
                 "apotheosis:malice_marker","apotheosis:touched_by_malice" -> true;
            default -> false;
        };
    }
}
