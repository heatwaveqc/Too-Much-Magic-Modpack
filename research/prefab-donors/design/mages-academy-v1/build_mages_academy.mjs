// Original Mage's Academy V1. Minecraft 1.21.1; Node.js stdlib only.
import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {gzipSync,gunzipSync} from 'node:zlib';
import {createHash} from 'node:crypto';
function build(){
 const W=47,H=33,D=36,Y=1,G=new Map(),A="minecraft:air",m=n=>"minecraft:"+n,i=n=>"irons_spellbooks:"+n,key=(x,y,z)=>x+W*(z+D*y);
 const put=(x,y,z,s)=>{if(x<0||y<0||z<0||x>=W||y>=H||z>=D)throw Error("bounds "+[x,y,z]);G.set(key(x,y,z),s)},get=(x,y,z)=>x<0||y<0||z<0||x>=W||y>=H||z>=D?undefined:G.get(key(x,y,z));
 const f=(x,y,z,X,Y,Z,s)=>{for(let a=x;a<=X;a++)for(let b=y;b<=Y;b++)for(let c=z;c<=Z;c++)put(a,b,c,s)};
 const stone=m("stone_bricks"),trim=m("chiseled_stone_bricks"),deep=m("deepslate_tiles"),wood=m("dark_oak_planks"),log=m("stripped_dark_oak_log")+"[axis=y]",purple=m("purple_terracotta"),glass=m("purple_stained_glass"),gold=m("yellow_terracotta"),pillar=i("nether_brick_pillar")+"[axis=y]",shelf=i("wisewood_bookshelf"),lamp=m("lantern")+"[hanging=false,waterlogged=false]";
 const stair=(face)=>m("dark_oak_stairs")+"[facing="+face+",half=bottom,shape=straight,waterlogged=false]";
 // Collegiate hall: 29x23, two levels; tower centered on north side, side reading rooms.
 f(2,0,3,30,0,25,stone);f(2,1,3,30,1,25,wood);
 f(3,2,4,29,16,24,A);
 for(let x=2;x<=30;x++)for(let z=3;z<=25;z++)if(x===2||x===30||z===3||z===25){
   let post=(x===2||x===30)?(z-3)%5===0:(x-2)%5===0;
   for(let y=2;y<=16;y++){let s=y===2||y===9||y===16?stone:post?pillar:(y>=4&&y<=7||y>=11&&y<=14)?((x+z)%3===0?purple:glass):purple;put(x,y,z,s);}
 }
 f(14,2,25,18,5,25,A);f(14,2,3,18,5,3,A);f(30,2,13,30,5,17,A);
 // Floor and split stairs; central corridor opens to front and east workshop/garden.
 f(3,9,4,29,9,24,wood);
 // Stairs run east along north internal wall; emerge onto second floor.
 const steps=[];for(let j=0;j<8;j++){let x=5+j,y=2+j;for(let z=5;z<=6;z++){put(x,y,z,stair("east"));f(x,y+1,z,x,Math.min(y+3,15),z,A);steps.push([x,y,z]);}}
 f(13,9,5,16,9,6,wood);f(13,10,5,16,12,6,A);
 // Two first-floor wings divided by nonlocked arches, dedicated workspace air.
 for(let x of [12,21]){for(let z=9;z<=23;z++){for(let y=2;y<=8;y++)put(x,y,z,y===8||z%5===0?pillar:purple);} f(x,2,13,x,5,17,A);}
 // Main teaching hall lectern and bookshelves only at the ends; retain 9x11 clear space.
 for(let x=14;x<=19;x++){put(x,2,7,shelf);put(x,3,7,shelf);}
 for(let x of [14,19]){put(x,2,22,trim);put(x,3,22,lamp);}
 // Iron's workroom west (x3..11, z9..23). Table y2, both halves same block; face north: right at x5, left at x6.
 put(5,2,10,i("inscription_table")+"[facing=north,type=right,waterlogged=false]");put(6,2,10,i("inscription_table")+"[facing=north,type=left,waterlogged=false]");
 put(9,2,10,i("scroll_forge")+"[facing=south]");
 put(4,2,21,m("crafting_table"));put(5,2,21,m("furnace")+"[facing=north,lit=false]");
 const chest=[7,2,21];put(...chest,m("chest")+"[facing=north,type=single,waterlogged=false]");
 put(9,2,21,m("barrel")+"[facing=north,open=false]");
 for(let x of [4,10]){put(x,2,12,trim);put(x,3,12,lamp);}
 // Future cauldron and arcane anvil stations at (5,2,17),(9,2,17); space below future fluid access, optional.
 for(let x of [5,9])put(x,1,17,stone);
 // Scriptor southeast writing room, six shelves and reserved writing-desk wall bench.
 for(let x=23;x<=28;x++)put(x,2,22,shelf);
 put(23,2,10,m("crafting_table"));put(25,2,10,m("barrel")+"[facing=south,open=false]");put(28,2,10,m("lectern")+"[facing=south,has_book=false,powered=false]");
 for(let z of [12,20]){put(28,2,z,trim);put(28,3,z,lamp);}
 // Second level: two sleeping alcoves and a dedicated large configurable Apothic Enchanting chamber.
 for(let x of [4,5]){put(x,10,20,m("white_bed")+"[facing=north,occupied=false,part=foot]");put(x,10,19,m("white_bed")+"[facing=north,occupied=false,part=head]");}
 for(let x=4;x<=8;x++)put(x,10,23,m("barrel")+"[facing=north,open=false]");
 for(let x of [4,10]){put(x,10,16,trim);put(x,11,16,lamp);}
 // Apache/Apothic modular workspace 13x11 x6, future bookshelves/enchanting table/salvage/reforge; no free gated stations.
 f(15,10,10,27,15,20,A);
 for(let x of [14,28])for(let z of [9,21]){put(x,10,z,trim);put(x,11,z,lamp);}
 for(let x=3;x<=11;x++)if(x!==7)put(x,10,13,wood);
 f(7,10,13,7,12,13,A);
 // Main roof with gabled profile, broad overhang; center tower continues north independently.
 for(let j=0;j<=11;j++){let z1=2+j,z2=26-j,y=17+j;for(let x=1;x<=31;x++){put(x,y,z1,m("deepslate_tile_stairs")+"[facing=south,half=bottom,shape=straight,waterlogged=false]");put(x,y,z2,m("deepslate_tile_stairs")+"[facing=north,half=bottom,shape=straight,waterlogged=false]");}}
 f(1,29,14,31,29,14,deep);
 for(let y=17;y<=27;y++){let j=y-17;for(let z=3+j;z<=25-j;z++)for(let x of [2,30])put(x,y,z,y%3===0?purple:wood);}
 // Central ornamental tower at roof ridge, not a player-accessible false floor. Native decorative pilasters.
 for(let y=17;y<=28;y++)for(let x=12;x<=20;x++)for(let z=7;z<=15;z++)if(x===12||x===20||z===7||z===15)
  put(x,y,z,(x===12||x===20)&&(z===7||z===15)?pillar:y===17||y===27||y===28?stone:(x+z)%3===0?purple:glass);
 // Compact turret battlements at local Y29–32, no valuable blocks, roof ridge above.
 f(11,29,6,21,29,16,deep);for(let x=11;x<=21;x+=2)for(let z of [6,16])f(x,30,z,x,31,z,stone);
 for(let z=8;z<=14;z+=2)for(let x of [11,21])f(x,30,z,x,31,z,stone);
 put(16,32,11,gold);
 // Court: eastern practice yard 11x11 roofless with dry safe targets (no mobs).
 f(33,0,5,45,0,17,stone);f(33,1,5,45,1,17,deep);f(34,2,6,44,H-1,16,A);
 f(30,1,13,34,1,15,stone);f(30,2,13,34,4,15,A);
 for(let x of [33,45])for(let z of [5,17]){put(x,2,z,pillar);put(x,3,z,lamp);}
 // Paper garden, two separate planted rows; water channel edges and access.
 f(32,0,20,45,0,31,stone);f(32,1,20,45,1,31,stone);f(32,2,20,45,5,31,A);
 f(33,1,22,44,1,22,m("sand"));f(33,1,23,44,1,23,m("water")+"[level=0]");f(33,1,24,44,1,24,m("sand"));
 const cane=[];for(let x of [35,38,41])for(let z of [22,24]){put(x,2,z,m("sugar_cane")+"[age=0]");cane.push([x,2,z]);}
 f(43,1,17,45,1,21,stone);f(43,2,17,45,4,21,A);
 f(33,1,25,44,1,29,wood);
 for(let x of [33,44]){put(x,2,29,pillar);put(x,3,29,lamp);}
 // Front approach and small patio.
 f(12,0,26,20,0,33,stone);f(12,1,26,20,1,33,stone);f(13,2,26,19,5,33,A);
 for(let x of [12,20]){put(x,2,30,pillar);put(x,3,30,lamp);}
 // Manifest exact component-bearing chest contents; eight ordinary schools only; Eldritch explicitly excluded.
 const items=[{Slot:0,id:i("graybeard_staff"),count:1},{Slot:1,id:i("copper_spell_book"),count:1},
 {Slot:2,id:i("scroll"),count:1,components:{"irons_spellbooks:spell_container":{maxSpells:1,spellWheel:false,mustEquip:false,data:[{id:i("spectral_hammer"),index:0,level:1,locked:true}]}}},
 {Slot:3,id:i("common_ink"),count:5},
 ...[m("blaze_rod"),i("frozen_bone"),i("lightning_bottle"),i("divine_pearl"),m("ender_pearl"),i("blood_vial"),m("emerald"),m("poisonous_potato")].map((id,j)=>({Slot:j+4,id,count:3}))];
 const payload={pos:chest,nbt:{id:"minecraft:chest",Items:items}};
 const zones=[{name:"Apothic Enchanting adjustable library",bounds:[15,10,10,27,15,20]},{name:"Practice court",bounds:[34,2,6,44,32,16]},{name:"Iron cauldron future position",bounds:[5,2,17,5,3,17]},{name:"Iron arcane anvil future position",bounds:[9,2,17,9,3,17]},{name:"Scriptor writing table reservation",bounds:[24,2,16,26,4,18]}];
 for(let q of zones){let [x,y,z,X,Y,Z]=q.bounds,n=0;for(let a=x;a<=X;a++)for(let b=y;b<=Y;b++)for(let c=z;c<=Z;c++){if(get(a,b,c)!==A)throw Error(q.name+" obstruction "+[a,b,c,get(a,b,c)]);n++}q.air=n;}
 for(let [x,y,z] of steps){if(get(x,y+1,z)!==A||get(x,y+2,z)!==A)throw Error("stair clearance "+[x,y,z]);}
 for(let [x,y,z] of cane){if(get(x,y-1,z)!==m("sand")||get(x,y-1,23)!==m("water")+"[level=0]"||get(x,y+1,z)!==A)throw Error("cane "+[x,y,z]);}
 if(get(...chest)!=="minecraft:chest[facing=north,type=single,waterlogged=false]"||get(chest[0],3,chest[2])!==A)throw Error("chest clearance");
 let counts={};for(let s of G.values())counts[s]=(counts[s]??0)+1;
 if(counts[i("scroll_forge")+"[facing=south]"]!==1||counts["minecraft:chest[facing=north,type=single,waterlogged=false]"]!==1||items.some(x=>["minecraft:paper","minecraft:echo_shard"].includes(x.id))||new Set(items.map(x=>x.Slot)).size!==items.length)throw Error("kit");
 const palette=[...new Set(G.values())],idx=new Map(palette.map((s,j)=>[s,j])),runs=[];
 for(let [k,s] of [...G.entries()].sort((a,b)=>a[0]-b[0])){let p=idx.get(s),last=runs.at(-1);if(last&&last[0]+last[1]===k&&last[2]===p)last[1]++;else runs.push([k,1,p]);}
 return {grid:{size:[W,H,D],palette,runs,blockEntities:[payload]},audit:{status:"STATIC_QA_PREVIEW_ONLY",size:[W,H,D],surfaceY:Y,nonair:G.size-counts[A],explicitAir:counts[A],paletteStates:palette.length,counts,starterChest:payload,schoolCount:8,scroll:{spell:i("spectral_hammer"),level:1,rarity:"UNCOMMON"},cane,stairs:steps.length,zones,limitations:["Faerithia donor binary not audited/available; original academy, no donor geometry or rights claim.","Exact pinned-source items/codec verified; client scroll and inventory load/claim/duplication, inscription halves, modded states, default tile initialization and import untested.","Eldritch focus excluded by owner; no research unlocked or addon school focus included.","Native simple Nether Brick Pillar and Wisewood Bookshelf used; no bulk Wisewood Planks with unverified survival recipe. EMC/material parity pending.","Scriptor writing desk and advanced Iron/Apothic devices reserved not placed; full secondary functional pattern/automation needs live QA.","One-time mutually exclusive starter claim must be enforced before deployment; no runtime or grants wired."]}};
}
function encode(grid){
 const out=[],u16=n=>out.push(n>>>8&255,n&255),i32=n=>out.push(n>>>24&255,n>>>16&255,n>>>8&255,n&255),str=s=>{if(!/^[\x00-\x7f]*$/.test(s))throw Error("nonASCII");u16(s.length);for(let c of s)out.push(c.charCodeAt(0));},head=(t,n)=>{out.push(t);str(n)};
 function named(t,n,v){head(t,n);val(t,v);}
 function type(v,k){if(typeof v==="boolean"||k==="Slot")return 1;if(typeof v==="number")return 3;if(typeof v==="string")return 8;if(Array.isArray(v))return 9;if(v&&typeof v==="object")return 10;throw Error("unknown type");}
 function val(t,v){if(t===1)out.push(Number(v)&255);else if(t===3)i32(v);else if(t===8)str(v);else if(t===9){let k=v.length?type(v[0]):10;out.push(k);i32(v.length);for(let e of v)val(k,e);}else if(t===10){for(let [k,e] of Object.entries(v))named(type(e,k),k,e);out.push(0);}else throw Error("bad tag");}
 const [W,H,D]=grid.size,be=new Map(grid.blockEntities.map(b=>[b.pos.join(),b.nbt]));
 head(10,"");named(3,"DataVersion",3955);named(9,"size",grid.size);
 head(9,"palette");out.push(10);i32(grid.palette.length);for(let s of grid.palette){let [n,p]=s.split("[");let tag={Name:n};if(p)tag.Properties=Object.fromEntries(p.slice(0,-1).split(",").map(s=>s.split("=")));val(10,tag);}
 head(9,"blocks");out.push(10);i32(grid.runs.reduce((a,r)=>a+r[1],0));for(let [start,length,p] of grid.runs)for(let k=start;k<start+length;k++){let pos=[k%W,Math.floor(k/(W*D)),Math.floor(k/W)%D],v={pos,state:p};let payload=be.get(pos.join());if(payload)v.nbt=payload;val(10,v);}
 named(9,"entities",[]);out.push(0);return Uint8Array.from(out);
}
function verify(raw,grid){
 let n=0,v=new DataView(raw.buffer,raw.byteOffset,raw.byteLength),byte=()=>raw[n++],int=()=>{let a=v.getInt32(n);n+=4;return a},str=()=>{let l=v.getUint16(n);n+=2;let s="";while(l--)s+=String.fromCharCode(byte());return s};
 function val(t){if(t===1)return byte();if(t===3)return int();if(t===8)return str();if(t===9){let k=byte(),l=int(),a=[];while(l--)a.push(val(k));return a}if(t===10){let a={};for(let k=byte();k;k=byte()){let s=str();a[s]=val(k)}return a}throw Error("tag "+t)}
 if(byte()!==10||str()!=="")throw Error("root");const r=val(10);if(n!==raw.length||r.DataVersion!==3955||r.size.join()!==grid.size.join()||r.entities.length)throw Error("header");const P=r.palette.map(s=>s.Name+(s.Properties?"["+Object.entries(s.Properties).map(([k,v])=>k+"="+v).join(",")+"]":""));if(P.join("|")!==grid.palette.join("|"))throw Error("palette");
 let j=0,[W,H,D]=grid.size,be=[];for(let [start,l,p] of grid.runs)for(let k=start;k<start+l;k++){let b=r.blocks[j++];if(b.state!==p||b.pos[0]+W*(b.pos[2]+D*b.pos[1])!==k)throw Error("voxel mismatch");if(b.nbt)be.push({pos:b.pos,nbt:b.nbt});}if(j!==r.blocks.length||be.length!==1)throw Error("block count");
 const exp=JSON.stringify(grid.blockEntities,(_,v)=>typeof v==="boolean"?Number(v):v);if(JSON.stringify(be)!==exp)throw Error("chest payload mismatch");
 let items=be[0].nbt.Items,s=items.find(x=>x.Slot===2);if(items.length!==12||s.components["irons_spellbooks:spell_container"].data[0].level!==1||items.some(x=>["minecraft:paper","minecraft:echo_shard"].includes(x.id)))throw Error("kit verify");
 return {voxelCount:j,chestCount:be.length,stacks:items.length,items:items.reduce((a,b)=>a+b.count,0)};
}
const here=path.dirname(fileURLToPath(import.meta.url));
const {grid,audit}=build();if(JSON.stringify(grid)!==JSON.stringify(JSON.parse(fs.readFileSync(path.join(here,'grid.json'),'utf8'))))throw Error('grid mismatch');
const raw=encode(grid);audit.nbtRoundtrip=verify(raw,grid);audit.nbtBytes=raw.length;
const gz=gzipSync(raw,{level:9});if(!gunzipSync(gz).equals(Buffer.from(raw)))throw Error('gzip mismatch');
audit.sha256=createHash('sha256').update(gz).digest('hex');const dir=path.join(here,'prefab-extension');fs.mkdirSync(dir,{recursive:true});fs.writeFileSync(path.join(dir,'mages_academy.nbt'),gz);fs.writeFileSync(path.join(here,'local-audit.json'),JSON.stringify(audit,null,2)+'\n');console.log(JSON.stringify(audit,null,2));
