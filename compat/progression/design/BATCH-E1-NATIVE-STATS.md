# Batch E1 — native stat catalog expansion (source/data only)

Owner authorized E1 on 2026-11-18. Existing native generic attribute runtime and MagicCatalog unchanged; 36 existing aliases preserved exactly, 44 additional aliases, total 80 across 10 namespaces (PsiTweaks, Malum, Enigmatic add three; Goety already present). No new mixins, dependencies, attributes, passive effects, XP hook, active skill tree, skill allocation, balance values, pack changes or build. Aliases are optional addressable native bindings, not grants. Existing runtime skips unavailable attribute/instance rather than creating a fake attribute. All new bindings use additive *native units*, scale 1, never automatically interpret values as universal percentages. Preserve existing Neo scale100.

## Evidence and semantics

- Goety 3.2.2: current upstream Vivideru/Goety-3 main src/main/java/com/Polarice3/Goety/init/ModAttributes.java blob c9958acf1f3d672cff003b83f0d5bf500e3b0266, prior categorical audit and #265 pinned report; 27 native definitions added to every living type via EntityAttributeModificationEvent. Source-family, not full exact 3.2.2 binary registry confirmation. Existing global potency and soul discount remain; added 25: duration/range/radius/burning/velocity, casting speed/cooldown discount, nine school potency/discount pairs. Range/duration/burning and potency getters truncate to int; radius double, velocity float. Native base-zero offsets, not percentage scale or universally blocks/seconds. Casting speed/cooldown/soul discount getter returns 1 minus attribute (-1..1); positive native bonus decreases cost/time, never intentionally zero/negative paid cost. Critically getPotency(living,spell) and getSoulDiscount(living,spell) choose first matched school attribute instead of summing general + school; do not assume global + school bonuses stack or even affect typed spell. Spell-specific read paths, school priority, actual clamp / affordability and cap need in-world QA at rank design. Projection/Persistence can bind the existing range/duration attributes on supported spells without new Java.
- Malum 1.8.2 target: SammySemicolon/Malum-Mod 1.21.1 src/main/java/com/sammy/malum/registry/common/MalumAttributes.java blob 0d8bdc51fd865adad7c49e80b04181e2f46ada14 registers 15 and attaches to all living types. Branch family version drift (see #197, later than exact installed 1.8.2), so added optional aliases are source-backed candidates, NOT a claim all 15 exist on pinned 1.8.2. Unknown native attributes safely absent at runtime; verify exact registry and consumer semantics before activating any corresponding skill. Arcane resonance/scythe proficiency default1 and percentage display; spirit spoils/capacities default0, ward integrity/recovery default1, charge duration/capacity default0 (native base-attribute modifiers, duration negative sentiment, do not assume longer is good), geas_limit default2 max8, conversion 0..1, aegis recovery defaults1/cap0. Do not globally boost item-only effects or add to base charge modifier IDs (not attributes). Generic player magic/ward skills only where approved. No Gaze-specific guessed bindings.
- PsiTweaks 0.10.10-hotfix: exact release-named branch moratan251/psitweaks src/main/java/com/moratan251/psitweaks/common/attributes/PsitweaksAttributes.java blob ae09885f3be052c7ad8c7a02085d199e2b8f251e; prior #12 PSI-SKILLS-DEEP-DIVE-2026-11-13.md confirms player attachment and selected consumers. spell_damage_factor default1 range0..100; +0.1 means native factor increment, not guaranteed base Psi damage. Existing Psi capacity/regen unchanged. No change to FE, cooldown or penalties.
- Enigmatic Legacy+ 1.21.1-1.1.2: version-aligned source Auviotre/Enigmatic-Legacy-Plus commit 9093d40910e66f7e58217d994222c310f62f3022 src/main/java/auviotre/enigmatic/legacy/registries/EnigmaticAttributes.java blob ef023e5cd5b7c5d25d5bfea6beff7e9fc9569a24 and earlier #12 ENIGMATIC-LEGACY-PLUS-1.1.2-SKILLS-DEATH-AUDIT-2026-11-14.md player/living registration. Shield threshold 0..1, native projectile reflect chance 0..1, lifesteal proportional to damage with high native max64. Not equivalent to existing custom projectile_deflection (discard), do not combine without design review. No separate tree, no magic XP, death/cooldown behavior unchanged. Native cap isn't approved balance.

## Added bindings

| Alias (tyr_progression:) | Native attribute |
|---|---|
| goety_duration | goety:spell_duration |
| goety_range | goety:spell_range |
| goety_radius | goety:spell_radius |
| goety_burning | goety:spell_burning |
| goety_velocity | goety:spell_velocity |
| goety_cast_speed | goety:casting_speed |
| goety_cooldown_reduction | goety:cooldown_discount |
| goety_abyss_potency | goety:abyss_potency |
| goety_abyss_cost_discount | goety:abyss_discount |
| goety_frost_potency | goety:frost_potency |
| goety_frost_cost_discount | goety:frost_discount |
| goety_geomancy_potency | goety:geomancy_potency |
| goety_geomancy_cost_discount | goety:geomancy_discount |
| goety_necromancy_potency | goety:necromancy_potency |
| goety_necromancy_cost_discount | goety:necromancy_discount |
| goety_nether_potency | goety:nether_potency |
| goety_nether_cost_discount | goety:nether_discount |
| goety_storm_potency | goety:storm_potency |
| goety_storm_cost_discount | goety:storm_discount |
| goety_void_potency | goety:void_potency |
| goety_void_cost_discount | goety:void_discount |
| goety_wild_potency | goety:wild_potency |
| goety_wild_cost_discount | goety:wild_discount |
| goety_wind_potency | goety:wind_potency |
| goety_wind_cost_discount | goety:wind_discount |
| malum_scythe_proficiency | malum:scythe_proficiency |
| malum_spirit_spoils | malum:spirit_spoils |
| malum_arcane_resonance | malum:arcane_resonance |
| malum_soul_ward_integrity | malum:soul_ward_integrity |
| malum_soul_ward_recovery_rate | malum:soul_ward_recovery_rate |
| malum_soul_ward_recovery_gain | malum:soul_ward_recovery_gain |
| malum_soul_ward_capacity | malum:soul_ward_capacity |
| malum_charge_duration | malum:charge_duration |
| malum_charge_capacity | malum:charge_capacity |
| malum_charge_recovery_rate | malum:charge_recovery_rate |
| malum_geas_limit | malum:geas_limit |
| malum_malignant_conversion | malum:malignant_conversion |
| malum_malignant_aegis_recovery_rate | malum:malignant_aegis_recovery_rate |
| malum_malignant_aegis_recovery_gain | malum:malignant_aegis_recovery_gain |
| malum_malignant_aegis_capacity | malum:malignant_aegis_capacity |
| psitweaks_potency | psitweaks:spell_damage_factor |
| enigmatic_etherium_shield | enigmaticlegacyplus:etherium_shield |
| enigmatic_projectile_deflect | enigmaticlegacyplus:projectile_deflect |
| enigmatic_lifesteal | enigmaticlegacyplus:lifesteal |

## Validation / next steps
Remote JSON generated with strict uniqueness and preservation checks: 44 new/80 total. Packaged catalog test verifies exact added alias IDs, native unit scale/operation, existing Neo scale, disjoint native attribute IDs and rank-array/conditional expansion. Test run must be checked on resulting commit; not yet gameplay proof. No in-world test or full build authorized. Need exact pinned registry census/actual player instance and equip/refund/relog/conditional isolation; no ranks approved. E2 next: reuse existing payment source adapters. Revised E1–E6 queue precedes this at a5f844d. E1 needs no bespoke attribute hooks.
