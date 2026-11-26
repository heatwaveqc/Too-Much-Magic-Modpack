package io.github.heatwaveqc.tyr.progression.client;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.data.ChallengeHudSpec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import java.lang.ref.WeakReference;
import java.util.List;

/** Bottom-right challenge overlay; never persists across player/session changes. */
@EventBusSubscriber(modid=ProgressionMod.MOD_ID,value=Dist.CLIENT)
public final class ChallengeHud {
    private ChallengeHud(){}
    private static WeakReference<LocalPlayer> owner=new WeakReference<>(null);
    private static List<ChallengeHudSpec.Row> rows=List.of();
    private static int received=-1;
    public static void accept(List<ChallengeHudSpec.Row> next){
        var p=Minecraft.getInstance().player;
        owner=new WeakReference<>(p);
        received=p==null?-1:p.tickCount;
        rows=p==null?List.of():List.copyOf(next);
    }
    private static Component text(ChallengeHudSpec.Text t){
        return t.key().isEmpty()?Component.literal(t.text()):Component.translatableWithFallback(t.key(),t.text());
    }
    @SubscribeEvent public static void render(RenderGuiEvent.Post event){
        var mc=Minecraft.getInstance();var p=mc.player;
        if(p==null||p!=owner.get()||!p.isAlive()||p.isSpectator()||p.isCreative()
                ||mc.options.hideGui||mc.screen!=null||received<0||p.tickCount<received
                ||p.tickCount-received>40||rows.isEmpty())return;
        var g=event.getGuiGraphics();
        int width=Math.min(208,g.guiWidth()/2-12);
        if(width<80)return;
        int right=g.guiWidth()-8,left=right-width,bottom=g.guiHeight()-58;
        // Primary lane nearest the lower-right; later independent lanes above it.
        for(var row:rows){
            String progress=row.total()>1?row.current()+"/"+row.total():"";
            int progressWidth=mc.font.width(progress);
            String title=mc.font.plainSubstrByWidth(text(row.title()).getString(),
                Math.max(0,width-12-progressWidth-6));
            var lines=mc.font.split(text(row.objective()),width-12);
            if(lines.isEmpty())continue;
            // Bound vertical space; data authors should use short actionable text.
            int shown=Math.min(lines.size(),3);
            int height=16+shown*10;
            int top=bottom-height;
            if(top<8)break;
            g.fill(left,top,right,bottom,0xA0101010);
            g.drawString(mc.font,title,left+6,top+4,0xffe5c27a,false);
            if(!progress.isEmpty())g.drawString(mc.font,progress,right-6-progressWidth,top+4,0xffffffff,false);
            for(int i=0;i<shown;i++)
                g.drawString(mc.font,lines.get(i),left+6,top+15+10*i,0xffffffff,false);
            bottom=top-4;
        }
    }
}
