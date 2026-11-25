package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.data.ElixirSpec;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.data.TrainingXpSpec;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

/** Only verified completed servings can bind or pay. Server-owned state, no client bind command. */
public final class ElixirRuntime {
    private ElixirRuntime(){}
    private static final Map<FoodData,WeakReference<ServerPlayer>> OWNERS=Collections.synchronizedMap(new WeakHashMap<>());
    private static final ThreadLocal<ServerPlayer> GRANTING=new ThreadLocal<>();
    private static String key(String path){return "tyr_progression:elixir/"+UUID.nameUUIDFromBytes(path.getBytes(StandardCharsets.UTF_8));}
    private static String binding(ServerPlayer p,String path){
        return p.getData(ProgressionAttachments.PLAYER_MODES.get()).get(key(path),"");
    }
    private static void store(ServerPlayer p,String path,String id){
        var modes=p.getData(ProgressionAttachments.PLAYER_MODES.get());
        p.setData(ProgressionAttachments.PLAYER_MODES.get(),modes.with(key(path),id));
    }
    private static ElixirSpec spec(ServerPlayer p,String path){
        if(!TrainingRewards.eligible(p,path))return null;
        String prefix=path.replace(':','/')+"/";
        return EffectRuntime.active(p).stream().filter(a->a.id().startsWith(prefix)&&a.effect().type().equals("personal_elixir"))
            .sorted(Comparator.comparing(EffectRuntime.Active::id))
            .map(a->ElixirSpec.read(a.effect())).findFirst().orElse(null);
    }
    private static String id(ItemStack stack){return BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();}
    private static boolean candidate(ElixirSpec spec,ItemStack stack){
        return !stack.isEmpty()&&!ElixirSpec.excluded(id(stack))
            &&stack.is(TagKey.create(Registries.ITEM,ResourceLocation.parse(spec.candidates())));
    }
    public static boolean candidateOrBound(ServerPlayer p,String path,ItemStack stack){
        if(stack.isEmpty()||ElixirSpec.excluded(id(stack)))return false;
        var spec=spec(p,path);
        return spec!=null&&(id(stack).equals(binding(p,path))||candidate(spec,stack));
    }
    private static void track(ServerPlayer p){OWNERS.put(p.getFoodData(),new WeakReference<>(p));}
    public static void playerTick(PlayerTickEvent.Pre event){if(event.getEntity() instanceof ServerPlayer p)track(p);}
    public static void login(PlayerEvent.PlayerLoggedInEvent event){if(event.getEntity() instanceof ServerPlayer p)track(p);}
    private static ElixirSpec locked(ServerPlayer p){
        for(String path:PathRegistry.all().keySet()){
            String bound=binding(p,path);
            if(bound.isEmpty()||ElixirSpec.excluded(bound))continue;
            var spec=spec(p,path);if(spec!=null)return spec;
        }
        return null;
    }
    public static boolean denySaturation(FoodData data){
        var ref=OWNERS.get(data);ServerPlayer p=ref==null?null:ref.get();
        return p!=null&&GRANTING.get()!=p&&p.getFoodData()==data&&locked(p)!=null;
    }
    private static boolean restricted(ElixirSpec spec,MobEffectInstance effect){
        if(effect.getEffect().value().getCategory()!=MobEffectCategory.BENEFICIAL)return false;
        var holder=effect.getEffect();
        return holder.is(TagKey.create(Registries.MOB_EFFECT,ResourceLocation.parse(spec.restricted())))
            &&!holder.is(TagKey.create(Registries.MOB_EFFECT,ResourceLocation.parse(spec.protectedEffects())));
    }
    public static void applicable(MobEffectEvent.Applicable event){
        if(!(event.getEntity() instanceof ServerPlayer p)||GRANTING.get()==p)return;
        var spec=locked(p);
        if(spec!=null&&event.getEffectInstance()!=null&&restricted(spec,event.getEffectInstance()))
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
    }
    /** Returns true when the special repeatable payment replaces ordinary meditative payment. */
    public static boolean finished(ServerPlayer p,String path,TrainingXpSpec rates,ItemStack consumed){
        var awards=new java.util.ArrayList<io.github.heatwaveqc.tyr.progression.data.XpSourceRouting.Candidate>();
        boolean replaces=finished(p,path,rates,consumed,awards);
        io.github.heatwaveqc.tyr.progression.core.ProgressionApi.awardSourceBatch(
                p,"training:"+UUID.randomUUID(),awards);
        return replaces;
    }
    static boolean finished(ServerPlayer p,String path,TrainingXpSpec rates,ItemStack consumed,
            java.util.List<io.github.heatwaveqc.tyr.progression.data.XpSourceRouting.Candidate> awards){
        var spec=spec(p,path);if(spec==null||consumed.isEmpty()||ElixirSpec.excluded(id(consumed)))return false;
        String item=id(consumed),before=binding(p,path);
        String bound=ElixirSpec.nextBinding(before,item,candidate(spec,consumed));
        if(!bound.equals(item))return false;
        track(p);
        if(before.isEmpty()){
            store(p,path,bound);
            // Snapshot before selectively removing known revocable ordinary buffs.
            for(var instance:List.copyOf(p.getActiveEffects()))
                if(restricted(spec,instance))p.removeEffect(instance.getEffect());
            p.displayClientMessage(Component.literal("Elixir of Life bound: ").append(consumed.getHoverName()),false);
        }
        if(GRANTING.get()!=null)return !spec.stackMeditative();
        GRANTING.set(p);
        try{
            var food=p.getFoodData();
            food.setSaturation(Math.min(food.getFoodLevel(),food.getSaturationLevel()+spec.saturation()));
            for(var buff:spec.buffs()){
                BuiltInRegistries.MOB_EFFECT.getHolder(ResourceLocation.parse(buff.id())).ifPresent(holder->{
                    if(holder.value().getCategory()==MobEffectCategory.BENEFICIAL)
                        p.addEffect(new MobEffectInstance(holder,buff.ticks(),buff.amplifier()));
                });
            }
        }finally{GRANTING.remove();}
        awards.add(TrainingRewards.candidate(rates,"elixir",BigDecimal.valueOf(spec.fixedMxp()),true,null));
        return !spec.stackMeditative();
    }
    public static void resetPath(ServerPlayer p,String path){
        var tree=PathRegistry.get(path);
        if(tree!=null&&tree.skills().stream().flatMap(s->s.effects().stream())
                .anyMatch(f->f.type().equals("personal_elixir")&&!ElixirSpec.read(f).reset()))return;
        if(!binding(p,path).isEmpty())store(p,path,"");
    }
}
