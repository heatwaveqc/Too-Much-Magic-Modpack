package io.github.heatwaveqc.tyr.progression.admin;

import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy;
import io.github.heatwaveqc.tyr.progression.data.ActivationSpec;
import io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyRuntime;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.runtime.SkillActivation;
import io.github.heatwaveqc.tyr.progression.state.HunterMutationState;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import java.util.*;
import static io.github.heatwaveqc.tyr.progression.admin.AdminCapabilityRegistry.*;

 /** Shared server-side capability catalog. New mechanics register a trusted
  * handler here or through registry().register at initialization, not a new command. */
public final class AdminCapabilities {
    private AdminCapabilities() {}
    private static final AdminCapabilityRegistry<ServerPlayer> REGISTRY = new AdminCapabilityRegistry<>();
    static {
        REGISTRY.register("tyr_progression:activation",new Activation());
        REGISTRY.register("tyr_progression:mutation",new Mutation());
        REGISTRY.register("tyr_progression:reward_receipt",new Receipt());
        REGISTRY.register("tyr_progression:derived_state",new Derived());
        REGISTRY.register("tyr_progression:resource",new Resource());
        REGISTRY.register("tyr_progression:elixir",new Elixir());
        REGISTRY.register("tyr_progression:discovery",new Discovery());
        REGISTRY.register("tyr_progression:mode",new Mode());
        REGISTRY.register("tyr_progression:tree",new Tree());
        REGISTRY.register("tyr_progression:milestone",new Milestone());
        REGISTRY.register("tyr_progression:counter",new Counter());
        REGISTRY.register("tyr_progression:path",new PathMembership());
    }
    public static AdminCapabilityRegistry<ServerPlayer> registry(){ return REGISTRY; }

    private static String[] pair(String key){
        var parts=key.split("\\|",-1);
        if(parts.length!=2 || parts[0].isBlank() || parts[1].isBlank())
            throw new IllegalArgumentException("Expected owner|entry");
        return parts;
    }

    private static final class Activation implements Handler<ServerPlayer> {
        public String description(){return "Configured skill activation and cooldown; reset stops activation and clears cooldown";}
        public Collection<String> keys(ServerPlayer p){
            var result=new ArrayList<String>();
            for(var path:PathRegistry.all().values())for(var skill:path.skills())
                if(PathRegistry.activation(path.id(),skill.id())!=null)
                    result.add(path.id()+"|"+skill.id());
            return result;
        }
        public String inspect(ServerPlayer p,String key){
            var ids=pair(key);
            return "state="+SkillActivation.adminState(p,ids[0],ids[1])
                +", active="+SkillActivation.active(p,ids[0],ids[1])
                +", cooldown ticks="+SkillActivation.remaining(p,ids[0],ids[1]);
        }
        public List<String> validate(ServerPlayer p,String key){
            var ids=pair(key);
            var raw=SkillActivation.adminState(p,ids[0],ids[1]);
            return ActivationSpec.State.decode(raw).encode().equals(raw)
                ? List.of() : List.of("Malformed activation state; reset recommended");
        }
        public Set<Action> actions(){return Set.of(Action.RESET);}
        public Change plan(ServerPlayer p,String key,Action action){
            var ids=pair(key);
            var before=SkillActivation.adminState(p,ids[0],ids[1]);
            return new Change(inspect(p,key),"state=0/0, inactive, no cooldown",
                ()->SkillActivation.adminReset(p,ids[0],ids[1],before));
        }
    }

