package io.github.heatwaveqc.tyr.progression.runtime;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/** Virtual vanilla tools are context-only stacks: never equipped, granted or persisted. */
public final class ToolForms {
    private ToolForms(){}
    private static final ThreadLocal<Boolean> USING=ThreadLocal.withInitial(()->false);
    static boolean usable(ServerPlayer p){
        return p.isAlive()&&!p.isCreative()&&!p.isSpectator()&&!NewbieMode.isNewbie(p)
            &&p.getAbilities().mayBuild&&p.getMainHandItem().isEmpty()
            &&p.getServer()!=null&&p.getServer().isSameThread()
            &&p.getServer().getPlayerList().getPlayer(p.getUUID())==p;
    }
    private static boolean allowed(ServerPlayer p,BlockPos pos){
        var level=p.serverLevel();
        return level.hasChunkAt(pos)&&!level.isOutsideBuildHeight(pos)
            &&level.getWorldBorder().isWithinBounds(pos)&&level.mayInteract(p,pos);
    }
    static ItemStack stack(String form){
        return switch(form){
            case "axe" -> new ItemStack(Items.IRON_AXE);
            case "shovel" -> new ItemStack(Items.IRON_SHOVEL);
            case "hoe" -> new ItemStack(Items.IRON_HOE);
            case "shears" -> new ItemStack(Items.SHEARS);
            default -> ItemStack.EMPTY;
        };
    }
    private static EffectRuntime.Active selected(ServerPlayer p){
        var forms=EffectRuntime.active(p).stream()
            .filter(a->a.effect().type().equals("tool_form")).toList();
        // A bad cross-path configuration must not execute multiple actions.
        return forms.size()==1?forms.getFirst():null;
    }
    static ItemStack shearTool(ServerPlayer p,BlockState state){
        if(!usable(p))return ItemStack.EMPTY;
        var active=selected(p);
        if(active==null||!active.effect().params().get("form").equals("shears")
            ||!Selectors.block(active.effect().selector(),state))return ItemStack.EMPTY;
        var tool=stack("shears");
        return tool.isCorrectToolForDrops(state)?tool:ItemStack.EMPTY;
    }
    private static final class VirtualContext extends UseOnContext {
        VirtualContext(ServerPlayer p,ItemStack tool,BlockHitResult hit){
            super(p.serverLevel(),p,InteractionHand.MAIN_HAND,tool,hit);
        }
    }
    public static void blockInteract(PlayerInteractEvent.RightClickBlock event){
        if(event.isCanceled()||event.getUseItem()==TriState.FALSE||event.getUseBlock()==TriState.FALSE
            ||event.getHand()!=InteractionHand.MAIN_HAND||!(event.getEntity() instanceof ServerPlayer p)
            ||!usable(p)||USING.get()||!allowed(p,event.getPos()))return;
        var active=selected(p);
        if(active==null||!Selectors.block(active.effect().selector(),p.serverLevel().getBlockState(event.getPos())))return;
        var tool=stack(active.effect().params().get("form"));
        if(tool.isEmpty())return;
        USING.set(true);
        try{
            // ItemStack.useOn posts native UseItemOnBlockEvent, invokes native
            // tool-modification rules and NeoForge's placement cancellation/rollback.
            var result=tool.useOn(new VirtualContext(p,tool,event.getHitVec()));
            if(result!=InteractionResult.PASS){
                event.setCanceled(true);
                event.setCancellationResult(result);
            }
        }finally{USING.remove();}
    }
    /** An eligible shears form takes precedence over barehand animal friendship. */
    public static boolean entityInteract(ServerPlayer p,PlayerInteractEvent.EntityInteract event){
        if(event.isCanceled()||event.getHand()!=InteractionHand.MAIN_HAND||!usable(p)||USING.get()
            ||!(event.getTarget() instanceof LivingEntity target)||!target.isAlive()
            ||target.level()!=p.serverLevel()||!allowed(p,target.blockPosition()))return false;
        var active=selected(p);
        if(active==null||!active.effect().params().get("form").equals("shears"))return false;
        var selector=active.effect().params().getOrDefault("entity_target","");
        if(selector.isEmpty()||!Selectors.entity(selector,target))return false;
        var tool=stack("shears");
        USING.set(true);
        try{
            var result=tool.getItem().interactLivingEntity(tool,p,target,InteractionHand.MAIN_HAND);
            if(result!=InteractionResult.PASS){
                event.setCanceled(true);
                event.setCancellationResult(result);
            }
            // Never breed an animal the player attempted to shear.
            return true;
        }finally{USING.remove();}
    }
}
