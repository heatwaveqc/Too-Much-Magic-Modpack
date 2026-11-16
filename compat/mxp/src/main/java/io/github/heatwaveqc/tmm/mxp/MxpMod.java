package io.github.heatwaveqc.tmm.mxp;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import io.github.heatwaveqc.tmm.mxp.core.MxpAccount;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Mod(MxpMod.ID)
public final class MxpMod {
    public static final String ID = "tmm_mxp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MxpMod.class);

    public MxpMod(IEventBus bus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, MxpSourceConfig.SPEC);
        container.registerConfig(ModConfig.Type.SERVER, MxpProgressionConfig.SPEC);
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        NeoForge.EVENT_BUS.addListener(this::onBossDeath);
        NeoForge.EVENT_BUS.addListener(this::onServerStopping);
        LOGGER.info("TMM MXP prototype initialized (boss registry empty by default)");
    }

    private void onServerStopping(ServerStoppingEvent event) {
        MxpService.clearBossRules();
    }

    private void onBossDeath(LivingDeathEvent event) {
        // MVP: only the verified FINAL-BLOW server player. Pet owners and
        // cooperative damage participation require a later attribution bridge.
        if (!(event.getSource().getEntity() instanceof ServerPlayer killer)) return;
        if (killer.level().isClientSide) return;
        ResourceLocation type = BuiltInRegistries.ENTITY_TYPE.getKey(event.getEntity().getType());
        if (type == null) return;
        String bossId = type.toString();
        if (MxpService.bossRule(bossId) == null) return;
        String transaction = "boss:" + event.getEntity().getUUID();
        try {
            MxpService.AwardStatus receipt = MxpService.boss(killer, bossId, transaction);
            if (receipt.accepted()) {
                killer.sendSystemMessage(Component.literal(
                    "MXP: " + String.format("%.2f", receipt.effectiveXP())
                    + (receipt.first() ? " (first victory)" : " (repeat victory)")));
            }
        } catch (RuntimeException e) {
            LOGGER.error("MXP boss award failed ({} / {}): {}",
                    bossId, transaction, e.toString(), e);
        }
    }

    private void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("tmmmxp")
            .then(Commands.literal("status").executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                MxpAccount account = MxpService.inspect(player);
                ctx.getSource().sendSuccess(() -> Component.literal(String.format(
                    "MXP lifetime levels: %d | progress: %.4f/1000 | "
                    + "pending Tyz delivery: %.6f | growth factor: %.4f | first rituals: %d | first bosses: %d",
                    account.counter().lifetimeLevels(), account.counter().progress(),
                    account.pendingDelivery(), MxpProgressionConfig.GROWTH_FACTOR.get(),
                    account.firstRituals().size(),
                    account.firstBosses().size())), false);
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
            "MXP %s: effective %.4f, Tyz delivered %.3f, lifetime P=%d, progress %.4f, pending %.4f (%s)",
            award.accepted() ? "credited" : "rejected", award.effectiveXP(),
            award.deliveredToTyz(), award.lifetimeLevels(), award.progress(),
            award.pendingXP(), award.note())), false);
    }
}
