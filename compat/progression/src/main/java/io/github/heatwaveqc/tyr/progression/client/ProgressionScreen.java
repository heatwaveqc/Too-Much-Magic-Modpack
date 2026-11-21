package io.github.heatwaveqc.tyr.progression.client;

import com.google.gson.*;
import io.github.heatwaveqc.tyr.progression.foundation.SkillGraph;
import io.github.heatwaveqc.tyr.progression.foundation.TreeLayout;
import io.github.heatwaveqc.tyr.progression.network.ProgressionMenuNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;
import java.util.*;

/** Epiphany-inspired connected module/tree browser, backed solely by server snapshots.
 * Uses the attributed Epiphany-derived TreeLayout. No upstream LDLib2 UI or assets required.
 */
public final class ProgressionScreen extends Screen {
    private static final int NODE_W=98,NODE_H=42,GAP_X=18,GAP_Y=22;
    private String pathId,selection="";
    private JsonObject data;
    private List<String> paths=List.of();
    private final Map<String,JsonObject> nodes=new LinkedHashMap<>();
    private SkillGraph.Graph graph;
    private final Map<String,int[]> panByPath=new HashMap<>();
    private int panX,panY,ticks;
    private Button purchase,respec,activate;
    private boolean confirmRespec;
    public ProgressionScreen(String pathId){super(Component.literal("Progression"));this.pathId=pathId;}
    private void request(){PacketDistributor.sendToServer(new ProgressionMenuNetwork.Request(pathId));}
    private static List<String> strings(JsonObject row,String key){
        List<String> result=new ArrayList<>();
        if(row.has(key))for(var entry:row.getAsJsonArray(key))result.add(entry.getAsString());
        return result;
    }
    private static String label(String id){
        String name=id.substring(id.lastIndexOf(':')+1).replace('_',' ');
        return name.isEmpty()?"":Character.toUpperCase(name.charAt(0))+name.substring(1);
    }
    private static String title(JsonObject row,String fallback){
        return row!=null&&row.has("title")&&!row.get("title").getAsString().isEmpty()?row.get("title").getAsString():label(fallback);
    }
    public static void receive(String json){
        if(!(Minecraft.getInstance().screen instanceof ProgressionScreen screen))return;
        try{
            JsonObject parsed=JsonParser.parseString(json).getAsJsonObject();
            String received=parsed.get("path").getAsString();
            if(!screen.pathId.isEmpty()&&!screen.pathId.equals(received)&&strings(parsed,"paths").contains(screen.pathId))return;
            screen.pathId=received;screen.paths=List.copyOf(strings(parsed,"paths"));
            var next=new LinkedHashMap<String,JsonObject>();var specs=new ArrayList<SkillGraph.Skill>();
            for(var entry:parsed.getAsJsonArray("skills")){
                var row=entry.getAsJsonObject();String id=row.get("id").getAsString();
                next.put(id,row);specs.add(new SkillGraph.Skill(id,row.get("required_rank").getAsInt(),strings(row,"requires"),strings(row,"requires_any")));
            }
            var layout=SkillGraph.arrange(specs,4,NODE_W,NODE_H,GAP_X,GAP_Y);
            screen.nodes.clear();screen.nodes.putAll(next);screen.graph=layout;screen.data=parsed;
            if(!next.containsKey(screen.selection))screen.selection="";
            screen.clampPan();
        }catch(RuntimeException ignored){/* malformed or incomplete network update does not replace the last valid view */}
    }
    @Override protected void init(){
        int x=10,y=28;
        addRenderableWidget(Button.builder(Component.literal("<"),b->cycle(-1)).bounds(x,y,24,20).build());
        addRenderableWidget(Button.builder(Component.literal(">"),b->cycle(1)).bounds(x+29,y,24,20).build());
        addRenderableWidget(Button.builder(Component.literal("Done"),b->onClose()).bounds(Math.max(10,width-66),height-25,56,20).build());
        purchase=addRenderableWidget(Button.builder(Component.literal("Learn rank"),b->{
            JsonObject n=nodes.get(selection);
            if(n!=null&&n.get("buyable").getAsBoolean())PacketDistributor.sendToServer(new ProgressionMenuNetwork.Mutate(pathId,"buy",selection));
        }).bounds(10,height-25,90,20).build());
        respec=addRenderableWidget(Button.builder(Component.literal("Respec"),b->{
            if(data==null||!data.get("joined").getAsBoolean())return;
            if(!confirmRespec){confirmRespec=true;respec.setMessage(Component.literal("Confirm reset"));}
            else{confirmRespec=false;respec.setMessage(Component.literal("Respec"));
                PacketDistributor.sendToServer(new ProgressionMenuNetwork.Mutate(pathId,"respec",""));}
        }).bounds(105,height-25,94,20).build());
        activate=addRenderableWidget(Button.builder(Component.literal("Activate"),b->{
            var node=nodes.get(selection);
            if(node!=null&&node.has("activation_ready")&&node.get("activation_ready").getAsBoolean())
                PacketDistributor.sendToServer(new ProgressionMenuNetwork.Mutate(pathId,"activate",selection));
        }).bounds(204,height-25,80,20).build());
        clampPan();request();
    }
    @Override public void tick(){if(++ticks%40==0)request();}
    private void cycle(int delta){
        if(paths.isEmpty())return;
        String next=paths.get(Math.floorMod(paths.indexOf(pathId)+delta,paths.size()));
        if(next.equals(pathId))return;
        panByPath.put(pathId,new int[]{panX,panY});pathId=next;
        var pos=panByPath.getOrDefault(next,new int[2]);panX=pos[0];panY=pos[1];
        data=null;graph=null;nodes.clear();selection="";confirmRespec=false;
        if(respec!=null)respec.setMessage(Component.literal("Respec"));request();
    }
    private int viewportLeft(){return 10;}
    private int viewportRight(){return width>=510?width-178:width-10;}
    private int viewportTop(){return 76;}
    private int viewportBottom(){return height>=200?height-65:height-32;}
    private boolean inside(double x,double y){return x>=viewportLeft()&&x<viewportRight()&&y>=viewportTop()&&y<viewportBottom();}
    private void clampPan(){
        if(graph==null)return;
        int vw=Math.max(1,viewportRight()-viewportLeft()),vh=Math.max(1,viewportBottom()-viewportTop());
        panX=Math.max(0,Math.min(panX,Math.max(0,graph.layout().width()+16-vw)));
        panY=Math.max(0,Math.min(panY,Math.max(0,graph.layout().height()+12-vh)));
    }
    private int originX(){return viewportLeft()+Math.max(8,(viewportRight()-viewportLeft()-graph.layout().width())/2)-panX;}
    private int originY(){return viewportTop()+6-panY;}
    private void line(GuiGraphics g,String text,int x,int y,int available,int color){
        if(available>0)g.drawString(font,font.plainSubstrByWidth(text,available),x,y,color,false);
    }
    private static int color(JsonObject n){
        if(n.get("owned").getAsInt()>0)return 0xFF284C42;
        if(n.get("buyable").getAsBoolean())return 0xFF72602C;
        if(n.get("auto").getAsBoolean())return 0xFF283C5B;
        return 0xFF302E3B;
    }
    @Override public void render(GuiGraphics g,int mx,int my,float partial){
        renderBackground(g,mx,my,partial);
        g.fill(4,4,width-4,height-4,0xF21A1725);
        line(g,"PROGRESSION",12,12,width-24,0xFFE8DFC9);
        line(g,title(data,pathId)+(data==null?"":data.get("eligible").getAsBoolean()?"":"  [locked]"),69,34,width-80,0xFFD4C4F0);
        JsonObject chosen=nodes.get(selection);
        if(purchase!=null)purchase.active=chosen!=null&&chosen.get("buyable").getAsBoolean();
        if(respec!=null)respec.active=data!=null&&data.get("joined").getAsBoolean();
        if(activate!=null){
            activate.visible=chosen!=null&&chosen.has("activation_mode")&&width>=355;
            activate.active=activate.visible&&chosen.get("activation_ready").getAsBoolean();
            if(activate.visible)activate.setMessage(Component.literal(chosen.get("activation_active").getAsBoolean()?"Active / off":"Activate"));
        }
        if(data!=null&&graph!=null&&viewportBottom()>viewportTop()){
            line(g,(data.get("joined").getAsBoolean()?"Enrolled":"Not enrolled")+"   |   "+label(data.get("xp_channel").getAsString())+
                " level "+data.get("rank").getAsInt()+"   |   "+data.get("points").getAsInt()+" points",12,53,width-24,0xFFDBC895);
            line(g,"Drag to pan · scroll vertically · select a skill",12,65,width-24,0xFFB8B3C6);
            g.fill(viewportLeft(),viewportTop(),viewportRight(),viewportBottom(),0xFF100F19);
            clampPan();int ox=originX(),oy=originY();
            g.enableScissor(viewportLeft(),viewportTop(),viewportRight(),viewportBottom());
            for(var edge:graph.connections()){
                var parent=graph.layout().positions().get(edge.parent());var child=graph.layout().positions().get(edge.child());
                int ax=ox+parent.x()+NODE_W/2,ay=oy+parent.y()+NODE_H,bx=ox+child.x()+NODE_W/2,by=oy+child.y(),mid=(ay+by)/2;
                int c=edge.alternative()?0xFFAF92D9:0xFF748896;
                g.fill(ax,ay,ax+1,mid+1,c);g.fill(Math.min(ax,bx),mid,Math.max(ax,bx)+1,mid+1,c);g.fill(bx,mid,bx+1,by,c);
            }
            for(var row:nodes.entrySet()){
                var n=row.getValue();var pos=graph.layout().positions().get(row.getKey());
                int x=ox+pos.x(),y=oy+pos.y();
                int border=row.getKey().equals(selection)?0xFFF3D375:n.get("buyable").getAsBoolean()?0xFFB9A165:0xFF686477;
                g.fill(x,y,x+NODE_W,y+NODE_H,border);g.fill(x+1,y+1,x+NODE_W-1,y+NODE_H-1,color(n));
                line(g,title(n,row.getKey()),x+5,y+6,NODE_W-10,0xFFF2EDEA);
                String owned=n.get("owned").getAsInt()+"/"+n.get("maximum").getAsInt();
                line(g,owned+(n.get("auto").getAsBoolean()?" · auto":" · "+n.get("price").getAsInt()+" pts"),x+5,y+20,NODE_W-10,0xFFD0CCDD);
                line(g,n.get("level_system").getAsString()+" "+n.get("required_rank").getAsInt(),x+5,y+31,NODE_W-10,0xFFA9C1CC);
            }
            if(nodes.isEmpty())line(g,"No configured skills in this tree.",viewportLeft()+10,viewportTop()+10,viewportRight()-viewportLeft()-20,0xFFB8B3C6);
            g.disableScissor();
            if(width>=510){
                int x=viewportRight()+8,w=width-x-10,y=viewportTop()+5;
                line(g,"EXPERIENCE WALLETS",x,y,w,0xFFE5C87D);y+=13;
                for(var entry:data.getAsJsonArray("wallets")){
                    if(y+22>=viewportBottom()-18)break;
                    var wallet=entry.getAsJsonObject();
                    line(g,wallet.get("name").getAsString()+" · L"+wallet.get("level").getAsInt(),x,y,w,0xFFD7CAE9);
                    line(g,wallet.get("points").getAsInt()+" points · "+wallet.get("xp").getAsLong()+" XP",x,y+10,w,0xFFBCB9C7);y+=23;
                }
                line(g,"Solid: all of · violet: any of",x,viewportBottom()-13,w,0xFFC0A7E1);
            }
            if(chosen!=null){
                int y=viewportBottom()+4;
                line(g,title(chosen,selection)+" | "+chosen.get("price").getAsInt()+" "+chosen.get("point_name").getAsString()+
                    " | "+chosen.get("level_system").getAsString()+" level "+chosen.get("required_rank").getAsInt(),12,y,width-24,0xFFF0D997);
                line(g,String.join(", ",strings(chosen,"effects"))+" | "+(chosen.get("buyable").getAsBoolean()?"Ready to learn":"Locked, automatic, or max rank"),12,y+11,width-24,0xFFD0CCDD);
            }
            if(inside(mx,my)){
                for(var row:nodes.entrySet()){
                    var pos=graph.layout().positions().get(row.getKey());int x=ox+pos.x(),y=oy+pos.y();
                    if(mx>=x&&mx<x+NODE_W&&my>=y&&my<y+NODE_H){
                        var n=row.getValue();List<Component> tooltip=new ArrayList<>();
                        tooltip.add(Component.literal(title(n,row.getKey())));
                        if(n.has("lane")&&!n.get("lane").getAsString().isBlank())tooltip.add(Component.literal(n.get("lane").getAsString()));
                        if(n.has("description")&&!n.get("description").getAsString().isBlank()){
                            for(String wordLine:n.get("description").getAsString().split("\\n")){
                                String remaining=wordLine;
                                while(!remaining.isEmpty()&&tooltip.size()<18){
                                    String chunk=font.plainSubstrByWidth(remaining,220);
                                    if(chunk.isEmpty())break;
                                    tooltip.add(Component.literal(chunk));remaining=remaining.substring(chunk.length()).stripLeading();
                                }
                            }
                        }
                        tooltip.add(Component.literal(n.get("price").getAsInt()+" "+n.get("point_name").getAsString()));
                        tooltip.add(Component.literal("Level: "+n.get("level_system").getAsString()+" "+n.get("required_rank").getAsInt()));
                        if(!strings(n,"requires").isEmpty())tooltip.add(Component.literal("All: "+String.join(", ",strings(n,"requires").stream().map(ProgressionScreen::label).toList())));
                        if(!strings(n,"requires_any").isEmpty())tooltip.add(Component.literal("Any: "+String.join(", ",strings(n,"requires_any").stream().map(ProgressionScreen::label).toList())));
                        if(!strings(n,"milestones").isEmpty())tooltip.add(Component.literal("Milestones: "+String.join(", ",strings(n,"milestones").stream().map(ProgressionScreen::label).toList())));
                        if(!n.get("eligible").getAsBoolean())tooltip.add(Component.literal("Eligibility condition not met"));
                        g.renderComponentTooltip(font,tooltip,mx,my);break;
                    }
                }
            }
        }else line(g,"Requesting skill data from server...",12,65,width-24,0xFFBBB5CB);
        super.render(g,mx,my,partial);
    }
    @Override public boolean mouseClicked(double mx,double my,int button){
        if(super.mouseClicked(mx,my,button))return true;
        if(button!=0||graph==null||!inside(mx,my))return false;
        int ox=originX(),oy=originY();
        for(var row:nodes.entrySet()){
            var pos=graph.layout().positions().get(row.getKey());int x=ox+pos.x(),y=oy+pos.y();
            if(mx>=x&&mx<x+NODE_W&&my>=y&&my<y+NODE_H){selection=row.getKey();break;}
        }
        return true;
    }
    @Override public boolean mouseDragged(double mx,double my,int button,double dx,double dy){
        if(button==0&&graph!=null&&inside(mx,my)){panX-=(int)dx;panY-=(int)dy;clampPan();return true;}
        return super.mouseDragged(mx,my,button,dx,dy);
    }
    @Override public boolean mouseScrolled(double mx,double my,double sx,double sy){
        if(graph!=null&&inside(mx,my)){panY-=(int)(sy*32);panX-=(int)(sx*32);clampPan();return true;}
        return super.mouseScrolled(mx,my,sx,sy);
    }
    @Override public boolean isPauseScreen(){return false;}
}
