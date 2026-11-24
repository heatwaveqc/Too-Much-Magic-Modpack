# Claimable one-time personal TMM starter supplies, invoked by Tips & Tricks.
# The tmm:starter_kit advancement is a LEGACY marker for old first-login automatic
# Tome + steaks. Do not double-grant those items on existing test-world players.
# Newly created profiles do not earn that legacy impossible advancement.
# The separate tmm:starter_kit_claimed advancement makes this function idempotent.
execute unless entity @s[advancements={tmm:starter_kit_claimed=true}] unless entity @s[advancements={tmm:starter_kit=true}] run give @s akashictome:tome 1
execute unless entity @s[advancements={tmm:starter_kit_claimed=true}] unless entity @s[advancements={tmm:starter_kit=true}] run give @s minecraft:cooked_beef 2
execute unless entity @s[advancements={tmm:starter_kit_claimed=true}] run give @s comforts:sleeping_bag_white 1
advancement grant @s only tmm:starter_kit_claimed
