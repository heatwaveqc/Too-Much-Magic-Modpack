package io.github.heatwaveqc.audit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;

public final class Placement {
    private static final ThreadLocal<Scope> ACTIVE=new ThreadLocal<>();
    private static final Path OUTPUT=Path.of("tmm-placement.tsv");
    private static final Object LOCK=new Object();
    private static final int LIMIT=200000;
    private static final class Scope {
        final String id;
        final ChunkPos start,target;
        final WorldGenRegion region;
        final ChunkGenerator generator;
        final boolean track;
        final Map<Long,Boolean> last=new HashMap<>();
        int accepted,cross;
        Scope(String id,ChunkPos start,ChunkPos target,WorldGenRegion region,ChunkGenerator generator) {
            this.id=id;this.start=start;this.target=target;this.region=region;this.generator=generator;
            // Measure starts in the exact inner pilot; retain unfiltered starts in region census.
            this.track=start.x>=-32&&start.x<32&&start.z>=-32&&start.z<32
                && !id.equals("astralsorcery:focal_point")
                && !id.startsWith("alexscaves:")&&!id.startsWith("betterdungeons:")
                && !id.startsWith("bettermineshafts:")&&!id.startsWith("pastel:");
        }
    }
    public static void begin(String id,ChunkPos start,ChunkPos target,WorldGenRegion region,ChunkGenerator generator) {
        if(ACTIVE.get()!=null) throw new IllegalStateException("nested or abandoned structure audit scope");
        ACTIVE.set(new Scope(id,start,target,region,generator));
    }
    public static void write(BlockPos pos,BlockState state) {
        Scope s=ACTIVE.get();
        if(s==null||!s.track)return;
        if((pos.getX()>>4)!=s.target.x||(pos.getZ()>>4)!=s.target.z){s.cross++;return;}
        s.accepted++;
        s.last.put(pos.asLong(),!state.isAir());
        if(s.last.size()>LIMIT) throw new IllegalStateException("structure placement audit guard exceeded");
    }
    public static void end() {
        Scope s=ACTIVE.get();
        if(s==null)throw new IllegalStateException("missing structure audit scope");
        ACTIVE.remove();
        if(!s.track)return;
        Map<Long,Integer> heights=new HashMap<>();
        int nonAir=0,above=0,maxRise=0;
        for(var e:s.last.entrySet()) {
            if(!e.getValue())continue;
            BlockPos p=BlockPos.of(e.getKey());
            nonAir++;
            long col=((long)p.getX()<<32)^(p.getZ()&0xffffffffL);
            int terrain=heights.computeIfAbsent(col,k->s.generator.getBaseHeight(p.getX(),p.getZ(),
                Heightmap.Types.WORLD_SURFACE_WG,s.region,s.region.getLevel().getChunkSource().randomState()));
            if(p.getY()>=terrain){above++;maxRise=Math.max(maxRise,p.getY()-terrain+1);}
        }
        String line=String.join("\t",s.id,Integer.toString(s.start.x),Integer.toString(s.start.z),
            Integer.toString(s.target.x),Integer.toString(s.target.z),Integer.toString(s.accepted),
            Integer.toString(s.last.size()),Integer.toString(nonAir),Integer.toString(above),
            Integer.toString(maxRise),Integer.toString(heights.size()),Integer.toString(s.cross))+"\n";
        synchronized(LOCK) {
            try {Files.writeString(OUTPUT,line,StandardOpenOption.CREATE,StandardOpenOption.APPEND);}
            catch(IOException e){throw new IllegalStateException("audit evidence write failed",e);}
        }
    }
    private Placement(){}
}
