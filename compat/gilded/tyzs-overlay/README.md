# Gilded Fortune Tyz overlay — source staging only

Owner rule (2026-11-16): **Gilded receives +3 virtual Fortune as soon as initiation completes**, then may buy **seven one-point ranks** through **Tyz's Skills** to reach **+10 virtual Fortune** on eligible gold/Netherite mining tools. Actual physical Fortune remains fully additive (Fortune III + 10 = effective XIII). No arbitrary combined enchant level cap. Silk Touch still takes precedence. Gilded ore XP (native path) and Tyz shared SP (one wallet) remain independent.

- Skill ID: \`tmm_gilded_fortune_mastery\`; \`type: IMMUTABLE\` because custom Tyz attribute-only JSON cannot express action-scoped enchantment levels.
- Seven Tyz ranks with one SP per rank and display value 4–10 inclusive; category \`tmm_gilded\`. Initial +3 bonus is granted by the independent path, not Tyz points and not Gilded XP level 3.
- Source: \`config/tyzs_skills/custom/skills/tmm_gilded_fortune_mastery.json\`. \`category-patch.json\` contains **only** the new category; do not replace the existing whole \`categories.json\` from this small overlay.
- Native runtime: \`compat/progression/src/main/java/io/github/heatwaveqc/tmm/progression/runtime/TyzGildedBridge.java\` reads actual purchased Tyz skill levels through reflection, gates ordinary purchases on native Gilded enrollment and ensures admin-granted/unqualified ranks stay dormant. If Tyz isn't installed or the skill is missing, eligible Gilded retains their baseline +3. The Gilded source must be compiled and imported alongside the Tyz 6.4.2 overlay, not as a separate wallet.

**Check without changing anything:**
\`\`\`sh
python3 compat/gilded/stage_gilded_tyz.py --check
\`\`\`

**Only when composing the next authorized integrated Tyz QA pack:**
\`\`\`sh
python3 compat/gilded/stage_gilded_tyz.py --install --pack-root /path/to/packwiz/checkout
\`\`\`

The installer refuses to run if the exact Tyz packwiz pin, full existing categories (including Core Magic and Monk), or requisite source overlay is missing; merges the one Gilded category without deleting the other six; leaves unrelated skill JSON intact. Do not run before the existing Tyz and Monk overlay is staged. This source file is **not auto-invoked in the existing full-integration workflow**. Integrate it explicitly in the next authorized build and add a positive test that the category and seven-rank node appear in the client, buy/refund/SP accounting works, and neither non-Gilded players nor Silk Touch gain the bonus.

My Nether's Delight separately has \`desired: true\`, \`enabled_in_pack: false\` in \`manifest/mods.json\` (both exploratory and integrated manifest). Official 1.21.1 NeoForge 1.10.4.1 CurseForge file ID 8854319. Exact packwiz pin and server/client food-registry checks are still required before a Gilded-enabled build.
