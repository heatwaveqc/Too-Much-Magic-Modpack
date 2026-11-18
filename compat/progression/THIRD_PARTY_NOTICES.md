# Third-party notices — progression

The accompanying MIT LICENSE covers original first-party module code and documentation only to the extent the Too Much Magic contributors hold the rights. Independently licensed dependencies, Minecraft, third-party content, and upstream assets retain their own terms. Do not treat this as a license for the entire modpack.

### Reference sources and planned reuse (not imports in this change)

- Tyz's Skills, by Tyzbrand: https://github.com/Tyzbrand/TyzsSkills — upstream LICENSE: MIT, copyright (c) 2026 Tyzbrand. Current optional compile/runtime API integration is not a bundled Tyz JAR. Planned source reuse requires resolved source/release metadata discrepancy, immutable donor provenance and the complete original license notice.
- Just Dire Things, by Direwolf20-MC: https://github.com/Direwolf20-MC/JustDireThings — 1.21.1 code license MIT, copyright (c) 2023 Direwolf20-MC. Enhanced night vision is a planned donor; no code imported by this licensing change.
- Create, by The Create Team / The Creators of Create: https://github.com/Creators-of-Create/Create — 1.21.1 source code MIT, assets specifically All Rights Reserved. Lava fog rendering is a planned donor; no code/assets imported by this licensing change.

When source is actually copied or adapted, append the exact copyright/permission notice(s), immutable commit and original file paths, and retain required attribution in the source and distributable. References here alone do not inventory all historical code provenance or satisfy notice obligations for future imports.

**Maintenance:** Preserve all applicable upstream notices verbatim, plus provenance for code/assets actually incorporated. This file is an initial record, not a certification of historical provenance. Review existing sources before public distribution and update this file whenever new third-party code is incorporated.

## Imported foundation code (2026-11-18)

The following adapted source files are shipped under their original permissive terms; complete original licenses are in third_party_licenses/ and included in JARs. No upstream assets, standalone donor mod JARs or external UI libraries are redistributed. No Tyz source imported. Original source paths and immutable revisions:

### Epiphany — MIT
HaooooZhang/Epiphany @ 1cbffb31ef4474fec73d66f1a888b4db27c4a71d
- https://github.com/HaooooZhang/Epiphany/blob/1cbffb31ef4474fec73d66f1a888b4db27c4a71d/src/main/java/ink/myumoon/epiphany/content/condition/Comparison.java
- https://github.com/HaooooZhang/Epiphany/blob/1cbffb31ef4474fec73d66f1a888b4db27c4a71d/src/main/java/ink/myumoon/epiphany/content/condition/logic/AndCondition.java
- https://github.com/HaooooZhang/Epiphany/blob/1cbffb31ef4474fec73d66f1a888b4db27c4a71d/src/main/java/ink/myumoon/epiphany/content/condition/logic/OrCondition.java
- https://github.com/HaooooZhang/Epiphany/blob/1cbffb31ef4474fec73d66f1a888b4db27c4a71d/src/main/java/ink/myumoon/epiphany/content/condition/logic/NotCondition.java
- https://github.com/HaooooZhang/Epiphany/blob/1cbffb31ef4474fec73d66f1a888b4db27c4a71d/src/main/java/ink/myumoon/epiphany/content/reward/RewardSource.java
- https://github.com/HaooooZhang/Epiphany/blob/1cbffb31ef4474fec73d66f1a888b4db27c4a71d/src/main/java/ink/myumoon/epiphany/client/ui/insight/InsightTreeView.java

Full original notice: third_party_licenses/Epiphany.txt.

### Oritech — CC0-1.0
Rearth/Oritech @ 225b182d0f8d2f9eb56a61220569dc32c4461f8e
- https://github.com/Rearth/Oritech/blob/225b182d0f8d2f9eb56a61220569dc32c4461f8e/common/src/main/java/rearth/oritech/block/entity/augmenter/api/Augment.java

Full original notice: third_party_licenses/Oritech.txt.

### EverlastingAbilities — MIT
CyclopsMC/EverlastingAbilities @ 53bc1f1cd9ca835e586bd0fb88d9a88afb85544e
- https://github.com/CyclopsMC/EverlastingAbilities/blob/53bc1f1cd9ca835e586bd0fb88d9a88afb85544e/loader-common/src/main/java/org/cyclops/everlastingabilities/ability/AbilityTypeSpecialMagnetize.java

Full original notice: third_party_licenses/EverlastingAbilities.txt.

### Apotheosis — MIT
Shadows-of-Fire/Apotheosis @ 84e27e7031c7e2ab2f2e8328daf491c88203fcbc
- https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/main/java/dev/shadowsoffire/apotheosis/attachments/AttributeToggles.java

Full original notice: third_party_licenses/Apotheosis.txt.

### JustDireThings — MIT
Direwolf20-MC/JustDireThings @ 506416a9c7675cacc246a47d2b0c9d6b27eff9a8
- https://github.com/Direwolf20-MC/JustDireThings/blob/506416a9c7675cacc246a47d2b0c9d6b27eff9a8/src/main/java/com/direwolf20/justdirethings/mixin/LightTextureMixin.java

Full original notice: third_party_licenses/JustDireThings.txt.

### Create — MIT
Creators-of-Create/Create @ 37ea4afcfe5bf85875d4b0749798a45330070a1f
- https://github.com/Creators-of-Create/Create/blob/37ea4afcfe5bf85875d4b0749798a45330070a1f/src/main/java/com/simibubi/create/foundation/events/ClientEvents.java

Full original notice: third_party_licenses/Create.txt.
