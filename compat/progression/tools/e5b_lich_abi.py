"""Hash-pinned native lich signatures and targeted caller audit, no game launch."""
import hashlib, pathlib, subprocess, tempfile, urllib.request, zipfile
PINS = {
 "goety": ("https://cdn.modrinth.com/data/4ZVIxU8x/versions/xLTYSsZD/goety-3.2.2.jar","507ad7c4462fe4d888a6458ad6dfbf1f320eadd626d714c96061c109493d888f",
 ("utils/SEHelper","utils/LichdomHelper","capabilities/soulenergy/SEImp","capabilities/soulenergy/ISoulEnergy","events/SoulEnergyEvents","events/spell/GoetyEventFactory","items/magic/DarkWand","api/entities/IOwned","entities/neutral/Owned","client/gui/overlay/SoulEnergyGui","config/MainConfig","magic/SummonSpell","necromancy/ZombieSpell","necromancy/SkeletonSpell","ritual/RitualRequirements","undead/zombie/ZombieServant","undead/skeleton/AbstractSkeletonServant"),
 ("increaseSE(","increaseSESouls(","increaseSouls(","decreaseSoulsNoDiscount(","rawHandleKill(","MagicResults(","onLivingDeathEvent(","teleportToArca(","setSoulsAmount(","getTrueOwner(","getOwnerId(","onSoulEnergyGain(","onSoulEnergyLoss(","getCapability(","getSoulEnergy(","conditionsMet(","summonLimit(","canSummon(","getSummonLimit(")),
 "forbidden": ("https://cdn.modrinth.com/data/pitZv4pV/versions/YgPMe2M7/witchery_forbidden_magic-0.5.37%2Bmc1.21.1.jar","e1f8a919946bc7997106fce1163dcfb5e436ae0f4482bbe7f4b0a81d35028fc0",
 ("LichdomEvents","LichdomSoulPoolHandler","LichPlayerAttachment","LichPlayerAttachment$Data","SummonUndeadAbility","NecroHandler","NecroEntityAttachment","NecroPlayerAttachment"),
 ("onDeath(","getCurrentSouls(","drainNewestSoul(","isLich(","getLevel(","invoke(","cast(","execute(","summon(","onUse(","use(","perform(","apply(","activate(","summonNecroAroundPos(","summonNecro(","getOwner(","getData(","setData(","getNecroOwner(")),
}
with tempfile.TemporaryDirectory() as tmp:
 for name,(url,expected,ends,methods) in PINS.items():
    req=urllib.request.Request(url,headers={"User-Agent":"TMM-static-lich-audit/1.0"})
    with urllib.request.urlopen(req,timeout=90) as res: data=res.read()
    actual=hashlib.sha256(data).hexdigest()
    if actual!=expected: raise ValueError(f"{name} hash mismatch {actual}")
    jar=pathlib.Path(tmp)/(name+".jar");jar.write_bytes(data)
    print("VERIFIED",name,actual,flush=True)
    with zipfile.ZipFile(jar) as z:
        names=[n[:-6].replace("/",".") for n in z.namelist() if n.endswith(".class") and any(n[:-6].endswith(x) for x in ends)]
        if name=="goety":
            for n in sorted(z.namelist()):
                if n.endswith(".json") and "/tags/entity_type/" in n and ("undead" in n or "servant" in n):
                    print("TAG",n,z.read(n).decode("utf-8")[:5000].replace("\n"," "))
        if name=="forbidden":
            for n in z.namelist():
                if n.endswith(".class") and any(t in n.lower() for t in ("servant","summon","minion","phylacter","necro")):
                    print("CANDIDATE",n)
    for clazz in names:
        output=subprocess.run(["javap","-classpath",str(jar),"-p","-s","-c",clazz],capture_output=True,text=True,timeout=45,check=True).stdout
        print("CLASS",clazz,flush=True)
        method=""; relevant=False
        for line in output.splitlines():
            if line.startswith("  ") and not line.startswith("    ") and line.rstrip().endswith(";"):
                method=line.strip(); relevant=any(m in method for m in methods)
                print("DECL",method)
            elif line.strip().startswith("descriptor:"): print("DESC",line.strip())
            elif relevant and line.lstrip().split(":",1)[0].isdigit():
                print("BC",method,"|",line.strip())
        print("END",clazz,flush=True)
