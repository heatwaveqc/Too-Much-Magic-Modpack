package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantments;

/** Player-owned virtual Unbreaking, preserving the native armor/tool roll and actual enchantments. */
public final class OwnedWear {
    private OwnedWear(){}
    public static int filter(ServerPlayer p,ServerLevel level,ItemStack stack,int damage){
        if(p==null||damage<=0||damage>4096||!p.isAlive()||p.isCreative()||p.isSpectator()||NewbieMode.isNewbie(p)||!stack.isDamageableItem())return damage;
        boolean main=p.getMainHandItem()==stack,off=p.getOffhandItem()==stack;
        boolean armor=p.getItemBySlot(EquipmentSlot.HEAD)==stack||p.getItemBySlot(EquipmentSlot.CHEST)==stack
            ||p.getItemBySlot(EquipmentSlot.LEGS)==stack||p.getItemBySlot(EquipmentSlot.FEET)==stack;
        if(!main&&!off&&!armor)return damage;
        double sum=0;int cap=0;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();if(!fx.type().equals("virtual_unbreaking")||!Selectors.item(fx.selector(),stack))continue;
            String slot=fx.params().getOrDefault("slot","equipped");
            if(slot.equals("mainhand")&&!main||slot.equals("offhand")&&!off||slot.equals("armor")&&!armor)continue;
            sum+=MechanicalSpec.scaled(fx.amountPerRank(),a.rank(),64);cap=Math.max(cap,MechanicalSpec.integer(fx.params(),"max_total",32,1,64));
        }
        int extra=(int)Math.min(sum,cap);if(extra<=0)return damage;
        int actual=stack.getEnchantmentLevel(level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.UNBREAKING));
        double keep=MechanicalSpec.unbreakingPrefilter(actual,extra,stack.getItem() instanceof ArmorItem);
        int remaining=0;for(int i=0;i<damage;i++)if(level.random.nextDouble()<keep)remaining++;
        return remaining;
    }
}
