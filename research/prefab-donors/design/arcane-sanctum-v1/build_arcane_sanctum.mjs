// Original Arcane Sanctum V1; Node standard library only.
import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {gzipSync,gunzipSync} from 'node:zlib';
import {createHash} from 'node:crypto';
function build(){
 const W=62,H=16,D=42,A="minecraft:air",G=new Map(),m=n=>"minecraft:"+n,pm=n=>"primalmagick:"+n,t=n=>"totemic:"+n,k=(x,y,z)=>x+W*(z+D*y);
 const p=(x,y,z,s)=>{if(x<0||y<0||z<0||x>=W||y>=H||z>=D)throw Error("bounds "+[x,y,z]);G.set(k(x,y,z),s)},g=(x,y,z)=>x<0||y<0||z<0||x>=W||y>=H||z>=D?undefined:G.get(k(x,y,z)),f=(x,y,z,X,Y,Z,s)=>{for(let a=x;a<=X;a++)for(let h=y;h<=Y;h++)for(let c=z;c<=Z;c++)p(a,h,c,s)};
 const grass=m("grass_block")+"[snowy=false]",mud=m("coarse_dirt"),path=m("dirt_path"),raw=pm("marble_raw"),brick=pm("marble_bricks"),tile=pm("marble_tiles"),rune=pm("marble_runed"),chisel=pm("marble_chiseled"),pill=type=>pm("marble_pillar")+"[type="+type+"]",c=t("cedar_planks"),log=t("cedar_log")+"[axis=y]",moss=m("moss_block"),lantern=m("lantern")+"[hanging=false,waterlogged=false]",stair=face=>t("cedar_stairs")+"[facing="+face+",half=bottom,shape=straight,waterlogged=false]";
 // Open stone sanctuary: 33x33 clear usable surface, full Manhattan radius15 and one block margin.
 const CX=18,CZ=18;f(2,0,2,34,0,34,grass);f(2,1,2,34,H-1,34,A);
 for(let x=2;x<=34;x++)for(let z=2;z<=34;z++){let dx=x-CX,dz=z-CZ,d=Math.hypot(dx,dz);if(d<=15.7)p(x,0,z,d>=14.5?brick:d>=13.5?rune:(dx===0||dz===0)&&d>=3?tile:raw);if(d<=2.1)p(x,0,z,d===0?chisel:tile);}
 // Standing stones outside the clear square (not in salt network or ritual scan).
 for(let [x,z] of [[1,7],[1,18],[1,29],[35,7],[35,29],[7,1],[18,1],[29,1],[7,35],[18,35],[29,35]]){
  p(x,0,z,brick);p(x,1,z,pill("bottom"));p(x,2,z,pill("base"));p(x,3,z,pill("base"));p(x,4,z,pill("top"));p(x,5,z,chisel);
 }
 // East entrance open, paths depart around the separate housing/ceremonial spaces.
 f(35,0,16,40,0,20,path);f(35,1,16,40,3,20,A);f(37,0,5,39,0,36,path);f(37,1,5,39,3,36,A);
 f(38,0,11,42,0,13,path);f(38,1,11,42,3,13,A);
 f(38,0,28,42,0,30,path);f(38,1,28,42,3,30,A);
 // 19x15 cedar and marble single-storey scholar-longhouse; two wings with central foyer.
 f(41,0,2,59,0,16,brick);f(42,0,3,58,0,15,c);f(42,1,3,58,11,15,A);
 for(let x=41;x<=59;x++)for(let z=2;z<=16;z++)if(x===41||x===59||z===2||z===16){
  let post=(x===41||x===59)&&(z===2||z===16)||((x===41||x===59)?(z-2)%4===0:(x-41)%4===0);
  for(let y=1;y<=5;y++)p(x,y,z,y===1?brick:y===5?c:post?log:y>=3&&((x+z)%3===0)?m("amber_stained_glass").replace("amber","orange"):c);
 }
 f(41,1,11,41,3,13,A);
 for(let j=0;j<=8;j++)for(let z=1;z<=17;z++){p(40+j,6+j,z,stair("east"));p(60-j,6+j,z,stair("west"));}
 f(49,15,1,51,15,17,c);
 for(let y=6;y<=13;y++)for(let x=41+y-6;x<=59-(y-6);x++)for(let z of [2,16])p(x,y,z,(x===50&&y%2===1)?m("orange_stained_glass"):c);
 // Research wing clear 11x9, center (52,1,8); wall-side vanilla bookshelves aid early Grimoire creation.
 // Keep all future research-aid blocks player-installed. No arcane workbench/research table is pre-granted.
 for(let x=44;x<=48;x++)p(x,1,4,m("bookshelf"));
 for(let x=51;x<=55;x++)p(x,1,4,m("bookshelf"));
 p(43,1,4,m("crafting_table"));p(43,1,6,m("furnace")+"[facing=east,lit=false]");
 p(43,1,8,m("barrel")+"[facing=east,open=false]");p(43,1,10,raw);p(43,2,10,lantern);
 // Residential corner separated by chest-height cedar screen; beds accessible from south.
 for(let x of [55,57]){p(x,1,14,m("green_bed")+"[facing=north,occupied=false,part=foot]");p(x,1,13,m("green_bed")+"[facing=north,occupied=false,part=head]");}
 for(let x of [52,53])p(x,1,15,m("barrel")+"[facing=north,open=false]");
 for(let x of [45,47]){p(x,1,15,brick);p(x,2,15,lantern);}
 // Outdoor Totemic dance grove: 17x17 free volume, natural ground, plain cedar border outside working area.
 const TX=51,TZ=29;f(43,0,21,59,0,37,grass);f(43,1,21,59,H-1,37,A);
 for(let x=43;x<=59;x++)for(let z=21;z<=37;z++){let dx=x-TX,dz=z-TZ,d=Math.hypot(dx,dz);if(d<=7.5)p(x,0,z,d>=6.5?mud:grass);if(d<=1.5)p(x,0,z,c);}
 // Decorative uncarved cedar pillars, deliberately NOT effective totem poles; open southern and west approach.
 for(let [x,z] of [[42,21],[60,21],[42,37],[60,37],[45,20],[57,20],[45,38],[57,38]]){p(x,0,z,moss);p(x,1,z,log);p(x,2,z,log);p(x,3,z,c);p(x,4,z,lantern);}
 // Simple planted crop and flower margin outside both functional courts; no mobs, seeds or active instruments.
 for(let [x,z,flower] of [[4,38,"poppy"],[7,38,"azure_bluet"],[10,38,"dandelion"],[14,39,"oxeye_daisy"],[30,38,"poppy"],[33,39,"azure_bluet"],[41,40,"dandelion"],[60,40,"oxeye_daisy"]]){p(x,0,z,grass);p(x,1,z,m(flower));}
 // Neutral dirt areas for nearby animal/growth ceremonies outside ceremony footprint, no animals.
 f(18,0,37,27,0,41,grass);f(18,1,37,27,4,41,A);
 // Verify reserve floor and full air; exact ritual Manhattan scan, no pre-laid salt.
 const zones=[{name:"Primal ritual court",bounds:[2,1,2,34,15,34]},{name:"Totemic dance grove",bounds:[43,1,21,59,15,37]},{name:"Research aid/workbench cluster",bounds:[46,1,6,54,4,11]},{name:"Growth ceremony extension",bounds:[18,1,37,27,4,41]}];
 for(let q of zones){let [x,y,z,X,Y,Z]=q.bounds,n=0;for(let a=x;a<=X;a++)for(let b=z;b<=Z;b++){if(!g(a,0,b)||g(a,0,b)===A)throw Error(q.name+" floor");for(let h=y;h<=Y;h++){if(g(a,h,b)!==A)throw Error(q.name+" obstacle "+[a,h,b,g(a,h,b)]);n++;}}q.air=n;q.pass=true;}
 let ritualCells=0;for(let dx=-15;dx<=15;dx++)for(let dz=-15;dz<=15;dz++)if(Math.abs(dx)+Math.abs(dz)<=15){if(!g(CX+dx,0,CZ+dz)||g(CX+dx,1,CZ+dz)!==A||g(CX+dx,2,CZ+dz)!==A||g(CX+dx,3,CZ+dz)!==A)throw Error("salt envelope");ritualCells++;}
 // Walkability of flat terrain from front approach to each zone, no floor gaps. Interior furniture is collision obstacle.
 const start=[38,18],seen=new Set(),queue=[start],walk=(x,z)=>!!g(x,0,z)&&g(x,0,z)!==A&&g(x,1,z)===A&&g(x,2,z)===A;
 for(let i=0;i<queue.length;i++){let [x,z]=queue[i],s=x+","+z;if(seen.has(s)||!walk(x,z))continue;seen.add(s);for(let [dx,dz] of [[1,0],[-1,0],[0,1],[0,-1]])queue.push([x+dx,z+dz]);}
 // Court boundary to paths: clear air at z18 x34, x35..40; grove link to x43, housing entry adjacent.
 for(let [x,z] of [[CX,CZ],[TX,TZ],[50,10]])if(!seen.has(x+","+z))throw Error("path gap "+[x,z]);
 const counts={};for(let s of G.values())counts[s]=(counts[s]??0)+1;
 if(Object.keys(counts).some(s=>/primalmagick:(ritual_altar|offering_pedestal|salt_trail|research_table|arcane_workbench|wood_table|.*_font|marble_(enchanted|hallowed|smoked))|totemic:totem_(base|pole)/.test(s)))throw Error("gated station");
 const palette=[...new Set(G.values())],idx=new Map(palette.map((s,i)=>[s,i])),runs=[];for(let [l,s] of [...G.entries()].sort((a,b)=>a[0]-b[0])){let i=idx.get(s),last=runs.at(-1);if(last&&last[0]+last[1]===l&&last[2]===i)last[1]++;else runs.push([l,1,i]);}
 return {grid:{size:[W,H,D],palette,runs},audit:{status:"STATIC_QA_PREVIEW_ONLY",size:[W,H,D],surfaceY:0,nonair:G.size-counts[A],explicitAir:counts[A],paletteStates:palette.length,counts,nativeMarble:Object.entries(counts).filter(([s])=>s.startsWith("primalmagick:")).reduce((a,[s,n])=>a+n,0),nativeCedar:Object.entries(counts).filter(([s])=>s.startsWith("totemic:")).reduce((a,[s,n])=>a+n,0),zones,ritual:{center:[CX,1,CZ],manhattanRange:15,safeSalt:16,horizontalCells:ritualCells},totemic:{center:[TX,1,TZ],maxPoleSize:5},walkableConnectedCells:seen.size,limitations:["No active ancient shrine/font, dust, arcane workbench, research station, ritual altar, props or carved totems; player obtains initiation and unlocks normally.","Primal ritual range is theoretical Manhattan distance, not a required filled salt pattern; avoid >16 salt unless stabilized. Gameplay and vertical/prop scan validation pending.","Totemic exact-tag MAX_POLE_SIZE=5; default effect range 5–10, music acceptance depends on actual instrument range; ceremony geometry/expanded effects and pack restrictions need live QA.","Primal research aids within five blocks; actual aid types, stations and magitech expansion are player-built, not a complete lab. Exact other device requirements pending.","Native decorative palette and recoverable material balance pending; source aligned, not game-tested.","Terrain/anchor/rotation, sky, full geometry, lighting, stairs/roof and NBT import pending."]}};
}
function encode(grid){
 const out=[],u16=n=>{out.push(n>>>8&255,n&255)},i32=n=>{out.push(n>>>24&255,n>>>16&255,n>>>8&255,n&255)};
 const str=s=>{if(!/^[\x00-\x7f]*$/.test(s))throw Error("non-ASCII NBT string");u16(s.length);for(let c of s)out.push(c.charCodeAt(0));},head=(t,n)=>{out.push(t);str(n)},namedInt=(n,v)=>{head(3,n);i32(v)},namedStr=(n,v)=>{head(8,n);str(v)};
 const intList=(n,vals)=>{head(9,n);out.push(3);i32(vals.length);vals.forEach(i32);};
 const [W,H,D]=grid.size;
 head(10,"");namedInt("DataVersion",3955);intList("size",grid.size);
 head(9,"palette");out.push(10);i32(grid.palette.length);
 for(let s of grid.palette){let [name,prop]=s.split("[");namedStr("Name",name);if(prop){head(10,"Properties");for(let kv of prop.slice(0,-1).split(",")){let [k,v]=kv.split("=");namedStr(k,v);}out.push(0);}out.push(0);}
 head(9,"blocks");out.push(10);i32(grid.runs.reduce((a,r)=>a+r[1],0));
 for(let [start,length,p] of grid.runs)for(let k=start;k<start+length;k++){let x=k%W,y=Math.floor(k/(W*D)),z=Math.floor(k/W)%D;intList("pos",[x,y,z]);namedInt("state",p);out.push(0);}
 head(9,"entities");out.push(10);i32(0);out.push(0);
 return Uint8Array.from(out);
}
function verify(raw,grid){
 let n=0;const dv=new DataView(raw.buffer,raw.byteOffset,raw.byteLength),byte=()=>raw[n++],int=()=>{let r=dv.getInt32(n);n+=4;return r;},str=()=>{let l=dv.getUint16(n);n+=2;let s="";for(let i=0;i<l;i++)s+=String.fromCharCode(byte());return s;};
 function value(t){if(t===3)return int();if(t===8)return str();if(t===9){let k=byte(),l=int(),a=[];for(let i=0;i<l;i++)a.push(value(k));return a;}if(t===10){let a={};for(let k=byte();k!==0;k=byte()){let s=str();a[s]=value(k);}return a;}throw Error("unsupported tag "+t);}
 if(byte()!==10||str()!=="")throw Error("root");const r=value(10);if(n!==raw.length||r.DataVersion!==3955||r.size.join()!==grid.size.join()||r.entities.length)throw Error("header");
 const P=r.palette.map(s=>s.Name+(s.Properties?"["+Object.entries(s.Properties).map(([k,v])=>k+"="+v).join(",")+"]":""));
 if(P.join("|")!==grid.palette.join("|"))throw Error("palette");
 let j=0,[W,H,D]=grid.size;for(let [start,l,p] of grid.runs)for(let k=start;k<start+l;k++){let v=r.blocks[j++];if(v.state!==p||v.pos[0]+W*(v.pos[2]+D*v.pos[1])!==k||v.nbt)throw Error("voxel mismatch");}if(j!==r.blocks.length)throw Error("extra blocks");return j;
}

const here=path.dirname(fileURLToPath(import.meta.url));const {grid,audit}=build();if(JSON.stringify(grid)!==JSON.stringify(JSON.parse(fs.readFileSync(path.join(here,'grid.json'),'utf8'))))throw Error('grid mismatch');const raw=encode(grid);audit.decodedNbtBlocks=verify(raw,grid);const gz=gzipSync(raw,{level:9});if(!gunzipSync(gz).equals(Buffer.from(raw)))throw Error('gzip mismatch');audit.sha256=createHash('sha256').update(gz).digest('hex');const dir=path.join(here,'prefab-extension');fs.mkdirSync(dir,{recursive:true});fs.writeFileSync(path.join(dir,'arcane_sanctum.nbt'),gz);fs.writeFileSync(path.join(here,'local-audit.json'),JSON.stringify(audit,null,2)+'\n');console.log(JSON.stringify(audit,null,2));