# Gilded Tyz Fortune and Looting — independent skill overlay

Owner-locked (2026-11-16): **both native bonuses start at +3 when Gilded initiates**, then each has **seven independent Tyz's Skills purchases** (+1 per rank) to reach **+10 virtual Fortune and +10 virtual Looting**, additive to any physical enchantments. Seven purchases of Fortune do not raise Looting, and vice versa. Each rank uses one point from the same Tyz SP wallet; these are not additional wallets.

- **\`tmm_gilded_fortune_mastery\`**: gold and Netherite mining tools; remains subject to actual block loot context and Silk Touch.
- **\`tmm_gilded_looting_mastery\`**: gold swords and axes for direct melee kills; rare/drop-count functions use native Looting level within the actual loot calculation. No bonus from bows, machines, Netherite weapons or non-Gilded players.
- \`type: IMMUTABLE\` holds server-owned purchased skill ranks only; live effects are calculated in \`TyzGildedBridge\`, \`GildedRules\`, and targeted Looting/Fortune Mixins. No unconditional physical-enchantment or equipment-attribute modifications.
- \`category-patch.json\` adds the *one* \`tmm_gilded\` tab, preserving vanilla Tyz, Core Magic, Monk and all other existing categories.

**Check only:**
\`\`\`sh
python3 compat/gilded/stage_gilded_tyz.py --check
\`\`\`

**To install ONLY in a separately authorized integrated test checkout (after Tyz and Monk staging):**
\`\`\`sh
python3 compat/gilded/stage_gilded_tyz.py --install --pack-root /path/to/packwiz/checkout
\`\`\`

The stager validates both skills, existing category set and all destination-file conflicts before writing anything. It refuses to overwrite existing owner-modified skill files. The targeted source/CI tests do not create a Prism build or install either skill in the current \`test/full-integration\` pack.

## QA

Test newly initiated Gilded at zero Tyz ranks: both bonuses +3. Purchase only Fortune rank 7: Fortune +10, Looting +3; purchase only Looting rank 7: Fortune +3, Looting +10; purchase both: +10/+10. Stored Fortune/Looting III should each function at effective XIII with the corresponding seven purchases. Test independent refunds, relogs, non-Gilded players, Silk Touch, ranged attacks, mob counts vs rare drops, and simultaneous Ars Drygmy virtual Looting. Verify server-side purchase gate actually fires against Tyz 6.4.2 and skill details appear as two separate nodes in the Gilded tab.
