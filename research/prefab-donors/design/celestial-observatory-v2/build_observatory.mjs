// Original Celestial Observatory V2. Node stdlib only; V1 exact grid is preserved as parent.
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { gzipSync, gunzipSync } from 'node:zlib';
import { createHash } from 'node:crypto';
function revise(old){
 const [W,OH,D]=old.size,H=OH+3,A="minecraft:air",G=new Map(),key=(x,y,z)=>x+W*(z+D*y);
 const put=(x,y,z,s)=>{if(x<0||x>=W||y<0||y>=H||z<0||z>=D)throw Error("bounds "+[x,y,z]);G.set(key(x,y,z),s)};
 const get=(x,y,z)=>G.get(key(x,y,z));
 const fill=(x0,y0,z0,x1,y1,z1,s)=>{for(let y=y0;y<=y1;y++)for(let z=z0;z<=z1;z++)for(let x=x0;x<=x1;x++)put(x,y,z,s)};
 const astr=n=>"astralsorcery:"+n, raw=astr("marble_raw"), brick=astr("marble_bricks"), chisel=astr("marble_chiseled"), sooty=astr("sooty_marble_raw"), sb=astr("sooty_marble_bricks"), wood=astr("infused_wood_planks"), arch=astr("marble_arch"), engraved=astr("marble_engraved"), iw=astr("infused_wood_engraved"), pillar=t=>astr("marble_pillar")+"[pillartype="+t+"]", stairs=(n,f)=>astr(n)+"[facing="+f+",half=bottom,shape=straight,waterlogged=false]";
 const mapping={"minecraft:calcite":raw,"minecraft:smooth_stone":brick,"minecraft:polished_diorite":chisel,"minecraft:deepslate_tiles":sb,"minecraft:polished_deepslate":engraved,"minecraft:dark_oak_planks":wood,"minecraft:stripped_dark_oak_log[axis=y]":iw,"minecraft:polished_diorite_stairs[facing=north,half=bottom,shape=straight,waterlogged=false]":stairs("marble_stairs","north"),"minecraft:dark_oak_stairs[facing=north,half=bottom,shape=straight,waterlogged=false]":stairs("infused_wood_stairs","north")};
 for(let [start,len,p] of old.runs)for(let k=start;k<start+len;k++){let x=k%W,y=Math.floor(k/(W*D)),z=Math.floor(k/W)%D,s=old.palette[p];put(x,y+3,z,mapping[s]??s);}
 // Native pillars on each storey at the existing facade piers, retaining the infused-wood cornices.
 const lantern="minecraft:lantern[hanging=false,waterlogged=false]";
 let columns=0;
 for(let x=23;x<=41;x++)for(let z=36;z<=54;z++){
   if(get(x,8,z)!==iw||get(x,9,z)!==iw||get(x,10,z)!==iw||get(x,11,z)!==iw)continue;
   for(let y=8;y<=11;y++)put(x,y,z,pillar(y===8?"bottom":y===11?"top":"middle"));
   if([14,15,16,17].every(y=>get(x,y,z)===iw))for(let y=14;y<=17;y++)put(x,y,z,pillar(y===14?"bottom":y===17?"top":"middle"));
   columns++;
 }
 if(!columns)throw Error("no facade columns");
 // Accent the door arches, without closing the entries.
 for(let x=31;x<=33;x++)for(let z of [36,54])put(x,11,z,arch);
 for(let z=44;z<=46;z++)for(let x of [23,41])put(x,11,z,arch);
 const pads=[
 {name:"Altar T2–T4",cx:15,cz:15,r:9,originY:6,below:1,height:10,sunken:true},
 {name:"Attunement",cx:46,cz:15,r:11,originY:7,below:1,height:7},
 {name:"Infuser",cx:12,cz:39,r:5,originY:6,below:2,height:6,sunken:true},
 {name:"Gateway",cx:51,cz:39,r:5,originY:7,below:1,height:4},
 {name:"Lightwell and crystal handling",cx:12,cz:53,r:5,originY:6,below:1,height:6,sunken:true,provisional:true},
 {name:"Lumen and auxiliary machinery",cx:51,cz:53,r:5,originY:7,below:1,height:6,provisional:true}
 ];
 const entries=[];
 for(const q of pads){
   let {cx,cz,r,originY,below,sunken}=q;
   if(sunken){
     fill(cx-r,originY-1,cz-r,cx+r,originY-1,cz+r,brick);
     fill(cx-r,originY,cz-r,cx+r,H-1,cz+r,A);
     if(below===2)fill(cx-r,originY-2,cz-r,cx+r,originY-2,cz+r,raw);
     // Approach from the north or south is a stair on the border, outside the machine square.
     const z=q.name.startsWith("Altar")?cz+r+1:cz-r-1, facing=q.name.startsWith("Altar")?"south":"north";
     // High side of stair meets raised path; low side meets recessed pad.
     for(let x=cx-1;x<=cx+1;x++){put(x,6,z,A);put(x,5,z,stairs("marble_stairs",facing));for(let y=7;y<=8;y++)put(x,y,z,A);entries.push([x,5,z]);}
     put(cx,originY-1,cz,engraved);
   }
   if(q.name==="Attunement"){fill(cx-r,6,cz-r,cx+r,6,cz+r,sooty);put(cx,6,cz,engraved);}
   // Retain four corner lanterns with three-block decorative marble columns outside sky envelope.
   for(let dx of [-r-1,r+1])for(let dz of [-r-1,r+1]){
     let x=cx+dx,z=cz+dz;
     put(x,7,z,pillar("bottom"));put(x,8,z,pillar("middle"));put(x,9,z,pillar("top"));put(x,10,z,lantern);
   }
 }
 // Four flush Lightwell installation sites on recessed terrace, no machine provided.
 // Removable marble mounting caps at y5; beneath them, a two-block-high service vault with access from the west.
 const mounts=[[9,50],[15,50],[9,56],[15,56]];
 fill(8,2,49,16,2,57,brick);fill(8,3,49,16,4,57,A);
 // Access passage runs west from vault to outside the pad. Descending from west at x1, floor y6 -> y2.
 fill(3,2,52,8,2,54,brick);fill(3,3,52,8,4,54,A);
 for(let z=52;z<=54;z++)for(let x=1;x<=4;x++){let y=6-x;put(x,y,z,stairs("marble_stairs","west"));for(let yy=y+1;yy<=8;yy++)put(x,yy,z,A);if(y>2)put(x,y-1,z,brick);}
 // At x5..7, leave a roof over vault approach at y5 and preserve pad walking surface.
 fill(5,5,52,7,5,54,brick);
 for(let [x,z] of mounts)put(x,5,z,engraved);
 // Explicitly clear the upper dome interior; no machine placement within enclosed residence.
 // All machine columns remain sky-open through the top of the structure.
 const checks=pads.map(q=>{let {cx,cz,r,originY,below,height}=q,sky=0,support=0;for(let x=cx-r;x<=cx+r;x++)for(let z=cz-r;z<=cz+r;z++){
  for(let y=originY-below;y<originY;y++){let s=get(x,y,z);if(![raw,brick,chisel,sooty,engraved,"minecraft:blue_terracotta"].includes(s))throw Error("foundation "+q.name+" "+[x,y,z,s]);support++;}
  for(let y=originY;y<H;y++){if(get(x,y,z)!==A)throw Error("sky "+q.name+" "+[x,y,z,get(x,y,z)]);sky++;}
 }return {...q,envelope:[2*r+1,height,2*r+1],replaceableSubfloor:support,explicitClearSkyCells:sky,pass:true};});
 for(let [x,z] of mounts){if(get(x,5,z)!==engraved||get(x,4,z)!==A||get(x,3,z)!==A||get(x,6,z)!==A||get(x,7,z)!==A)throw Error("lightwell mount");}
 for(let [x,y,z] of entries)if(!get(x,y,z)?.startsWith(astr("marble_stairs"))||get(x,y+1,z)!==A||get(x,y+2,z)!==A)throw Error("entry clearance");
 // Old house stair tread clearance is preserved.
 for(let i=0;i<6;i++)for(let x=26;x<=27;x++){let y=7+i,z=49-i;if(get(x,y,z)!==stairs("infused_wood_stairs","north")||get(x,y+1,z)!==A||get(x,y+2,z)!==A)throw Error("residence staircase");}
 const counts={};for(let s of G.values())counts[s]=(counts[s]??0)+1;
 const palette=[...new Set(G.values())],idx=new Map(palette.map((s,i)=>[s,i])),runs=[];
 for(let [k,s] of [...G.entries()].sort((a,b)=>a[0]-b[0])){let p=idx.get(s),last=runs[runs.length-1];if(last&&last[0]+last[1]===k&&last[2]===p)last[1]++;else runs.push([k,1,p]);}
 const totals=(f)=>Object.entries(counts).filter(([s])=>f(s)).reduce((a,[s,n])=>a+n,0);
 if(totals(s=>/dark_oak|calcite|polished_diorite|smooth_stone/.test(s)))throw Error("stand-in material remains");
 return {grid:{size:[W,H,D],palette,runs},audit:{status:"STATIC_QA_PREVIEW_ONLY",size:[W,H,D],boundingVolume:W*H*D,anchorSurfaceY:6,nonair:G.size-counts[A],explicitAir:counts[A],paletteStates:palette.length,astralMarble:totals(s=>s.startsWith("astralsorcery:")&&s.includes("marble")),infusedWood:totals(s=>s.startsWith("astralsorcery:infused_wood")),facadePierPositions:columns,reservations:checks,recessedAccessTreads:entries.length,houseStairTreads:12,lightwellMounts:mounts.map(([x,z])=>({x,y:6,z,removableCapY:5,serviceAirY:[3,4]})),counts,limitations:["Static sky through template only; actual terrain and altitude not tested.","Marble and infused wood registry/state source verified at 2.0.3 upstream, exact build 59 untested.","Lightwell service vault and recess stairs need collision/placement test; remove caps for underside extraction.","Auxiliary dimensions and Enigmatic Legacy+ requirements provisional.","Native material economy including recoverable infused wood needs review. No devices, fluids, catalysts or progression grants.","No live game import, block-state initialization, lighting or spawn-safety test."]}};
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

const here=path.dirname(fileURLToPath(import.meta.url));
const old=JSON.parse(fs.readFileSync(path.join(here,'../celestial-observatory-v1/grid.json'),'utf8'));
const {grid,audit}=revise(old);
const recorded=JSON.parse(fs.readFileSync(path.join(here,'grid.json'),'utf8'));
if(JSON.stringify(grid)!==JSON.stringify(recorded))throw Error('Geometry differs from preserved grid');
const raw=encode(grid);
audit.nbtPayloadBytes=raw.length;audit.decodedNbtBlocks=verify(raw,grid);
const gzip=gzipSync(raw,{level:9});if(!gunzipSync(gzip).equals(Buffer.from(raw)))throw Error('Gzip roundtrip');
audit.sha256=createHash('sha256').update(gzip).digest('hex');audit.rawSha256=createHash('sha256').update(raw).digest('hex');
const dir=path.join(here,'prefab-extension');fs.mkdirSync(dir,{recursive:true});
fs.writeFileSync(path.join(dir,'celestial_observatory.nbt'),gzip);fs.writeFileSync(path.join(here,'local-audit.json'),JSON.stringify(audit,null,2)+'\n');console.log(JSON.stringify(audit,null,2));
