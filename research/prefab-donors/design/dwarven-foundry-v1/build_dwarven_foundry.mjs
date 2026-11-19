// Original Dwarven Foundry V1; Node stdlib only. Fixed shell/shaft-head, no world-relative shaft placement.
import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {gzipSync,gunzipSync} from 'node:zlib';
import {createHash} from 'node:crypto';
function build(){
 const W=49,H=16,D=37,S=3,A="minecraft:air",G=new Map(),m=n=>"minecraft:"+n,e=n=>"embers:"+n,k=(x,y,z)=>x+W*(z+D*y);
 const p=(x,y,z,s)=>{if(x<0||x>=W||y<0||y>=H||z<0||z>=D)throw Error("bounds "+[x,y,z]);G.set(k(x,y,z),s)},g=(x,y,z)=>x<0||x>=W||y<0||y>=H||z<0||z>=D?undefined:G.get(k(x,y,z)),f=(x,y,z,X,Y,Z,s)=>{for(let a=x;a<=X;a++)for(let b=y;b<=Y;b++)for(let c=z;c<=Z;c++)p(a,b,c,s)};
 const shell=m("deepslate")+"[axis=y]",cob=m("cobbled_deepslate"),brick=m("deepslate_bricks"),tile=m("deepslate_tiles"),pol=m("polished_deepslate"),cam=e("caminite_bricks"),larg=e("caminite_large_bricks"),ct=e("caminite_tiles"),copper=m("cut_copper"),oxid=m("oxidized_cut_copper"),lantern=m("lantern")+"[hanging=false,waterlogged=false]",hanging=m("lantern")+"[hanging=true,waterlogged=false]",chain=m("chain")+"[axis=y,waterlogged=false]",stairs=(face)=>m("deepslate_brick_stairs")+"[facing="+face+",half=bottom,shape=straight,waterlogged=false]";
 // 2-thick enclosing rough rock, with no windows or secondary exits.
 f(0,0,0,48,0,36,shell);f(0,1,0,48,2,36,cob);f(0,3,0,48,3,36,tile);
 f(2,4,2,46,13,34,A);
 for(let y=1;y<=14;y++)for(let z=0;z<=36;z++)for(let x=0;x<=48;x++)if(x<2||x>46||z<2||z>34)p(x,y,z,shell);
 f(0,14,0,48,15,36,shell);
 // No authored rock outside shell except decorated exposed inset face on z=0 and 1.
 // Wide carved dwarven portal, single entrance. Portal geometrics, modest copper accents.
 for(let z of [0,1])for(let y=3;y<=12;y++)for(let x=17;x<=31;x++){
  const dx=Math.abs(x-24);let aperture=y>=4&&y<=10-dx/2&&dx<=4;
  if(aperture)p(x,y,z,A);else if(dx<=6)p(x,y,z,y===3||y===12?tile:dx===6||y===11?brick:cam);
 }
 // level 5-wide door throat, arch above; no forced orientation to compass.
 f(22,4,0,26,7,1,A);f(22,3,0,26,3,1,pol);
 for(let x of [17,31]){f(x,4,0,x,10,0,brick);p(x,7,0,copper);p(x,11,0,ct);}
 for(let x of [19,29]){p(x,4,1,larg);p(x,5,1,lantern);}
 // Main corridor; common center aisle, native accents, raised trim never in reserved rectangles.
 f(20,3,2,28,3,34,pol);f(24,3,2,24,3,34,ct);
 for(let x of [19,29])for(let z=2;z<=34;z++){for(let y=4;y<=13;y++)p(x,y,z,y===4||y===13?brick:z%5===0?larg:cam);}
 // Three side chambers on each side; passage openings and lintels.
 for(let x of [19,29])for(let z0 of [6,18,29]){f(x,4,z0-2,x,8,z0+2,A);for(let z of [z0-3,z0+3]){p(x,4,z,brick);p(x,5,z,larg);}}
 for(let z of [13,25])for(let x of [...Array.from({length:17},(_,i)=>2+i),...Array.from({length:17},(_,i)=>30+i)])for(let y=4;y<=13;y++)p(x,y,z,y===4||y===13?brick:(x%5===0?larg:cam));
 // Native caminite border and interior bands by all chambers; leave general floors solid.
 for(let z0 of [2,14,26]){let z1=z0===26?34:z0+10;for(let x0 of [2,30]){let x1=x0+16;for(let x=x0;x<=x1;x++)for(let z=z0;z<=z1;z++)if(x===x0||x===x1||z===z0||z===z1)p(x,3,z,ct);}}
 // Barrel-vault ribs across central hall; continuous 5-wide walking headroom.
 for(let z of [3,12,23,34]){for(let x of [20,28]){f(x,9,z,x,12,z,brick);p(x,12,z,stairs(x===20?"east":"west"));}f(21,13,z,27,13,z,brick);p(24,12,z,chain);p(24,11,z,chain);p(24,10,z,hanging);}
 for(let x of [3,45])for(let z of [3,12,15,24,27,33]){p(x,4,z,brick);p(x,5,z,lantern);}
 // Front-left humble barracks, empty storage, normal vanilla workstations.
 for(let x of [5,7]){p(x,4,4,m("red_bed")+"[facing=south,occupied=false,part=foot]");p(x,4,5,m("red_bed")+"[facing=south,occupied=false,part=head]");}
 for(let x of [5,7,9,11])p(x,4,11,m("barrel")+"[facing=north,open=false]");
 p(15,4,4,m("crafting_table"));p(16,4,4,m("furnace")+"[facing=south,lit=false]");p(16,4,6,m("blast_furnace")+"[facing=south,lit=false]");
 // Main shop services: floor-only removable access panels, not active conduits.
 const zones=[
 {name:"Metalworking and Ember conversion",bounds:[4,4,15,16,11,23]},
 {name:"Exchange Tablet + pedestals and beam",bounds:[32,4,15,44,11,23]},
 {name:"Inferno Forge / Hearth Coil",bounds:[4,4,27,16,11,33]},
 {name:"Steam / Reservoir / generation expansion",bounds:[32,4,27,44,11,33]},
 {name:"Theurgy alchemy suite",bounds:[32,4,3,44,11,11]}];
 // Independent 2-block-high under-floor passages: access via own shaft covers outside device zones.
 const ducts=[];
 for(let [x0,z0,X,Z] of [[5,17,15,22],[33,17,43,22],[5,28,15,32],[33,28,43,32],[33,5,43,10]]){
  f(x0,1,z0,X,2,Z,A);ducts.push([x0,1,z0,X,2,Z]);
  // Connect each duct to a side access point within that room, hatch at edge, ladder backing.
  let x=x0,z=z0+1;p(x,3,z,m("iron_trapdoor")+"[facing=south,half=bottom,open=false,powered=false,waterlogged=false]");
  // Use wooden trapdoor (hand-open) to avoid needing a powered mechanism.
  p(x,3,z,m("dark_oak_trapdoor")+"[facing=south,half=bottom,open=false,powered=false,waterlogged=false]");f(x,1,z-1,x,2,z-1,pol);
  for(let y=1;y<=2;y++)p(x,y,z,m("ladder")+"[facing=south,waterlogged=false]");
 }
 // Enclosed shaft-head at far end of central aisle. A guardrail to each side, entry from front.
 const hatch=[24,3,32],ladder=m("ladder")+"[facing=south,waterlogged=false]",trap=m("dark_oak_trapdoor")+"[facing=south,half=bottom,open=false,powered=false,waterlogged=false]";
 p(...hatch,trap);for(let y=0;y<=2;y++)p(24,y,32,ladder);f(24,0,31,24,2,31,pol);f(23,0,32,23,2,32,shell);f(25,0,32,25,2,32,shell);f(24,0,33,24,2,33,shell);
 // Trapdoor is at floor level; back/side guard is permanent masonry, front path unblocked.
 for(let x of [23,25]){p(x,4,32,brick);p(x,5,32,larg);}
 f(23,4,33,25,4,33,brick);p(24,5,33,lantern);
 // Keep front entry and main corridor route clear, including shaft approach.
 for(let z=0;z<=31;z++)for(let y=4;y<=6;y++)if(g(24,y,z)!==A)throw Error("central corridor obstacle "+[24,y,z,g(24,y,z)]);
 // Confirm six entrances and complete work envelopes; solid floors or removable covers accepted.
 for(let q of zones){let [x,y,z,X,Y,Z]=q.bounds,n=0;for(let xx=x;xx<=X;xx++)for(let zz=z;zz<=Z;zz++){let below=g(xx,3,zz);if(!below||below===A)throw Error("missing floor "+[xx,zz]);for(let yy=y;yy<=Y;yy++){if(g(xx,yy,zz)!==A)throw Error(q.name+" blocked "+[xx,yy,zz,g(xx,yy,zz)]);n++;}}q.air=n;q.pass=true;}
 for(let x of [19,29])for(let z of [6,18,29])for(let y=4;y<=7;y++)if(g(x,y,z)!==A)throw Error("side doorway");
 // Shell: all nonfront perimeter and roof deepslate, no accidental window in mountain envelope.
 let facadeChecks=0;for(let y=1;y<=14;y++)for(let z=1;z<=36;z++)for(let x=0;x<=48;x++)if(x===0||x===48||z===36){if(g(x,y,z)!==shell)throw Error("shell leak "+[x,y,z,g(x,y,z)]);facadeChecks++;}
 for(let z=0;z<D;z++)for(let x=0;x<W;x++)for(let y=14;y<=15;y++)if(g(x,y,z)!==shell)throw Error("roof shell hole");
 const counts={};for(let s of G.values())counts[s]=(counts[s]??0)+1;
 if(Object.keys(counts).some(s=>/embers:(ember_bore|ember_activator|archaic|inferno|alchemy_tablet|hearth_coil|reservoir|mechanical_core|copper_cell)|theurgy:|minecraft:bedrock|minecraft:lava/.test(s)))throw Error("progression leak");
 const palette=[...new Set(G.values())],idx=new Map(palette.map((s,i)=>[s,i])),runs=[];for(let [l,s] of [...G.entries()].sort((a,b)=>a[0]-b[0])){let i=idx.get(s),last=runs.at(-1);if(last&&last[0]+last[1]===l&&last[2]===i)last[1]++;else runs.push([l,1,i]);}
 return {grid:{size:[W,H,D],palette,runs},audit:{status:"STATIC_QA_PREVIEW_ONLY",size:[W,H,D],surfaceY:S,nonair:G.size-counts[A],explicitAir:counts[A],paletteStates:palette.length,counts,rawDeepslate:counts[shell],nativeCaminite:Object.entries(counts).filter(([s])=>s.startsWith("embers:")).reduce((a,[s,n])=>a+n,0),shaft:{hatch,templateMinY:0,ladderFacing:"south",dynamicRequired:true,enabledFor:"dwarven_foundry",defaultForOtherHouses:false,stockPrefabMinimum:"minBuildHeight+21"},zones,ducts,facadeChecks,limitations:["Only fixed shell and shaft-head built; dynamic shaft and natural bedrock chamber are placement-hook requirements, not implemented runtime.","Stock Prefab shaft terminates minBuildHeight+21, and optional chest collects excavated resources; do not enable stock behavior blindly.","Wall/roof enclosure geometry checked, terrain/anchor/rotation/claims/fluid containment and actual movement pending in-game.","Exact Embers source dimensions verified for MechEdge machines; full machine chaining and Theurgy exact pin geometry pending, no devices supplied.","Caminite decorative cost, all deepslate/copper recoverable mass and material parity pending; no Archaic Bricks, Ember product or research bypass.","Native palette, trapdoor ladder/support behavior, shaft and bottom floor safety, lighting, runtime import and gzip export untested."]}};
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

const here=path.dirname(fileURLToPath(import.meta.url));const {grid,audit}=build();if(JSON.stringify(grid)!==JSON.stringify(JSON.parse(fs.readFileSync(path.join(here,'grid.json'),'utf8'))))throw Error('grid mismatch');const raw=encode(grid);audit.decodedNbtBlocks=verify(raw,grid);const gz=gzipSync(raw,{level:9});if(!gunzipSync(gz).equals(Buffer.from(raw)))throw Error('gzip mismatch');audit.sha256=createHash('sha256').update(gz).digest('hex');const dir=path.join(here,'prefab-extension');fs.mkdirSync(dir,{recursive:true});fs.writeFileSync(path.join(dir,'dwarven_foundry.nbt'),gz);fs.writeFileSync(path.join(here,'local-audit.json'),JSON.stringify(audit,null,2)+'\n');console.log(JSON.stringify(audit,null,2));