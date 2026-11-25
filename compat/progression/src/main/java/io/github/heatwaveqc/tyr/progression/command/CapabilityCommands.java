package io.github.heatwaveqc.tyr.progression.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.admin.AdminCapabilities;
import io.github.heatwaveqc.tyr.progression.admin.AdminCapabilityRegistry;
import io.github.heatwaveqc.tyr.progression.admin.AdminStateRefresh;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import java.util.Locale;

/** One command tree for all registered capabilities. No mechanic-specific dispatch. */
final class CapabilityCommands {
    private CapabilityCommands(){}
    private static final int LIST_LIMIT=50;
    private static final int SUGGEST_LIMIT=100;

    private static AdminCapabilityRegistry<ServerPlayer> registry(){return AdminCapabilities.registry();}
    private static String id(CommandContext<CommandSourceStack> ctx){return ResourceLocationArgument.getId(ctx,"capability").toString();}
    private static ServerPlayer target(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        return EntityArgument.getPlayer(ctx,"player");
    }
    private static String key(CommandContext<CommandSourceStack> ctx){return StringArgumentType.getString(ctx,"key");}
    private static void reply(CommandContext<CommandSourceStack> ctx,String msg){
        ctx.getSource().sendSuccess(()->Component.literal(msg),false);
    }
    private static int fail(CommandContext<CommandSourceStack> ctx,RuntimeException ex){
        ctx.getSource().sendFailure(Component.literal("Capability operation rejected: "+ex.getMessage()));
        return 0;
    }
    private static RequiredArgumentBuilder<CommandSourceStack,String> keyArgument(){
        return Commands.argument("key",StringArgumentType.greedyString()).suggests((ctx,builder)->{
            try{
                String prefix=builder.getRemainingLowerCase();
                return SharedSuggestionProvider.suggest(registry().keys(id(ctx),target(ctx)).stream()
                    .filter(k->k.toLowerCase(Locale.ROOT).startsWith(prefix)).limit(SUGGEST_LIMIT).toList(),builder);
            }catch(CommandSyntaxException | IllegalArgumentException | IllegalStateException ex){return builder.buildFuture();}
        });
    }
    static LiteralArgumentBuilder<CommandSourceStack> root(){
        var capability=Commands.argument("capability",ResourceLocationArgument.id())
            .suggests((ctx,builder)->SharedSuggestionProvider.suggest(registry().ids(),builder))
            .then(Commands.literal("keys").executes(CapabilityCommands::keys))
            .then(Commands.literal("inspect").then(keyArgument().executes(ctx->read(ctx,false))))
            .then(Commands.literal("validate").then(keyArgument().executes(ctx->read(ctx,true))));
        for(var action:AdminCapabilityRegistry.Action.values()){
            capability.then(Commands.literal(action.name().toLowerCase(Locale.ROOT))
                .then(Commands.literal("preview").then(keyArgument().executes(ctx->change(ctx,action,false))))
                .then(Commands.literal("confirm").then(keyArgument().executes(ctx->change(ctx,action,true)))));
        }
        return Commands.literal("capability").requires(s->s.hasPermission(2))
            .then(Commands.argument("player",EntityArgument.player())
                .then(Commands.literal("list").executes(ctx->{
                    for(String id:registry().ids()){
                        var handler=registry().require(id);
                        reply(ctx,id+" "+handler.actions()+": "+handler.description());
                    }
                    return 1;
                })).then(capability));
    }
    private static int keys(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        try{
            var list=registry().keys(id(ctx),target(ctx));
            reply(ctx,id(ctx)+": "+list.size()+" keys; showing up to "+LIST_LIMIT);
            list.stream().limit(LIST_LIMIT).forEach(k->reply(ctx,k));
            if(list.size()>LIST_LIMIT)reply(ctx,"Use key autocomplete to narrow the remaining entries.");
            return 1;
        }catch(IllegalArgumentException | IllegalStateException ex){return fail(ctx,ex);}
    }
    private static int read(CommandContext<CommandSourceStack> ctx,boolean validate) throws CommandSyntaxException {
        try{
            var p=target(ctx);
            if(validate){
                var issues=registry().validate(id(ctx),p,key(ctx));
                reply(ctx,issues.isEmpty()?"No registered validation findings":String.join("; ",issues));
            }else reply(ctx,registry().inspect(id(ctx),p,key(ctx)));
            return 1;
        }catch(IllegalArgumentException | IllegalStateException ex){return fail(ctx,ex);}
    }
    private static int change(CommandContext<CommandSourceStack> ctx,AdminCapabilityRegistry.Action action,boolean confirm)
            throws CommandSyntaxException {
        var p=target(ctx);
        try{
            if(p.getServer()==null || !p.getServer().isSameThread())
                throw new IllegalStateException("Administrative changes require the server thread");
            // Re-plan on confirmation against current state; never execute a cached preview.
            var plan=registry().plan(id(ctx),p,key(ctx),action);
            String detail=id(ctx)+" "+key(ctx)+": "+plan.before()+" -> "+plan.after();
            if(!confirm){reply(ctx,"Preview only; use confirm: "+detail);return 1;}
            plan.commit().run();
            ProgressionMod.LOGGER.info("Admin capability {} by {} for {} ({}): {}",action,
                ctx.getSource().getTextName(),p.getScoreboardName(),p.getUUID(),detail);
            if(action==AdminCapabilityRegistry.Action.RESET){
                try{AdminStateRefresh.reconcile(p);}
                catch(RuntimeException ex){
                    ProgressionMod.LOGGER.error("Admin reset committed but derived refresh failed for {}",p.getUUID(),ex);
                    ctx.getSource().sendFailure(Component.literal("Reset committed; derived refresh failed. Inspect before retrying."));
                    return 0;
                }
            }
            reply(ctx,detail);
            return 1;
        }catch(IllegalArgumentException | IllegalStateException ex){return fail(ctx,ex);}
    }
}
