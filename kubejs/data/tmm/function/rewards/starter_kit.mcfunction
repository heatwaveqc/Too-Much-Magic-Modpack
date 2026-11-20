# TMM once-per-player starter kit. Earned minecraft:tick advancement persists on the player.
# Keep this separate from tmm:first_log (the one-time Trinkets Pouch on first log).
# Two *ordinary* steaks; do not grant the rare Eternal Steak or its quest reward.
# Native Akashic Tome 1.8-30 tool_content component; each book needs defined_mod for morph lookup.
# Initial vetted book candidates only; full installed-JAR book registry and automatic loose-book grants still require QA.
give @s akashictome:tome[akashictome:tool_content=[{"id":"occultism:dictionary_of_spirits","count":1,"components":{"akashictome:defined_mod":"occultism"}},{"id":"evilcraft:origins_of_darkness","count":1,"components":{"akashictome:defined_mod":"evilcraft"}},{"id":"theurgy:grimiore","count":1,"components":{"akashictome:defined_mod":"theurgy"}},{"id":"draconicevolution:info_tablet","count":1,"components":{"akashictome:defined_mod":"draconicevolution"}}]] 1
give @s minecraft:cooked_beef 2
# Improvable Skills 3's own operator command sets the saved hasCraftedSkillBook flag.
# Last boolean = silent. No skills, scrolls, or levels are granted.
improvableskills book unlock @s true
