# TMM once-per-player starter kit. Earned minecraft:tick advancement persists on the player.
# Keep this separate from tmm:first_log (the one-time Trinkets Pouch on first log).
# Two *ordinary* steaks; do not grant the rare Eternal Steak or its quest reward.
give @s akashictome:tome 1
give @s minecraft:cooked_beef 2
# Improvable Skills 3's own operator command sets the saved hasCraftedSkillBook flag.
# Last boolean = silent. No skills, scrolls, or levels are granted.
improvableskills book unlock @s true
