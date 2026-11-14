package io.github.heatwaveqc.tmm.monk.faction;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IFaction;
import de.teamlapen.vampirism.api.entity.player.actions.IActionHandler;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.entity.player.FactionBasePlayer;
import de.teamlapen.vampirism.entity.player.actions.ActionHandler;
import de.teamlapen.vampirism.entity.player.skills.SkillHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.NotNull;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Stores Vampirism-owned skill/action progress, but does not implement
 * player initiation, eligibility, bonuses, penalties or resurrection.
 */
public final class MonkPlayer extends FactionBasePlayer<IMonkPlayer> implements IMonkPlayer {
    private final ActionHandler<IMonkPlayer> actions;
    private final SkillHandler<IMonkPlayer> skills;

    public MonkPlayer(Player player) {
        super(player);
        actions = new ActionHandler<>(this);
        skills = new SkillHandler<>(this, MonkFaction.FACTION);
    }

    @Override public boolean canLeaveFaction() { return true; } // debug safety until departure ritual exists
    @Override public IFaction<?> getDisguisedAs() { return getFaction(); }
    @Override public boolean isDisguised() { return false; }
    @Override public int getMaxLevel() { return MonkFaction.PROVISIONAL_MAX_LEVEL; }
    @Override public IActionHandler<IMonkPlayer> getActionHandler() { return actions; }
    @Override public @NotNull ISkillHandler<IMonkPlayer> getSkillHandler() { return skills; }
    @Override public Predicate<LivingEntity> getNonFriendlySelector(boolean otherFactionPlayers, boolean ignoreDisguise) {
        return VampirismAPI.factionRegistry().getPredicate(getFaction(), ignoreDisguise);
    }
    @Override public @NotNull ResourceLocation getAttachedKey() {
        return ResourceLocation.fromNamespaceAndPath("tmm_monk", "monk_player");
    }
    @Override public String nbtKey() { return getAttachedKey().getPath(); }
    @Override public void onChangedDimension(ResourceKey<Level> from, ResourceKey<Level> to) { }

    @Override public void onUpdate() {
        super.onUpdate();
        if (getLevel() > 0) {
            if (!isRemote()) {
                CompoundTag changes = new CompoundTag();
                boolean changed = actions.updateActions();
                boolean toAll = changed;
                if (changed) changes.put(actions.nbtKey(), actions.serializeUpdateNBT(player.registryAccess()));
                if (skills.isDirty()) {
                    changes.put(skills.nbtKey(), skills.serializeUpdateNBT(player.registryAccess()));
                    changed = true;
                }
                if (changed) sync(changes, toAll);
            } else {
                actions.updateActions();
            }
        }
    }
    @Override public @NotNull CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag data = super.serializeNBT(provider);
        data.put(actions.nbtKey(), actions.serializeNBT(provider));
        data.put(skills.nbtKey(), skills.serializeNBT(provider));
        return data;
    }
    @Override public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag data) {
        super.deserializeNBT(provider, data);
        actions.deserializeNBT(provider, data.getCompound(actions.nbtKey()));
        skills.deserializeNBT(provider, data.getCompound(skills.nbtKey()));
    }
    @Override public @NotNull CompoundTag serializeUpdateNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag data = super.serializeUpdateNBT(provider);
        data.put(actions.nbtKey(), actions.serializeUpdateNBT(provider));
        data.put(skills.nbtKey(), skills.serializeUpdateNBT(provider));
        return data;
    }
    @Override public void deserializeUpdateNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag data) {
        super.deserializeUpdateNBT(provider, data);
        actions.deserializeUpdateNBT(provider, data.getCompound(actions.nbtKey()));
        skills.deserializeUpdateNBT(provider, data.getCompound(skills.nbtKey()));
    }
    public static final class Factory implements Function<IAttachmentHolder, MonkPlayer> {
        @Override public MonkPlayer apply(IAttachmentHolder holder) {
            if (holder instanceof Player player) return new MonkPlayer(player);
            throw new IllegalArgumentException("Monk attachment requires Player, got " + holder.getClass());
        }
    }
    public static final class Serializer implements IAttachmentSerializer<CompoundTag, MonkPlayer> {
        @Override public @NotNull MonkPlayer read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag,
                                                  HolderLookup.@NotNull Provider provider) {
            MonkPlayer player = new Factory().apply(holder);
            player.deserializeNBT(provider, tag);
            return player;
        }
        @Override public CompoundTag write(MonkPlayer player, HolderLookup.@NotNull Provider provider) {
            return player.serializeNBT(provider);
        }
    }
}
