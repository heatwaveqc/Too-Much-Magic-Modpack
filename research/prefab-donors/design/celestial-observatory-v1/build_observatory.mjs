// Original TMM QA geometry. Node.js standard library only.
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { gzipSync, gunzipSync } from 'node:zlib';
import { createHash } from 'node:crypto';
function build(){
 const W=67,H=24,D=62,G=new Map(),key=(x,y,z)=>x+W*(z+D*y),A="minecraft:air";
 const b=(n,p)=>"minecraft:"+n+(p?"["+Object.entries(p).map(([k,v])=>k+"="+v).join(",")+"]":"");
 const put=(x,y,z,s)=>{if(x<0||x>=W||y<0||y>=H||z<0||z>=D)throw Error("out of bounds "+[x,y,z]);G.set(key(x,y,z),s)};
 const get=(x,y,z)=>G.get(key(x,y,z));
 const fill=(x0,y0,z0,x1,y1,z1,s)=>{for(let y=y0;y<=y1;y++)for(let z=z0;z<=z1;z++)for(let x=x0;x<=x1;x++)put(x,y,z,s)};
 const C=b("calcite"),S=b("smooth_stone"),P=b("polished_diorite"),B=b("blue_terracotta"),K=b("deepslate_tiles"),O=b("dark_oak_planks"),L=b("lantern",{hanging:false,waterlogged:false}),glass=b("cyan_stained_glass"),R=b("polished_deepslate"),wood=b("stripped_dark_oak_log",{axis:"y"});
 const stair=(n)=>b(n,{facing:"north",half:"bottom",shape:"straight",waterlogged:false});
 const reserves=[
 {name:"Altar T2–T4",cx:15,cz:15,r:9,originY:4,below:1,height:10,pattern:[15,8,15]},
 {name:"Attunement",cx:46,cz:15,r:11,originY:4,below:1,height:7,pattern:[19,5,19]},
 {name:"Infuser",cx:12,cz:39,r:5,originY:4,below:2,height:6,pattern:[7,4,7]},
 {name:"Gateway",cx:51,cz:39,r:5,originY:4,below:1,height:4,pattern:[7,2,7]},
 {name:"Lightwell and crystal handling",cx:12,cz:53,r:5,originY:4,below:1,height:6,provisional:true},
 {name:"Lumen and auxiliary machinery",cx:51,cz:53,r:5,originY:4,below:1,height:6,provisional:true}
 ];
 // Axis, connecting walks, southern entry stair.
 fill(30,3,1,33,3,60,S);fill(3,3,29,63,3,32,S);
 fill(11,3,25,18,3,33,S);fill(43,3,27,49,3,32,S);
 fill(10,3,32,14,3,34,S);fill(49,3,32,53,3,34,S);
 fill(10,3,44,14,3,48,S);fill(49,3,44,53,3,48,S);
 fill(18,3,38,24,3,40,S);fill(40,3,38,46,3,40,S);fill(28,3,54,36,3,60,S);
 for(let x=30;x<=33;x++)put(x,2,61,stair("polished_diorite_stairs"));
 for(const q of reserves){
  let {cx,cz,r,below}=q;
  fill(cx-r-1,3,cz-r-1,cx+r+1,3,cz+r+1,P);
  fill(cx-r,3,cz-r,cx+r,3,cz+r,S);
  if(below===2)fill(cx-r,2,cz-r,cx+r,2,cz+r,S);
  for(let x=cx-r;x<=cx+r;x++)for(let z=cz-r;z<=cz+r;z++){
   if((x===cx||z===cz)&&Math.max(Math.abs(x-cx),Math.abs(z-cz))===r)put(x,3,z,B);
   fill(x,4,z,x,H-1,z,A);
  }
  for(let dx of [-r-1,r+1])for(let dz of [-r-1,r+1]){put(cx+dx,4,cz+dz,C);put(cx+dx,5,cz+dz,L);}
  put(cx,3,cz,R); // replaceable guide, not a machine or ritual pattern
 }
 // Relay/assembly expansion alongside the northern axis.
 fill(27,3,7,32,3,23,P);fill(28,4,8,31,23,22,A);
 const X=32,Z=45;
 const inside=(x,z,r)=>Math.max(Math.abs(x-X),Math.abs(z-Z))<=r&&Math.abs(x-X)+Math.abs(z-Z)<=r+Math.floor(r*.57);
 // Octagonal masonry residence, upper timber band and blue glazed dome.
 for(let x=23;x<=41;x++)for(let z=36;z<=54;z++)if(inside(x,z,9)){
  put(x,3,z,K);for(let y=4;y<=15;y++)put(x,y,z,A);if(inside(x,z,8))put(x,3,z,O);put(x,10,z,O);
  if(!inside(x,z,8)){for(let y=4;y<=15;y++){
   let corner=(Math.abs(x-X)+Math.abs(z-Z)>=13)||((x-X)%4===0&&(z===36||z===54))||((z-Z)%4===0&&(x===23||x===41));
   put(x,y,z,y===4||y===9||y===10||y===15?P:corner?wood:(y===6||y===7||y===12||y===13)?glass:C);
  }put(x,16,z,P);}
 }
 for(let x=31;x<=33;x++)for(let y=4;y<=7;y++){put(x,y,36,A);put(x,y,54,A);}
 for(let z=44;z<=46;z++)for(let y=4;y<=7;y++){put(23,y,z,A);put(41,y,z,A);}
 fill(31,3,33,33,3,36,S);fill(18,3,44,23,3,46,S);fill(41,3,44,46,3,46,S);
 // Two-wide stairs; their openings are cleared explicitly.
 const treads=[];
 for(let i=0;i<6;i++)for(let x=26;x<=27;x++){
  let y=4+i,z=49-i;put(x,y,z,stair("dark_oak_stairs"));for(let h=1;h<=3;h++)put(x,y+h,z,A);if(y>4)put(x,y-1,z,O);treads.push([x,y,z]);
 }
 for(let x=26;x<=27;x++){put(x,10,43,O);put(x,11,43,A);put(x,12,43,A);}
 const radii=[8,7,6,5,3,1];
 for(let i=0;i<radii.length;i++){let r=radii[i],prev=i?radii[i-1]:9,y=16+i;
  for(let x=X-prev;x<=X+prev;x++)for(let z=Z-prev;z<=Z+prev;z++)if(inside(x,z,prev)&&(!inside(x,z,r-1)||i===radii.length-1))put(x,y,z,(x===X||z===Z)?glass:(i===0?P:B));
 }
 put(X,22,Z,C);put(X,23,Z,L);
 // Vanilla furnishings only. No payload, loot or magical research bypass.
 put(29,4,39,b("crafting_table"));put(30,4,39,b("furnace",{facing:"south",lit:false}));
 for(let x=34;x<=36;x++)put(x,4,39,b("barrel",{facing:"south",open:false}));
 for(let x of [29,35]){put(x,4,51,b("white_bed",{facing:"north",occupied:false,part:"foot"}));put(x,4,50,b("white_bed",{facing:"north",occupied:false,part:"head"}));}
 for(let x of [28,36])for(let z of [41,49]){put(x,4,z,P);put(x,5,z,L);}
 for(let x=30;x<=34;x++)put(x,11,39,b("bookshelf"));
 for(let x=34;x<=36;x++)put(x,11,50,O);put(35,11,40,b("crafting_table"));
 for(let x of [28,36])for(let z of [42,48]){put(x,11,z,P);put(x,12,z,L);}
 // Check full rectangular footprint, replaceable subfloor, explicit air and independence.
 const checks=reserves.map(q=>{let {cx,cz,r,below,originY,height}=q,bad=[],support=0,sky=0;
  for(let x=cx-r;x<=cx+r;x++)for(let z=cz-r;z<=cz+r;z++){
   for(let y=originY-below;y<originY;y++){let s=get(x,y,z);if(![S,B,R].includes(s))bad.push([x,y,z,s]);else support++;}
   for(let y=originY;y<H;y++){if(get(x,y,z)!==A)bad.push([x,y,z,get(x,y,z)]);else sky++;}
  }if(bad.length)throw Error(q.name+" obstruction "+JSON.stringify(bad.slice(0,10)));
  return {...q,envelope:[2*r+1,height,2*r+1],replaceableSubfloor:support,explicitClearSkyCells:sky,pass:true};
 });
 for(let i=0;i<reserves.length;i++)for(let j=i+1;j<reserves.length;j++){let a=reserves[i],b=reserves[j];if(Math.abs(a.cx-b.cx)<=a.r+b.r&&Math.abs(a.cz-b.cz)<=a.r+b.r)throw Error("overlapping reservations");}
 for(let [x,y,z] of treads)for(let h=1;h<=2;h++)if(get(x,y+h,z)!==A)throw Error("stair obstruction "+[x,y,z,h]);
 // Ground-level reachability on authored floor (unplaced exterior air is assumed for this static test).
 const ok=(x,z)=>x>=0&&x<W&&z>=0&&z<D&&get(x,3,z)&&get(x,3,z)!==A&&[4,5].every(y=>!get(x,y,z)||get(x,y,z)===A);
 const seen=new Set(),queue=[[31,60]];for(let i=0;i<queue.length;i++){let [x,z]=queue[i],k=x+W*z;if(seen.has(k)||!ok(x,z))continue;seen.add(k);for(let [dx,dz] of [[1,0],[-1,0],[0,1],[0,-1]])queue.push([x+dx,z+dz]);}
 const goals=[...reserves.map(q=>[q.cx,q.cz]),[32,45],[31,36],[32,54],[23,45],[41,45],[26,50]];
 for(let [x,z] of goals)if(!seen.has(x+W*z))throw Error("unreachable authored floor "+[x,z]);
 const counts={};for(let s of G.values())counts[s]=(counts[s]??0)+1;
 const palette=[...new Set(G.values())],idx=new Map(palette.map((s,i)=>[s,i])),runs=[];
 for(let [k,s] of [...G.entries()].sort((a,b)=>a[0]-b[0])){let p=idx.get(s),last=runs[runs.length-1];if(last&&last[0]+last[1]===k&&last[2]===p)last[1]++;else runs.push([k,1,p]);}
 const air=counts[A]??0;return {grid:{size:[W,H,D],palette,runs},audit:{status:"STATIC_QA_PREVIEW_ONLY",size:[W,H,D],boundingVolume:W*H*D,anchorSurfaceY:3,nonair:G.size-air,explicitAir:air,paletteStates:palette.length,entities:0,blockEntityPayloads:0,reservations:checks,stairTreads:treads.length,groundRouteGoals:goals.length,groundRouteCells:seen.size,counts,limitations:["Open sky only checked through template top; clear real terrain above.","Auxiliary bays provisional pending exact pinned Astral and Enigmatic Legacy+ device audit.","Ground route assumes unplaced exterior cells are clear; no real collision test.","Replaceable floors are not assembled machines.","No live import, lighting, material economy or progression verification."]}};
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
const {grid,audit}=build();
const recorded=JSON.parse(fs.readFileSync(path.join(here,'grid.json'),'utf8'));
if(JSON.stringify(grid)!==JSON.stringify(recorded))throw Error('Geometry differs from preserved grid');
const raw=encode(grid);
audit.nbtPayloadBytes=raw.length;audit.decodedNbtBlocks=verify(raw,grid);
const gzip=gzipSync(raw,{level:9});
if(!gunzipSync(gzip).equals(Buffer.from(raw)))throw Error('Gzip roundtrip');
audit.sha256= createHash('sha256').update(gzip).digest('hex');
audit.rawSha256=createHash('sha256').update(raw).digest('hex');
const dir=path.join(here,'prefab-extension');fs.mkdirSync(dir,{recursive:true});
fs.writeFileSync(path.join(dir,'celestial_observatory.nbt'),gzip);
fs.writeFileSync(path.join(here,'local-audit.json'),JSON.stringify(audit,null,2)+'\n');
console.log(JSON.stringify(audit,null,2));
