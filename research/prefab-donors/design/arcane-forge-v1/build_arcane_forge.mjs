// Modified MIT Wayside Forge; see README.md and LICENSE-MIMICRY. Node stdlib only.
import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {gzipSync,gunzipSync} from 'node:zlib';
import {createHash} from 'node:crypto';
function nbt(d){let p=0,v=new DataView(d.buffer,d.byteOffset,d.byteLength),rd=(n,f)=>{let x=v[f](p);p+=n;return x},byte=()=>rd(1,"getUint8"),i32=()=>rd(4,"getInt32"),str=()=>{let n=rd(2,"getUint16"),s="";for(let i=0;i<n;i++)s+=String.fromCharCode(byte());return s};function val(t){if(t===1)return rd(1,"getInt8");if(t===2)return rd(2,"getInt16");if(t===3)return i32();if(t===4){let x=v.getBigInt64(p);p+=8;return x.toString();}if(t===5)return rd(4,"getFloat32");if(t===6)return rd(8,"getFloat64");if(t===8)return str();if(t===7||t===11||t===12){let n=i32();let a=[];while(n--)a.push(val(t===7?1:t===11?3:4));return a;}if(t===9){let t=byte(),n=i32(),a=[];while(n--)a.push(val(t));return a;}if(t===10){let a={};for(let t=byte();t;t=byte()){let k=str();a[k]=val(t);}return a;}throw Error("tag "+t)}if(byte()!==10)throw Error("root");str();let a=val(10);if(p!==d.length)throw Error("trailing");return a}
function build(src){
 if(src.DataVersion!==3955||src.size.join()!=="23,19,21")throw Error("unexpected donor");
 const W=47,H=19,D=32,A="minecraft:air",G=new Map(),key=(x,y,z)=>x+W*(z+D*y),fa=n=>"forbidden_arcanus:"+n,m=n=>"minecraft:"+n;
 const put=(x,y,z,s)=>{if(x<0||x>=W||y<0||y>=H||z<0||z>=D)throw Error("bounds "+[x,y,z]);G.set(key(x,y,z),s)};
 const get=(x,y,z)=>x<0||x>=W||y<0||y>=H||z<0||z>=D?undefined:G.get(key(x,y,z));
 const f=(x0,y0,z0,x1,y1,z1,s)=>{for(let y=y0;y<=y1;y++)for(let z=z0;z<=z1;z++)for(let x=x0;x<=x1;x++)put(x,y,z,s)};
 const st=(n,p={})=>n+(Object.keys(p).length?"["+Object.entries(p).map(([k,v])=>k+"="+v).join(",")+"]":"");
 const dark=fa("darkstone"),pol=fa("polished_darkstone"),brick=fa("polished_darkstone_bricks"),tile=fa("tiled_polished_darkstone_bricks"),chisel=fa("chiseled_polished_darkstone"),pl=fa("edelwood_planks"),lamp=st(m("lantern"),{hanging:"false",waterlogged:"false"}),core=fa("clibano_core");
 const map={stone_bricks:"polished_darkstone_bricks",cobblestone:"darkstone",mossy_cobblestone:"darkstone",andesite:"darkstone",polished_andesite:"polished_darkstone",cracked_stone_bricks:"cracked_polished_darkstone_bricks",bricks:"polished_darkstone_bricks",stone_brick_stairs:"polished_darkstone_brick_stairs",brick_stairs:"polished_darkstone_brick_stairs",stone_brick_slab:"polished_darkstone_brick_slab",cobblestone_wall:"darkstone_wall",mossy_cobblestone_wall:"darkstone_wall",brick_wall:"polished_darkstone_brick_wall",deepslate_tile_stairs:"polished_darkstone_brick_stairs",deepslate_tile_slab:"polished_darkstone_brick_slab",spruce_planks:"edelwood_planks",dark_oak_planks:"edelwood_planks",spruce_stairs:"edelwood_stairs",dark_oak_stairs:"edelwood_stairs",spruce_slab:"edelwood_slab",dark_oak_slab:"edelwood_slab",spruce_fence:"edelwood_fence",spruce_fence_gate:"edelwood_fence_gate",spruce_door:"edelwood_door",spruce_trapdoor:"edelwood_trapdoor",dark_oak_trapdoor:"edelwood_trapdoor"};
 let replaced=0,removedBe=0,corePos=[],origN=0;
 for(const b of src.blocks){let [x,y,z]=b.pos,q=src.palette[b.state],n=q.Name,p={...(q.Properties??{})},out=st(n,p),short=n.split(":")[1];if(n!==A)origN++;if(b.nbt)removedBe++;
  if(map[short]&&n.startsWith("minecraft:"))out=st(fa(map[short]),p);
  // Retain ordinary oriented timber skeleton, no raw special Edelwood logs.
  if(n==="minecraft:coal_block")out=dark;
  if(n==="minecraft:iron_block")out=pol;
  if(n==="minecraft:structure_block")out=A;
  if(n.startsWith("mimicry:"))out=brick;
  if(n==="minecraft:lava_cauldron")out=m("cauldron");
  if(n==="minecraft:lectern")out=st(n,{...p,has_book:"false",powered:"false"});
  if(n==="minecraft:campfire")out=st(n,{...p,lit:"false"});
  if(n==="minecraft:blast_furnace"){out=st(core,{facing:p.facing??"south"});corePos.push(b.pos);}
  if(n==="minecraft:chest")out=st(n,{...p,type:"single"});
  if(n==="minecraft:spruce_hanging_sign")out=A;
  if(out!==st(n,q.Properties??{}))replaced++;
  put(x,y,z,out);
 }
 // East approach, continuous extension crossing (previous gap corrected).
 f(21,0,17,43,0,19,pol);f(21,1,17,43,2,19,A);
 f(32,0,14,34,0,21,pol);f(32,1,14,34,2,21,A);
 f(42,0,19,44,0,21,pol);f(42,1,19,44,2,21,A);
 // Hephaestus courtyard.
 f(26,0,1,40,0,15,brick);f(27,0,2,39,0,14,pol);f(27,1,2,39,H-1,14,A);
 const FX=33,FZ=8;
 for(let [dx,dz] of [[0,0],[-1,0],[1,0],[0,-1],[0,1],[-3,0],[3,0],[0,-3],[0,3],[-2,-2],[-2,2],[2,-2],[2,2]])put(FX+dx,0,FZ+dz,dark);
 for(let x of [26,40])for(let z of [1,15]){put(x,1,z,chisel);put(x,2,z,brick);put(x,3,z,chisel);put(x,4,z,lamp);}
 for(let x of [28,30,36,38])for(let z of [1,15]){put(x,1,z,chisel);put(x,2,z,lamp);}
 for(let x=27;x<=39;x++)if(x<32||x>34){if(![28,30,36,38].includes(x)){put(x,1,1,brick);put(x,1,15,brick);}}
 for(let z=2;z<=14;z++)if(z<7||z>9){put(26,1,z,brick);put(40,1,z,brick);}
 f(32,1,15,34,3,15,A);
 // Clibano annex and gabled roof.
 f(28,0,20,36,0,28,brick);f(29,0,21,35,0,27,pol);f(29,1,21,35,6,27,A);
 for(let x=28;x<=36;x++)for(let z=20;z<=28;z++)if(x===28||x===36||z===20||z===28)for(let y=1;y<=6;y++)put(x,y,z,y===1||y===6?brick:(x+z)%4===0?chisel:y>=3&&y<=4?m("glass"):brick);
 f(32,1,20,34,3,20,A);f(28,7,20,36,7,28,pl);
 for(let j=0;j<5;j++){let xL=27+j,xR=37-j,y=8+j;for(let z=19;z<=29;z++){put(xL,y,z,st(fa("polished_darkstone_brick_stairs"),{facing:"east",half:"bottom",shape:"straight",waterlogged:"false"}));put(xR,y,z,st(fa("polished_darkstone_brick_stairs"),{facing:"west",half:"bottom",shape:"straight",waterlogged:"false"}));}}
 f(32,12,19,32,12,29,tile);
 for(let y=8;y<=11;y++)for(let x=28+(y-8);x<=36-(y-8);x++)for(let z of [20,28])put(x,y,z,pl);
 for(let x of [28,36]){put(x,1,18,chisel);put(x,2,18,lamp);}
 put(33,0,24,dark);
 // Altar of Light: standalone outdoor work bay, unobstructed sky.
 f(40,0,20,46,0,26,brick);f(41,0,21,45,0,25,pol);f(41,1,21,45,H-1,25,A);put(43,0,23,dark);
 for(let x of [40,46])for(let z of [20,26]){put(x,1,z,chisel);put(x,2,z,lamp);}
 f(41,1,20,45,3,20,A);
 // Donor western room: reserve cauldron over an ordinary extinguished campfire and dry mortar socket.
 if(!get(3,1,9)?.startsWith("minecraft:campfire"))throw Error("donor campfire moved");
 f(3,2,9,3,3,9,A);
 if(get(8,1,11)!==A)throw Error("mortar location occupied");
 f(8,1,11,8,3,11,A);
 if(get(3,1,10)===A)put(3,1,10,m("water_cauldron")+"[level=3]");
 // Explicit clear route, including the original donor's entry throat, to every extension.
 const reservations=[{name:"Hephaestus ritual court",bounds:[27,1,2,39,H-1,14],floorY:0,center:[FX,1,FZ],sky:true},{name:"Clibano annex",bounds:[29,1,21,35,6,27],floorY:0,center:[33,1,24],sky:false},{name:"Altar of Light",bounds:[41,1,21,45,H-1,25],floorY:0,center:[43,1,23],sky:true}];
 for(let q of reservations){let [x0,y0,z0,x1,y1,z1]=q.bounds,n=0;for(let x=x0;x<=x1;x++)for(let y=y0;y<=y1;y++)for(let z=z0;z<=z1;z++){if(get(x,y,z)!==A)throw Error(q.name+" obstruction "+[x,y,z,get(x,y,z)]);n++;}q.explicitAir=n;q.pass=true;}
 const pattern=["***PPP***","*PPPAPPP*","*PAPPPAP*","PPPPCPPPP","PAPCACPAP","PPPPCPPPP","*PAPPPAP*","*PPPAPPP*","***PPP***"];
 let baseSlots=0,countsPer={};for(let z=0;z<9;z++)for(let x=0;x<9;x++){let c=pattern[z][x];if(c==="*")continue;let state=get(FX+x-4,0,FZ+z-4);if(![dark,pol].includes(state))throw Error("forge floor "+[x,z,state]);baseSlots++;countsPer[c]=(countsPer[c]??0)+1;}
 const pedestalPositions=[[-3,0],[3,0],[0,-3],[0,3],[-2,-2],[-2,2],[2,-2],[2,2]].map(([dx,dz])=>[FX+dx,1,FZ+dz]);
 for(let [x,y,z] of pedestalPositions)for(let j=0;j<3;j++)if(get(x,y+j,z)!==A)throw Error("pedestal headroom");
 for(let [x,y,z] of [[3,2,9],[8,1,11]])if(get(x,y,z)!==A)throw Error("alchemy");
 let counts={};for(let s of G.values())counts[s]=(counts[s]??0)+1;
 const n=(p)=>Object.entries(counts).filter(([s])=>p(s)).reduce((a,[s,v])=>a+v,0);
 if(n(s=>s.startsWith(core))!==1||Object.keys(counts).some(s=>/^(mimicry:)|structure_block|iron_block|coal_block|gilded_chiseled|arcane_polished|arcane_crystal|darkstone_pedestal|hephaestus_forge|reliquary:|lava_cauldron|has_book=true/.test(s)))throw Error("progression or payload leak");
 const walk=(x,z)=>{let s=get(x,0,z);return !!s&&s!==A&&[1,2].every(y=>get(x,y,z)===A)};
 let seen=new Set(),queue=[[23,18]];for(let i=0;i<queue.length;i++){let [x,z]=queue[i],k=x+","+z;if(seen.has(k)||!walk(x,z))continue;seen.add(k);for(let [dx,dz] of [[-1,0],[1,0],[0,-1],[0,1]])queue.push([x+dx,z+dz]);}
 for(let [x,z] of [[21,18],[33,8],[33,24],[43,23]])if(!seen.has(x+","+z))throw Error("extension path "+[x,z]);
 const palette=[...new Set(G.values())],index=new Map(palette.map((s,i)=>[s,i])),runs=[];
 for(let [k,s] of [...G.entries()].sort((a,b)=>a[0]-b[0])){let i=index.get(s),last=runs.at(-1);if(last&&last[0]+last[1]===k&&last[2]===i)last[1]++;else runs.push([k,1,i]);}
 return {grid:{size:[W,H,D],palette,runs},audit:{status:"STATIC_QA_PREVIEW_ONLY",size:[W,H,D],anchorSurfaceY:0,boundingVolume:W*H*D,nonair:G.size-counts[A],explicitAir:counts[A],paletteStates:palette.length,donor:{size:src.size,nonair:origN,replacedStates:replaced,discardedEntities:src.entities.length,discardedBlockEntityPayloads:removedBe,retainedVanillaTimberSkeleton:true},nativeDarkstone:n(s=>s.startsWith("forbidden_arcanus:")&&s.includes("darkstone")),edelwood:n(s=>s.startsWith("forbidden_arcanus:edelwood")),corePos:corePos[0],forgeBaseSlots:baseSlots,forgeBaseTypeCounts:countsPer,pedestalPositions,reservations,alchemy:{campfire:[3,1,9],cauldron:[3,2,9],mortar:[8,1,11]},extensionPathCells:seen.size,entities:0,blockEntityPayloads:0,counts,limitations:["Exact mod pin registry/state initialization, original habitation/circulation and actual collision pending.","No formed forge or gilded/arcane/gated components; standalone Clibano Core only.","Original ordinary axis timber skeleton retained; no raw magical Edelwood logs. Material parity pending.","Campfire block accepted per source; extinguished campfire cooking needs live confirmation.","Sky through template top only. Test open terrain placement, ritual activation, obelisks, roof, lighting, rotation and claim.","No live import, block entity initialization or runtime index changes."]}};
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
const donor=fs.readFileSync(path.join(here,'../../mimicry/wayside_forge.nbt'));
const {grid,audit}=build(nbt(gunzipSync(donor)));
if(JSON.stringify(grid)!==JSON.stringify(JSON.parse(fs.readFileSync(path.join(here,'grid.json'),'utf8'))))throw Error('Geometry differs from preserved grid');
const raw=encode(grid);audit.nbtPayloadBytes=raw.length;audit.decodedNbtBlocks=verify(raw,grid);
const gz=gzipSync(raw,{level:9});if(!gunzipSync(gz).equals(Buffer.from(raw)))throw Error('gzip roundtrip');
audit.sha256=createHash('sha256').update(gz).digest('hex');
const dir=path.join(here,'prefab-extension');fs.mkdirSync(dir,{recursive:true});
fs.writeFileSync(path.join(dir,'arcane_forge.nbt'),gz);fs.writeFileSync(path.join(here,'local-audit.json'),JSON.stringify(audit,null,2)+'\n');console.log(JSON.stringify(audit,null,2));