    private static final class Mutation implements Handler<ServerPlayer> {
        public String description(){return "Saved mutation effect, potency and progress per policy owner; set uses human rank 1+; rank zero or reset removes only this effect";}
        public Collection<String> keys(ServerPlayer p){
            var result=new TreeSet<String>();
            p.getData(ProgressionAttachments.MUTATIONS.get()).paths().forEach((owner,state)->{
                state.unlocked().keySet().forEach(effect->result.add(owner+"|"+effect));
                state.progress().keySet().forEach(k->{
                    int at=k.lastIndexOf('@');
                    if(at>0)result.add(owner+"|"+k.substring(0,at));
                });
            });
            for(var owner:PathRegistry.mutationPolicies().entrySet()){
                if(!ledger(p).get(owner.getKey()).joined())continue;
                for(var id:BuiltInRegistries.MOB_EFFECT.keySet()){
                    var holder=BuiltInRegistries.MOB_EFFECT.getHolder(id).orElse(null);
                    if(holder!=null&&owner.getValue().eligible(id.toString(),holder.value().isInstantenous()))
                        result.add(owner.getKey()+"|"+id);
                }
            }
            return result;
        }
        private HunterMutationState state(ServerPlayer p,String owner){
            return p.getData(ProgressionAttachments.MUTATIONS.get()).paths()
                .getOrDefault(owner,HunterMutationState.empty());
        }
        private Map<String,Long> progress(HunterMutationState state,String effect){
            var result=new TreeMap<String,Long>();
            state.progress().forEach((k,v)->{if(k.startsWith(effect+"@"))result.put(k,v);});
            return result;
        }
        public String inspect(ServerPlayer p,String key){
            var ids=pair(key);
            var state=state(p,ids[0]);
            return "rank="+(state.unlocked().getOrDefault(ids[1],-1)+1)+", progress="+progress(state,ids[1]);
        }
        public List<String> validate(ServerPlayer p,String key){
            var ids=pair(key);
            var policy=PathRegistry.hunterMutationPolicy(ids[0]);
            var state=state(p,ids[0]);
            var issues=new ArrayList<String>();
            if(policy==null){issues.add("Mutation owner no longer configured");return issues;}
            var id=ResourceLocation.tryParse(ids[1]);
            var effect=id==null?null:BuiltInRegistries.MOB_EFFECT.getHolder(id).orElse(null);
            if(effect==null || !policy.eligible(ids[1],effect.value().isInstantenous()))
                issues.add("Effect no longer eligible or registered");
            var amp=state.unlocked().get(ids[1]);
            if(amp!=null && (amp<0 || amp>policy.maximumAmplifier()))issues.add("Unlocked amplifier outside policy");
            for(var row:progress(state,ids[1]).entrySet()){
                try{
                    int tier=Integer.parseInt(row.getKey().substring(row.getKey().lastIndexOf('@')+1));
                    if(tier<0 || tier>policy.maximumAmplifier() || row.getValue()<0 || row.getValue()>policy.targetDurationTicks())
                        issues.add("Progress outside configured bounds: "+row.getKey());
                }catch(NumberFormatException ex){issues.add("Malformed progress key: "+row.getKey());}
            }
            return issues;
        }
        public Set<Action> actions(){return Set.of(Action.RESET,Action.SET,Action.ADD,Action.SUBTRACT);}
        public Change plan(ServerPlayer p,String key,Action action,long amount){
            var ids=pair(key);
            var old=p.getData(ProgressionAttachments.MUTATIONS.get());
            var state=state(p,ids[0]);
            int current=state.unlocked().getOrDefault(ids[1],-1)+1;
            if(amount>10)throw new IllegalArgumentException("Mutation amount outside supported range");
            long rank=switch(action){case RESET->0;case SET->amount;case ADD->current+amount;case SUBTRACT->current-amount;default->throw new IllegalArgumentException("Unsupported action");};
            var policy=PathRegistry.hunterMutationPolicy(ids[0]);
            if(rank<0)throw new IllegalArgumentException("Negative mutation rank");
            if(rank>0){
                var id=ResourceLocation.tryParse(ids[1]);
                var effect=id==null?null:BuiltInRegistries.MOB_EFFECT.getHolder(id).orElse(null);
                if(policy==null||!ledger(p).get(ids[0]).joined()||effect==null||!policy.eligible(ids[1],effect.value().isInstantenous()))
                    throw new IllegalArgumentException("Requires joined owner and eligible registered effect");
            }
            var changed=state.adminRank(ids[1],(int)rank,policy==null?0:policy.maximumAmplifier(),policy==null?1:policy.targetDurationTicks());
            var next=old.with(ids[0],changed);
            return new Change(inspect(p,key),"mutation rank="+rank+"; selected effect progress normalized; other effects/owners preserved",()->{
                if(!p.getData(ProgressionAttachments.MUTATIONS.get()).equals(old))
                    throw new IllegalStateException("Mutation state changed");
                p.setData(ProgressionAttachments.MUTATIONS.get(),next);
            });
        }
    }

