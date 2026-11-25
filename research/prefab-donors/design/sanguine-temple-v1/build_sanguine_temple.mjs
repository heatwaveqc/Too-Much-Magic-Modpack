// Original Sanguine Temple; Node.js standard library only.
import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {gzipSync,gunzipSync} from 'node:zlib';
import {createHash} from 'node:crypto';
function build(input){
 const W=91,H=38,D=85,G=new Map(),A="minecraft:air",key=(x,y,z)=>x+W*(z+D*y);
 const p=(x,y,z,s)=>{if(x<0||x>=W||y<0||y>=H||z<0||z>=D)throw Error("bounds "+[x,y,z]);G.set(key(x,y,z),s)};
 const g=(x,y,z)=>G.get(key(x,y,z));
 const f=(x0,y0,z0,x1,y1,z1,s)=>{for(let y=y0;y<=y1;y++)for(let z=z0;z<=z1;z++)for(let x=x0;x<=x1;x++)p(x,y,z,s)};
 const m=n=>"minecraft:"+n,deep=m("deepslate"),pol=m("polished_deepslate"),tiles=m("deepslate_tiles"),brick=m("deepslate_bricks"),black=m("polished_blackstone_bricks"),red=m("red_terracotta"),crim=m("crimson_planks"),stem=m("stripped_crimson_stem")+"[axis=y]",glass=m("red_stained_glass"),lamp=m("lantern")+"[hanging=false,waterlogged=false]",blood="neovitae:bloodstone",bg="neovitae:blood_stained_glass",altar="neovitae:ara_vitae";
 const stairs=(dir)=>m("polished_deepslate_stairs")+"[facing="+dir+",half=bottom,shape=straight,waterlogged=false]";
 const Y=12,X=45,Z=28,AY=18;
 f(43,Y,33,47,Y,84,pol);f(1,Y,35,89,Y,38,pol);f(11,Y,33,15,Y,49,pol);f(75,Y,33,79,Y,49,pol);f(13,Y,66,35,Y,68,pol);f(57,Y,66,77,Y,68,pol);f(35,Y,68,69,Y,72,pol);
 const courts=[
 {name:"Edge of Hell",id:"simple_dungeon",cx:13,cz:20,r:12,kind:"portal"},
 {name:"Highway to Hell",id:"standard_dungeon",cx:77,cz:20,r:12,kind:"portal"},
 {name:"Spiritus / Crystallum Fractura",id:"deus_crystallum_fractura",cx:14,cz:58,r:8,kind:"spiritus"},
 {name:"General ritual court",cx:77,cz:58,r:8,kind:"general"},
 {name:"Mahou dojo",cx:35,cz:77,r:5,kind:"mahou"}];
 for(const q of courts){let {cx,cz,r}=q;f(cx-r-1,Y,cz-r-1,cx+r+1,Y,cz+r+1,black);f(cx-r,Y,cz-r,cx+r,Y,cz+r,pol);f(cx-r,Y+1,cz-r,cx+r,H-1,cz+r,A);p(cx,Y,cz,deep);
 if(q.kind!=="portal")for(let dx of [-r-1,r+1])for(let dz of [-r-1,r+1]){p(cx+dx,Y+1,cz+dz,black);p(cx+dx,Y+2,cz+dz,lamp);}}
 // Raised sanctuary, clear central interior. Ornament is native bloodstone (not bloodstone bricks).
 f(28,Y,11,62,Y,45,tiles);f(29,13,12,61,24,44,A);
 for(let x=28;x<=62;x++)for(let z=11;z<=45;z++)if(x===28||x===62||z===11||z===45){let post=(x===28||x===62)?(z-11)%5===0:(x-28)%5===0;for(let y=13;y<=24;y++)p(x,y,z,y===13?blood:y===14||y>=23?black:post?stem:y>=17&&y<=20?(y===17?bg:glass):red);}
 for(let x=43;x<=47;x++)for(let z of [11,45]){for(let y=13;y<=18;y++)p(x,y,z,A);p(x,19,z,blood);}
 for(let z=26;z<=30;z++)for(let x of [28,62]){for(let y=13;y<=18;y++)p(x,y,z,A);p(x,19,z,blood);}
 const roof=[19,18,17,16,15,14,15,13,10,7,4,1];
 for(let i=0;i<roof.length;i++){let r=roof[i],prev=i?roof[i-1]:r,out=Math.max(r,prev),inn=i===roof.length-1?-1:Math.min(r,prev)-1;for(let x=X-out;x<=X+out;x++)for(let z=Z-out;z<=Z+out;z++)if(Math.max(Math.abs(x-X),Math.abs(z-Z))>inn)p(x,25+i,z,i===0||i===6?crim:tiles);}
 p(X,37,Z,blood);
 // Stepped solid dais above the main floor; placeholders for every source-defined tier.
 for(const [y,r] of [[13,12],[14,8],[15,5],[16,3],[17,1]])f(X-r,y,Z-r,X+r,y,Z+r,y%2?brick:pol);
 p(X,AY,Z,altar);
 const union=new Map();
 for(const [tier,cs] of Object.entries(input.tiers))for(const c of cs){let k=c.pos.join(",");if(!union.has(k))union.set(k,[]);union.get(k).push({tier,valid:c.valid});}
 let runes=0,cap=0,pillars=0;
 for(const [pos,vs] of union){let [dx,dy,dz]=pos.split(",").map(Number),x=X+dx,y=AY+dy,z=Z+dz,kinds=vs.map(v=>v.valid);if(kinds.includes(altar))continue;
 if(kinds.some(v=>v.includes("/runes"))){p(x,y,z,deep);runes++;}
 else if(kinds.some(v=>v.includes("capstones"))){p(x,y,z,y<=17?pol:A);cap++;}
 else if(kinds.some(v=>v.includes("/pillars"))){p(x,y,z,A);pillars++;}}
 // Vault is below altar and separate from tier upgrade envelope.
 f(35,7,18,55,7,38,deep);f(35,12,18,55,12,38,tiles);f(36,8,19,54,11,37,A);
 for(let x=35;x<=55;x++)for(let z=18;z<=38;z++)if(x===35||x===55||z===18||z===38)f(x,8,z,x,11,z,brick);
 const M=[45,9,28];f(44,8,27,46,8,29,pol);p(45,8,28,deep);
 for(let x of [36,54])for(let z of [19,37]){p(x,8,z,black);p(x,9,z,lamp);}
 // Full floor at Y7 with a three-wide external staircase and service tunnel.
 f(55,7,35,61,7,40,brick);f(55,8,35,61,11,40,A);f(55,12,35,61,12,40,tiles);
 for(let x=55;x<=61;x++)for(let z of [34,41])f(x,8,z,x,12,z,brick);
 f(55,8,35,55,10,37,A);
 const access=[];f(68,12,39,70,12,41,pol);
 for(let i=0;i<6;i++)for(let z=39;z<=41;z++){let x=67-i,y=12-i;p(x,y,z,stairs("east"));if(y>7)p(x,y-1,z,brick);f(x,y+1,z,x,y+3,z,A);access.push([x,y,z]);}
 // Single secured passage with an ordinary wooden door operable from both sides; replace with iron in QA if desired.
 f(55,8,35,55,10,37,brick);for(let y=8;y<=9;y++)p(55,y,36,m("iron_door")+"[facing=east,half="+(y===8?"lower":"upper")+",hinge=left,open=false,powered=false]");
 p(56,8,35,m("lever")+"[face=floor,facing=east,powered=false]");p(54,8,35,m("lever")+"[face=floor,facing=east,powered=false]");
 // Independent wings, no advanced stations. The roof has a shallow pagoda pitch.
 function wing(x0,x1,z0,z1,left){f(x0,Y,z0,x1,Y,z1,pol);f(x0+1,13,z0+1,x1-1,23,z1-1,A);
 for(let x=x0;x<=x1;x++)for(let z=z0;z<=z1;z++)if(x===x0||x===x1||z===z0||z===z1){let post=(x===x0||x===x1)?(z-z0)%4===0:(x-x0)%4===0;for(let y=13;y<=23;y++)p(x,y,z,y===13?blood:y===23?black:post?stem:y>=16&&y<=18?glass:crim);}
 for(let k=0;k<=4;k++)for(let x=x0-1+k;x<=x1+1-k;x++)for(let z=z0-1+k;z<=z1+1-k;z++)if(k===4||x===x0-1+k||x===x1+1-k||z===z0-1+k||z===z1+1-k)p(x,24+k,z,tiles);
 let dx=left?x1:x0,mid=Math.floor((z0+z1)/2);f(dx,13,mid-1,dx,16,mid+1,A);f(left?dx:47,Y,mid-1,left?43:dx,Y,mid+1,pol);}
 wing(27,42,50,69,true);wing(48,64,50,69,false);
 for(let x of [29,32]){p(x,13,52,m("white_bed")+"[facing=south,occupied=false,part=foot]");p(x,13,53,m("white_bed")+"[facing=south,occupied=false,part=head]");}
 p(39,13,52,m("crafting_table"));p(40,13,52,m("furnace")+"[facing=south,lit=false]");
 for(let x=29;x<=33;x++)p(x,13,67,m("barrel")+"[facing=north,open=false]");
 for(let x=35;x<=40;x++)p(x,13,67,m("cherry_planks"));
 for(let x of [29,40])for(let z of [55,65]){p(x,13,z,black);p(x,14,z,lamp);}
 f(50,13,52,62,21,64,A);
 for(let x=50;x<=62;x++)p(x,13,67,crim);for(let x of [50,62]){p(x,13,66,black);p(x,14,66,lamp);}
 // Reflecting pool is ordinary water, not a Mahou spell lake.
 f(50,11,74,62,11,82,tiles);f(50,12,74,62,12,82,black);f(51,12,75,61,12,81,m("water")+"[level=0]");
 // Torii narrower than the central axis, outside the dojo reservation.
 for(let x of [42,48])f(x,13,83,x,18,83,stem);
 f(41,19,83,49,19,83,crim);f(41,20,83,49,20,83,tiles);f(42,17,83,48,17,83,crim);
 const checks=[];
 for(const q of courts){let {cx,cz,r}=q,air=0;for(let x=cx-r;x<=cx+r;x++)for(let z=cz-r;z<=cz+r;z++)for(let y=13;y<H;y++){if(g(x,y,z)!==A)throw Error("court obstruction "+q.name+" "+[x,y,z,g(x,y,z)]);air++;}
 if(q.id)for(const c of input.rituals[q.id]){let [dx,dy,dz]=c.pos;if(g(cx+dx,13+dy,cz+dz)!==A)throw Error("ritual obstruction "+q.id+" "+c.pos);}
 checks.push({...q,origin:[cx,13,cz],footprint:2*r+1,explicitAir:air,staticPass:true,...(q.kind==="portal"?{spawnBoundsVerified:false}:{})});}
 let req=0;
 for(const [tier,cs] of Object.entries(input.tiers))for(const c of cs){let [dx,dy,dz]=c.pos,s=g(X+dx,AY+dy,Z+dz);
 if(c.valid===altar?s!==altar:c.valid.includes("/runes")?s!==deep:c.valid.includes("capstones")?![A,pol].includes(s):c.valid.includes("/pillars")?s!==A:!s)throw Error("tier "+tier+" "+c.pos+" "+s);req++;}
 for(const n of ["torment_nexus","deus_torment_nexus","well_of_suffering","deus_well_of_suffering"])for(const c of input.rituals[n]){let [dx,dy,dz]=c.pos,s=g(M[0]+dx,M[1]+dy,M[2]+dz);if(![A,pol,deep].includes(s))throw Error("vault ritual "+n+" "+c.pos+" "+s);}
 if(AY-M[1]>10)throw Error("altar range");
 for(const [x,y,z] of access)if(g(x,y+1,z)!==A||g(x,y+2,z)!==A)throw Error("stairs "+[x,y,z]);
 for(let x=50;x<=62;x++)for(let y=13;y<=21;y++)for(let z=52;z<=64;z++)if(g(x,y,z)!==A)throw Error("EvilCraft"); 
 const counts={};for(const s of G.values())counts[s]=(counts[s]??0)+1;
 if(counts[altar]!==1||Object.keys(counts).some(s=>/bloodstone_brick|ritual_stone|spawner|capstone|rune_|essentia_vitae|spiked_plate|dark_blood_brick/.test(s)))throw Error("progression leak");
 const palette=[...new Set(G.values())],idx=new Map(palette.map((s,i)=>[s,i])),runs=[];
 for(const [k,s] of [...G.entries()].sort((a,b)=>a[0]-b[0])){let i=idx.get(s),last=runs[runs.length-1];if(last&&last[0]+last[1]===k&&last[2]===i)last[1]++;else runs.push([k,1,i]);}
 return {grid:{size:[W,H,D],palette,runs},audit:{status:"STATIC_QA_PREVIEW_ONLY",size:[W,H,D],volume:W*H*D,anchorSurfaceY:Y,nonair:G.size-counts[A],explicitAir:counts[A],paletteStates:palette.length,altar:{origin:[X,AY,Z],tiers:Object.keys(input.tiers),sourceComponentChecks:req,distinctRuneSockets:runes,capstoneReservations:cap,pillarReservations:pillars},vault:{interior:[19,4,19],floorY:7,ceilingY:12,ritualOrigin:M,altarVerticalDistance:AY-M[1],stairTreads:access.length,rituals:["torment_nexus","deus_torment_nexus","well_of_suffering","deus_well_of_suffering"]},courts:checks,evilcraftClear:[13,9,13],counts,limitations:["Portal template NBT bounds unverified; 25×25 open courts provisional, test both spawned structures in all four orientations.","Exact pinned jar/config, block states, material economy, importer and game validation pending.","Vault door power, movement, mob isolation, spawner/spikes EV and altar discovery require testing.","Spiritus chunk alignment, aspects and crystal growth require testing; assumed Crystallum Fractura.","EvilCraft/Mahou requirements and native decorative palette provisional; no advanced machines."]}};
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
const input=JSON.parse(fs.readFileSync(path.join(here,'source-geometry.json'),'utf8'));
const {grid,audit}=build(input);
if(JSON.stringify(grid)!==JSON.stringify(JSON.parse(fs.readFileSync(path.join(here,'grid.json'),'utf8'))))throw Error('Geometry differs from preserved grid');
const raw=encode(grid);audit.decodedNbtBlocks=verify(raw,grid);
const gzip=gzipSync(raw,{level:9});if(!gunzipSync(gzip).equals(Buffer.from(raw)))throw Error('gzip roundtrip');
audit.sha256=createHash('sha256').update(gzip).digest('hex');
const dir=path.join(here,'prefab-extension');fs.mkdirSync(dir,{recursive:true});
fs.writeFileSync(path.join(dir,'sanguine_temple.nbt'),gzip);fs.writeFileSync(path.join(here,'local-audit.json'),JSON.stringify(audit,null,2)+'\n');console.log(JSON.stringify(audit,null,2));