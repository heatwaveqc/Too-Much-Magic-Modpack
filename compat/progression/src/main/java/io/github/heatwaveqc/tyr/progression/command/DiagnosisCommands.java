package io.github.heatwaveqc.tyr.progression.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.admin.*;
import io.github.heatwaveqc.tyr.progression.runtime.NewbieMode;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;

final class DiagnosisCommands {
    private DiagnosisCommands(){}
    static LiteralArgumentBuilder<CommandSourceStack> diagnose(){
        return Commands.literal("diagnose").requires(s->s.hasPermission(2))
            .then(Commands.argument("player",EntityArgument.player()).executes(DiagnosisCommands::report));
    }
    static LiteralArgumentBuilder<CommandSourceStack> repair(){
        return Commands.literal("repair").requires(s->s.hasPermission(2))
            .then(Commands.argument("player",EntityArgument.player())
                .executes(ctx->{reply(ctx,"Preview: repair baseline/Core projection and refresh native observations and owned effects only; use confirm.");return report(ctx);})
                .then(Commands.literal("confirm").executes(ctx->{
                    var p=EntityArgument.getPlayer(ctx,"player");
                    if(p.getServer()==null||!p.getServer().isSameThread())return 0;
                    ProgressionMod.LOGGER.info("Admin derived repair by {} for {} ({})",ctx.getSource().getTextName(),p.getScoreboardName(),p.getUUID());
                    try{AdminDiagnosis.repairDerived(p);}
                    catch(RuntimeException ex){
                        ProgressionMod.LOGGER.error("Derived repair failed or partially applied for {}",p.getUUID(),ex);
                        ctx.getSource().sendFailure(Component.literal("Repair interrupted; inspect state before retrying."));return 0;
                    }
                    return report(ctx);
                })));
    }
    private static void reply(CommandContext<CommandSourceStack> ctx,String msg){ctx.getSource().sendSuccess(()->Component.literal(msg),false);}
    private static int report(CommandContext<CommandSourceStack> ctx)throws CommandSyntaxException{
        var p=EntityArgument.getPlayer(ctx,"player");
        var ledger=p.getData(ProgressionAttachments.LEDGER.get());
        var core=AdminDiagnosis.ledgerFindings(ledger,NewbieMode.isNewbie(p));
        var scan=AdminCapabilities.registry().diagnose(p,256,2048);
        reply(ctx,"Diagnosis "+p.getScoreboardName()+": "+core.size()+" ledger findings; "+scan.findings().size()
            +" capability findings (bounded), "+scan.checked()+" checked, "+scan.omitted()+" keys omitted by budget.");
        core.stream().limit(20).forEach(v->reply(ctx,v));
        scan.findings().stream().limit(30).forEach(f->reply(ctx,f.capability()+" "+f.key()+": "+f.detail()
            +"; inspect/validate using /tyrprogress admin capability "+p.getScoreboardName()+" "+f.capability()));
        if(core.size()>20||scan.findings().size()>30||scan.omitted()>0)reply(ctx,"Report bounded; inspect individual capabilities for remaining detail.");
        if(core.isEmpty()&&scan.findings().isEmpty()&&scan.omitted()==0)reply(ctx,"No registered validation findings (not proof of gameplay correctness).");
        return 1;
    }
}
