"""Read-only exact-pin source evidence for the Mahou -> Discidia compatibility adapter."""
import hashlib
import pathlib
import re
import subprocess
import tempfile
import urllib.request
import zipfile

PINS = {
    "mahou": ("sha256", "4022fb3cee32b28df02994cc4c5b40069651583516f6abd04775789616739aad",
        ["https://cdn.modrinth.com/data/7jOxn6qJ/versions/GrYWbBFj/mahoutsukai-1.21.1-v1.36.31.jar"]),
    "astral": ("sha1", "66f36655eae7cb404c9c16da6f33db31dff6ed9a",
        ["https://mediafilez.forgecdn.net/files/9281/73/AstralSorcery-2.0.3.59.jar",
         "https://mediafilez.forgecdn.net/files/9281/073/AstralSorcery-2.0.3.59.jar",
         "https://media.forgecdn.net/files/9281/73/AstralSorcery-2.0.3.59.jar"]),
}
def method_section(out, signature):
    lines = out.splitlines()
    starts = [i for i,l in enumerate(lines) if l.strip() == signature]
    if len(starts) != 1:
        raise RuntimeError("Expected exactly one signature: "+signature)
    start = starts[0]
    end = next((i for i in range(start+1,len(lines))
                if re.match(r"^  \S.*;\s*$",lines[i]) or lines[i] == "}"),len(lines))
    return "\n".join(lines[start:end])
def count_call(out, signature, call, count):
    body = method_section(out,signature)
    actual = body.count(call)
    if actual != count:
        raise RuntimeError(f"Call count {actual} != {count}: {signature} -> {call}")
    print("ASSERT_CALL", signature, call, count, flush=True)

def assert_calls(cls,out):
    if cls.endswith(".DrainLifeBoundaryMahoujinTileEntity"):
        count_call(out,"public boolean drainLife(net.minecraft.world.entity.LivingEntity);",
            "EffectUtil.magicAttack:(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/entity/LivingEntity;)V",1)
        count_call(out,"private void lambda$doBarrier$0(net.minecraft.world.entity.LivingEntity, float, net.minecraft.world.entity.player.Player);",
            "EffectUtil.genericAttack:(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/entity/LivingEntity;)V",1)
        count_call(out,"private void lambda$doBarrier$0(net.minecraft.world.entity.LivingEntity, float, net.minecraft.world.entity.player.Player);",
            "DamageSources.generic:()Lnet/minecraft/world/damagesource/DamageSource;",1)
    elif cls.endswith(".EffectUtil"):
        count_call(out,"public static void magicAttack(net.minecraft.world.entity.LivingEntity, float, net.minecraft.world.entity.LivingEntity);",
            "DamageSources.magic:()Lnet/minecraft/world/damagesource/DamageSource;",1)
        count_call(out,"public static void magicAttack(net.minecraft.world.entity.LivingEntity, float, net.minecraft.world.entity.LivingEntity);",
            "genericAttack:(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/entity/LivingEntity;)V",1)
        count_call(out,"public static void genericAttack(net.minecraft.world.entity.LivingEntity, float, net.minecraft.world.damagesource.DamageSource, net.minecraft.world.entity.LivingEntity);",
            "LivingEntity.hurt:(Lnet/minecraft/world/damagesource/DamageSource;F)Z",1)
    elif cls.endswith(".RootPerkDiscidia"):
        count_call(out,"private void onAttack(net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent);",
            "DamageSource.getEntity:()Lnet/minecraft/world/entity/Entity;",2)
        count_call(out,"private void onAttack(net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent);",
            "ResearchHelper.addPerkExp:(Lnet/minecraft/server/level/ServerPlayer;D)Z",1)
    elif cls.endswith(".MahoujinTileEntity"):
        method_section(out,"public java.util.UUID getCasterUUID();")
        print("ASSERT_OWNER_ACCESSOR",cls,flush=True)

def audit(provider, path):
    with zipfile.ZipFile(path) as z:
        names = sorted(n for n in z.namelist() if n.endswith(".class"))
        for meta in ("META-INF/neoforge.mods.toml","META-INF/mods.toml"):
            if meta in z.namelist():
                metadata=z.read(meta).decode("utf-8")
                expected_id,expected_version=("mahoutsukai","1.21.1-v1.36.31") if provider=="mahou" else ("astralsorcery","2.0.3")
                if not re.search(r"(?m)^\s*modId\s*=\s*"+re.escape(chr(34)+expected_id+chr(34)),metadata) or not re.search(r"(?m)^\s*version\s*=\s*"+re.escape(chr(34)+expected_version+chr(34)),metadata):
                    raise RuntimeError("Unexpected pinned mod metadata "+provider)
                for line in metadata.splitlines():
                    if line.strip().startswith(("modId","version")):
                        print("METADATA",provider,line.strip(),flush=True)
        if "META-INF/MANIFEST.MF" in z.namelist():
            for line in z.read("META-INF/MANIFEST.MF").decode("utf-8",errors="replace").splitlines():
                if line.lower().startswith(("implementation-version:","specification-version:")):
                    print("METADATA",provider,line,flush=True)
        if provider == "mahou":
            candidates = [n for n in names if "drainlife" in n.lower().replace("_","")]
            candidates += ["stepsword/mahoutsukai/util/EffectUtil.class","stepsword/mahoutsukai/tile/MahoujinTileEntity.class"]
        else:
            candidates = [n for n in names if n.endswith("/RootPerkDiscidia.class")]
        if not candidates:
            raise RuntimeError("No relevant classes for " + provider)
        print("CANDIDATES", provider, candidates, flush=True)
        for n in candidates:
            cls = n[:-6].replace("/", ".")
            print("CLASS", cls, flush=True)
            out = subprocess.run(["javap","-p","-s","-c","-l","-classpath",str(path),cls],
                text=True, capture_output=True, check=True, timeout=60).stdout
            print(out, flush=True)
            assert_calls(cls,out)
        if provider == "mahou":
            # Enumerate inheritance/UUID evidence to guide a narrow owner read.
            for n in names:
                if n.startswith("stepsword/mahoutsukai/tile/") and (
                    "mahoujin" in n.lower() or "barrier" in n.lower() or "boundary" in n.lower()):
                    raw=z.read(n)
                    if b"java/util/UUID" in raw:
                        cls=n[:-6].replace("/", ".")
                        print("OWNER_CANDIDATE", cls, flush=True)
                        print(subprocess.run(["javap","-p","-s","-classpath",str(path),cls],
                            text=True,capture_output=True,check=True,timeout=30).stdout,flush=True)
with tempfile.TemporaryDirectory() as tmp:
    failures=[]
    for provider,(algo,expected,urls) in PINS.items():
        try:
            for url in urls:
                try:
                    request=urllib.request.Request(url,headers={"User-Agent":"TMM-static-ABI/1.0"})
                    with urllib.request.urlopen(request,timeout=60) as response:
                        data=response.read()
                    if hashlib.new(algo,data).hexdigest()!=expected:
                        raise ValueError("hash mismatch")
                    break
                except Exception as err:
                    print("DOWNLOAD_ATTEMPT",provider,str(err),flush=True)
            else:
                raise RuntimeError("No hash-verified JAR")
            path=pathlib.Path(tmp)/(provider+".jar")
            path.write_bytes(data)
            print("VERIFIED",provider,expected,flush=True)
            audit(provider,path)
        except Exception as err:
            failures.append(provider)
            print("AUDIT_FAILED",provider,str(err),flush=True)
    if failures:
        raise SystemExit("Failed exact-pin verification: "+",".join(failures))
