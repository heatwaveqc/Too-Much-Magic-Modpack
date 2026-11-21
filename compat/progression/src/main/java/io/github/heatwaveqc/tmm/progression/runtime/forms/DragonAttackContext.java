package io.github.heatwaveqc.tmm.progression.runtime.forms;

import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpReflect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import static io.github.heatwaveqc.tmm.progression.foundation.Condition.Result.PASS;

/** Read only exact 2.0.74 native claw state; never swap tools, invent harvest permission or change durability. */
public final class DragonAttackContext {
    private static final String CLAWS="by.dragonsurvivalteam.dragonsurvival.registry.attachments.ClawInventoryData";
    private static final String STATE="by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider";
    private DragonAttackContext(){}
    private static Object claws(ServerPlayer p){
        return ProviderFlags.nativeDragon(p)==PASS?MxpReflect.staticCall(CLAWS,"getData",p):null;
    }
    private static String slot(Object data){
        Object value=MxpReflect.field(data,"switchedToolSlot");
        if(!(value instanceof Integer i)||i<0)return "";
        try {
            Object[] slots=Class.forName(CLAWS+"$Slot").getEnumConstants();
            return slots!=null&&i<slots.length&&slots[i] instanceof Enum<?> e?e.name():"";
        }catch(ClassNotFoundException|LinkageError|RuntimeException e){return "";}
    }
    public static boolean emptyClaw(ServerPlayer p){
        Object data=claws(p);
        if(!Boolean.FALSE.equals(MxpReflect.field(data,"switchedTool"))||!p.getMainHandItem().isEmpty())return false;
        return MxpReflect.call(data,"getSword") instanceof ItemStack sword&&sword.isEmpty();
    }
    public static boolean clawSword(ServerPlayer p){
        Object data=claws(p);
        return Boolean.TRUE.equals(MxpReflect.field(data,"switchedTool"))&&
            "SWORD".equals(slot(data))&&!p.getMainHandItem().isEmpty()&&
            MxpReflect.field(data,"storedMainHandTool") instanceof ItemStack;
    }
    public static boolean legalClawMining(ServerPlayer p,BlockState block){
        Object data=claws(p);
        Object switched=MxpReflect.field(data,"switchedTool");
        if(!(switched instanceof Boolean s))return false;
        // A held ordinary tool is not a claw; allow the native selected slot during a real swap.
        if(s){
            if(slot(data).isEmpty()||p.getMainHandItem().isEmpty()||
                !(MxpReflect.field(data,"storedMainHandTool") instanceof ItemStack hand)||!hand.isEmpty())return false;
        }else if(!p.getMainHandItem().isEmpty())return false;
        Object state=MxpReflect.staticCall(STATE,"getData",p);
        Object permitted=MxpReflect.call(state,"canHarvestWithPaw",p,block);
        return permitted instanceof Boolean b&&(b||p.hasCorrectToolForDrops(block));
    }
}
