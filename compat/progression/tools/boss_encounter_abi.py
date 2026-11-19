"""Inspect pinned installed boss interfaces, never boot Minecraft or execute a JAR."""
import hashlib, pathlib, subprocess, tempfile, urllib.request, re, zipfile, tomllib
PINS={
"neo":("https://cdn.modrinth.com/data/rvaW4C93/versions/FR26bWPa/neovitae-1.21.1-1.1.53.jar","a3ad9be2f28abd1eeafe6cdc5a3961d04439f54ddcd2c57d25f0793e951c0dd9"),
"void":("https://mediafilez.forgecdn.net/files/8175/491/Voidscape-1.21.1-1.9.588.jar","f667d32e44286f0d75c18b0affbb8ecf87f5aac4b7c945e78df6db5a09f251a1"),
"knight":("https://mediafilez.forgecdn.net/files/7797/302/twilightforest-1.21.1-4.8.3345-universal.jar","493da16d10210f9f53a3c33b3d8fcb4ae9b14016ee420d753c894562bd9aba35"),
"bee":("https://cdn.modrinth.com/data/38tpSycf/versions/LwGXnQCW/the_bumblezone-7.17.3%2B1.21.1-neoforge.jar","859d87b44d3d17c63f38317ac34d124e30b6de8c3043fa61ee987ff8b1f236d6"),
"chaos":("https://mediafilez.forgecdn.net/files/8797/42/Draconic-Evolution-1.21.1-3.1.4.633.jar","e89f3a23a5478e35099905d7c08f9f6a2ff70289e964904e91f06a7ceeed39b5")
}
GUARDS={"neo":("neovitae","1.1.53"),"void":("voidscape","1.21.1-1.9.588"),"knight":("twilightforest","4.8.3345"),"bee":("the_bumblezone","7.17.3+1.21.1-neoforge"),"chaos":("draconicevolution","3.1.4.633")}
CLASSES={
"neo":{"com.breakinblocks.neovitae.common.entity.mob.DaemoniumDolorisEntity":["isForeman()","()Z","IsForeman"]},
"void":{"tamaized.voidscape.entity.NullServantEntity":["getAugment()","()Ljava/lang/Integer;","augment"]},
"knight":{"twilightforest.entity.boss.KnightPhantom":["postmortem(","getNearbyKnights()","markStructureConquered","IT_IS_OVER"]},
"bee":{"com.telepathicgrunt.the_bumblezone.blocks.EssenceBlockWhite":["awardPlayerWinStat(","crystalKilled(","EndEvent","cosmicCrystalHealth"],"com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity":["EndEvent(","awardPlayerWinStat","getEventTimer","getPlayerInArena"],"com.telepathicgrunt.the_bumblezone.entities.living.CosmicCrystalEntity":["actuallyHurt(","crystalKilled","getEssenceController"]},
"chaos":{"com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianEntity":["attackDragonFrom(","tickDeath()","processDragonDeath","deathTicks"],"com.brandon3055.draconicevolution.entity.guardian.GuardianFightManager":["processDragonDeath(","setDefeated","guardianKilled"]}
}
mismatches=[]
with tempfile.TemporaryDirectory() as temp:
    for key,(url,expected) in PINS.items():
        req=urllib.request.Request(url,headers={"User-Agent":"TMM-static-boss-audit/1.0"})
        with urllib.request.urlopen(req,timeout=120) as response:
            data=response.read(250_000_001)
        actual=hashlib.sha256(data).hexdigest()
        if len(data)>250_000_000 or actual!=expected:
            raise RuntimeError(f"{key} artifact rejected: {actual}")
        jar=pathlib.Path(temp)/(key+".jar");jar.write_bytes(data)
        print("VERIFIED",key,actual,flush=True)
        with zipfile.ZipFile(jar) as z:
            metadata=tomllib.loads(z.read("META-INF/neoforge.mods.toml").decode())
            metadata_mod=next(m for m in metadata["mods"] if m["modId"]==GUARDS[key][0])
            version=metadata_mod["version"]
            if version=="${file.jarVersion}":
                manifest=z.read("META-INF/MANIFEST.MF").decode()
                version=next(line.partition(": ")[2].strip() for line in manifest.splitlines() if line.startswith("Implementation-Version: "))
            print("RUNTIME_GUARD",key,metadata_mod["modId"],version,flush=True)
            if version!=GUARDS[key][1]: mismatches.append(f"{key} runtime guard expects {GUARDS[key][1]}, pinned jar declares {version}")

        for cls,need in CLASSES[key].items():
            out=subprocess.run(["javap","-classpath",str(jar),"-p","-s","-c",cls],capture_output=True,text=True,check=True,timeout=60).stdout
            for token in need:
                if token not in out: raise RuntimeError(f"{cls}: missing {token}")
            print("CLASS",cls,flush=True)
            active=False
            for line in out.splitlines():
                if line.startswith("  ") and not line.startswith("    ") and line.rstrip().endswith(";"):
                    active=bool(re.search(r"isForeman|setForeman|getAugment|postmortem|getNearbyKnights|EndEvent|awardPlayerWinStat|crystalKilled|attackDragonFrom|tickDeath|processDragonDeath|performUniqueArenaTick|actuallyHurt|lambda\$EndEvent",line))
                    if active: print("DECL",line.strip(),flush=True)
                elif active and (line.strip().startswith("descriptor:") or "//" in line):
                    print(line,flush=True)

if mismatches: raise RuntimeError("; ".join(mismatches))
