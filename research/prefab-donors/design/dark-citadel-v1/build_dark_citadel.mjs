// Original Dark Citadel V1; Node stdlib only.
import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {gzipSync,gunzipSync} from 'node:zlib';
import {createHash} from 'node:crypto';
function build(){
 const W=73,H=34,D=57,S=12,G=new Map(),A="minecraft:air",m=n=>"minecraft:"+n,go=n=>"goety:"+n,k=(x,y,z)=>x+W*(z+D*y);
 const p=(x,y,z,s)=>{if(x<0||x>=W||y<0||y>=H||z<0||z>=D||typeof s!=="string")throw Error("bad voxel "+[x,y,z,s]);G.set(k(x,y,z),s)},g=(x,y,z)=>x<0||x>=W||y<0||y>=H||z<0||z>=D?undefined:G.get(k(x,y,z)),f=(x,y,z,X,Y,Z,s)=>{if(typeof s!=="string")throw Error("bad fill "+[x,y,z,X,Y,Z,s]);for(let a=x;a<=X;a++)for(let b=y;b<=Y;b++)for(let c=z;c<=Z;c++)p(a,b,c,s)};
 const brick=m("deepslate_bricks"),tile=m("deepslate_tiles"),black=m("polished_blackstone_bricks"),crypt=go("crypt_bricks"),pol=go("crypt_stone_polished"),pillar=go("crypt_pillar")+"[axis=y]",wood=go("haunted_planks"),grass=m("grass_block")+"[snowy=false]",soul=m("soul_lantern")+"[hanging=false,waterlogged=false]",hang=m("soul_lantern")+"[hanging=true,waterlogged=false]",chain=m("chain")+"[axis=y,waterlogged=false]",slab=m("polished_blackstone_brick_slab")+"[type=bottom,waterlogged=false]",glass=m("red_stained_glass"),rug=m("red_carpet"),stair=face=>m("deepslate_brick_stairs")+"[facing="+face+",half=bottom,shape=straight,waterlogged=false]";
 // Crypt envelope and grade roof/floor.
 f(3,1,13,48,2,48,brick);f(4,3,14,47,11,47,A);f(3,12,12,48,12,48,black);
 for(let y=3;y<=11;y++)for(let x=3;x<=48;x++)for(let z=13;z<=48;z++)if(x===3||x===48||z===13||z===48)p(x,y,z,y===3||y===11?brick:crypt);
 for(let x=15;x<=35;x++)for(let z=19;z<=39;z++)if(x===15||x===35||z===19||z===39)for(let y=3;y<=11;y++)p(x,y,z,y===3||y===11?pol:(x+z)%5===0?pillar:crypt);
 for(let x=16;x<=34;x++)for(let z=20;z<=38;z++){let r=Math.max(Math.abs(x-25),Math.abs(z-29));p(x,2,z,r===7?pol:r===0?black:tile);}
 for(let x of [15,35])for(let z0 of [25,34])f(x,3,z0-1,x,6,z0+1,A);
 f(23,3,19,27,6,19,A);f(23,3,39,27,6,39,A);
 // West/east chamber walls and independently reachable entrances.
 for(let z0 of [27,36])f(4,3,z0,13,11,z0,crypt);
 for(let z0 of [27,31,42])f(37,3,z0,47,11,z0,crypt);
 for(let x of [13,37])for(let z=14;z<=47;z++)if(![27,31,36,42].includes(z))f(x,3,z,x,11,z,z%4===0?pillar:crypt);
 for(let z0 of [21,31,41])f(13,3,z0-1,13,6,z0+1,A);
 for(let z0 of [20,36,45])f(37,3,z0-1,37,6,z0+1,A);
 for(let x of [14,36])for(let z of [16,23,30,37,44]){p(x,10,z,chain);p(x,9,z,hang);}
 for(let z of [29,33,38,44]){p(5,3,z,pol);p(5,4,z,soul);}
 for(let z of [15,32])for(let x of [38,46]){p(x,3,z,pol);p(x,4,z,soul);}
 p(5,3,15,m("crafting_table"));p(5,3,17,m("barrel")+"[facing=east,open=false]");
 // Ground fortress.
 f(4,13,13,47,25,47,A);
 for(let y=13;y<=26;y++)for(let x=3;x<=48;x++)for(let z=12;z<=48;z++)if(x===3||x===48||z===12||z===48){let post=(x===3||x===48)?(z-12)%6===0:(x-3)%6===0,win=y>=17&&y<=21&&!post&&((x===3||x===48)?(z-12)%6===3:(x-3)%6===3);p(x,y,z,y===13||y===26?black:post?pillar:win?glass:crypt);}
 for(let x=22;x<=28;x++)for(let y=13;y<=21-Math.abs(x-25);y++)p(x,y,12,A);
 for(let x of [20,30]){f(x,13,11,x,21,11,black);p(x,22,11,pol);}
 f(22,12,7,28,12,11,tile);f(22,13,7,28,24,11,A);
 for(let x of [15,35])for(let z=14;z<=47;z++)for(let y=13;y<=25;y++)p(x,y,z,y===13||y===25?black:z%6===0?pillar:crypt);
 for(let x of [15,35])for(let z0 of [19,31,43])f(x,13,z0-2,x,18,z0+2,A);
 for(let z of [27,37])for(let x of [4,36])f(x,13,z,x+11,19,z,crypt);
 for(let z of [27,37])for(let x of [9,41])f(x,13,z,x+2,16,z,A);
 // Roof, battlements, four compact decorative turrets.
 f(3,26,12,48,26,48,black);
 for(let x=3;x<=48;x+=2)for(let z of [12,48])f(x,27,z,x,28,z,crypt);
 for(let z=14;z<=46;z+=2)for(let x of [3,48])f(x,27,z,x,28,z,crypt);
 for(let [x0,z0] of [[3,12],[43,12],[3,43],[43,43]]){for(let y=27;y<=30;y++)for(let x=x0;x<=x0+5;x++)for(let z=z0;z<=z0+5;z++)if(x===x0||x===x0+5||z===z0||z===z0+5)p(x,y,z,crypt);f(x0,31,z0,x0+5,31,z0+5,black);for(let x=x0;x<=x0+5;x+=2)for(let z of [z0,z0+5])p(x,32,z,pol);}
 for(let z of [15,24,33,42]){for(let x of [16,34]){f(x,22,z,x,25,z,pillar);p(x===16?17:33,25,z,stair(x===16?"east":"west"));}f(18,25,z,32,25,z,black);p(25,24,z,chain);p(25,23,z,chain);p(25,22,z,hang);}
 // Throne and hall carpet; haunting wood trim on library floor and residence.
 f(21,13,43,29,13,46,black);f(23,14,45,27,14,46,pol);f(24,15,46,26,17,46,crypt);p(25,15,45,rug);
 for(let z=16;z<=40;z++)for(let x=24;x<=26;x++)p(x,13,z,rug);
 f(4,12,39,14,12,47,wood);f(36,12,14,47,12,26,wood);
 f(48,13,24,48,18,28,A);f(49,12,24,51,12,28,tile);f(49,13,24,51,16,28,A);
 for(let x of [39,41]){p(x,13,15,m("red_bed")+"[facing=south,occupied=false,part=foot]");p(x,13,16,m("red_bed")+"[facing=south,occupied=false,part=head]");}
 for(let x of [39,41,43,45])p(x,13,19,m("barrel")+"[facing=north,open=false]");
 p(45,13,31,m("crafting_table"));p(45,13,32,m("furnace")+"[facing=west,lit=false]");p(45,13,34,m("barrel")+"[facing=west,open=false]");
 for(let z=40;z<=45;z++){p(5,13,z,m("bookshelf"));p(5,14,z,m("bookshelf"));}
 p(10,13,43,m("lectern")+"[facing=east,has_book=false,powered=false]");p(12,13,44,m("barrel")+"[facing=west,open=false]");
 for(let x of [5,46])for(let z of [16,25,34,45])if(g(x,13,z)===A&&g(x,14,z)===A){p(x,13,z,pol);p(x,14,z,soul);}
 // Stair from lower landing at z26 to upper landing z15; 3 wide.
 const steps=[];for(let j=0;j<10;j++){let z=25-j,y=3+j;for(let x=7;x<=9;x++){p(x,y,z,stair("north"));f(x,y+1,z,x,y+3,z,A);steps.push([x,y,z]);}}
 f(7,12,14,9,12,15,black);f(7,13,14,9,16,15,A);f(7,3,26,9,6,26,A);f(13,3,24,13,6,26,A);
 // Grave forecourt, faux stone markers only.
 f(4,12,1,47,12,10,grass);f(4,13,1,47,16,10,A);f(22,12,0,28,12,11,tile);f(22,13,0,28,16,11,A);
 for(let [x,z] of [[7,4],[12,4],[17,4],[32,4],[37,4],[42,4],[7,8],[17,8],[32,8],[42,8]]){p(x,12,z,m("coarse_dirt"));p(x,13,z,crypt);p(x,14,z,slab);p(x,13,z+1,slab);}
 for(let x of [4,47])for(let z of [2,10]){p(x,13,z,pillar);p(x,14,z,soul);}
 for(let [x,z] of [[2,5],[49,5]]){f(x,13,z,x,17,z,m("stripped_dark_oak_log")+"[axis=y]");f(x-1,16,z,x+1,16,z,m("stripped_dark_oak_log")+"[axis=x]");}
 // Roofless altar courtyard and adjacent construct area.
 f(52,12,17,70,12,35,grass);f(52,13,17,70,H-1,35,A);
 for(let x=52;x<=70;x++)for(let z=17;z<=35;z++){let r=Math.max(Math.abs(x-61),Math.abs(z-26));if(r===8||r===0)p(x,12,z,pol);}
 for(let x of [51,71])for(let z of [16,36]){p(x,12,z,black);p(x,13,z,pillar);p(x,14,z,soul);}
 f(52,12,37,70,12,46,grass);f(52,13,37,70,H-1,46,A);f(60,12,36,62,12,36,tile);f(60,13,36,62,16,36,A);
 const zones=[{name:"Goety main ritual crypt",bounds:[16,3,20,34,11,38],cage:[25,3,29],altar:[25,4,29]},{name:"Goety sky court",bounds:[52,13,17,70,33,35],cage:[61,13,26],altar:[61,14,26]},{name:"Servant holding / summoning support",bounds:[6,3,28,12,10,35]},{name:"Necromancy / soul lab",bounds:[6,3,37,12,10,46]},{name:"Eidolon patron chapel A",bounds:[39,3,16,45,10,26]},{name:"Eidolon patron chapel B",bounds:[39,3,33,45,10,41]},{name:"Eidolon crucible / brazier support",bounds:[39,3,43,45,10,46]},{name:"Outdoor large-construct extension",bounds:[52,13,37,70,33,46]}];
 // Explicit route correction: side hall opening at east x35 z25; door leads to sky court.
 f(35,13,24,35,18,26,A);
 for(let q of zones){let [x,y,z,X,Y,Z]=q.bounds,n=0;for(let xx=x;xx<=X;xx++)for(let zz=z;zz<=Z;zz++){let below=g(xx,y-1,zz);if(!below||below===A)throw Error(q.name+" floor");for(let yy=y;yy<=Y;yy++){if(g(xx,yy,zz)!==A)throw Error(q.name+" obstruction "+[xx,yy,zz,g(xx,yy,zz)]);n++;}}q.air=n;q.pass=true;}
 for(let [x,y,z] of steps)if(g(x,y+1,z)!==A||g(x,y+2,z)!==A)throw Error("stair headroom "+[x,y,z]);
 function flood(start,y){let seen=new Set(),queue=[start];const ok=(x,z)=>{let floor=g(x,y-1,z),feet=g(x,y,z);return !!floor&&floor!==A&&!floor.includes("stairs[")&&(feet===A||feet===rug)&&g(x,y+1,z)===A;};for(let i=0;i<queue.length;i++){let [x,z]=queue[i],key=x+","+z;if(seen.has(key)||!ok(x,z))continue;seen.add(key);for(let [dx,dz] of [[1,0],[-1,0],[0,1],[0,-1]])queue.push([x+dx,z+dz]);}return seen;}
 const upper=flood([25,4],13),lower=flood([8,26],3);for(let [x,z] of [[25,20],[8,15],[61,26],[10,42],[41,17],[61,41]])if(!upper.has(x+","+z))throw Error("upper route "+[x,z]+" reachable "+upper.size);
 for(let [x,z] of [[25,29],[9,32],[9,41],[42,21],[42,36],[42,44]])if(!lower.has(x+","+z))throw Error("crypt route "+[x,z]+" reachable "+lower.size);
 const counts={};for(let s of G.values())counts[s]=(counts[s]??0)+1;if(Object.keys(counts).some(s=>/goety:(dark_altar|cursed_cage|grave_soil|pedestal|arca|crypt_plinth|crypt_sarcophagus)|eidolon_repraised:|tombstone:|minecraft:(spawner|sculk|crying_obsidian|obsidian|lava|soul_fire|soul_soil|enchanting_table)/.test(s)))throw Error("progression leak");
 const palette=[...new Set(G.values())],idx=new Map(palette.map((s,i)=>[s,i])),runs=[];for(let [l,s] of [...G.entries()].sort((a,b)=>a[0]-b[0])){let i=idx.get(s),last=runs.at(-1);if(last&&last[0]+last[1]===l&&last[2]===i)last[1]++;else runs.push([l,1,i]);}
 return {grid:{size:[W,H,D],palette,runs},audit:{status:"STATIC_QA_PREVIEW_ONLY",size:[W,H,D],surfaceY:S,nonair:G.size-counts[A],explicitAir:counts[A],paletteStates:palette.length,counts,nativeGoety:Object.entries(counts).filter(([s])=>s.startsWith("goety:")).reduce((n,[s,c])=>n+c,0),zones,stairs:steps.length,walkable:{ground:upper.size,crypt:lower.size},altarRange:8,limitations:["Default source range; exact live config and ritual predicates pending. Scan extends through floor and ceiling; clear horizontal work envelope is not a full exclusion cube.","Corail precise mechanics not source mapped; faux graves only, player-built actual graves and oversized constructs external as needed.","Eidolon heat/water and implements player-installed; no guaranteed servant containment.","Native Goety material recovery and EMC, size parity, native registry, collision, lighting, stairs and grade offset, rotation, exact pinned behavior and deployment pending.","Local Node gzip export not run. No claims or runtime grants."]}};
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

const here=path.dirname(fileURLToPath(import.meta.url));const {grid,audit}=build();if(JSON.stringify(grid)!==JSON.stringify(JSON.parse(fs.readFileSync(path.join(here,'grid.json'),'utf8'))))throw Error('grid mismatch');const raw=encode(grid);audit.decodedNbtBlocks=verify(raw,grid);const gz=gzipSync(raw,{level:9});if(!gunzipSync(gz).equals(Buffer.from(raw)))throw Error('gzip mismatch');audit.sha256=createHash('sha256').update(gz).digest('hex');const dir=path.join(here,'prefab-extension');fs.mkdirSync(dir,{recursive:true});fs.writeFileSync(path.join(dir,'dark_citadel.nbt'),gz);fs.writeFileSync(path.join(here,'local-audit.json'),JSON.stringify(audit,null,2)+'\n');console.log(JSON.stringify(audit,null,2));