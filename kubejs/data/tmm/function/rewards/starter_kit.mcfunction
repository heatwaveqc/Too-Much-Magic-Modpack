# TMM once-per-player starter kit. Earned minecraft:tick advancement persists on the player.
# Keep this separate from tmm:first_log (the one-time Trinkets Pouch on first log).
# Two *ordinary* steaks; do not grant the rare Eternal Steak or its quest reward.
# Native 1.8-30 Akashic data component; each nested book has defined_mod for morphing.
# Curated registered guides only. See docs/qa/AKASHIC-STARTER-TOME-2026-11-20.md.
give @s akashictome:tome[akashictome:tool_content=[{id:"ars_nouveau:worn_notebook",count:1,components:{"akashictome:defined_mod":"ars_nouveau"}},{id:"botania:lexicon",count:1,components:{"akashictome:defined_mod":"botania"}},{id:"occultism:dictionary_of_spirits",count:1,components:{"akashictome:defined_mod":"occultism"}},{id:"evilcraft:origins_of_darkness",count:1,components:{"akashictome:defined_mod":"evilcraft"}}]] 1
give @s minecraft:cooked_beef 2
# Improvable Skills 3's own operator command sets the saved hasCraftedSkillBook flag.
# Last boolean = silent. No skills, scrolls, or levels are granted.
improvableskills book unlock @s true
