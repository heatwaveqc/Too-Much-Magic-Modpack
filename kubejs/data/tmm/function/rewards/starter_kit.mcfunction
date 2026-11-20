# TMM once-per-player starter kit. Earned minecraft:tick advancement persists on the player.
# Keep this separate from tmm:first_log (the one-time Trinkets Pouch on first log).
# Two *ordinary* steaks; do not grant the rare Eternal Steak or its quest reward.
# Native 1.8-30 Akashic data component; each nested book has defined_mod for morphing.
# All eligible introductory manuals are tracked in manifest/starter_guidebooks.json; final roster QA pending.
give @s akashictome:tome[akashictome:tool_content=[{id:"ars_nouveau:worn_notebook",count:1,components:{"akashictome:defined_mod":"ars_nouveau"}},{id:"botania:lexicon",count:1,components:{"akashictome:defined_mod":"botania"}},{id:"occultism:dictionary_of_spirits",count:1,components:{"akashictome:defined_mod":"occultism"}},{id:"evilcraft:origins_of_darkness",count:1,components:{"akashictome:defined_mod":"evilcraft"}},{id:"rootsclassic:runic_tablet",count:1,components:{"akashictome:defined_mod":"rootsclassic"}},{id:"hexerei:book_of_shadows",count:1,components:{"akashictome:defined_mod":"hexerei"}},{id:"modonomicon:modonomicon",count:1,components:{"akashictome:defined_mod":"theurgy","modonomicon:book_id":"theurgy:the_hermetica"}},{id:"patchouli:guide_book",count:1,components:{"akashictome:defined_mod":"goety","patchouli:book":"goety:black_book"}},{id:"eidolon_repraised:codex",count:1,components:{"akashictome:defined_mod":"eidolon_repraised"}},{id:"neovitae:guide_book",count:1,components:{"akashictome:defined_mod":"neovitae"}},{id:"astralsorcery:tome",count:1,components:{"akashictome:defined_mod":"astralsorcery"}}]] 1
give @s minecraft:cooked_beef 2
# Improvable Skills 3's own operator command sets the saved hasCraftedSkillBook flag.
# Last boolean = silent. No skills, scrolls, or levels are granted.
improvableskills book unlock @s true
