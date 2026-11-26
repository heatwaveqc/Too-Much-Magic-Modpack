package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.MiningPredictionRules;
import io.github.heatwaveqc.tyr.progression.core.MiningPredictionRules.Rule;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/** Client prediction only. Server break, loot and XP authorization remain unchanged. */
public final class MiningPrediction {
    private MiningPrediction(){}
    public static String encode(ServerPlayer player){
        if(!player.isAlive()||player.isCreative()||player.isSpectator()||NewbieMode.isNewbie(player))return "";
        var rules=new ArrayList<Rule>();
        try{
            for(var a:EffectRuntime.active(player)){
                var fx=a.effect();
                switch(fx.type()){
                    case "break_speed" -> {
                        // Provider-specific form predicates are not represented by this projection.
                        if(!fx.params().containsKey("native_mining"))
                            rules.add(new Rule("speed",fx.selector(),"",fx.amountPerRank()*a.rank(),fx.emptyHand(),fx.unarmored()));
                    }
                    case "native_harvest" -> {
                        if(ToolForms.usable(player))rules.add(new Rule("native",fx.selector(),fx.params().get("tool"),0,true,fx.unarmored()));
                    }
                    case "harvest_tier" -> rules.add(new Rule("tier",fx.selector(),"",Math.min(fx.tier(),a.rank()),fx.emptyHand(),fx.unarmored()));
                }
            }
            return MiningPredictionRules.encode(rules);
        }catch(IllegalArgumentException invalid){
            // Fail closed only for the prediction; the authoritative effects still work.
            return "";
        }
    }
    private static boolean unarmored(Player player){
        for(ItemStack stack:player.getArmorSlots())if(!stack.isEmpty())return false;
        return true;
    }
    public static void breakSpeed(PlayerEvent.BreakSpeed event){
        Player player=event.getEntity();
        if(!player.level().isClientSide())return;
        List<Rule> rules=ClientPowerSnapshot.mining(player);
        double scale=MiningPredictionRules.scale(rules,s->Selectors.block(s,event.getState()),
            player.getMainHandItem().isEmpty(),unarmored(player));
        if(scale!=1)event.setNewSpeed((float)Math.min(100000,Math.max(0,event.getNewSpeed()*scale)));
    }
    public static boolean correctTool(Player player,BlockState state){
        if(!player.level().isClientSide())return false;
        boolean empty=player.getMainHandItem().isEmpty(),bare=unarmored(player);
        for(var rule:ClientPowerSnapshot.mining(player)){
            if(!rule.eligible(empty,bare)||!Selectors.block(rule.selector(),state))continue;
            if(rule.kind().equals("native")&&player.getAbilities().mayBuild&&NativeHarvest.correct(rule.tool(),state))return true;
            if(rule.kind().equals("tier")&&rule.amount()>=EffectRuntime.requiredTier(state))return true;
        }
        return false;
    }
    public static void harvest(PlayerEvent.HarvestCheck event){
        if(correctTool(event.getEntity(),event.getTargetBlock()))event.setCanHarvest(true);
    }
}
