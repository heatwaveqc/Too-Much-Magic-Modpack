"""Exact pinned Neo Vitae Well/Nexus owner and damage call census, no game launch."""
import hashlib, pathlib, subprocess, tempfile, urllib.request, zipfile, re
URL="https://cdn.modrinth.com/data/rvaW4C93/versions/FR26bWPa/neovitae-1.21.1-1.1.53.jar"
HASH="f29b391e391476c232acbe9ba69c42c8539b5eefdd8ca324ab5e04c47dd0e708d7bd9f727cd358099c1c1066b80c759dc86d659549dee54affa17ba588ac5b0b"
with tempfile.TemporaryDirectory() as tmp:
    req=urllib.request.Request(URL,headers={"User-Agent":"TMM-static-ABI/1.0"})
    with urllib.request.urlopen(req,timeout=120) as response:
        data=response.read()
    assert hashlib.sha512(data).hexdigest()==HASH
    print("VERIFIED",HASH,flush=True)
    path=pathlib.Path(tmp)/"neo.jar";path.write_bytes(data)
    with zipfile.ZipFile(path) as z:
        names=sorted(n for n in z.namelist() if n.endswith(".class"))
        for meta in ("META-INF/neoforge.mods.toml","META-INF/mods.toml","META-INF/MANIFEST.MF"):
            if meta in z.namelist():
                for l in z.read(meta).decode("utf-8",errors="replace").splitlines():
                    if re.match(r"\s*(modId|version\s*=|Implementation-Version|Specification-Version)",l):
                        print("METADATA",l.strip(),flush=True)
        candidates=[n for n in names if any(s in n for s in ("WellOfSuffering","TormentNexus","IMasterRitualStone.class","MasterRitualStoneBlockEntity","RitualHelper.class","RitualHelper$RitualContext.class","NVDamageSources.class"))]
        for required in ("RitualWellOfSuffering","RitualDeusWellOfSuffering","RitualTormentNexus","MasterRitualStoneBlockEntity","IMasterRitualStone"):
            assert any(n.endswith("/"+required+".class") for n in candidates),required
        for n in candidates:
            name=n[:-6].replace("/",".")
            print("CLASS",name,flush=True)
            out=subprocess.run(["javap","-p","-s","-c","-l","-classpath",str(path),name],capture_output=True,text=True,check=True,timeout=60).stdout
            if name.endswith(".RitualWellOfSuffering"):
                assert out.count("LivingEntity.hurt:(Lnet/minecraft/world/damagesource/DamageSource;F)Z")==1
                assert "NVDamageSources.RITUAL:Lnet/minecraft/resources/ResourceKey;" in out
                print("ASSERT_WELL_TERMINAL",flush=True)
            if name.endswith(".RitualDeusWellOfSuffering"):
                assert "extends com.breakinblocks.neovitae.ritual.types.RitualWellOfSuffering" in out
                assert "performRitual(" not in out
                print("ASSERT_DEUS_INHERITANCE",flush=True)
            if name.endswith(".RitualTormentNexus"):
                assert not re.search(r"// (?:InterfaceMethod|Method) .*?/(?:LivingEntity|Entity)\\.(?:hurt|die|kill):",out)
                assert "LivingEntity.discard:()V" in out
                print("ASSERT_NEXUS_NO_NATIVE_DEATH",flush=True)
            if name.endswith(".MasterRitualStoneBlockEntity"):
                assert "public java.util.UUID getOwner();" in out
                assert "public boolean isActive();" in out
                assert "public com.breakinblocks.neovitae.ritual.Ritual getCurrentRitual();" in out
                assert "Ritual.performRitual:(Lcom/breakinblocks/neovitae/ritual/IMasterRitualStone;)V" in out
                print("ASSERT_MASTER_OWNER_AND_DISPATCH",flush=True)
            if "MasterRitualStoneBlockEntity" in name or "RitualHelper.class" in n or "TormentNexus" in name:
                # Retain declarations and targeted native owner/terminal paths, not unrelated bytecode.
                active=False
                for line in out.splitlines():
                    if re.match(r"^  \S.*;\s*$",line):
                        active=bool(re.search(r"owner|activat|loadAdditional|saveAdditional|createContext|performRitual|simulateKill|harvestLiving|reapLiving|fakePlayer|setOwner|getCurrentRitual|isActive",line,re.I))
                        print(line)
                    elif "descriptor:" in line or active or re.search(r"// (?:InterfaceMethod|Method).*?(?:hurt:|discard:|die:|kill:)",line):
                        print(line)
            else:
                print(out,flush=True)
