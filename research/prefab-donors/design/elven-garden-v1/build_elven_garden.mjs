// Original Elven Garden V1; Node stdlib only.
import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {gzipSync,gunzipSync} from 'node:zlib';
import {createHash} from 'node:crypto';
function build(){
 const W=45,H=15,D=33,A="minecraft:air",G=new Map(),k=(x,y,z)=>x+W*(z+D*y),m=n=>"minecraft:"+n,b=n=>"botania:"+n;
 const p=(x,y,z,s)=>{if(x<0||y<0||z<0||x>=W||y>=H||z>=D)throw Error("bounds "+[x,y,z]);G.set(k(x,y,z),s)},g=(x,y,z)=>x<0||y<0||z<0||x>=W||y>=H||z>=D?undefined:G.get(k(x,y,z));
 const f=(x,y,z,X,Y,Z,s)=>{for(let a=x;a<=X;a++)for(let c=z;c<=Z;c++)for(let h=y;h<=Y;h++)p(a,h,c,s)};
 const grass=m("grass_block")+"[snowy=false]",path=m("dirt_path"),rock=b("livingrock"),brick=b("livingrock_bricks"),wood=b("livingwood_planks"),log=b("livingwood_log")+"[axis=y]",framed=b("framed_livingwood"),glass=m("lime_stained_glass"),moss=m("moss_block"),lantern=m("lantern")+"[hanging=false,waterlogged=false]";
 const stair=(name,face)=>name+"[facing="+face+",half=bottom,shape=straight,waterlogged=false]";
 // Minimal landscaped plots, not a complete fenced complex. Grass pads carry no machine or progression input.
 const plots=[{name:"Alfheim Gateway",cx:23,cz:8,r:6},{name:"Mana Enchanter",cx:37,cz:8,r:6},{name:"Mana production and Pure Daisy",cx:23,cz:23,r:6},{name:"Terrasteel",cx:37,cz:22,r:4}];
 for(let q of plots){let {cx,cz,r}=q;f(cx-r,0,cz-r,cx+r,0,cz+r,grass);f(cx-r,1,cz-r,cx+r,H-1,cz+r,A);}
 // Small Runic Altar/Brewery space deliberately separate from Terrasteel.
 f(33,0,28,43,0,32,grass);f(33,1,28,43,H-1,32,A);
 // Paths arranged to leave edges open. Do not pave across reserved plots.
 f(13,0,13,16,0,17,path);f(16,0,15,43,0,16,path);
 f(15,0,17,16,0,28,path);f(30,0,17,32,0,31,path);f(32,0,28,32,0,32,path);
 f(0,0,12,13,0,14,path);
 // Cottage 13x11. One comfortable floor with vaulted gable and organic palette.
 f(2,0,2,14,0,12,brick);f(3,0,3,13,0,11,wood);f(3,1,3,13,9,11,A);
 for(let x=2;x<=14;x++)for(let z=2;z<=12;z++)if(x===2||x===14||z===2||z===12){
  let post=(x===2||x===14)&&(z===2||z===12)||((x===2||x===14)?(z-2)%5===0:(x-2)%4===0);
  for(let y=1;y<=5;y++)p(x,y,z,y===1?rock:y===5?wood:post?log:y===3||y===4?((x+z)%4===0?framed:glass):framed);
 }
 f(7,1,12,9,3,12,A);f(6,0,13,10,0,14,brick);
 // Roof climbs east and west, with distinct warm green planted-edge accents; native wood stairs.
 for(let j=0;j<=6;j++){let x0=1+j,x1=15-j,y=6+j;for(let z=1;z<=13;z++){p(x0,y,z,stair(b("livingwood_planks_stairs"),"east"));p(x1,y,z,stair(b("livingwood_planks_stairs"),"west"));}}
 f(8,13,1,8,13,13,wood);
 for(let y=6;y<=11;y++)for(let x=2+(y-6);x<=14-(y-6);x++)for(let z of [2,12])p(x,y,z,(y%2===0&&x===8)?glass:framed);
 // Basic furnishings, no magical inventory/chest payload.
 for(let x of [4,5]){p(x,1,4,m("white_bed")+"[facing=south,occupied=false,part=foot]");p(x,1,5,m("white_bed")+"[facing=south,occupied=false,part=head]");}
 p(12,1,4,m("crafting_table"));p(12,1,5,m("furnace")+"[facing=west,lit=false]");
 for(let z of [7,9])p(12,1,z,m("barrel")+"[facing=west,open=false]");
 for(let x of [4,12]){p(x,1,10,rock);p(x,2,10,lantern);}
 // Tiny greenhouse conservatory (11x9), not a mana factory. 9x7 interior.
 f(2,0,18,12,0,26,brick);f(3,0,19,11,0,25,grass);f(3,1,19,11,6,25,A);
 for(let x=2;x<=12;x++)for(let z=18;z<=26;z++)if(x===2||x===12||z===18||z===26){
  for(let y=1;y<=4;y++)p(x,y,z,(x===2||x===12)&&(z===18||z===26)||y===4?log:glass);
 }
 for(let z=17;z<=27;z++)for(let j=0;j<=5;j++){let y=5+Math.min(j,4),x=1+j,X=13-j;p(x,y,z,glass);p(X,y,z,glass);}
 f(7,9,17,7,9,27,wood);
 // Seal glass end-gables; keep door passage on east face.
 for(let y=5;y<=8;y++)for(let z of [18,26])for(let x=2+y-5;x<=12-(y-5);x++)p(x,y,z,glass);
 f(12,1,21,12,3,23,A);f(13,0,21,16,0,23,path);f(13,1,21,16,4,23,A);
 // Fey altar / work table intentionally reserved in center; only ordinary flowers in edge planters.
 for(let x of [4,10])for(let z of [20,24]){p(x,1,z,moss);p(x,2,z,m(x===4?"azure_bluet":"pink_petals")+(x===4?"":"[facing=north,flower_amount=2]"));}
 p(4,1,22,m("crafting_table"));p(10,1,22,m("barrel")+"[facing=west,open=false]");
 // First station near cottage door, no stored petals, mana or flowers.
 const apo=[5,1,15];f(3,0,14,7,0,16,grass);f(3,1,14,7,3,16,A);p(...apo,b("apothecary_default"));
 // Small naturally styled pond, fenced nowhere. Ordinary flowering accents outside the workflow plots.
 f(2,0,29,11,0,32,m("dirt"));f(3,0,30,10,0,31,m("water")+"[level=0]");f(2,1,29,11,3,32,A);
 for(let x of [2,11])for(let z of [29,32]){p(x,0,z,moss);p(x,1,z,m("flowering_azalea"));}
 // Low marker only below ground plane at plot centers? leave plain grass, all air above exact footprint.
 const zones=plots.map(q=>({...q,bounds:[q.cx-q.r,1,q.cz-q.r,q.cx+q.r,H-1,q.cz+q.r]}));
 zones.push({name:"Runic Altar / Brewery",bounds:[33,1,28,43,H-1,32]},{name:"Fey altar central reservation",bounds:[6,1,20,8,4,24]});
 for(let q of zones){let [x,y,z,X,Y,Z]=q.bounds;let a=0;for(let xx=x;xx<=X;xx++)for(let yy=y;yy<=Y;yy++)for(let zz=z;zz<=Z;zz++){if(g(xx,yy,zz)!==A)throw Error(q.name+" blocked "+[xx,yy,zz,g(xx,yy,zz)]);a++;}q.air=a;q.pass=true;}
 // Exact source masks: portal span 5x5 in vertical plane, pylon search +/-5; terra 3x3 base and plate; mana enchanter 11x7x3 (ground, flowers, pylons). 
 const masks={portal:{center:[23,1,8],span:[5,5,1],pylonRange:5},terra:{center:[37,1,22],span:[3,2,3]},enchanter:{center:[37,1,8],span:[11,3,7]}};
 const counts={};for(let s of G.values())counts[s]=(counts[s]??0)+1;
 if(counts[b("apothecary_default")]!==1||Object.keys(counts).some(s=>/mana_pool|mana_spreader|pure_daisy|alfheim_portal|terra_plate|pylon|enchanter|fey_altar|gaia_|lapis_block|obsidian/.test(s)))throw Error("progression leak");
 const palette=[...new Set(G.values())],idx=new Map(palette.map((s,i)=>[s,i])),runs=[];
 for(let [key,s] of [...G.entries()].sort((a,b)=>a[0]-b[0])){let i=idx.get(s),last=runs.at(-1);if(last&&last[0]+last[1]===key&&last[2]===i)last[1]++;else runs.push([key,1,i]);}
 return {grid:{size:[W,H,D],palette,runs},audit:{status:"STATIC_QA_PREVIEW_ONLY",size:[W,H,D],surfaceY:0,nonair:G.size-counts[A],explicitAir:counts[A],paletteStates:palette.length,counts,landscapedGrass:counts[grass],nativeBlocks:Object.entries(counts).filter(([s])=>s.startsWith("botania:")).reduce((a,[s,n])=>a+n,0),apothecary:apo,zones,masks,gaia:{mode:"external player-built clearing",radius:12,clearance:"source checks two below through five above beacon; reserve 27x27 level clearing beyond house, no beacon or pylons supplied"},limitations:["Player-led expansion: no prebuilt Gaia arena, no prescribed mana layout, no completed portal or enchanter.","Native Livingwood/Livingrock decorative early-resource grant, material/EMC parity pending.","Alfheim and Enchanter source patterns and envelopes checked; actual floor/subfloor, orientation, claim boundaries, mana lines, spark and pylons need live testing.","Feywild altar recipe and late sovereign altars not supplied; central greenhouse reserved, larger summoning or boss site player-built.","Extra Botany module-specific geometry and exact runtime config need live review.","Open-sky verified only through template height; no in-game placement or NBT gzip export yet."]}};
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

const here=path.dirname(fileURLToPath(import.meta.url));const {grid,audit}=build();if(JSON.stringify(grid)!==JSON.stringify(JSON.parse(fs.readFileSync(path.join(here,'grid.json'),'utf8'))))throw Error('grid mismatch');const raw=encode(grid);audit.decodedNbtBlocks=verify(raw,grid);const gz=gzipSync(raw,{level:9});if(!gunzipSync(gz).equals(Buffer.from(raw)))throw Error('gzip mismatch');audit.sha256=createHash('sha256').update(gz).digest('hex');const dir=path.join(here,'prefab-extension');fs.mkdirSync(dir,{recursive:true});fs.writeFileSync(path.join(dir,'elven_garden.nbt'),gz);fs.writeFileSync(path.join(here,'local-audit.json'),JSON.stringify(audit,null,2)+'\n');console.log(JSON.stringify(audit,null,2));