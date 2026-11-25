package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.brigadier.ParseResults;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.JawrRitualOutput;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandResultCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
@Pseudo
@Mixin(targets="dev.sterner.witchery.content.block.ritual.ritual_event.RitualCommandExecutor",remap=false)
public abstract class JawrRitualCommandMixin{
    @WrapOperation(method="execute(Ljava/lang/String;Ldev/sterner/witchery/content/block/ritual/ritual_event/RitualContext;)V",at=@At(value="INVOKE",target="Lnet/minecraft/commands/Commands;performCommand(Lcom/mojang/brigadier/ParseResults;Ljava/lang/String;)V"),require=0,remap=false)
    private void tmm$command(Commands commands,ParseResults<CommandSourceStack> parsed,String text,Operation<Void> original){
        var level=parsed.getContext().getSource().getLevel();
        boolean lightning=JawrRitualOutput.wantsLightningCommand(level,text);
        var teleport=JawrRitualOutput.teleportBefore(level,text);
        if(!lightning&&teleport==null){original.call(commands,parsed,text);return;}
        boolean[] success={false},failure={false},open={true};
        ParseResults<CommandSourceStack> mapped;
        try{
            CommandResultCallback witness=(ok,result)->{if(open[0]){if(ok&&result>0)success[0]=true;else failure[0]=true;}};
            mapped=Commands.mapSource(parsed,source->source.withCallback(witness,CommandResultCallback::chain));
        }catch(RuntimeException unavailable){original.call(commands,parsed,text);return;}
        try{original.call(commands,mapped,text);}finally{open[0]=false;}
        if(success[0]&&!failure[0]){
            if(lightning)JawrRitualOutput.lightningCompleted(level,text);
            JawrRitualOutput.teleportAfter(teleport,true);
        }
    }
}
