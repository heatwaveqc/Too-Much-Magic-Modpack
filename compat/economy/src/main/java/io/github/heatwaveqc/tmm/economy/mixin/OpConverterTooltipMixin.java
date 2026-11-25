package io.github.heatwaveqc.tmm.economy.mixin;

import dev.glorps.energy.ConverterBlock;
import io.github.heatwaveqc.tmm.economy.converter.OpExchange;
import java.util.List;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** First two List.add invocations in exact 0.1.4 appendHoverText are the
 * exchange ratio and tier throughput, respectively. Keep all other lines. */
@Pseudo
@Mixin(targets = "dev.glorps.energy.ConverterItem", remap = false)
public abstract class OpConverterTooltipMixin {
    @Redirect(method = "appendHoverText", at = @At(value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    private boolean tmm$ratio(List<Component> tooltip, Object original) {
        ConverterBlock block = (ConverterBlock)((BlockItem)(Object)this).getBlock();
        String line = block.toEnergy ? "1 EMC → " + OpExchange.OP_PER_EMC_OUT + " OP"
                : OpExchange.OP_PER_EMC_IN + " OP → 1 EMC";
        return tooltip.add(Component.literal(line).withStyle(ChatFormatting.GRAY));
    }

    @Redirect(method = "appendHoverText", at = @At(value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
    private boolean tmm$rate(List<Component> tooltip, Object original) {
        ConverterBlock block = (ConverterBlock)((BlockItem)(Object)this).getBlock();
        return tooltip.add(Component.literal(String.format(Locale.ROOT, "%,d EMC/t · %,d OP/t",
                block.tier.emcRate(), block.tier.energyRate(block.toEnergy))).withStyle(ChatFormatting.AQUA));
    }
}