    private static final class Resource implements Handler<ServerPlayer> {
        public String description(){return "Configured personal resource; amounts are raw integer units, not displayed fractions";}
        public Collection<String> keys(ServerPlayer p){
            return io.github.heatwaveqc.tyr.progression.runtime.resources.ResourceCatalog.current().keySet();
        }
        private io.github.heatwaveqc.tyr.progression.runtime.resources.PathResources.AdminSnapshot snapshot(ServerPlayer p,String key){
            return io.github.heatwaveqc.tyr.progression.runtime.resources.PathResources.adminSnapshot(p,key);
        }
        public String inspect(ServerPlayer p,String key){
            var s=snapshot(p,key);
            return s.value()+"/"+s.capacity()+", initial="+s.initial()+", stored="+s.stored()
                +", joined="+s.joined()+", active="+s.active();
        }
        public List<String> validate(ServerPlayer p,String key){
            var s=snapshot(p,key);
            return s.stored()!=null&&s.stored()>s.capacity()?List.of("Stored value exceeds current capacity"):List.of();
        }
        public Set<Action> actions(){return Set.of(Action.SET,Action.ADD,Action.SUBTRACT,Action.FILL,Action.EMPTY,Action.RESET);}
        public Change plan(ServerPlayer p,String key,Action action,long amount){
            var s=snapshot(p,key);
            if(!s.joined())throw new IllegalArgumentException("Resource owner path not joined");
            if(amount>s.capacity())throw new IllegalArgumentException("Amount exceeds current resource capacity");
            long next=switch(action){
                case SET -> amount;
                case ADD -> s.value()+amount;
                case SUBTRACT -> s.value()-amount;
                case FILL -> s.capacity();
                case EMPTY -> 0;
                case RESET -> s.initial();
                default -> throw new IllegalArgumentException("Unsupported resource action");
            };
            if(next<0||next>s.capacity())throw new IllegalArgumentException("Result outside current resource capacity");
            return new Change(inspect(p,key),next+"/"+s.capacity(),()->io.github.heatwaveqc.tyr.progression.runtime.resources.PathResources.adminSet(p,s,(int)next));
        }
    }

    private static final class Elixir implements Handler<ServerPlayer> {
        public String description(){return "Personal elixir binding; reset explicitly unbinds and ends exclusive nourishment until the next eligible serving";}
        public Collection<String> keys(ServerPlayer p){return io.github.heatwaveqc.tyr.progression.runtime.ElixirRuntime.adminPaths();}
        public String inspect(ServerPlayer p,String key){
            var bound=io.github.heatwaveqc.tyr.progression.runtime.ElixirRuntime.adminBinding(p,key);
            return "bound="+(bound.isEmpty()?"none":bound)+", exclusive="+io.github.heatwaveqc.tyr.progression.runtime.ElixirRuntime.adminExclusive(p,key);
        }
        public List<String> validate(ServerPlayer p,String key){
            var bound=io.github.heatwaveqc.tyr.progression.runtime.ElixirRuntime.adminBinding(p,key);
            if(bound.isEmpty())return List.of();
            var id=ResourceLocation.tryParse(bound);
            if(id==null||!BuiltInRegistries.ITEM.containsKey(id)||io.github.heatwaveqc.tyr.progression.data.ElixirSpec.excluded(bound))
                return List.of("Bound item missing or excluded; explicit unbind recommended");
            return List.of();
        }
        public Set<Action> actions(){return Set.of(Action.RESET);}
        public Change plan(ServerPlayer p,String key,Action action){
            var bound=io.github.heatwaveqc.tyr.progression.runtime.ElixirRuntime.adminBinding(p,key);
            return new Change(inspect(p,key),"unbound; ordinary nourishment resumes unless another binding is active",
                ()->io.github.heatwaveqc.tyr.progression.runtime.ElixirRuntime.adminUnbind(p,key,bound));
        }
    }

