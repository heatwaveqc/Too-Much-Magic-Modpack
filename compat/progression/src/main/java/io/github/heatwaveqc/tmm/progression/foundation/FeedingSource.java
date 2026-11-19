package io.github.heatwaveqc.tmm.progression.foundation;

/** Known native consumption callers; not generic blood increases or conversion/storage paths. */
public final class FeedingSource {
    private FeedingSource(){}
    private static final String JAWR="dev.sterner.witchery.content.";
    private static final String INTEROP="io.github.heatwaveqc.tmm.transformations.vampire.JAWRBottleInterop";
    public static boolean nativeDrink(String owner,String method){
        return (JAWR+"item.wine_glass.WineGlassItem").equals(owner)&&"consumeBlood".equals(method)
            ||(JAWR+"item.cane_sword.CaneSwordItem").equals(owner)&&"finishUsingItem".equals(method)
            ||(JAWR+"block.crucible.BloodCrucibleBlockEntity").equals(owner)&&"onUseWithItem".equals(method);
    }
    public static boolean interopCredit(String owner,String method){return INTEROP.equals(owner)&&"creditNativeBlood".equals(method);}
    public static boolean interopFinish(String owner,String method){return INTEROP.equals(owner)&&"onDrinkFinish".equals(method);}
}
