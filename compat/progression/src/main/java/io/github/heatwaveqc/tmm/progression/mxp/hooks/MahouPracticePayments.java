package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import io.github.heatwaveqc.tmm.progression.mxp.MxpSourceConfig;
import io.github.heatwaveqc.tmm.progression.mxp.core.MahouPracticeReceipt;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import java.util.IdentityHashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.function.IntSupplier;

/** Actual personal/gem/circuit expenditure inside the pinned player payment methods. */
public final class MahouPracticePayments {
    private static final String UTILS="stepsword.mahoutsukai.util.Utils";
    private static final ThreadLocal<Scope> ACTIVE=new ThreadLocal<>();
    private record Scope(ServerPlayer player,MahouPracticeReceipt receipt){}
    private MahouPracticePayments(){}
    public static int practice(Player player,int requested,IntSupplier original){
        Scope outer=ACTIVE.get();
        if(outer!=null){outer.receipt.invalidate();return original.getAsInt();}
        if(!(player instanceof ServerPlayer p)||!NativePaidSources.eligible(p)||requested<=0||
            MxpSourceConfig.MAHOU_INITIAL_POOL.get()<=0)return original.getAsInt();
        Scope s=new Scope(p,new MahouPracticeReceipt(requested));ACTIVE.set(s);
        try{
            int result=original.getAsInt();
            long debit=s.receipt.accepted(result);
            if(debit>0)NativePaidSources.credit(p,"mahoutsukai:practice",debit,MxpSourceConfig.MAHOU_INITIAL_POOL.get(),debit);
            return result;
        }finally{ACTIVE.remove();}
    }
    private static int amount(Object owner,String getter){
        Object value=MxpReflect.call(owner,getter);
        return value instanceof Integer n&&n>=0?n:-1;
    }
    public static void storedMana(Object holder,Runnable original){
        Scope s=ACTIVE.get();if(s==null){original.run();return;}
        int before=amount(holder,"getStoredMana");original.run();
        s.receipt.observe(before,amount(holder,"getStoredMana"));
    }
    private static Map<ItemStack,Integer> gems(ArrayList<ItemStack> stacks){
        var out=new IdentityHashMap<ItemStack,Integer>();
        for(ItemStack stack:stacks){
            if(stack==null||stack.isEmpty()||out.containsKey(stack))continue;
            int mana=amount(MxpReflect.staticCall(UTILS,"getGemMahou",stack),"getStoredMana");
            if(mana<0)return null;
            out.put(stack,mana);
        }
        return out;
    }
    public static int gems(ArrayList<ItemStack> stacks,IntSupplier original){
        Scope s=ACTIVE.get();if(s==null)return original.getAsInt();
        Map<ItemStack,Integer> before=gems(stacks);
        int result=original.getAsInt();
        Map<ItemStack,Integer> after=gems(stacks);
        if(before==null||after==null||before.size()!=after.size()){s.receipt.invalidate();return result;}
        for(var e:before.entrySet()){
            Integer remaining=after.get(e.getKey());
            if(remaining==null){s.receipt.invalidate();break;}
            s.receipt.observe(e.getValue(),remaining);
        }
        return result;
    }
    public static int batteries(UUID caller,IntSupplier original){
        Scope s=ACTIVE.get();
        if(s!=null&&!s.player.getUUID().equals(caller))s.receipt.invalidate();
        return original.getAsInt();
    }
    public static void circuit(Object tile,Runnable original){
        Scope s=ACTIVE.get();if(s==null){original.run();return;}
        int before=amount(tile,"getMana");original.run();
        s.receipt.observe(before,amount(tile,"getMana"));
    }
}