    private static final class Discovery implements Handler<ServerPlayer> {
        public String description(){return "Per-path food discovery; reset forgets only that local item, never the lifetime XP receipt";}
        public Collection<String> keys(ServerPlayer p){
            var keys=new ArrayList<String>();
            p.getData(ProgressionAttachments.PATH_DISCOVERIES.get()).consumed()
                .forEach((path,items)->items.forEach(item->keys.add(path+"|"+item)));
            return keys;
        }
        public String inspect(ServerPlayer p,String key){return "local discovery present: "+key;}
        public List<String> validate(ServerPlayer p,String key){
            var ids=pair(key);var item=ResourceLocation.tryParse(ids[1]);
            var issues=new ArrayList<String>();
            if(PathRegistry.get(ids[0])==null)issues.add("Path no longer configured");
            if(item==null||!BuiltInRegistries.ITEM.containsKey(item))issues.add("Item no longer registered");
            return issues;
        }
        public Set<Action> actions(){return Set.of(Action.RESET);}
        public Change plan(ServerPlayer p,String key,Action action){
            var ids=pair(key);
            var old=p.getData(ProgressionAttachments.PATH_DISCOVERIES.get());
            var rows=new HashMap<>(old.consumed());
            var items=new ArrayList<>(rows.getOrDefault(ids[0],List.of()));
            if(!items.remove(ids[1]))throw new IllegalStateException("Discovery already absent");
            if(items.isEmpty())rows.remove(ids[0]);else rows.put(ids[0],items);
            var next=new io.github.heatwaveqc.tyr.progression.state.PathDiscoveries(rows);
            return new Change(inspect(p,key),"local discovery absent; lifetime reward receipts unchanged",()->{
                if(!p.getData(ProgressionAttachments.PATH_DISCOVERIES.get()).equals(old))
                    throw new IllegalStateException("Discovery state changed");
                p.setData(ProgressionAttachments.PATH_DISCOVERIES.get(),next);
            });
        }
    }

