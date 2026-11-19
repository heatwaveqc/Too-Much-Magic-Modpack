package io.github.heatwaveqc.tmm.economy.converter;

import com.mojang.logging.LogUtils;
import java.math.BigInteger;
import java.util.UUID;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import moze_intel.projecte.api.capabilities.PECapabilities;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;

/** Server-thread debit directly from the real online placing player's account.
 * No link capability, simulated withdrawal, chunk tickets or cached wallet. */
final class AccountRefill {
    private static final Logger LOG = LogUtils.getLogger();
    private AccountRefill() {}

    static void tick(ServerLevel level, UUID owner, int capacity, IntSupplier stored,
                     IntConsumer receive, int resourcePerBatch, int emcPerBatch) {
        if (owner == null || !level.getServer().isSameThread()) return;
        var player = level.getServer().getPlayerList().getPlayer(owner);
        if (player == null) return;
        var account = player.getCapability(PECapabilities.KNOWLEDGE_CAPABILITY);
        if (account == null) return;
        int before = stored.getAsInt();
        BigInteger balance = account.getEmc();
        var plan = TransferBudget.plan(before, capacity, balance, resourcePerBatch, emcPerBatch);
        if (plan.units() == 0) return;
        account.setEmc(balance.subtract(plan.cost()));
        // These adapters target native deterministic pool/jar methods. Charge
        // first; observe the actual delta; refund only definitively unused batches.
        // If a third-party modification throws after transferring, fail closed:
        // retain debit rather than risk a refunded-but-delivered transfer.
        try {
            receive.accept(plan.units());
            long accepted = (long) stored.getAsInt() - before;
            if (accepted != plan.units())
                LOG.warn("TMM EMC refill accepted {} of {} requested resource units", accepted, plan.units());
            var refund = plan.refundForAccepted(accepted);
            if (refund.signum() > 0) account.setEmc(account.getEmc().add(refund));
        } catch (RuntimeException ex) {
            LOG.error("TMM EMC refill transfer threw after debit; retaining debit to avoid duplication", ex);
        } finally {
            if (!balance.equals(account.getEmc())) account.syncEmc(player);
        }
    }
}
