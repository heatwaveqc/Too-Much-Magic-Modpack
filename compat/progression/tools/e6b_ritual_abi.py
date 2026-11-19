"""Exact pinned E6b ritual census; static inspection only, no JAR redistribution."""
import hashlib, json, pathlib, re, subprocess, tempfile, urllib.request, zipfile, tomllib
PINS = {
    "enchanted": (("https://mediafilez.forgecdn.net/files/8055/168/enchanted-neoforge-1.21.1-4.2.7.jar","https://edge.forgecdn.net/files/8055/168/enchanted-neoforge-1.21.1-4.2.7.jar"), "sha1", "2380cb5a49c7eb86afa6f790a920d1872d5cb85e"),
    "witchery": (("https://cdn.modrinth.com/data/LCO8KZvC/versions/TWcJbsjg/witchery-0.5.39%2Bmc1.21.1.jar",), "sha512", "a3d50e65bb494b4d9b709163a295ddcecf53c498314df473d6de55633a4a5ebe810b9a0b5672f97f41786c03af01ef3ac91f0262216e074887e75d6afc35141d"),
    "eidolon": (("https://mediafilez.forgecdn.net/files/9332/315/eidolon_repraised-1.21.1-0.5.0.8.jar","https://edge.forgecdn.net/files/9332/315/eidolon_repraised-1.21.1-0.5.0.8.jar"), "sha1", "12a816c11a064a7050e74c5c15cf07fb8269b6d4"),
    "totemic": (("https://cdn.modrinth.com/data/GLffYJtD/versions/6Vny8MoL/Totemic-neoforge-1.21.1-0.12.18.jar",), "sha512", "4b3c4a6c926b8bf203b5593c558264b096f870ecfe74e6b6dbeef850825c9814a3b9a2d9f03c3a91680042dd684574f0259280210b9173750c921ff733e94896"),
}
def download(urls, alg, expected):
    errors=[]
    for u in urls:
        try:
            req=urllib.request.Request(u,headers={"User-Agent":"TMM-static-ritual-audit/1.0"})
            with urllib.request.urlopen(req,timeout=90) as response:
                data=response.read(80_000_001)
            if len(data)>80_000_000 or hashlib.new(alg,data).hexdigest()!=expected:
                raise ValueError("oversize or hash mismatch")
            return data
        except (OSError,ValueError) as exc: errors.append(str(exc))
    raise RuntimeError("No exact artifact: "+" | ".join(errors))

