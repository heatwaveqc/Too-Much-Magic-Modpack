package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import io.github.heatwaveqc.tmm.progression.mxp.MxpSourceConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;
import io.github.heatwaveqc.tmm.progression.mxp.core.PaymentReceiptPolicy;

/** Count only native, actual loss of carried canonical blood in known personal item actions. */
public final class EvilCraftPaidBlood {
    private static final String ITEMS="org.cyclops.evilcraft.item.";
    private static final String BROOM="org.cyclops.evilcraft.entity.item.EntityBroom";
    private static final Set<String> PERSONAL=Set.of("ItemNecromancerStaff","ItemBloodPearlOfTeleportation","ItemMace","ItemInvigoratingPendant","ItemPrimedPendant","ItemKineticator","ItemBroom");
    private static final ThreadLocal<Scope> ACTIVE=new ThreadLocal<>();
    private static final class Scope{boolean valid=true;}
    private EvilCraftPaidBlood(){}
    private static boolean blood(FluidStack f){
        if(f==null||f.isEmpty())return false;
        var id=BuiltInRegistries.FLUID.getKey(f.getFluid());
        return id!=null&&("evilcraft:blood".equals(id.toString())||"evilcraft:flowing_blood".equals(id.toString()));
    }
    private static long amount(ItemStack stack){
        if(stack==null||stack.isEmpty())return 0;
        try{
            var optional=FluidUtil.getFluidHandler(stack);
            if(optional.isEmpty())return 0;
            var handler=optional.get();long amount=0;
            for(int i=0;i<handler.getTanks();i++){
                FluidStack f=handler.getFluidInTank(i);
                if(blood(f))amount+=f.getAmount();
            }
            return amount;
        }catch(RuntimeException|LinkageError ex){return -1;}
    }
    private static Map<ItemStack,Long> snapshot(Player player){
        var stacks=new IdentityHashMap<ItemStack,Long>();
        try{
            // Same extended-inventory traversal as native fallback consumption (includes supported wearables).
            Class<?> type=Class.forName("org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator");
            Object raw=type.getConstructor(Player.class).newInstance(player);
            if(!(raw instanceof Iterator<?> it))return null;
            int count=0;
            while(it.hasNext()){
                if(++count>1024)return null;
                Object next=it.next();
                if(!(next instanceof ItemStack stack))return null;
                if(!stack.isEmpty()&&!stacks.containsKey(stack)){
                    long stored=amount(stack);
                    if(stored<0)return null;
                    stacks.put(stack,stored);
                }
            }
            // Ensure ordinary player-owned equipment is included if the native iterator excludes a slot.
            var inv=player.getInventory();
            for(int i=0;i<inv.getContainerSize();i++){
                ItemStack stack=inv.getItem(i);
                if(!stack.isEmpty()&&!stacks.containsKey(stack)){
                    long stored=amount(stack);if(stored<0)return null;stacks.put(stack,stored);
                }
            }
            return stacks;
        }catch(ReflectiveOperationException|RuntimeException|LinkageError ex){return null;}
    }
    private static String personalClass(ItemStack stack){
        for(Class<?> c=stack.getItem().getClass();c!=null;c=c.getSuperclass()){
            String name=c.getName();
            if(name.startsWith(ITEMS)&&PERSONAL.contains(name.substring(ITEMS.length())))return name;
        }
        return "";
    }
    private static boolean caller(String itemClass){
        if(itemClass.isEmpty())return false;
        return StackWalker.getInstance().walk(frames->frames.anyMatch(f->f.getClassName().equals(itemClass)));
    }
    private static long measured(Map<ItemStack,Long> before,Map<ItemStack,Long> after){
        if(before==null||after==null)return 0;
        long debit=0;
        for(var e:before.entrySet()){
            if(e.getValue()<=0)continue;
            Long remaining=after.get(e.getKey());
            // A missing/replaced stack is ambiguous (e.g. moving the reservoir), not evidence of payment.
            if(remaining==null||remaining<0)return 0;
            if(remaining<e.getValue())debit+=e.getValue()-remaining;
        }
        // New or replenished blood in the same native invocation could be a transfer.
        for(var e:after.entrySet())if(e.getValue()>before.getOrDefault(e.getKey(),0L))return 0;
        return debit;
    }
    public static FluidStack consume(Player player,ItemStack stack,int requested,Supplier<FluidStack> original){
        Scope outer=ACTIVE.get();
        if(outer!=null){outer.valid=false;return original.get();}
        if(!(player instanceof ServerPlayer p)||!NativePaidSources.eligible(p)||requested<=0||
            MxpSourceConfig.EVILCRAFT_BLOOD_POOL.get()<=0)return original.get();
        String itemClass=personalClass(stack);
        if(!caller(itemClass))return original.get();
        var before=snapshot(p);
        if(before==null)return original.get();
        boolean broom=itemClass.equals(ITEMS+"ItemBroom");
        if(broom){
            var vehicle=p.getVehicle();
            if(vehicle==null||!BROOM.equals(vehicle.getClass().getName())||before.containsKey(stack))return original.get();
        }else if(!before.containsKey(stack))return original.get();
        Scope s=new Scope();ACTIVE.set(s);
        try{
            FluidStack result=original.get();
            if(s.valid&&blood(result)&&result.getAmount()<=requested){
                long actual=measured(before,snapshot(p));
                if(actual>0&&actual<=result.getAmount())
                    credit(p,actual);
            }
            return result;
        }finally{ACTIVE.remove();}
    }
    private static void credit(ServerPlayer p,long actual){
        if(actual>0)NativePaidSources.credit(p,"evilcraft:carried_blood",actual,MxpSourceConfig.EVILCRAFT_BLOOD_POOL.get(),actual);
    }
    /** Native direct item drain path: no central consume() call. */
    public static <T> T direct(LivingEntity actor,ItemStack stack,Supplier<T> original){
        Scope outer=ACTIVE.get();
        if(outer!=null){outer.valid=false;return original.get();}
        if(!(actor instanceof ServerPlayer p)||!NativePaidSources.eligible(p)||
            MxpSourceConfig.EVILCRAFT_BLOOD_POOL.get()<=0)return original.get();
        var before=snapshot(p);
        if(before==null||!before.containsKey(stack)||before.get(stack)<=0)return original.get();
        Scope s=new Scope();ACTIVE.set(s);
        try{
            T result=original.get();
            var after=snapshot(p);
            if(s.valid&&after!=null&&after.containsKey(stack)&&!Boolean.FALSE.equals(result)){
                long actual=before.get(stack)-after.get(stack);
                if(actual>0&&actual==measured(before,after))credit(p,actual);
            }
            return result;
        }finally{ACTIVE.remove();}
    }
}