    private static io.github.heatwaveqc.tyr.progression.state.Ledger ledger(ServerPlayer p){
        return p.getData(ProgressionAttachments.LEDGER.get());
    }
    private static Change ledgerChange(ServerPlayer p,String before,String after,
            io.github.heatwaveqc.tyr.progression.state.Ledger old,
            io.github.heatwaveqc.tyr.progression.state.Ledger next){
        return new Change(before,after,()->{
            if(!ledger(p).equals(old))throw new IllegalStateException("Ledger changed");
            p.setData(ProgressionAttachments.LEDGER.get(),next);
        });
    }
    private static final class Milestone implements Handler<ServerPlayer>{
        public String description(){return "Configured trial; grant completion without XP, reset unfinished criteria only";}
        public Collection<String> keys(ServerPlayer p){
            var keys=new ArrayList<String>();
            for(var path:PathRegistry.all().values())for(var m:path.milestones())keys.add(path.id()+"|"+m.id());
            return keys;
        }
        public String inspect(ServerPlayer p,String key){
            var ids=pair(key);var path=PathRegistry.get(ids[0]);
            var m=io.github.heatwaveqc.tyr.progression.core.AdminMilestones.milestone(path,ids[1]);
            var old=ledger(p);var track=old.get(ids[0]);var counts=new ArrayList<String>();
            for(int i=0;i<m.criteria().size();i++)counts.add(track.criterionCount(
                io.github.heatwaveqc.tyr.progression.core.ProgressionMath.counterKey(m.id(),i))+"/"+m.criteria().get(i).count());
            return "joined="+track.joined()+", complete="+track.completed(m.id())+", achievement="+old.economy().achievements().contains(m.id())
                +", criteria="+counts+", requires="+m.after()+", fixed XP="+m.fixedRewardXp();
        }
        public List<String> validate(ServerPlayer p,String key){
            var ids=pair(key);var m=io.github.heatwaveqc.tyr.progression.core.AdminMilestones.milestone(PathRegistry.get(ids[0]),ids[1]);
            var old=ledger(p);var t=old.get(ids[0]);var issues=new ArrayList<String>();
            if(t.completed(m.id())&&!old.economy().achievements().contains(m.id()))issues.add("Completion lacks achievement");
            if(t.completed(m.id())&&!t.milestones().containsAll(m.after()))issues.add("Completed milestone lacks prerequisite");
            for(int i=0;i<m.criteria().size();i++){
                int count=t.criterionCount(io.github.heatwaveqc.tyr.progression.core.ProgressionMath.counterKey(m.id(),i));
                if(count<0||count>m.criteria().get(i).count())issues.add("Counter "+i+" outside bounds");
            }
            return issues;
        }
        public Set<Action> actions(){return Set.of(Action.GRANT,Action.RESET);}
        public Change plan(ServerPlayer p,String key,Action action){
            var ids=pair(key);var path=PathRegistry.get(ids[0]);var old=ledger(p);
            var next=action==Action.GRANT?io.github.heatwaveqc.tyr.progression.core.AdminMilestones.complete(old,path,ids[1])
                :io.github.heatwaveqc.tyr.progression.core.AdminMilestones.resetProgress(old,path,ids[1]);
            return ledgerChange(p,inspect(p,key),action==Action.GRANT?"completed; achievement granted; NO XP or points awarded"
                :"unfinished criteria cleared; achievements and receipts unchanged",old,next);
        }
    }
    private static final class Counter implements Handler<ServerPlayer>{
        public String description(){return "Configured milestone criterion (zero-based index); edits do not settle rewards until a legitimate gameplay event";}
        private String[] ids(String key){
            var parts=key.split("\\\\|",-1);
            if(parts.length!=3)throw new IllegalArgumentException("Expected path|milestone|index");
            return parts;
        }
        public Collection<String> keys(ServerPlayer p){
            var keys=new ArrayList<String>();
            for(var path:PathRegistry.all().values())for(var m:path.milestones())
                for(int i=0;i<m.criteria().size();i++)keys.add(path.id()+"|"+m.id()+"|"+i);
            return keys;
        }
        private int count(ServerPlayer p,String[] ids){
            return ledger(p).get(ids[0]).criterionCount(io.github.heatwaveqc.tyr.progression.core.ProgressionMath.counterKey(ids[1],Integer.parseInt(ids[2])));
        }
        public String inspect(ServerPlayer p,String key){
            var ids=ids(key);var m=io.github.heatwaveqc.tyr.progression.core.AdminMilestones.milestone(PathRegistry.get(ids[0]),ids[1]);
            var criterion=m.criteria().get(Integer.parseInt(ids[2]));
            return count(p,ids)+"/"+criterion.count()+"; event="+criterion.event()+", selector="+criterion.selector();
        }
        public List<String> validate(ServerPlayer p,String key){
            var ids=ids(key);var m=io.github.heatwaveqc.tyr.progression.core.AdminMilestones.milestone(PathRegistry.get(ids[0]),ids[1]);
            int n=count(p,ids);
            return n<0||n>m.criteria().get(Integer.parseInt(ids[2])).count()?List.of("Counter outside bounds"):List.of();
        }
        public Set<Action> actions(){return Set.of(Action.SET,Action.ADD,Action.SUBTRACT,Action.RESET);}
        public Change plan(ServerPlayer p,String key,Action action,long amount){
            var ids=ids(key);var old=ledger(p);int n=count(p,ids);
            if(amount>Integer.MAX_VALUE)throw new IllegalArgumentException("Amount too large");
            long desired=switch(action){case SET->amount;case ADD->n+amount;case SUBTRACT->n-amount;case RESET->0;default->throw new IllegalArgumentException("Unsupported action");};
            var next=io.github.heatwaveqc.tyr.progression.core.AdminMilestones.counter(old,PathRegistry.get(ids[0]),ids[1],Integer.parseInt(ids[2]),desired);
            return ledgerChange(p,inspect(p,key),"count="+desired+"; no immediate reward, normal event may settle completion",old,next);
        }
    }

