package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MahouResourceSkills;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
@Pseudo
@Mixin(targets="stepsword.mahoutsukai.commands.AddMana",remap=false)
public abstract class MahouAddManaCapacityMixin {
    @WrapOperation(method="run(Lcom/mojang/brigadier/context/CommandContext;IFI)I",at=@At(value="INVOKE",target="Lstepsword/mahoutsukai/dataattachments/mahou/IMahou;getMaxMana()I"),require=0,remap=false)
    private static int tyr$cap(@Coerce Object holder,Operation<Integer> original,CommandContext<CommandSourceStack> context,int amount,float overDrainHurts,int damageType){
        int earned=original.call(holder);
        try{return MahouResourceSkills.capacity(EntityArgument.getPlayer(context,"player"),earned);}
        catch(CommandSyntaxException invalid){return earned;}
    }
}
