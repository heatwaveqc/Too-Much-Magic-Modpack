package io.github.heatwaveqc.tmm.progression.mxp;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import io.github.heatwaveqc.tmm.progression.state.Wallet;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public final class MxpMod {
    public static final String ID = "tmm_progression";
    public static boolean active(){return !net.neoforged.fml.ModList.get().isLoaded("tmm_mxp");}
    public static final Logger LOGGER = LoggerFactory.getLogger(MxpMod.class);

    public MxpMod(IEventBus bus, ModContainer container) {
        if(!active())return;
        container.registerConfig(ModConfig.Type.COMMON, MxpSourceConfig.SPEC);
        container.registerConfig(ModConfig.Type.SERVER, MxpProgressionConfig.SPEC);
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        NeoForge.EVENT_BUS.addListener(net.neoforged.bus.api.EventPriority.LOWEST,BossRewards::onDamage);
        NeoForge.EVENT_BUS.addListener(net.neoforged.bus.api.EventPriority.LOWEST,BossRewards::onDeath);
        NeoForge.EVENT_BUS.addListener(this::onServerStopping);
        NeoForge.EVENT_BUS.addListener(this::onTickEnd);
        LOGGER.info("TMM MXP initialized (registry-projected boss/miniboss rewards)");
    }

    private static void clearPaymentScopes() {
        io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpCastHooks.clear();
        io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpPortableManaHook.clear();
        io.github.heatwaveqc.tmm.progression.mxp.hooks.PsiCastHooks.clear();
    }

    private void onTickEnd(net.neoforged.neoforge.event.tick.ServerTickEvent.Post event) { BossRewards.finishTick(); clearPaymentScopes(); }

    private void onServerStopping(ServerStoppingEvent event) {
        clearPaymentScopes();
        BossRewards.clear();
        MxpService.clearBossRules();
    }

    private void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("tmmmxp")
            .then(Commands.literal("status").executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                Wallet account = MxpService.inspect(player);
                ctx.getSource().sendSuccess(() -> Component.literal(String.format(
                    "MXP lifetime levels: %d | progress: %.4f/1000 | "
                    + "unspent points: %d | growth factor: %.4f | first completions: %d",
                    account.magicLevels(), account.magicProgress(),
                    account.points(), MxpProgressionConfig.GROWTH_FACTOR.get(),
                    account.firstCompletions().size())), false);
                return 1;
            }))
            .then(Commands.literal("test").requires(src -> src.hasPermission(2))
                .then(Commands.literal("fixed")
                    .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0.01, 100_000))
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            double amount = DoubleArgumentType.getDouble(ctx, "amount");
                            // New synthetic event ID guarantees the diagnostic is a
                            // FIRST ritual, hence undivided. Not a survival source.
                            String unique = UUID.randomUUID().toString();
                            MxpService.AwardStatus award = MxpService.ritual(player,
                                "tmm_mxp:diagnostic/" + unique,
                                "diagnostic:" + unique, amount, amount);
                            output(ctx.getSource(), award);
                            return award.accepted() ? 1 : 0;
                        })))
                .then(Commands.literal("ordinary")
                    .then(Commands.argument("base", DoubleArgumentType.doubleArg(0.01, 100_000))
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            double amount = DoubleArgumentType.getDouble(ctx, "base");
                            MxpService.AwardStatus award = MxpService.resource(player,
                                "tmm_mxp:diagnostic", "diagnostic:" + UUID.randomUUID(),
                                amount, 1000, 1);
                            output(ctx.getSource(), award);
                            return award.accepted() ? 1 : 0;
                        })))));
    }

    private static void output(net.minecraft.commands.CommandSourceStack source,
                               MxpService.AwardStatus award) {
        source.sendSuccess(() -> Component.literal(String.format(
            "MXP %s: effective %.4f, lifetime P=%d, progress %.4f (%s)",
            award.accepted() ? "credited" : "rejected", award.effectiveXP(),
            award.lifetimeLevels(), award.progress(), award.note())), false);
    }
}