    private static final class PathMembership implements Handler<ServerPlayer>{
        public String description(){return "Path membership diagnosis and policy-aware join/leave; never bypasses provider vetoes or baseline stage gateway";}
        public Collection<String> keys(ServerPlayer p){return PathRegistry.all().keySet();}
        public String inspect(ServerPlayer p,String key){
            var join=io.github.heatwaveqc.tyr.progression.core.ProgressionApi.joinBlocker(p,key);
            var leave=io.github.heatwaveqc.tyr.progression.core.ProgressionApi.leaveBlocker(p,key);
            return "joined="+ledger(p).get(key).joined()+", eligible="+PathRegistry.eligible(p,key)
                +", enrollment="+PathRegistry.enrollment(key)+", join="+(join.isEmpty()?"allowed":join)
                +", leave="+(leave.isEmpty()?"allowed":leave);
        }
        public List<String> validate(ServerPlayer p,String key){
            if(ledger(p).get(key).joined()&&!PathRegistry.eligible(p,key))
                return List.of("Joined but currently ineligible; may be intentionally dormant");
            return List.of();
        }
        public Set<Action> actions(){return Set.of(Action.JOIN,Action.LEAVE);}
        public Change plan(ServerPlayer p,String key,Action action){
            var old=ledger(p);
            boolean join=action==Action.JOIN;
            if(join&&old.get(key).joined())throw new IllegalArgumentException("Already joined");
            var blocker=join?io.github.heatwaveqc.tyr.progression.core.ProgressionApi.joinBlocker(p,key)
                :io.github.heatwaveqc.tyr.progression.core.ProgressionApi.leaveBlocker(p,key);
            if(!blocker.isEmpty())throw new IllegalArgumentException(blocker);
            return new Change(inspect(p,key),join?"join through normal enrollment; configured free-XP wipe may apply"
                :"leave through normal lifecycle; configured investment forfeiture/refund and local discovery reset apply",()->{
                if(!ledger(p).equals(old))throw new IllegalStateException("Path ledger changed");
                boolean ok=join?io.github.heatwaveqc.tyr.progression.core.ProgressionApi.join(p,key)
                    :io.github.heatwaveqc.tyr.progression.core.ProgressionApi.leave(p,key);
                if(!ok)throw new IllegalStateException("Membership change rejected");
            });
        }
    }

    private static final class Tree implements Handler<ServerPlayer> {
        public String description(){return "Configured path tree respec using normal per-currency retain/refund/forfeit policy and path reset hooks";}
        public Collection<String> keys(ServerPlayer p){return PathRegistry.all().keySet();}
        private io.github.heatwaveqc.tyr.progression.state.Ledger ledger(ServerPlayer p){
            return p.getData(ProgressionAttachments.LEDGER.get());
        }
        private String ranks(io.github.heatwaveqc.tyr.progression.state.Ledger ledger,String path){
            var view=io.github.heatwaveqc.tyr.progression.core.ChannelEngine.view(ledger,PathRegistry.get(path),PathRegistry.config());
            return "joined="+view.joined()+", ranks="+new TreeMap<>(view.skills())
                +", invested="+io.github.heatwaveqc.tyr.progression.core.ChannelEngine.investedRanks(ledger,path,PathRegistry.config());
        }
        public String inspect(ServerPlayer p,String key){return ranks(ledger(p),key);}
        public List<String> validate(ServerPlayer p,String key){
            var view=io.github.heatwaveqc.tyr.progression.core.ChannelEngine.view(ledger(p),PathRegistry.get(key),PathRegistry.config());
            var issues=new ArrayList<String>();
            for(var skill:PathRegistry.get(key).skills())
                if(view.skillRank(skill.id())>0&&!io.github.heatwaveqc.tyr.progression.core.AdminSkills.prerequisites(skill,view))
                    issues.add("Owned skill has unmet declared prerequisites: "+skill.id());
            return issues;
        }
        public Set<Action> actions(){return Set.of(Action.RESET);}
        public Change plan(ServerPlayer p,String key,Action action){
            var old=ledger(p);
            if(!old.get(key).joined())throw new IllegalArgumentException("Path not joined");
            var next=io.github.heatwaveqc.tyr.progression.core.ChannelEngine.refund(old,PathRegistry.get(key));
            return new Change(ranks(old,key),ranks(next,key)+"; normal respec hooks clear local discoveries and eligible bindings, lifetime receipts retained",()->{
                if(!ledger(p).equals(old))throw new IllegalStateException("Path ledger changed");
                if(!io.github.heatwaveqc.tyr.progression.core.ProgressionApi.respec(p,key))
                    throw new IllegalStateException("Path respec rejected");
            });
        }
    }

