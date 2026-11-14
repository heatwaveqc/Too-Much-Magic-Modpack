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
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import io.github.heatwaveqc.tmm.monk.progression.MonkMasteryRules;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Stores Vampirism-owned faction skills/actions and a small, separate native
 * Monk mastery ledger for the initial activity-based rank pilot.
 */
public final class MonkPlayer extends FactionBasePlayer<IMonkPlayer> implements IMonkPlayer {
    private final ActionHandler<IMonkPlayer> actions;
    private final SkillHandler<IMonkPlayer> skills;
    private int masteryXp;
    private final Set<ResourceLocation> discoveredFoods = new HashSet<>();

    /** Monastic mastery advances native faction ranks; never grants Tyz SP or MXP. */
    public int getMasteryXp() { return masteryXp; }
    public int getFoodDiscoveryCount() { return discoveredFoods.size(); }
    public boolean discoverFood(ResourceLocation foodId) {
        return discoveredFoods.add(foodId);
    }
    public void awardMastery(int amount) {
        masteryXp = MonkMasteryRules.safeAdd(masteryXp, amount);
    }

    public MonkPlayer(Player player) {
        super(player);
        actions = new ActionHandler<>(this);
        skills = new SkillHandler<>(this, MonkFaction.FACTION);
    }

    @Override public void onUpdatePlayer(PlayerTickEvent event) { }
    @Override public void onPlayerLoggedIn() { }
    @Override public void onPlayerLoggedOut() { }
    @Override public void onJoinWorld() {
        if (getLevel() > 0) actions.onActionsReactivated();
    }
    @Override public boolean onEntityAttacked(DamageSource source, float amount) { return false; }
    @Override public boolean canLeaveFaction() { return true; } // debug safety until departure ritual exists
    @Override public IFaction<?> getDisguisedAs() { return getFaction(); }
    @Override public boolean isDisguised() { return false; }
    @Override public int getMaxLevel() { return getFaction().getHighestReachableLevel(); }
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
        data.putInt("tmm_monk_mastery_xp", masteryXp);
        ListTag foods = new ListTag();
        discoveredFoods.stream().map(ResourceLocation::toString).sorted().forEach(id -> foods.add(StringTag.valueOf(id)));
        data.put("tmm_monk_discovered_foods", foods);
        return data;
    }
    @Override public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag data) {
        super.deserializeNBT(provider, data);
        actions.deserializeNBT(provider, data.getCompound(actions.nbtKey()));
        skills.deserializeNBT(provider, data.getCompound(skills.nbtKey()));
        masteryXp = Math.max(0, data.getInt("tmm_monk_mastery_xp"));
        discoveredFoods.clear();
        ListTag foods = data.getList("tmm_monk_discovered_foods", Tag.TAG_STRING);
        for (int i = 0; i < foods.size(); i++) {
            ResourceLocation id = ResourceLocation.tryParse(foods.getString(i));
            if (id != null) discoveredFoods.add(id);
        }
    }
    @Override public @NotNull CompoundTag serializeUpdateNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag data = super.serializeUpdateNBT(provider);
        data.put(actions.nbtKey(), actions.serializeUpdateNBT(provider));
        data.put(skills.nbtKey(), skills.serializeUpdateNBT(provider));
        data.putInt("tmm_monk_mastery_xp", masteryXp);
        return data;
    }
    @Override public void deserializeUpdateNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag data) {
        super.deserializeUpdateNBT(provider, data);
        actions.deserializeUpdateNBT(provider, data.getCompound(actions.nbtKey()));
        skills.deserializeUpdateNBT(provider, data.getCompound(skills.nbtKey()));
        if (data.contains("tmm_monk_mastery_xp", Tag.TAG_INT))
            masteryXp = Math.max(0, data.getInt("tmm_monk_mastery_xp"));
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
