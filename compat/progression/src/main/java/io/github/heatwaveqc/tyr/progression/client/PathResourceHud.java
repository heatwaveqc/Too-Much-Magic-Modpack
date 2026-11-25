package io.github.heatwaveqc.tyr.progression.client;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.network.PathResourceNetwork;
import io.github.heatwaveqc.tyr.progression.runtime.resources.ResourcePolicy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import java.lang.ref.WeakReference;
import java.util.List;

/** Personal read-only HUD. Never a local grant or persistent client meter. */
@EventBusSubscriber(modid=ProgressionMod.MOD_ID,value=Dist.CLIENT)
public final class PathResourceHud {
    private PathResourceHud(){}
    private static WeakReference<LocalPlayer> owner=new WeakReference<>(null);
    private static List<PathResourceNetwork.Meter> meters=List.of();
    private static int received=-1;
    public static void accept(List<PathResourceNetwork.Meter> next){
        var p=Minecraft.getInstance().player;
        owner=new WeakReference<>(p);received=p==null?-1:p.tickCount;
        meters=p==null?List.of():List.copyOf(next);
    }
    @SubscribeEvent public static void render(RenderGuiEvent.Post event){
        var mc=Minecraft.getInstance();var p=mc.player;
        if(p==null||p!=owner.get()||!p.isAlive()||p.isSpectator()||mc.options.hideGui||mc.screen!=null
                ||received<0||p.tickCount<received||p.tickCount-received>40)return;
        var g=event.getGuiGraphics();int x=8,y=8,width=Math.min(154,g.guiWidth()-16);
        if(width<50)return;
        for(var m:meters){
            if(y+23>g.guiHeight()-40)break;
            String value=ResourcePolicy.display(m.value(),m.divisor())+" / "+ResourcePolicy.display(m.capacity(),m.divisor());
            int valueWidth=mc.font.width(value);
            String title=mc.font.plainSubstrByWidth(m.title(),Math.max(0,width-valueWidth-5));
            g.fill(x-2,y-2,x+width+2,y+20,0xA0101010);
            g.drawString(mc.font,title,x,y,0xffffffff,false);
            g.drawString(mc.font,value,x+width-valueWidth,y,0xffffffff,false);
            g.fill(x,y+12,x+width,y+18,0xff303030);
            int filled=(int)((long)width*m.value()/m.capacity());
            if(filled>0)g.fill(x,y+12,x+filled,y+18,0xff000000|m.color());
            y+=24;
        }
    }
}