    private static final class Mode implements Handler<ServerPlayer> {
        public String description(){return "Read-only stored mode inventory; edits require a registered mechanic-specific validator";}
        public Collection<String> keys(ServerPlayer p){return p.getData(ProgressionAttachments.PLAYER_MODES.get()).values().keySet();}
        public String inspect(ServerPlayer p,String key){return p.getData(ProgressionAttachments.PLAYER_MODES.get()).values().get(key);}
        public List<String> validate(ServerPlayer p,String key){
            return List.of("Opaque mechanic-owned value; use a dedicated handler to validate or edit");
        }
    }

    private static final class Derived implements Handler<ServerPlayer> {
        public String description(){return "Refresh player-wide Progression-owned effects, activations, mutation attributes and client view";}
        public Collection<String> keys(ServerPlayer p){return List.of("player");}
        public String inspect(ServerPlayer p,String key){
            return "joined paths="+PathRegistry.all().keySet().stream()
                .filter(id->io.github.heatwaveqc.tyr.progression.core.ProgressionApi.track(p,id).joined()).count()
                +", eligible mutation effects="+io.github.heatwaveqc.tyr.progression.runtime.HunterMutations.active(p).size();
        }
        public List<String> validate(ServerPlayer p,String key){
            return List.of("Derived state is runtime-dependent; reconcile refreshes owned effects without resetting purchases or XP");
        }
        public Set<Action> actions(){return Set.of(Action.RECONCILE);}
        public Change plan(ServerPlayer p,String key,Action action){
            return new Change(inspect(p,key),"refresh Progression-owned effects and client view",
                ()->AdminStateRefresh.reconcile(p));
        }
    }

    private static final class Receipt implements Handler<ServerPlayer> {
        public String description(){return "Exact paid first-completion receipt; reset permits possible replay and does not remove previously earned XP";}
        public Collection<String> keys(ServerPlayer p){
            return p.getData(ProgressionAttachments.LEDGER.get()).economy().rewardReceipts();
        }
        public String inspect(ServerPlayer p,String key){return "paid first-completion receipt: "+key;}
        public List<String> validate(ServerPlayer p,String key){
            var parts=key.split("\\|",-1);
            if(parts.length!=3 || Arrays.stream(parts).anyMatch(String::isBlank))return List.of("Malformed receipt identity");
            if(!ExperienceEconomyRuntime.current().systems().containsKey(parts[0]))
                return List.of("Receipt currency no longer configured");
            return List.of();
        }
        public Set<Action> actions(){return Set.of(Action.RESET);}
        public Change plan(ServerPlayer p,String key,Action action){
            var old=p.getData(ProgressionAttachments.LEDGER.get());
            var economy=old.economy();
            var receipts=new HashSet<>(economy.rewardReceipts());
            if(!receipts.remove(key))throw new IllegalStateException("Receipt already absent");
            var next=new ExperienceEconomy.Ledger(economy.currentXp(),economy.paidRanks(),
                economy.achievements(),receipts,economy.recentTransactions());
            return new Change(inspect(p,key),"receipt absent; WARNING: qualifying reward may pay again; XP unchanged",()->{
                if(!p.getData(ProgressionAttachments.LEDGER.get()).equals(old))
                    throw new IllegalStateException("Ledger changed");
                p.setData(ProgressionAttachments.LEDGER.get(),old.withEconomy(next));
            });
        }
    }
}