MATCH = re.compile(r"ritual|ceremon|circle|rite|brazier|totempole", re.I)
def inspect(jar, key):
    classes=[]
    with zipfile.ZipFile(jar) as z:
        for metadata in ("META-INF/neoforge.mods.toml","META-INF/mods.toml"):
            if metadata in z.namelist():
                for mod in tomllib.loads(z.read(metadata).decode())["mods"]:
                    print("METADATA",key,mod.get("modId"),mod.get("version"),flush=True)
        if "META-INF/MANIFEST.MF" in z.namelist():
            for line in z.read("META-INF/MANIFEST.MF").decode(errors="replace").splitlines():
                if line.startswith("Implementation-Version:"): print("MANIFEST",key,line,flush=True)
        for n in sorted(z.namelist()):
            if n.startswith("data/") and n.endswith(".json") and MATCH.search(n) and not any(t in n for t in ("/tags/","/advancement/","/modonomicon/","/modopedia/","/loot_table/","/worldgen/")):
                try: data=json.loads(z.read(n))
                except ValueError: continue
                print("RECIPE",key,n,json.dumps(data,separators=(",",":")),flush=True)
            if n.endswith(".class") and not n.startswith("META-INF/") and (MATCH.search(n) or (key == "enchanted" and re.search(r"CurseSavedData|CurseHandler|CurseManager|CurseHelper|FamiliarSavedData|FamiliarManager|RiteManager",n,re.I)) or (key == "eidolon" and re.search(r"EidolonItems|ModItems|ItemRegistry",n,re.I)) or (key == "witchery" and re.search(r"CurseHandler|CurseManager|InfusionHandler|InfusionManager|ManifestationHandler|WitcheryCommands|CommandKt|FamiliarHandler|WerewolfHandler|WerewolfPlayerAttachment|InfusionPlayerAttachment|EffigyBlockEntity|MirrorBlockEntity|BiomeHelper|BiomeUtil",n,re.I)) or (key == "witchery" and re.search(r"/commands?/|Command\.class$",n,re.I)) or (key == "totemic" and re.search(r"StateStartup|StateSelection|StartupContext|TotemicEventHooks|TotemState|TotemBaseBlockEntity|ModContent|ModRegistries|MusicEvent|MusicContext|MusicInstrument|MusicAcceptor",n,re.I))):
                classes.append(n[:-6].replace("/","."))
    if not classes: raise RuntimeError("No ritual classes for "+key)
    print("CENSUS",key,len(classes),flush=True)
    output=pathlib.Path("e6b-audit")/(key+".txt")
    output.parent.mkdir(exist_ok=True)
    with output.open("w") as f:
        for offset in range(0,len(classes),20):
            out=subprocess.run(["javap","-classpath",str(jar),"-p","-s","-c",*classes[offset:offset+20]],capture_output=True,text=True,check=True,timeout=90).stdout
            f.write(out)
    print("BYTECODE",key,output.stat().st_size,flush=True)
    # Report declarations and terminal-call landmarks only, rather than upload disassembly.
    for line in output.read_text().splitlines():
        if line.startswith(("public class ","public final class ","public abstract class ","class ","Compiled from")) or line.strip().startswith("descriptor:") or (line.startswith("  ") and not line.startswith("    ") and line.rstrip().endswith(";")) or ( "//" in line and re.search(r"finish|complete|success|fail|start|owner|caster|player|addFreshEntity|setBlock|setItem|setDayTime|setWeatherParameters|hurt|consume|perform|execute|save|load|reset|active|ritualId|eventQueue|eventIndex|outputEvents|RecipeHolder|setState|cancel|music|trigger|registryName|detatch|burning|ritualDone|Ritual\.tick|RitualResult|isInitialising|isExecuting|RITE_TYPES_REGISTRY|withCallback|performCommand|setRegistryName|lambda\$tick\$3|war_dance|depths|fertility|zaphkiel|animal_growth|buffalo|rain|drought|flute_infusion|eagle|cleansing|sun_dance|danse_macabre|baykok",line,re.I)):
            print(line,flush=True)
    # Focused residual-outcome witness census. Report method/field references only,
    # not a second generic class dump or copies of proprietary methods.
    focus = {
        "eidolon": r"\.(AbsorptionRitual|AllureRitual|DeceitRitual|RepellingRitual|CrystalRitual|PurifyRitual|LocationRitual|RechargingRitual|BrazierTileEntity|Ritual|EidolonItems|ModItems|ItemRegistry)\s",
        "enchanted": r"\.(ApplyCurseRite|RemoveCurseRite|BindTalismanRite|BroilingRite|BindFamiliarRite|SummonFamiliarRite|TransposeEntityRite|TransposeBlocksRite|FertilityRite|BlightRite|ForestRite|SkyWrathRite|TotalEclipseRite|ProtectionRite|ImprisonmentRite|SanctityRite|FamiliarSavedData|RiteManager|Rite|CurseSavedData|CurseHandler|CurseManager|CurseHelper|GoldChalkBlockEntity)\s",
        "witchery": r"\.(RemoveCurseRitualEvent|SummonItemWithOwnerRitualEvent|RunCommandRitualEvent|RitualCommandExecutor|WitcheryCommands|AfflictionCommandBuilder|CurseHandler|CurseManager|InfusionHandler|InfusionManager|ManifestationHandler|BindFamiliarRitualEvent|ResurrectFamiliarRitualEvent|PairMirrorsRitualEvent|PairMirrorRitualEvent|RainingToadRitualEvent|MineBlocksBelowRitualEvent|PushMobsRitualEvent|PullMobsRitualEvent|BindSpectralCreaturesRitualEvent|BestialCallRitualEvent|RotRitualEvent|FamiliarHandler|WerewolfHandler|WerewolfPlayerAttachment|EffigyBlockEntity|MirrorBlockEntity|ParallelRitualEvent|GoldenRitualChalkBlockEntity|BrazierBlockEntity)\s",
    }.get(key)
    if focus:
        active = False
        for line in output.read_text().splitlines():
            if line.startswith(("public class ", "public final class ", "public abstract class ", "class ", "public interface ")):
                active = re.search(focus, line) is not None
                if active: print("FOCUS", line, flush=True)
            elif active:
                if (line.startswith("  ") and not line.startswith("    ") and line.rstrip().endswith(";")):
                    print("FOCUS",line,flush=True)
                elif "//" in line and re.search(r"\b(invoke\w*|getfield|putfield|getstatic|putstatic|ldc\w*)\b",line):
                    # Exact called symbols and constant identities; omit branch/opcode bodies.
                    print("FOCUS",line.split("//",1)[1].strip(),flush=True)
if __name__ == "__main__":
    import sys
    key=sys.argv[1]
    urls,alg,expected=PINS[key]
    with tempfile.TemporaryDirectory() as directory:
        jar=pathlib.Path(directory)/(key+".jar")
        jar.write_bytes(download(urls,alg,expected))
        print("VERIFIED",key,alg,expected,flush=True)
        inspect(jar,key)
