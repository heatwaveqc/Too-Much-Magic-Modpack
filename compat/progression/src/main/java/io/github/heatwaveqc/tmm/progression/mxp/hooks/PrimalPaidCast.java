package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import io.github.heatwaveqc.tmm.progression.mxp.MxpSourceConfig;
import io.github.heatwaveqc.tmm.progression.mxp.core.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import java.util.function.Supplier;

public final class PrimalPaidCast {
    private static final String MANA="com.verdantartifice.primalmagick.common.wands.ManaManager";
    private static final ThreadLocal<Scope> ACTIVE=new ThreadLocal<>();
    private PrimalPaidCast(){}
    private static final class Scope {
        final ServerPlayer player; final ItemStack wand;
        MultiSourcePayment payment;
        boolean consuming,accepted,valid=true;
        Scope(ServerPlayer player,ItemStack wand){this.player=player;this.wand=wand;}
    }
    public static InteractionResultHolder<ItemStack> use(Player player,ItemStack wand,Supplier<InteractionResultHolder<ItemStack>> original){
        Scope outer=ACTIVE.get();
        if(outer!=null){outer.valid=false;return original.get();}
        if(!(player instanceof ServerPlayer p)||!NativePaidSources.eligible(p)||
            MxpSourceConfig.PRIMAL_CENTIMANA_POOL.get()<=0)return original.get();
        Scope s=new Scope(p,wand);ACTIVE.set(s);
        try{
            InteractionResultHolder<ItemStack> result=original.get();
            if(s.valid&&s.accepted&&s.payment!=null&&s.payment.complete()&&result.getResult().consumesAction())
                NativePaidSources.credit(p,"primalmagick:mana",s.payment.nominal(),MxpSourceConfig.PRIMAL_CENTIMANA_POOL.get(),s.payment.actual());
            return result;
        }finally{ACTIVE.remove();}
    }
    public static boolean consume(Player player,ItemStack wand,Object sources,java.util.function.BooleanSupplier original){
        Scope s=ACTIVE.get();
        if(s==null)return original.getAsBoolean();
        if(s.player!=player||s.wand!=wand||s.payment!=null||s.consuming){s.valid=false;return original.getAsBoolean();}
        Object raw=MxpReflect.call(sources,"getSources");
        if(!(raw instanceof Iterable<?> entries)){s.valid=false;return original.getAsBoolean();}
        s.payment=new MultiSourcePayment();
        for(Object source:entries){
            Object nominal=MxpReflect.call(sources,"getAmount",source);
            if(!(nominal instanceof Integer cost)){s.valid=false;continue;}
            s.payment.expect(source,cost);
        }
        s.consuming=true;
        try{boolean ok=original.getAsBoolean();s.accepted=ok;return ok;}
        finally{s.consuming=false;}
    }
    private static double balance(Player player,Object source){
        return MxpReflect.number(MxpReflect.staticCall(MANA,"getMana",player,source));
    }
    public static int debit(Player player,ItemStack wand,Object source,int price,java.util.function.IntSupplier original){
        Scope s=ACTIVE.get();
        if(s==null||!s.consuming)return original.getAsInt();
        if(s.player!=player||s.wand!=wand){s.valid=false;return original.getAsInt();}
        double before=balance(player,source);
        int leftover=original.getAsInt();
        double actual=ObservedPayment.delta(before,balance(player,source));
        s.payment.observe(source,price,leftover,actual);
        return leftover;
    }
}
