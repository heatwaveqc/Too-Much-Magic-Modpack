package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.foundation.FeedingSource;
import io.github.heatwaveqc.tyr.progression.foundation.BloodSkillMath;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class FeedingSourceTest{
 @Test void nativeConsumptionNotTransfer(){
    assertTrue(FeedingSource.nativeDrink("dev.sterner.witchery.content.item.wine_glass.WineGlassItem","consumeBlood"));
    assertTrue(FeedingSource.nativeDrink("dev.sterner.witchery.content.item.cane_sword.CaneSwordItem","finishUsingItem"));
    assertTrue(FeedingSource.nativeDrink("dev.sterner.witchery.content.block.crucible.BloodCrucibleBlockEntity","onUseWithItem"));
    assertFalse(FeedingSource.nativeDrink("dev.sterner.witchery.content.item.wine_glass.WineGlassItem","useOn"));
    assertFalse(FeedingSource.nativeDrink("dev.sterner.witchery.integration.neovitae.NeoVitaeCompat","interactWithBloodOrb"));
    assertFalse(FeedingSource.nativeDrink("dev.sterner.witchery.feature.affliction.vampire.VampireEvents","vampireTick"));
    assertFalse(FeedingSource.nativeDrink("dev.sterner.witchery.feature.blood_pool.BloodPoolHandler","tickBloodRegen"));
 }
 @Test void bridgeSpecificallyRequiresCompletedDrink(){
    String bridge="io.github.heatwaveqc.tmm.transformations.vampire.JAWRBottleInterop";
    assertTrue(FeedingSource.interopCredit(bridge,"creditNativeBlood"));
    assertTrue(FeedingSource.interopFinish(bridge,"onDrinkFinish"));
    assertFalse(FeedingSource.interopFinish(bridge,"onRightClickBlock"));
 }
 @Test void ordinaryBottleBonusAndCapacity(){
    assertEquals(100,BloodSkillMath.feedingBonus(500,900,.2));
    assertEquals(25,BloodSkillMath.feedingBonus(125,25,.2));
    assertEquals(0,BloodSkillMath.feedingBonus(500,0,.2));
 }
}
